package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserSiteOrganisation;
import com.mycompany.myapp.repository.UserSiteOrganisationRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UserSiteOrganisation}.
 */
@RestController
@RequestMapping("/api/user-site-organisations")
@Transactional
public class UserSiteOrganisationResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserSiteOrganisationResource.class);

    private static final String ENTITY_NAME = "userSiteOrganisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserSiteOrganisationRepository userSiteOrganisationRepository;

    public UserSiteOrganisationResource(UserSiteOrganisationRepository userSiteOrganisationRepository) {
        this.userSiteOrganisationRepository = userSiteOrganisationRepository;
    }

    /**
     * {@code POST  /user-site-organisations} : Create a new userSiteOrganisation.
     *
     * @param userSiteOrganisation the userSiteOrganisation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userSiteOrganisation, or with status {@code 400 (Bad Request)} if the userSiteOrganisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserSiteOrganisation> createUserSiteOrganisation(@RequestBody UserSiteOrganisation userSiteOrganisation)
        throws URISyntaxException {
        LOG.debug("REST request to save UserSiteOrganisation : {}", userSiteOrganisation);
        if (userSiteOrganisation.getId() != null) {
            throw new BadRequestAlertException("A new userSiteOrganisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userSiteOrganisation = userSiteOrganisationRepository.save(userSiteOrganisation);
        return ResponseEntity.created(new URI("/api/user-site-organisations/" + userSiteOrganisation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userSiteOrganisation.getId().toString()))
            .body(userSiteOrganisation);
    }

    /**
     * {@code PUT  /user-site-organisations/:id} : Updates an existing userSiteOrganisation.
     *
     * @param id the id of the userSiteOrganisation to save.
     * @param userSiteOrganisation the userSiteOrganisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSiteOrganisation,
     * or with status {@code 400 (Bad Request)} if the userSiteOrganisation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userSiteOrganisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserSiteOrganisation> updateUserSiteOrganisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSiteOrganisation userSiteOrganisation
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserSiteOrganisation : {}, {}", id, userSiteOrganisation);
        if (userSiteOrganisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSiteOrganisation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSiteOrganisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userSiteOrganisation = userSiteOrganisationRepository.save(userSiteOrganisation);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSiteOrganisation.getId().toString()))
            .body(userSiteOrganisation);
    }

    /**
     * {@code PATCH  /user-site-organisations/:id} : Partial updates given fields of an existing userSiteOrganisation, field will ignore if it is null
     *
     * @param id the id of the userSiteOrganisation to save.
     * @param userSiteOrganisation the userSiteOrganisation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userSiteOrganisation,
     * or with status {@code 400 (Bad Request)} if the userSiteOrganisation is not valid,
     * or with status {@code 404 (Not Found)} if the userSiteOrganisation is not found,
     * or with status {@code 500 (Internal Server Error)} if the userSiteOrganisation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserSiteOrganisation> partialUpdateUserSiteOrganisation(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserSiteOrganisation userSiteOrganisation
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserSiteOrganisation partially : {}, {}", id, userSiteOrganisation);
        if (userSiteOrganisation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userSiteOrganisation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userSiteOrganisationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserSiteOrganisation> result = userSiteOrganisationRepository
            .findById(userSiteOrganisation.getId())
            .map(userSiteOrganisationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userSiteOrganisation.getId().toString())
        );
    }

    /**
     * {@code GET  /user-site-organisations} : get all the userSiteOrganisations.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userSiteOrganisations in body.
     */
    @GetMapping("")
    public List<UserSiteOrganisation> getAllUserSiteOrganisations(
        @RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload
    ) {
        LOG.debug("REST request to get all UserSiteOrganisations");
        if (eagerload) {
            return userSiteOrganisationRepository.findAllWithEagerRelationships();
        } else {
            return userSiteOrganisationRepository.findAll();
        }
    }

    /**
     * {@code GET  /user-site-organisations/:id} : get the "id" userSiteOrganisation.
     *
     * @param id the id of the userSiteOrganisation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userSiteOrganisation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserSiteOrganisation> getUserSiteOrganisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserSiteOrganisation : {}", id);
        Optional<UserSiteOrganisation> userSiteOrganisation = userSiteOrganisationRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(userSiteOrganisation);
    }

    /**
     * {@code DELETE  /user-site-organisations/:id} : delete the "id" userSiteOrganisation.
     *
     * @param id the id of the userSiteOrganisation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserSiteOrganisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserSiteOrganisation : {}", id);
        userSiteOrganisationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
