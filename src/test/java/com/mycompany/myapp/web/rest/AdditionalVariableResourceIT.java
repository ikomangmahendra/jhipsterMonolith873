package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AdditionalVariableAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AdditionalVariable;
import com.mycompany.myapp.repository.AdditionalVariableRepository;
import com.mycompany.myapp.service.dto.AdditionalVariableDTO;
import com.mycompany.myapp.service.mapper.AdditionalVariableMapper;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link AdditionalVariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AdditionalVariableResourceIT {

    private static final Integer DEFAULT_PROGRAM_VERSION_ID = 1;
    private static final Integer UPDATED_PROGRAM_VERSION_ID = 2;

    private static final String DEFAULT_JSON_SCHEMA = "AAAAAAAAAA";
    private static final String UPDATED_JSON_SCHEMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/additional-variables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AdditionalVariableRepository additionalVariableRepository;

    @Autowired
    private AdditionalVariableMapper additionalVariableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAdditionalVariableMockMvc;

    private AdditionalVariable additionalVariable;

    private AdditionalVariable insertedAdditionalVariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalVariable createEntity() {
        return new AdditionalVariable().programVersionId(DEFAULT_PROGRAM_VERSION_ID).jsonSchema(DEFAULT_JSON_SCHEMA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AdditionalVariable createUpdatedEntity() {
        return new AdditionalVariable().programVersionId(UPDATED_PROGRAM_VERSION_ID).jsonSchema(UPDATED_JSON_SCHEMA);
    }

    @BeforeEach
    public void initTest() {
        additionalVariable = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAdditionalVariable != null) {
            additionalVariableRepository.delete(insertedAdditionalVariable);
            insertedAdditionalVariable = null;
        }
    }

    @Test
    @Transactional
    void createAdditionalVariable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);
        var returnedAdditionalVariableDTO = om.readValue(
            restAdditionalVariableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalVariableDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AdditionalVariableDTO.class
        );

        // Validate the AdditionalVariable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAdditionalVariable = additionalVariableMapper.toEntity(returnedAdditionalVariableDTO);
        assertAdditionalVariableUpdatableFieldsEquals(
            returnedAdditionalVariable,
            getPersistedAdditionalVariable(returnedAdditionalVariable)
        );

        insertedAdditionalVariable = returnedAdditionalVariable;
    }

    @Test
    @Transactional
    void createAdditionalVariableWithExistingId() throws Exception {
        // Create the AdditionalVariable with an existing ID
        additionalVariable.setId(1L);
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAdditionalVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalVariableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkProgramVersionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        additionalVariable.setProgramVersionId(null);

        // Create the AdditionalVariable, which fails.
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        restAdditionalVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAdditionalVariables() throws Exception {
        // Initialize the database
        insertedAdditionalVariable = additionalVariableRepository.saveAndFlush(additionalVariable);

        // Get all the additionalVariableList
        restAdditionalVariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(additionalVariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].programVersionId").value(hasItem(DEFAULT_PROGRAM_VERSION_ID)))
            .andExpect(jsonPath("$.[*].jsonSchema").value(hasItem(DEFAULT_JSON_SCHEMA.toString())));
    }

    @Test
    @Transactional
    void getAdditionalVariable() throws Exception {
        // Initialize the database
        insertedAdditionalVariable = additionalVariableRepository.saveAndFlush(additionalVariable);

        // Get the additionalVariable
        restAdditionalVariableMockMvc
            .perform(get(ENTITY_API_URL_ID, additionalVariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(additionalVariable.getId().intValue()))
            .andExpect(jsonPath("$.programVersionId").value(DEFAULT_PROGRAM_VERSION_ID))
            .andExpect(jsonPath("$.jsonSchema").value(DEFAULT_JSON_SCHEMA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAdditionalVariable() throws Exception {
        // Get the additionalVariable
        restAdditionalVariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAdditionalVariable() throws Exception {
        // Initialize the database
        insertedAdditionalVariable = additionalVariableRepository.saveAndFlush(additionalVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalVariable
        AdditionalVariable updatedAdditionalVariable = additionalVariableRepository.findById(additionalVariable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedAdditionalVariable are not directly saved in db
        em.detach(updatedAdditionalVariable);
        updatedAdditionalVariable.programVersionId(UPDATED_PROGRAM_VERSION_ID).jsonSchema(UPDATED_JSON_SCHEMA);
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(updatedAdditionalVariable);

        restAdditionalVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, additionalVariableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalVariableDTO))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAdditionalVariableToMatchAllProperties(updatedAdditionalVariable);
    }

    @Test
    @Transactional
    void putNonExistingAdditionalVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalVariable.setId(longCount.incrementAndGet());

        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, additionalVariableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAdditionalVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalVariable.setId(longCount.incrementAndGet());

        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(additionalVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAdditionalVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalVariable.setId(longCount.incrementAndGet());

        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalVariableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(additionalVariableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAdditionalVariableWithPatch() throws Exception {
        // Initialize the database
        insertedAdditionalVariable = additionalVariableRepository.saveAndFlush(additionalVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalVariable using partial update
        AdditionalVariable partialUpdatedAdditionalVariable = new AdditionalVariable();
        partialUpdatedAdditionalVariable.setId(additionalVariable.getId());

        partialUpdatedAdditionalVariable.programVersionId(UPDATED_PROGRAM_VERSION_ID).jsonSchema(UPDATED_JSON_SCHEMA);

        restAdditionalVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalVariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdditionalVariable))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalVariable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdditionalVariableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAdditionalVariable, additionalVariable),
            getPersistedAdditionalVariable(additionalVariable)
        );
    }

    @Test
    @Transactional
    void fullUpdateAdditionalVariableWithPatch() throws Exception {
        // Initialize the database
        insertedAdditionalVariable = additionalVariableRepository.saveAndFlush(additionalVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the additionalVariable using partial update
        AdditionalVariable partialUpdatedAdditionalVariable = new AdditionalVariable();
        partialUpdatedAdditionalVariable.setId(additionalVariable.getId());

        partialUpdatedAdditionalVariable.programVersionId(UPDATED_PROGRAM_VERSION_ID).jsonSchema(UPDATED_JSON_SCHEMA);

        restAdditionalVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAdditionalVariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAdditionalVariable))
            )
            .andExpect(status().isOk());

        // Validate the AdditionalVariable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAdditionalVariableUpdatableFieldsEquals(
            partialUpdatedAdditionalVariable,
            getPersistedAdditionalVariable(partialUpdatedAdditionalVariable)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAdditionalVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalVariable.setId(longCount.incrementAndGet());

        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAdditionalVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, additionalVariableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(additionalVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAdditionalVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalVariable.setId(longCount.incrementAndGet());

        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(additionalVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAdditionalVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        additionalVariable.setId(longCount.incrementAndGet());

        // Create the AdditionalVariable
        AdditionalVariableDTO additionalVariableDTO = additionalVariableMapper.toDto(additionalVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAdditionalVariableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(additionalVariableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AdditionalVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAdditionalVariable() throws Exception {
        // Initialize the database
        insertedAdditionalVariable = additionalVariableRepository.saveAndFlush(additionalVariable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the additionalVariable
        restAdditionalVariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, additionalVariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return additionalVariableRepository.count();
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

    protected AdditionalVariable getPersistedAdditionalVariable(AdditionalVariable additionalVariable) {
        return additionalVariableRepository.findById(additionalVariable.getId()).orElseThrow();
    }

    protected void assertPersistedAdditionalVariableToMatchAllProperties(AdditionalVariable expectedAdditionalVariable) {
        assertAdditionalVariableAllPropertiesEquals(expectedAdditionalVariable, getPersistedAdditionalVariable(expectedAdditionalVariable));
    }

    protected void assertPersistedAdditionalVariableToMatchUpdatableProperties(AdditionalVariable expectedAdditionalVariable) {
        assertAdditionalVariableAllUpdatablePropertiesEquals(
            expectedAdditionalVariable,
            getPersistedAdditionalVariable(expectedAdditionalVariable)
        );
    }
}
