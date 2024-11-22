package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.UserTemp;
import com.mycompany.myapp.repository.UserTempRepository;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.UserTemp}.
 */
@RestController
@RequestMapping("/api/user-temps")
@Transactional
public class UserTempResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserTempResource.class);

    private static final String ENTITY_NAME = "userTemp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTempRepository userTempRepository;

    public UserTempResource(UserTempRepository userTempRepository) {
        this.userTempRepository = userTempRepository;
    }

    /**
     * {@code POST  /user-temps} : Create a new userTemp.
     *
     * @param userTemp the userTemp to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userTemp, or with status {@code 400 (Bad Request)} if the userTemp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserTemp> createUserTemp(@RequestBody UserTemp userTemp) throws URISyntaxException {
        LOG.debug("REST request to save UserTemp : {}", userTemp);
        if (userTemp.getId() != null) {
            throw new BadRequestAlertException("A new userTemp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userTemp = userTempRepository.save(userTemp);
        return ResponseEntity.created(new URI("/api/user-temps/" + userTemp.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userTemp.getId().toString()))
            .body(userTemp);
    }

    /**
     * {@code PUT  /user-temps/:id} : Updates an existing userTemp.
     *
     * @param id the id of the userTemp to save.
     * @param userTemp the userTemp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTemp,
     * or with status {@code 400 (Bad Request)} if the userTemp is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userTemp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserTemp> updateUserTemp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTemp userTemp
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserTemp : {}, {}", id, userTemp);
        if (userTemp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTemp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTempRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userTemp = userTempRepository.save(userTemp);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userTemp.getId().toString()))
            .body(userTemp);
    }

    /**
     * {@code PATCH  /user-temps/:id} : Partial updates given fields of an existing userTemp, field will ignore if it is null
     *
     * @param id the id of the userTemp to save.
     * @param userTemp the userTemp to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTemp,
     * or with status {@code 400 (Bad Request)} if the userTemp is not valid,
     * or with status {@code 404 (Not Found)} if the userTemp is not found,
     * or with status {@code 500 (Internal Server Error)} if the userTemp couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserTemp> partialUpdateUserTemp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTemp userTemp
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserTemp partially : {}, {}", id, userTemp);
        if (userTemp.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTemp.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTempRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserTemp> result = userTempRepository.findById(userTemp.getId()).map(userTempRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userTemp.getId().toString())
        );
    }

    /**
     * {@code GET  /user-temps} : get all the userTemps.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTemps in body.
     */
    @GetMapping("")
    public List<UserTemp> getAllUserTemps() {
        LOG.debug("REST request to get all UserTemps");
        return userTempRepository.findAll();
    }

    /**
     * {@code GET  /user-temps/:id} : get the "id" userTemp.
     *
     * @param id the id of the userTemp to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userTemp, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserTemp> getUserTemp(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserTemp : {}", id);
        Optional<UserTemp> userTemp = userTempRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(userTemp);
    }

    /**
     * {@code DELETE  /user-temps/:id} : delete the "id" userTemp.
     *
     * @param id the id of the userTemp to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserTemp(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserTemp : {}", id);
        userTempRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
