package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProgramVersionAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.ProgramVersion;
import com.mycompany.myapp.repository.ProgramVersionRepository;
import com.mycompany.myapp.service.dto.ProgramVersionDTO;
import com.mycompany.myapp.service.mapper.ProgramVersionMapper;
import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProgramVersionResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ProgramVersionResourceIT {

    private static final Integer DEFAULT_VERSION = 1;
    private static final Integer UPDATED_VERSION = 2;

    private static final Boolean DEFAULT_IS_ACTIVE = false;
    private static final Boolean UPDATED_IS_ACTIVE = true;

    private static final LocalDate DEFAULT_PUBLISH_DATE = LocalDate.ofEpochDay(0L);
    private static final LocalDate UPDATED_PUBLISH_DATE = LocalDate.now(ZoneId.systemDefault());

    private static final String ENTITY_API_URL = "/api/program-versions";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProgramVersionRepository programVersionRepository;

    @Autowired
    private ProgramVersionMapper programVersionMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgramVersionMockMvc;

    private ProgramVersion programVersion;

    private ProgramVersion insertedProgramVersion;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgramVersion createEntity() {
        return new ProgramVersion().version(DEFAULT_VERSION).isActive(DEFAULT_IS_ACTIVE).publishDate(DEFAULT_PUBLISH_DATE);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ProgramVersion createUpdatedEntity() {
        return new ProgramVersion().version(UPDATED_VERSION).isActive(UPDATED_IS_ACTIVE).publishDate(UPDATED_PUBLISH_DATE);
    }

    @BeforeEach
    public void initTest() {
        programVersion = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProgramVersion != null) {
            programVersionRepository.delete(insertedProgramVersion);
            insertedProgramVersion = null;
        }
    }

    @Test
    @Transactional
    void createProgramVersion() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);
        var returnedProgramVersionDTO = om.readValue(
            restProgramVersionMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programVersionDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProgramVersionDTO.class
        );

        // Validate the ProgramVersion in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProgramVersion = programVersionMapper.toEntity(returnedProgramVersionDTO);
        assertProgramVersionUpdatableFieldsEquals(returnedProgramVersion, getPersistedProgramVersion(returnedProgramVersion));

        insertedProgramVersion = returnedProgramVersion;
    }

    @Test
    @Transactional
    void createProgramVersionWithExistingId() throws Exception {
        // Create the ProgramVersion with an existing ID
        programVersion.setId(1L);
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programVersionDTO)))
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkVersionIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        programVersion.setVersion(null);

        // Create the ProgramVersion, which fails.
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        restProgramVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsActiveIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        programVersion.setIsActive(null);

        // Create the ProgramVersion, which fails.
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        restProgramVersionMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programVersionDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllProgramVersions() throws Exception {
        // Initialize the database
        insertedProgramVersion = programVersionRepository.saveAndFlush(programVersion);

        // Get all the programVersionList
        restProgramVersionMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(programVersion.getId().intValue())))
            .andExpect(jsonPath("$.[*].version").value(hasItem(DEFAULT_VERSION)))
            .andExpect(jsonPath("$.[*].isActive").value(hasItem(DEFAULT_IS_ACTIVE.booleanValue())))
            .andExpect(jsonPath("$.[*].publishDate").value(hasItem(DEFAULT_PUBLISH_DATE.toString())));
    }

    @Test
    @Transactional
    void getProgramVersion() throws Exception {
        // Initialize the database
        insertedProgramVersion = programVersionRepository.saveAndFlush(programVersion);

        // Get the programVersion
        restProgramVersionMockMvc
            .perform(get(ENTITY_API_URL_ID, programVersion.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(programVersion.getId().intValue()))
            .andExpect(jsonPath("$.version").value(DEFAULT_VERSION))
            .andExpect(jsonPath("$.isActive").value(DEFAULT_IS_ACTIVE.booleanValue()))
            .andExpect(jsonPath("$.publishDate").value(DEFAULT_PUBLISH_DATE.toString()));
    }

    @Test
    @Transactional
    void getNonExistingProgramVersion() throws Exception {
        // Get the programVersion
        restProgramVersionMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgramVersion() throws Exception {
        // Initialize the database
        insertedProgramVersion = programVersionRepository.saveAndFlush(programVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the programVersion
        ProgramVersion updatedProgramVersion = programVersionRepository.findById(programVersion.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProgramVersion are not directly saved in db
        em.detach(updatedProgramVersion);
        updatedProgramVersion.version(UPDATED_VERSION).isActive(UPDATED_IS_ACTIVE).publishDate(UPDATED_PUBLISH_DATE);
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(updatedProgramVersion);

        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programVersionDTO))
            )
            .andExpect(status().isOk());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProgramVersionToMatchAllProperties(updatedProgramVersion);
    }

    @Test
    @Transactional
    void putNonExistingProgramVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programVersion.setId(longCount.incrementAndGet());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programVersionDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgramVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programVersion.setId(longCount.incrementAndGet());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgramVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programVersion.setId(longCount.incrementAndGet());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programVersionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgramVersionWithPatch() throws Exception {
        // Initialize the database
        insertedProgramVersion = programVersionRepository.saveAndFlush(programVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the programVersion using partial update
        ProgramVersion partialUpdatedProgramVersion = new ProgramVersion();
        partialUpdatedProgramVersion.setId(programVersion.getId());

        partialUpdatedProgramVersion.version(UPDATED_VERSION).isActive(UPDATED_IS_ACTIVE);

        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProgramVersion))
            )
            .andExpect(status().isOk());

        // Validate the ProgramVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProgramVersionUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedProgramVersion, programVersion),
            getPersistedProgramVersion(programVersion)
        );
    }

    @Test
    @Transactional
    void fullUpdateProgramVersionWithPatch() throws Exception {
        // Initialize the database
        insertedProgramVersion = programVersionRepository.saveAndFlush(programVersion);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the programVersion using partial update
        ProgramVersion partialUpdatedProgramVersion = new ProgramVersion();
        partialUpdatedProgramVersion.setId(programVersion.getId());

        partialUpdatedProgramVersion.version(UPDATED_VERSION).isActive(UPDATED_IS_ACTIVE).publishDate(UPDATED_PUBLISH_DATE);

        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgramVersion.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProgramVersion))
            )
            .andExpect(status().isOk());

        // Validate the ProgramVersion in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProgramVersionUpdatableFieldsEquals(partialUpdatedProgramVersion, getPersistedProgramVersion(partialUpdatedProgramVersion));
    }

    @Test
    @Transactional
    void patchNonExistingProgramVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programVersion.setId(longCount.incrementAndGet());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programVersionDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgramVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programVersion.setId(longCount.incrementAndGet());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(programVersionDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgramVersion() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        programVersion.setId(longCount.incrementAndGet());

        // Create the ProgramVersion
        ProgramVersionDTO programVersionDTO = programVersionMapper.toDto(programVersion);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramVersionMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(programVersionDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ProgramVersion in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgramVersion() throws Exception {
        // Initialize the database
        insertedProgramVersion = programVersionRepository.saveAndFlush(programVersion);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the programVersion
        restProgramVersionMockMvc
            .perform(delete(ENTITY_API_URL_ID, programVersion.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return programVersionRepository.count();
    }

    protected void assertIncrementedRepositoryCount(long countBefore) {
        assertThat(countBefore + 1).isEqualTo(getRepositoryCount());
    }

    protected void assertDecrementedRepositoryCount(long countBefore) {
        assertThat(countBefore - 1).isEqualTo(getRepositoryCount());
    }

    protected void assertSameRepositoryCount(long countBefore) {
        assertThat(countBefore).isEqualTo(getRepositoryCount());
    }

    protected ProgramVersion getPersistedProgramVersion(ProgramVersion programVersion) {
        return programVersionRepository.findById(programVersion.getId()).orElseThrow();
    }

    protected void assertPersistedProgramVersionToMatchAllProperties(ProgramVersion expectedProgramVersion) {
        assertProgramVersionAllPropertiesEquals(expectedProgramVersion, getPersistedProgramVersion(expectedProgramVersion));
    }

    protected void assertPersistedProgramVersionToMatchUpdatableProperties(ProgramVersion expectedProgramVersion) {
        assertProgramVersionAllUpdatablePropertiesEquals(expectedProgramVersion, getPersistedProgramVersion(expectedProgramVersion));
    }
}
