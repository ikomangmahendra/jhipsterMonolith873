package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.AccuteProgramVariableAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.AccuteProgramVariable;
import com.mycompany.myapp.repository.AccuteProgramVariableRepository;
import com.mycompany.myapp.service.dto.AccuteProgramVariableDTO;
import com.mycompany.myapp.service.mapper.AccuteProgramVariableMapper;
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
 * Integration tests for the {@link AccuteProgramVariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AccuteProgramVariableResourceIT {

    private static final String DEFAULT_SECTION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_ORDER_INDEX = 1;
    private static final Integer UPDATED_ORDER_INDEX = 2;

    private static final String DEFAULT_JSON_SCHEMA = "AAAAAAAAAA";
    private static final String UPDATED_JSON_SCHEMA = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/accute-program-variables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private AccuteProgramVariableRepository accuteProgramVariableRepository;

    @Autowired
    private AccuteProgramVariableMapper accuteProgramVariableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAccuteProgramVariableMockMvc;

    private AccuteProgramVariable accuteProgramVariable;

    private AccuteProgramVariable insertedAccuteProgramVariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccuteProgramVariable createEntity() {
        return new AccuteProgramVariable()
            .sectionId(DEFAULT_SECTION_ID)
            .sectionName(DEFAULT_SECTION_NAME)
            .orderIndex(DEFAULT_ORDER_INDEX)
            .jsonSchema(DEFAULT_JSON_SCHEMA);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static AccuteProgramVariable createUpdatedEntity() {
        return new AccuteProgramVariable()
            .sectionId(UPDATED_SECTION_ID)
            .sectionName(UPDATED_SECTION_NAME)
            .orderIndex(UPDATED_ORDER_INDEX)
            .jsonSchema(UPDATED_JSON_SCHEMA);
    }

    @BeforeEach
    public void initTest() {
        accuteProgramVariable = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedAccuteProgramVariable != null) {
            accuteProgramVariableRepository.delete(insertedAccuteProgramVariable);
            insertedAccuteProgramVariable = null;
        }
    }

    @Test
    @Transactional
    void createAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);
        var returnedAccuteProgramVariableDTO = om.readValue(
            restAccuteProgramVariableMockMvc
                .perform(
                    post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accuteProgramVariableDTO))
                )
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            AccuteProgramVariableDTO.class
        );

        // Validate the AccuteProgramVariable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedAccuteProgramVariable = accuteProgramVariableMapper.toEntity(returnedAccuteProgramVariableDTO);
        assertAccuteProgramVariableUpdatableFieldsEquals(
            returnedAccuteProgramVariable,
            getPersistedAccuteProgramVariable(returnedAccuteProgramVariable)
        );

        insertedAccuteProgramVariable = returnedAccuteProgramVariable;
    }

    @Test
    @Transactional
    void createAccuteProgramVariableWithExistingId() throws Exception {
        // Create the AccuteProgramVariable with an existing ID
        accuteProgramVariable.setId(1L);
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAccuteProgramVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accuteProgramVariableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectionIdIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accuteProgramVariable.setSectionId(null);

        // Create the AccuteProgramVariable, which fails.
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        restAccuteProgramVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accuteProgramVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accuteProgramVariable.setSectionName(null);

        // Create the AccuteProgramVariable, which fails.
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        restAccuteProgramVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accuteProgramVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        accuteProgramVariable.setOrderIndex(null);

        // Create the AccuteProgramVariable, which fails.
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        restAccuteProgramVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accuteProgramVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllAccuteProgramVariables() throws Exception {
        // Initialize the database
        insertedAccuteProgramVariable = accuteProgramVariableRepository.saveAndFlush(accuteProgramVariable);

        // Get all the accuteProgramVariableList
        restAccuteProgramVariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(accuteProgramVariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionId").value(hasItem(DEFAULT_SECTION_ID)))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME)))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX)))
            .andExpect(jsonPath("$.[*].jsonSchema").value(hasItem(DEFAULT_JSON_SCHEMA.toString())));
    }

    @Test
    @Transactional
    void getAccuteProgramVariable() throws Exception {
        // Initialize the database
        insertedAccuteProgramVariable = accuteProgramVariableRepository.saveAndFlush(accuteProgramVariable);

        // Get the accuteProgramVariable
        restAccuteProgramVariableMockMvc
            .perform(get(ENTITY_API_URL_ID, accuteProgramVariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(accuteProgramVariable.getId().intValue()))
            .andExpect(jsonPath("$.sectionId").value(DEFAULT_SECTION_ID))
            .andExpect(jsonPath("$.sectionName").value(DEFAULT_SECTION_NAME))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX))
            .andExpect(jsonPath("$.jsonSchema").value(DEFAULT_JSON_SCHEMA.toString()));
    }

    @Test
    @Transactional
    void getNonExistingAccuteProgramVariable() throws Exception {
        // Get the accuteProgramVariable
        restAccuteProgramVariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingAccuteProgramVariable() throws Exception {
        // Initialize the database
        insertedAccuteProgramVariable = accuteProgramVariableRepository.saveAndFlush(accuteProgramVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accuteProgramVariable
        AccuteProgramVariable updatedAccuteProgramVariable = accuteProgramVariableRepository
            .findById(accuteProgramVariable.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedAccuteProgramVariable are not directly saved in db
        em.detach(updatedAccuteProgramVariable);
        updatedAccuteProgramVariable
            .sectionId(UPDATED_SECTION_ID)
            .sectionName(UPDATED_SECTION_NAME)
            .orderIndex(UPDATED_ORDER_INDEX)
            .jsonSchema(UPDATED_JSON_SCHEMA);
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(updatedAccuteProgramVariable);

        restAccuteProgramVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accuteProgramVariableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accuteProgramVariableDTO))
            )
            .andExpect(status().isOk());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedAccuteProgramVariableToMatchAllProperties(updatedAccuteProgramVariable);
    }

    @Test
    @Transactional
    void putNonExistingAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accuteProgramVariable.setId(longCount.incrementAndGet());

        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccuteProgramVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, accuteProgramVariableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accuteProgramVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accuteProgramVariable.setId(longCount.incrementAndGet());

        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccuteProgramVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(accuteProgramVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accuteProgramVariable.setId(longCount.incrementAndGet());

        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccuteProgramVariableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(accuteProgramVariableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAccuteProgramVariableWithPatch() throws Exception {
        // Initialize the database
        insertedAccuteProgramVariable = accuteProgramVariableRepository.saveAndFlush(accuteProgramVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accuteProgramVariable using partial update
        AccuteProgramVariable partialUpdatedAccuteProgramVariable = new AccuteProgramVariable();
        partialUpdatedAccuteProgramVariable.setId(accuteProgramVariable.getId());

        partialUpdatedAccuteProgramVariable.orderIndex(UPDATED_ORDER_INDEX);

        restAccuteProgramVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccuteProgramVariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccuteProgramVariable))
            )
            .andExpect(status().isOk());

        // Validate the AccuteProgramVariable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccuteProgramVariableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedAccuteProgramVariable, accuteProgramVariable),
            getPersistedAccuteProgramVariable(accuteProgramVariable)
        );
    }

    @Test
    @Transactional
    void fullUpdateAccuteProgramVariableWithPatch() throws Exception {
        // Initialize the database
        insertedAccuteProgramVariable = accuteProgramVariableRepository.saveAndFlush(accuteProgramVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the accuteProgramVariable using partial update
        AccuteProgramVariable partialUpdatedAccuteProgramVariable = new AccuteProgramVariable();
        partialUpdatedAccuteProgramVariable.setId(accuteProgramVariable.getId());

        partialUpdatedAccuteProgramVariable
            .sectionId(UPDATED_SECTION_ID)
            .sectionName(UPDATED_SECTION_NAME)
            .orderIndex(UPDATED_ORDER_INDEX)
            .jsonSchema(UPDATED_JSON_SCHEMA);

        restAccuteProgramVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAccuteProgramVariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedAccuteProgramVariable))
            )
            .andExpect(status().isOk());

        // Validate the AccuteProgramVariable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertAccuteProgramVariableUpdatableFieldsEquals(
            partialUpdatedAccuteProgramVariable,
            getPersistedAccuteProgramVariable(partialUpdatedAccuteProgramVariable)
        );
    }

    @Test
    @Transactional
    void patchNonExistingAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accuteProgramVariable.setId(longCount.incrementAndGet());

        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAccuteProgramVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, accuteProgramVariableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accuteProgramVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accuteProgramVariable.setId(longCount.incrementAndGet());

        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccuteProgramVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(accuteProgramVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAccuteProgramVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        accuteProgramVariable.setId(longCount.incrementAndGet());

        // Create the AccuteProgramVariable
        AccuteProgramVariableDTO accuteProgramVariableDTO = accuteProgramVariableMapper.toDto(accuteProgramVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAccuteProgramVariableMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(accuteProgramVariableDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the AccuteProgramVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAccuteProgramVariable() throws Exception {
        // Initialize the database
        insertedAccuteProgramVariable = accuteProgramVariableRepository.saveAndFlush(accuteProgramVariable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the accuteProgramVariable
        restAccuteProgramVariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, accuteProgramVariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return accuteProgramVariableRepository.count();
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

    protected AccuteProgramVariable getPersistedAccuteProgramVariable(AccuteProgramVariable accuteProgramVariable) {
        return accuteProgramVariableRepository.findById(accuteProgramVariable.getId()).orElseThrow();
    }

    protected void assertPersistedAccuteProgramVariableToMatchAllProperties(AccuteProgramVariable expectedAccuteProgramVariable) {
        assertAccuteProgramVariableAllPropertiesEquals(
            expectedAccuteProgramVariable,
            getPersistedAccuteProgramVariable(expectedAccuteProgramVariable)
        );
    }

    protected void assertPersistedAccuteProgramVariableToMatchUpdatableProperties(AccuteProgramVariable expectedAccuteProgramVariable) {
        assertAccuteProgramVariableAllUpdatablePropertiesEquals(
            expectedAccuteProgramVariable,
            getPersistedAccuteProgramVariable(expectedAccuteProgramVariable)
        );
    }
}
