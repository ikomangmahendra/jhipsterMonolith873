package com.mycompany.myapp.web.rest;

import static com.mycompany.myapp.domain.UserSiteOrganisationAsserts.*;
import static com.mycompany.myapp.web.rest.TestUtil.createUpdateProxyForBean;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mycompany.myapp.IntegrationTest;
import com.mycompany.myapp.domain.UserSiteOrganisation;
import com.mycompany.myapp.repository.UserSiteOrganisationRepository;
import jakarta.persistence.EntityManager;
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
 * Integration tests for the {@link UserSiteOrganisationResource} REST controller.
 */
@IntegrationTest
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
@WithMockUser
class UserSiteOrganisationResourceIT {

    private static final String ENTITY_API_URL = "/api/user-site-organisations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ObjectMapper om;

    @Autowired
    private UserSiteOrganisationRepository userSiteOrganisationRepository;

    @Mock
    private UserSiteOrganisationRepository userSiteOrganisationRepositoryMock;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restUserSiteOrganisationMockMvc;

    private UserSiteOrganisation userSiteOrganisation;

    private UserSiteOrganisation insertedUserSiteOrganisation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSiteOrganisation createEntity() {
        return new UserSiteOrganisation();
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static UserSiteOrganisation createUpdatedEntity() {
        return new UserSiteOrganisation();
    }

    @BeforeEach
    public void initTest() {
        userSiteOrganisation = createEntity();
    }

    @AfterEach
    public void cleanup() {
        if (insertedUserSiteOrganisation != null) {
            userSiteOrganisationRepository.delete(insertedUserSiteOrganisation);
            insertedUserSiteOrganisation = null;
        }
    }

    @Test
    @Transactional
    void createUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeCreate = getRepositoryCount();
        // Create the UserSiteOrganisation
        var returnedUserSiteOrganisation = om.readValue(
            restUserSiteOrganisationMockMvc
                .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSiteOrganisation)))
                .andExpect(status().isCreated())
                .andReturn()
                .getResponse()
                .getContentAsString(),
            UserSiteOrganisation.class
        );

        // Validate the UserSiteOrganisation in the database
        assertIncrementedRepositoryCount(databaseSizeBeforeCreate);
        assertUserSiteOrganisationUpdatableFieldsEquals(
            returnedUserSiteOrganisation,
            getPersistedUserSiteOrganisation(returnedUserSiteOrganisation)
        );

        insertedUserSiteOrganisation = returnedUserSiteOrganisation;
    }

    @Test
    @Transactional
    void createUserSiteOrganisationWithExistingId() throws Exception {
        // Create the UserSiteOrganisation with an existing ID
        userSiteOrganisation.setId(1L);

        long databaseSizeBeforeCreate = getRepositoryCount();

        // An entity with an existing ID cannot be created, so this API call must fail
        restUserSiteOrganisationMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSiteOrganisation)))
            .andExpect(status().isBadRequest());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllUserSiteOrganisations() throws Exception {
        // Initialize the database
        insertedUserSiteOrganisation = userSiteOrganisationRepository.saveAndFlush(userSiteOrganisation);

        // Get all the userSiteOrganisationList
        restUserSiteOrganisationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(userSiteOrganisation.getId().intValue())));
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserSiteOrganisationsWithEagerRelationshipsIsEnabled() throws Exception {
        when(userSiteOrganisationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserSiteOrganisationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=true")).andExpect(status().isOk());

        verify(userSiteOrganisationRepositoryMock, times(1)).findAllWithEagerRelationships(any());
    }

    @SuppressWarnings({ "unchecked" })
    void getAllUserSiteOrganisationsWithEagerRelationshipsIsNotEnabled() throws Exception {
        when(userSiteOrganisationRepositoryMock.findAllWithEagerRelationships(any())).thenReturn(new PageImpl(new ArrayList<>()));

        restUserSiteOrganisationMockMvc.perform(get(ENTITY_API_URL + "?eagerload=false")).andExpect(status().isOk());
        verify(userSiteOrganisationRepositoryMock, times(1)).findAll(any(Pageable.class));
    }

    @Test
    @Transactional
    void getUserSiteOrganisation() throws Exception {
        // Initialize the database
        insertedUserSiteOrganisation = userSiteOrganisationRepository.saveAndFlush(userSiteOrganisation);

        // Get the userSiteOrganisation
        restUserSiteOrganisationMockMvc
            .perform(get(ENTITY_API_URL_ID, userSiteOrganisation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(userSiteOrganisation.getId().intValue()));
    }

    @Test
    @Transactional
    void getNonExistingUserSiteOrganisation() throws Exception {
        // Get the userSiteOrganisation
        restUserSiteOrganisationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingUserSiteOrganisation() throws Exception {
        // Initialize the database
        insertedUserSiteOrganisation = userSiteOrganisationRepository.saveAndFlush(userSiteOrganisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSiteOrganisation
        UserSiteOrganisation updatedUserSiteOrganisation = userSiteOrganisationRepository
            .findById(userSiteOrganisation.getId())
            .orElseThrow();
        // Disconnect from session so that the updates on updatedUserSiteOrganisation are not directly saved in db
        em.detach(updatedUserSiteOrganisation);

        restUserSiteOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedUserSiteOrganisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(updatedUserSiteOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertPersistedUserSiteOrganisationToMatchAllProperties(updatedUserSiteOrganisation);
    }

    @Test
    @Transactional
    void putNonExistingUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSiteOrganisation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSiteOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, userSiteOrganisation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSiteOrganisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSiteOrganisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSiteOrganisationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(om.writeValueAsBytes(userSiteOrganisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSiteOrganisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSiteOrganisationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(om.writeValueAsBytes(userSiteOrganisation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateUserSiteOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedUserSiteOrganisation = userSiteOrganisationRepository.saveAndFlush(userSiteOrganisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSiteOrganisation using partial update
        UserSiteOrganisation partialUpdatedUserSiteOrganisation = new UserSiteOrganisation();
        partialUpdatedUserSiteOrganisation.setId(userSiteOrganisation.getId());

        restUserSiteOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSiteOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSiteOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the UserSiteOrganisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSiteOrganisationUpdatableFieldsEquals(
            createUpdateProxyForBean(partialUpdatedUserSiteOrganisation, userSiteOrganisation),
            getPersistedUserSiteOrganisation(userSiteOrganisation)
        );
    }

    @Test
    @Transactional
    void fullUpdateUserSiteOrganisationWithPatch() throws Exception {
        // Initialize the database
        insertedUserSiteOrganisation = userSiteOrganisationRepository.saveAndFlush(userSiteOrganisation);

        long databaseSizeBeforeUpdate = getRepositoryCount();

        // Update the userSiteOrganisation using partial update
        UserSiteOrganisation partialUpdatedUserSiteOrganisation = new UserSiteOrganisation();
        partialUpdatedUserSiteOrganisation.setId(userSiteOrganisation.getId());

        restUserSiteOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedUserSiteOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(partialUpdatedUserSiteOrganisation))
            )
            .andExpect(status().isOk());

        // Validate the UserSiteOrganisation in the database

        assertSameRepositoryCount(databaseSizeBeforeUpdate);
        assertUserSiteOrganisationUpdatableFieldsEquals(
            partialUpdatedUserSiteOrganisation,
            getPersistedUserSiteOrganisation(partialUpdatedUserSiteOrganisation)
        );
    }

    @Test
    @Transactional
    void patchNonExistingUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSiteOrganisation.setId(longCount.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restUserSiteOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, userSiteOrganisation.getId())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSiteOrganisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSiteOrganisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSiteOrganisationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, longCount.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(om.writeValueAsBytes(userSiteOrganisation))
            )
            .andExpect(status().isBadRequest());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamUserSiteOrganisation() throws Exception {
        long databaseSizeBeforeUpdate = getRepositoryCount();
        userSiteOrganisation.setId(longCount.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restUserSiteOrganisationMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(om.writeValueAsBytes(userSiteOrganisation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the UserSiteOrganisation in the database
        assertSameRepositoryCount(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteUserSiteOrganisation() throws Exception {
        // Initialize the database
        insertedUserSiteOrganisation = userSiteOrganisationRepository.saveAndFlush(userSiteOrganisation);

        long databaseSizeBeforeDelete = getRepositoryCount();

        // Delete the userSiteOrganisation
        restUserSiteOrganisationMockMvc
            .perform(delete(ENTITY_API_URL_ID, userSiteOrganisation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        assertDecrementedRepositoryCount(databaseSizeBeforeDelete);
    }

    protected long getRepositoryCount() {
        return userSiteOrganisationRepository.count();
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

    protected UserSiteOrganisation getPersistedUserSiteOrganisation(UserSiteOrganisation userSiteOrganisation) {
        return userSiteOrganisationRepository.findById(userSiteOrganisation.getId()).orElseThrow();
    }

    protected void assertPersistedUserSiteOrganisationToMatchAllProperties(UserSiteOrganisation expectedUserSiteOrganisation) {
        assertUserSiteOrganisationAllPropertiesEquals(
            expectedUserSiteOrganisation,
            getPersistedUserSiteOrganisation(expectedUserSiteOrganisation)
        );
    }

    protected void assertPersistedUserSiteOrganisationToMatchUpdatableProperties(UserSiteOrganisation expectedUserSiteOrganisation) {
        assertUserSiteOrganisationAllUpdatablePropertiesEquals(
            expectedUserSiteOrganisation,
            getPersistedUserSiteOrganisation(expectedUserSiteOrganisation)
        );
    }
}
