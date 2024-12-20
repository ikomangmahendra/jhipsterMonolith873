package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserTempAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserTemp;
import com.mycompany.myapp.repository.UserTempRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link UserTempResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class UserTempResourceIT {

    private static final String ENTITY_API_URL = "/api/user-temps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserTempRepository userTempRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserTempMockMvc;

    private UserTemp userTemp;

    private UserTemp insertedUserTemp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserTemp createEntity() {
        return new UserTemp();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserTemp createUpdatedEntity() {
        return new UserTemp();
    }

    @BeforeEach
    public void initTest() {
        userTemp = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserTemp != null) {
            userTempRepository.delete(insertedUserTemp);
            insertedUserTemp = null;
        }
    }

    @Test
    @Transactional
    void createUserTemp() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserTemp
        var returnedUserTemp = om.readValue(
            restUserTempMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userTemp)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserTemp.class
        );

        // Validate the UserTemp in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserTempUpdatableFieldsEquals(returnedUserTemp, getPersistedUserTemp(returnedUserTemp));

        insertedUserTemp = returnedUserTemp;
    }

    @Test
    @Transactional
    void createUserTempWithExistingId() throws Exception {
        // Create the UserTemp with an existing ID
        userTemp.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserTempMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userTemp)))
            .andExpect(status().isBadRequest());

        // Validate the UserTemp in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserTemps() throws Exception {
        // Initialize the database
        insertedUserTemp = userTempRepository.saveAndFlush(userTemp);

        // Get all the userTempList
        restUserTempMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userTemp.getId().intValue())));
    }

    @Test
    @Transactional
    void getUserTemp() throws Exception {
        // Initialize the database
        insertedUserTemp = userTempRepository.saveAndFlush(userTemp);

        // Get the userTemp
        restUserTempMockMvc
            .perform(get(ENTITY_API_URL_ID, userTemp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userTemp.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserTemp() throws Exception {
        // Get the userTemp
        restUserTempMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteUserTemp() throws Exception {
        // Initialize the database
        insertedUserTemp = userTempRepository.saveAndFlush(userTemp);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userTemp
        restUserTempMockMvc
            .perform(delete(ENTITY_API_URL_ID, userTemp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userTempRepository.count();
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

    protected UserTemp getPersistedUserTemp(UserTemp userTemp) {
        return userTempRepository.findById(userTemp.getId()).orElseThrow();
    }

    protected void assertPersistedUserTempToMatchAllProperties(UserTemp expectedUserTemp) {
        assertUserTempAllPropertiesEquals(expectedUserTemp, getPersistedUserTemp(expectedUserTemp));
    }

    protected void assertPersistedUserTempToMatchUpdatableProperties(UserTemp expectedUserTemp) {
        assertUserTempAllUpdatablePropertiesEquals(expectedUserTemp, getPersistedUserTemp(expectedUserTemp));
    }
}
