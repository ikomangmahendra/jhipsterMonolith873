package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.ProgramAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.domain.enumeration.ProgramStatus;
import com.mycompany.myapp.domain.enumeration.ProgramType;
import com.mycompany.myapp.repository.ProgramRepository;
import com.mycompany.myapp.service.ProgramService;
import com.mycompany.myapp.service.dto.ProgramDTO;
import com.mycompany.myapp.service.mapper.ProgramMapper;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link ProgramResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class ProgramResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final ProgramType DEFAULT_TYPE = ProgramType.PERMANENT;
    private static final ProgramType UPDATED_TYPE = ProgramType.TIME_LIMITED;

    private static final Instant DEFAULT_START_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_START_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final Instant DEFAULT_END_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_END_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final ProgramStatus DEFAULT_STATUS = ProgramStatus.PENDING;
    private static final ProgramStatus UPDATED_STATUS = ProgramStatus.ACTIVE;

    private static final Integer DEFAULT_EXTERNAL_SYSTEM_LKP = 1;
    private static final Integer UPDATED_EXTERNAL_SYSTEM_LKP = 2;

    private static final Boolean DEFAULT_IS_ENABLE_FOLLOW_UP = false;
    private static final Boolean UPDATED_IS_ENABLE_FOLLOW_UP = true;

    private static final Boolean DEFAULT_IS_NSF_SURVEY_ACCESS = false;
    private static final Boolean UPDATED_IS_NSF_SURVEY_ACCESS = true;

    private static final Boolean DEFAULT_IS_OPT_OUT_ALLOWED = false;
    private static final Boolean UPDATED_IS_OPT_OUT_ALLOWED = true;

    private static final String ENTITY_API_URL = "/api/programs";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private ProgramRepository programRepository;

    @Mock
    private ProgramRepository programRepositoryMock;

    @Autowired
    private ProgramMapper programMapper;

    @Mock
    private ProgramService programServiceMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restProgramMockMvc;

    private Program program;

    private Program insertedProgram;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createEntity() {
        return new Program()
            .name(DEFAULT_NAME)
            .type(DEFAULT_TYPE)
            .startDate(DEFAULT_START_DATE)
            .endDate(DEFAULT_END_DATE)
            .status(DEFAULT_STATUS)
            .externalSystemLkp(DEFAULT_EXTERNAL_SYSTEM_LKP)
            .isEnableFollowUp(DEFAULT_IS_ENABLE_FOLLOW_UP)
            .isNsfSurveyAccess(DEFAULT_IS_NSF_SURVEY_ACCESS)
            .isOptOutAllowed(DEFAULT_IS_OPT_OUT_ALLOWED);
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Program createUpdatedEntity() {
        return new Program()
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .externalSystemLkp(UPDATED_EXTERNAL_SYSTEM_LKP)
            .isEnableFollowUp(UPDATED_IS_ENABLE_FOLLOW_UP)
            .isNsfSurveyAccess(UPDATED_IS_NSF_SURVEY_ACCESS)
            .isOptOutAllowed(UPDATED_IS_OPT_OUT_ALLOWED);
    }

    @BeforeEach
    public void initTest() {
        program = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedProgram != null) {
            programRepository.delete(insertedProgram);
            insertedProgram = null;
        }
    }

    @Test
    @Transactional
    void createProgram() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);
        var returnedProgramDTO = om.readValue(
            restProgramMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            ProgramDTO.class
        );

        // Validate the Program in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedProgram = programMapper.toEntity(returnedProgramDTO);
        assertProgramUpdatableFieldsEquals(returnedProgram, getPersistedProgram(returnedProgram));

        insertedProgram = returnedProgram;
    }

    @Test
    @Transactional
    void createProgramWithExistingId() throws Exception {
        // Create the Program with an existing ID
        program.setId(1L);
        ProgramDTO programDTO = programMapper.toDto(program);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setName(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setType(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setStatus(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkExternalSystemLkpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setExternalSystemLkp(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsEnableFollowUpIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setIsEnableFollowUp(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsNsfSurveyAccessIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setIsNsfSurveyAccess(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkIsOptOutAllowedIsRequired() throws Exception {
        long databaseSizeBeforeTest = getRepositoryCount();
        // set the field null
        program.setIsOptOutAllowed(null);

        // Create the Program, which fails.
        ProgramDTO programDTO = programMapper.toDto(program);

        restProgramMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isBadRequest());

        assertSameRepositoryCount(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllPrograms() throws Exception {
        // Initialize the database
        insertedProgram = programRepository.saveAndFlush(program);

        // Get all the programList
        restProgramMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(program.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE.toString())))
            .andExpect(jsonPath("$.[*].startDate").value(hasItem(DEFAULT_START_DATE.toString())))
            .andExpect(jsonPath("$.[*].endDate").value(hasItem(DEFAULT_END_DATE.toString())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].externalSystemLkp").value(hasItem(DEFAULT_EXTERNAL_SYSTEM_LKP)))
            .andExpect(jsonPath("$.[*].isEnableFollowUp").value(hasItem(DEFAULT_IS_ENABLE_FOLLOW_UP.booleanValue())))
            .andExpect(jsonPath("$.[*].isNsfSurveyAccess").value(hasItem(DEFAULT_IS_NSF_SURVEY_ACCESS.booleanValue())))
            .andExpect(jsonPath("$.[*].isOptOutAllowed").value(hasItem(DEFAULT_IS_OPT_OUT_ALLOWED.booleanValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProgramsWithEagerRelationshipsIsEnabled() throws Exception {
        when(programServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProgramMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(programServiceMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllProgramsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(programServiceMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restProgramMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(programRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getProgram() throws Exception {
        // Initialize the database
        insertedProgram = programRepository.saveAndFlush(program);

        // Get the program
        restProgramMockMvc
            .perform(get(ENTITY_API_URL_ID, program.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(program.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE.toString()))
            .andExpect(jsonPath("$.startDate").value(DEFAULT_START_DATE.toString()))
            .andExpect(jsonPath("$.endDate").value(DEFAULT_END_DATE.toString()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.externalSystemLkp").value(DEFAULT_EXTERNAL_SYSTEM_LKP))
            .andExpect(jsonPath("$.isEnableFollowUp").value(DEFAULT_IS_ENABLE_FOLLOW_UP.booleanValue()))
            .andExpect(jsonPath("$.isNsfSurveyAccess").value(DEFAULT_IS_NSF_SURVEY_ACCESS.booleanValue()))
            .andExpect(jsonPath("$.isOptOutAllowed").value(DEFAULT_IS_OPT_OUT_ALLOWED.booleanValue()));
    }

    @Test
    @Transactional
    void getNonExistingProgram() throws Exception {
        // Get the program
        restProgramMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingProgram() throws Exception {
        // Initialize the database
        insertedProgram = programRepository.saveAndFlush(program);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the program
        Program updatedProgram = programRepository.findById(program.getId()).orElseThrow();
        // Disconnect from session so that the updates on updatedProgram are not directly saved in db
        em.detach(updatedProgram);
        updatedProgram
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .externalSystemLkp(UPDATED_EXTERNAL_SYSTEM_LKP)
            .isEnableFollowUp(UPDATED_IS_ENABLE_FOLLOW_UP)
            .isNsfSurveyAccess(UPDATED_IS_NSF_SURVEY_ACCESS)
            .isOptOutAllowed(UPDATED_IS_OPT_OUT_ALLOWED);
        ProgramDTO programDTO = programMapper.toDto(updatedProgram);

        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedProgramToMatchAllProperties(updatedProgram);
    }

    @Test
    @Transactional
    void putNonExistingProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        program.setId(longCount.incrementAndGet());

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, programDTO.getId()).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        program.setId(longCount.incrementAndGet());

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(programDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        program.setId(longCount.incrementAndGet());

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateProgramWithPatch() throws Exception {
        // Initialize the database
        insertedProgram = programRepository.saveAndFlush(program);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the program using partial update
        Program partialUpdatedProgram = new Program();
        partialUpdatedProgram.setId(program.getId());

        partialUpdatedProgram
            .startDate(UPDATED_START_DATE)
            .status(UPDATED_STATUS)
            .externalSystemLkp(UPDATED_EXTERNAL_SYSTEM_LKP)
            .isNsfSurveyAccess(UPDATED_IS_NSF_SURVEY_ACCESS);

        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProgramUpdatableFieldsEquals(createUpdateProxyForBean(partialUpdatedProgram, program), getPersistedProgram(program));
    }

    @Test
    @Transactional
    void fullUpdateProgramWithPatch() throws Exception {
        // Initialize the database
        insertedProgram = programRepository.saveAndFlush(program);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the program using partial update
        Program partialUpdatedProgram = new Program();
        partialUpdatedProgram.setId(program.getId());

        partialUpdatedProgram
            .name(UPDATED_NAME)
            .type(UPDATED_TYPE)
            .startDate(UPDATED_START_DATE)
            .endDate(UPDATED_END_DATE)
            .status(UPDATED_STATUS)
            .externalSystemLkp(UPDATED_EXTERNAL_SYSTEM_LKP)
            .isEnableFollowUp(UPDATED_IS_ENABLE_FOLLOW_UP)
            .isNsfSurveyAccess(UPDATED_IS_NSF_SURVEY_ACCESS)
            .isOptOutAllowed(UPDATED_IS_OPT_OUT_ALLOWED);

        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedProgram.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedProgram))
            )
            .andExpect(status().isOk());

        // Validate the Program in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertProgramUpdatableFieldsEquals(partialUpdatedProgram, getPersistedProgram(partialUpdatedProgram));
    }

    @Test
    @Transactional
    void patchNonExistingProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        program.setId(longCount.incrementAndGet());

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, programDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(programDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        program.setId(longCount.incrementAndGet());

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(programDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamProgram() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        program.setId(longCount.incrementAndGet());

        // Create the Program
        ProgramDTO programDTO = programMapper.toDto(program);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restProgramMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(programDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Program in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteProgram() throws Exception {
        // Initialize the database
        insertedProgram = programRepository.saveAndFlush(program);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the program
        restProgramMockMvc
            .perform(delete(ENTITY_API_URL_ID, program.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return programRepository.count();
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

    protected Program getPersistedProgram(Program program) {
        return programRepository.findById(program.getId()).orElseThrow();
    }

    protected void assertPersistedProgramToMatchAllProperties(Program expectedProgram) {
        assertProgramAllPropertiesEquals(expectedProgram, getPersistedProgram(expectedProgram));
    }

    protected void assertPersistedProgramToMatchUpdatableProperties(Program expectedProgram) {
        assertProgramAllUpdatablePropertiesEquals(expectedProgram, getPersistedProgram(expectedProgram));
    }
}
