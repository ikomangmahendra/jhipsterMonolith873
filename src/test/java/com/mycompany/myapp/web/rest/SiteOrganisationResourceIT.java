package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SiteOrganisationAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SiteOrganisation;
import com.mycompany.myapp.repository.SiteOrganisationRepository;
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
 * Integration tests for the {@link SiteOrganisationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteOrganisationResourceIT {

    private static final String ENTITY_API_URL = "/api/site-organisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SiteOrganisationRepository siteOrganisationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteOrganisationMockMvc;

    private SiteOrganisation siteOrganisation;

    private SiteOrganisation insertedSiteOrganisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteOrganisation createEntity() {
        return new SiteOrganisation();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteOrganisation createUpdatedEntity() {
        return new SiteOrganisation();
    }

    @BeforeEach
    public void initTest() {
        siteOrganisation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSiteOrganisation != null) {
            siteOrganisationRepository.delete(insertedSiteOrganisation);
            insertedSiteOrganisation = null;
        }
    }

    @Test
    @Transactional
    void createSiteOrganisation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SiteOrganisation
        var returnedSiteOrganisation = om.readValue(
            restSiteOrganisationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteOrganisation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SiteOrganisation.class
        );

        // Validate the SiteOrganisation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertSiteOrganisationUpdatableFieldsEquals(returnedSiteOrganisation, getPersistedSiteOrganisation(returnedSiteOrganisation));

        insertedSiteOrganisation = returnedSiteOrganisation;
    }

    @Test
    @Transactional
    void createSiteOrganisationWithExistingId() throws Exception {
        // Create the SiteOrganisation with an existing ID
        siteOrganisation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteOrganisation)))
            .andExpect(status().isBadRequest());

        // Validate the SiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSiteOrganisations() throws Exception {
        // Initialize the database
        insertedSiteOrganisation = siteOrganisationRepository.saveAndFlush(siteOrganisation);

        // Get all the siteOrganisationList
        restSiteOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteOrganisation.getId().intValue())));
    }

    @Test
    @Transactional
    void getSiteOrganisation() throws Exception {
        // Initialize the database
        insertedSiteOrganisation = siteOrganisationRepository.saveAndFlush(siteOrganisation);

        // Get the siteOrganisation
        restSiteOrganisationMockMvc
            .perform(get(ENTITY_API_URL_ID, siteOrganisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteOrganisation.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSiteOrganisation() throws Exception {
        // Get the siteOrganisation
        restSiteOrganisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteSiteOrganisation() throws Exception {
        // Initialize the database
        insertedSiteOrganisation = siteOrganisationRepository.saveAndFlush(siteOrganisation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the siteOrganisation
        restSiteOrganisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteOrganisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return siteOrganisationRepository.count();
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

    protected SiteOrganisation getPersistedSiteOrganisation(SiteOrganisation siteOrganisation) {
        return siteOrganisationRepository.findById(siteOrganisation.getId()).orElseThrow();
    }

    protected void assertPersistedSiteOrganisationToMatchAllProperties(SiteOrganisation expectedSiteOrganisation) {
        assertSiteOrganisationAllPropertiesEquals(expectedSiteOrganisation, getPersistedSiteOrganisation(expectedSiteOrganisation));
    }

    protected void assertPersistedSiteOrganisationToMatchUpdatableProperties(SiteOrganisation expectedSiteOrganisation) {
        assertSiteOrganisationAllUpdatablePropertiesEquals(
            expectedSiteOrganisation,
            getPersistedSiteOrganisation(expectedSiteOrganisation)
        );
    }
}
