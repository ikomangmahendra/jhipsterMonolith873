package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.SiteOrgTempAsserts.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.SiteOrgTemp;
import com.mycompany.myapp.repository.SiteOrgTempRepository;
import com.mycompany.myapp.service.dto.SiteOrgTempDTO;
import com.mycompany.myapp.service.mapper.SiteOrgTempMapper;
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
 * Integration tests for the {@link SiteOrgTempResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class SiteOrgTempResourceIT {

    private static final String ENTITY_API_URL = "/api/site-org-temps";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    @Autowired
    private ObjectMapper om;

    @Autowired
    private SiteOrgTempRepository siteOrgTempRepository;

    @Autowired
    private SiteOrgTempMapper siteOrgTempMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restSiteOrgTempMockMvc;

    private SiteOrgTemp siteOrgTemp;

    private SiteOrgTemp insertedSiteOrgTemp;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteOrgTemp createEntity() {
        return new SiteOrgTemp();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static SiteOrgTemp createUpdatedEntity() {
        return new SiteOrgTemp();
    }

    @BeforeEach
    public void initTest() {
        siteOrgTemp = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedSiteOrgTemp != null) {
            siteOrgTempRepository.delete(insertedSiteOrgTemp);
            insertedSiteOrgTemp = null;
        }
    }

    @Test
    @Transactional
    void createSiteOrgTemp() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the SiteOrgTemp
        SiteOrgTempDTO siteOrgTempDTO = siteOrgTempMapper.toDto(siteOrgTemp);
        var returnedSiteOrgTempDTO = om.readValue(
            restSiteOrgTempMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteOrgTempDTO)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            SiteOrgTempDTO.class
        );

        // Validate the SiteOrgTemp in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        var returnedSiteOrgTemp = siteOrgTempMapper.toEntity(returnedSiteOrgTempDTO);
        assertSiteOrgTempUpdatableFieldsEquals(returnedSiteOrgTemp, getPersistedSiteOrgTemp(returnedSiteOrgTemp));

        insertedSiteOrgTemp = returnedSiteOrgTemp;
    }

    @Test
    @Transactional
    void createSiteOrgTempWithExistingId() throws Exception {
        // Create the SiteOrgTemp with an existing ID
        siteOrgTemp.setId(1L);
        SiteOrgTempDTO siteOrgTempDTO = siteOrgTempMapper.toDto(siteOrgTemp);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restSiteOrgTempMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(siteOrgTempDTO)))
            .andExpect(status().isBadRequest());

        // Validate the SiteOrgTemp in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllSiteOrgTemps() throws Exception {
        // Initialize the database
        insertedSiteOrgTemp = siteOrgTempRepository.saveAndFlush(siteOrgTemp);

        // Get all the siteOrgTempList
        restSiteOrgTempMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(siteOrgTemp.getId().intValue())));
    }

    @Test
    @Transactional
    void getSiteOrgTemp() throws Exception {
        // Initialize the database
        insertedSiteOrgTemp = siteOrgTempRepository.saveAndFlush(siteOrgTemp);

        // Get the siteOrgTemp
        restSiteOrgTempMockMvc
            .perform(get(ENTITY_API_URL_ID, siteOrgTemp.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(siteOrgTemp.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingSiteOrgTemp() throws Exception {
        // Get the siteOrgTemp
        restSiteOrgTempMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void deleteSiteOrgTemp() throws Exception {
        // Initialize the database
        insertedSiteOrgTemp = siteOrgTempRepository.saveAndFlush(siteOrgTemp);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the siteOrgTemp
        restSiteOrgTempMockMvc
            .perform(delete(ENTITY_API_URL_ID, siteOrgTemp.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return siteOrgTempRepository.count();
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

    protected SiteOrgTemp getPersistedSiteOrgTemp(SiteOrgTemp siteOrgTemp) {
        return siteOrgTempRepository.findById(siteOrgTemp.getId()).orElseThrow();
    }

    protected void assertPersistedSiteOrgTempToMatchAllProperties(SiteOrgTemp expectedSiteOrgTemp) {
        assertSiteOrgTempAllPropertiesEquals(expectedSiteOrgTemp, getPersistedSiteOrgTemp(expectedSiteOrgTemp));
    }

    protected void assertPersistedSiteOrgTempToMatchUpdatableProperties(SiteOrgTemp expectedSiteOrgTemp) {
        assertSiteOrgTempAllUpdatablePropertiesEquals(expectedSiteOrgTemp, getPersistedSiteOrgTemp(expectedSiteOrgTemp));
    }
}
