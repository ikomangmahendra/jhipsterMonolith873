package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.FormVariableAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.FormVariable;
import com.mycompany.myapp.domain.enumeration.FormVariableType;
import com.mycompany.myapp.repository.FormVariableRepository;
import com.mycompany.myapp.service.dto.FormVariableDTO;
import com.mycompany.myapp.service.mapper.FormVariableMapper;
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
 * Integration tests for the {@link FormVariableResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class FormVariableResourceIT {

    private static final String DEFAULT_SECTION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_CODE = "BBBBBBBBBB";

    private static final String DEFAULT_SECTION_NAME = "AAAAAAAAAA";
    private static final String UPDATED_SECTION_NAME = "BBBBBBBBBB";

    private static final FormVariableType DEFAULT_FORM_VARIABLE_TYPE = FormVariableType.ACUTE;
    private static final FormVariableType UPDATED_FORM_VARIABLE_TYPE = FormVariableType.OUTCOME_ASSESSMENT;

    private static final Integer DEFAULT_ORDER_INDEX = 1;
    private static final Integer UPDATED_ORDER_INDEX = 2;

    private static final String ENTITY_API_URL = "/api/form-variables";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private FormVariableRepository formVariableRepository;

    @Autowired
    private FormVariableMapper formVariableMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restFormVariableMockMvc;

    private FormVariable formVariable;

    private FormVariable insertedFormVariable;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormVariable createEntity() {
        return new FormVariable()
            .sectionCode(DEFAULT_SECTION_CODE)
            .sectionName(DEFAULT_SECTION_NAME)
            .formVariableType(DEFAULT_FORM_VARIABLE_TYPE)
            .orderIndex(DEFAULT_ORDER_INDEX);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static FormVariable createUpdatedEntity() {
        return new FormVariable()
            .sectionCode(UPDATED_SECTION_CODE)
            .sectionName(UPDATED_SECTION_NAME)
            .formVariableType(UPDATED_FORM_VARIABLE_TYPE)
            .orderIndex(UPDATED_ORDER_INDEX);
    }

    @BeforeEach
    public void initTest() {
        formVariable = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedFormVariable != null) {
            formVariableRepository.delete(insertedFormVariable);
            insertedFormVariable = null;
        }
    }

    @Test
    @Transactional
    void createFormVariable() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);
        var returnedFormVariableDTO = om.readValue(
            restFormVariableMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            FormVariableDTO.class
        );

        // Validate the FormVariable in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedFormVariable = formVariableMapper.toEntity(returnedFormVariableDTO);
        assertFormVariableUpdatableFieldsEquals(returnedFormVariable, getPersistedFormVariable(returnedFormVariable));

        insertedFormVariable = returnedFormVariable;
    }

    @Test
    @Transactional
    void createFormVariableWithExistingId() throws Exception {
        // Create the FormVariable with an existing ID
        formVariable.setId(1L);
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restFormVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isBadRequest());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSectionCodeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        formVariable.setSectionCode(null);

        // Create the FormVariable, which fails.
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        restFormVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSectionNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        formVariable.setSectionName(null);

        // Create the FormVariable, which fails.
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        restFormVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkFormVariableTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        formVariable.setFormVariableType(null);

        // Create the FormVariable, which fails.
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        restFormVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkOrderIndexIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        formVariable.setOrderIndex(null);

        // Create the FormVariable, which fails.
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        restFormVariableMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllFormVariables() throws Exception {
        // Initialize the database
        insertedFormVariable = formVariableRepository.saveAndFlush(formVariable);

        // Get all the formVariableList
        restFormVariableMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(formVariable.getId().intValue())))
            .andExpect(jsonPath("$.[*].sectionCode").value(hasItem(DEFAULT_SECTION_CODE)))
            .andExpect(jsonPath("$.[*].sectionName").value(hasItem(DEFAULT_SECTION_NAME)))
            .andExpect(jsonPath("$.[*].formVariableType").value(hasItem(DEFAULT_FORM_VARIABLE_TYPE.toString())))
            .andExpect(jsonPath("$.[*].orderIndex").value(hasItem(DEFAULT_ORDER_INDEX)));
    }

    @Test
    @Transactional
    void getFormVariable() throws Exception {
        // Initialize the database
        insertedFormVariable = formVariableRepository.saveAndFlush(formVariable);

        // Get the formVariable
        restFormVariableMockMvc
            .perform(get(ENTITY_API_URL_ID, formVariable.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(formVariable.getId().intValue()))
            .andExpect(jsonPath("$.sectionCode").value(DEFAULT_SECTION_CODE))
            .andExpect(jsonPath("$.sectionName").value(DEFAULT_SECTION_NAME))
            .andExpect(jsonPath("$.formVariableType").value(DEFAULT_FORM_VARIABLE_TYPE.toString()))
            .andExpect(jsonPath("$.orderIndex").value(DEFAULT_ORDER_INDEX));
    }

    @Test
    @Transactional
    void getNonExistingFormVariable() throws Exception {
        // Get the formVariable
        restFormVariableMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingFormVariable() throws Exception {
        // Initialize the database
        insertedFormVariable = formVariableRepository.saveAndFlush(formVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formVariable
        FormVariable updatedFormVariable = formVariableRepository.findById(formVariable.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedFormVariable are not directly saved in db
        em.detach(updatedFormVariable);
        updatedFormVariable
            .sectionCode(UPDATED_SECTION_CODE)
            .sectionName(UPDATED_SECTION_NAME)
            .formVariableType(UPDATED_FORM_VARIABLE_TYPE)
            .orderIndex(UPDATED_ORDER_INDEX);
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(updatedFormVariable);

        restFormVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formVariableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formVariableDTO))
            )
            .andExpect(status().isOk());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedFormVariableToMatchAllProperties(updatedFormVariable);
    }

    @Test
    @Transactional
    void putNonExistingFormVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formVariable.setId(longCount.incrementAndGet());

        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, formVariableDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchFormVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formVariable.setId(longCount.incrementAndGet());

        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormVariableMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(formVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamFormVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formVariable.setId(longCount.incrementAndGet());

        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormVariableMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateFormVariableWithPatch() throws Exception {
        // Initialize the database
        insertedFormVariable = formVariableRepository.saveAndFlush(formVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formVariable using partial update
        FormVariable partialUpdatedFormVariable = new FormVariable();
        partialUpdatedFormVariable.setId(formVariable.getId());

        partialUpdatedFormVariable.sectionName(UPDATED_SECTION_NAME);

        restFormVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormVariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormVariable))
            )
            .andExpect(status().isOk());

        // Validate the FormVariable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormVariableUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedFormVariable, formVariable),
            getPersistedFormVariable(formVariable)
        );
    }

    @Test
    @Transactional
    void fullUpdateFormVariableWithPatch() throws Exception {
        // Initialize the database
        insertedFormVariable = formVariableRepository.saveAndFlush(formVariable);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the formVariable using partial update
        FormVariable partialUpdatedFormVariable = new FormVariable();
        partialUpdatedFormVariable.setId(formVariable.getId());

        partialUpdatedFormVariable
            .sectionCode(UPDATED_SECTION_CODE)
            .sectionName(UPDATED_SECTION_NAME)
            .formVariableType(UPDATED_FORM_VARIABLE_TYPE)
            .orderIndex(UPDATED_ORDER_INDEX);

        restFormVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedFormVariable.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedFormVariable))
            )
            .andExpect(status().isOk());

        // Validate the FormVariable in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertFormVariableUpdatableFieldsEquals(partialUpdatedFormVariable, getPersistedFormVariable(partialUpdatedFormVariable));
    }

    @Test
    @Transactional
    void patchNonExistingFormVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formVariable.setId(longCount.incrementAndGet());

        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restFormVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, formVariableDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchFormVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formVariable.setId(longCount.incrementAndGet());

        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormVariableMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(formVariableDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamFormVariable() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        formVariable.setId(longCount.incrementAndGet());

        // Create the FormVariable
        FormVariableDTO formVariableDTO = formVariableMapper.toDto(formVariable);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restFormVariableMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(formVariableDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the FormVariable in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteFormVariable() throws Exception {
        // Initialize the database
        insertedFormVariable = formVariableRepository.saveAndFlush(formVariable);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the formVariable
        restFormVariableMockMvc
            .perform(delete(ENTITY_API_URL_ID, formVariable.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return formVariableRepository.count();
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

    protected FormVariable getPersistedFormVariable(FormVariable formVariable) {
        return formVariableRepository.findById(formVariable.getId()).orElseThrow();
    }

    protected void assertPersistedFormVariableToMatchAllProperties(FormVariable expectedFormVariable) {
        assertFormVariableAllPropertiesEquals(expectedFormVariable, getPersistedFormVariable(expectedFormVariable));
    }

    protected void assertPersistedFormVariableToMatchUpdatableProperties(FormVariable expectedFormVariable) {
        assertFormVariableAllUpdatablePropertiesEquals(expectedFormVariable, getPersistedFormVariable(expectedFormVariable));
    }
}
