package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.UserTempRepository;
import com.mycompany.myapp.service.UserTempService;
import com.mycompany.myapp.service.dto.UserTempDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.UserTemp}.
 */
@RestController
@RequestMapping("/api/user-temps")
public class UserTempResource {

    private static final Logger LOG = LoggerFactory.getLogger(UserTempResource.class);

    private static final String ENTITY_NAME = "userTemp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final UserTempService userTempService;

    private final UserTempRepository userTempRepository;

    public UserTempResource(UserTempService userTempService, UserTempRepository userTempRepository) {
        this.userTempService = userTempService;
        this.userTempRepository = userTempRepository;
    }

    /**
     * {@code POST  /user-temps} : Create a new userTemp.
     *
     * @param userTempDTO the userTempDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new userTempDTO, or with status {@code 400 (Bad Request)} if the userTemp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<UserTempDTO> createUserTemp(@RequestBody UserTempDTO userTempDTO) throws URISyntaxException {
        LOG.debug("REST request to save UserTemp : {}", userTempDTO);
        if (userTempDTO.getId() != null) {
            throw new BadRequestAlertException("A new userTemp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        userTempDTO = userTempService.save(userTempDTO);
        return ResponseEntity.created(new URI("/api/user-temps/" + userTempDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, userTempDTO.getId().toString()))
            .body(userTempDTO);
    }

    /**
     * {@code PUT  /user-temps/:id} : Updates an existing userTemp.
     *
     * @param id the id of the userTempDTO to save.
     * @param userTempDTO the userTempDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTempDTO,
     * or with status {@code 400 (Bad Request)} if the userTempDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the userTempDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserTempDTO> updateUserTemp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTempDTO userTempDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update UserTemp : {}, {}", id, userTempDTO);
        if (userTempDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTempDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTempRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        userTempDTO = userTempService.update(userTempDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userTempDTO.getId().toString()))
            .body(userTempDTO);
    }

    /**
     * {@code PATCH  /user-temps/:id} : Partial updates given fields of an existing userTemp, field will ignore if it is null
     *
     * @param id the id of the userTempDTO to save.
     * @param userTempDTO the userTempDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated userTempDTO,
     * or with status {@code 400 (Bad Request)} if the userTempDTO is not valid,
     * or with status {@code 404 (Not Found)} if the userTempDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the userTempDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<UserTempDTO> partialUpdateUserTemp(
        @PathVariable(value = "id", required = false) final Long id,
        @RequestBody UserTempDTO userTempDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update UserTemp partially : {}, {}", id, userTempDTO);
        if (userTempDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, userTempDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!userTempRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<UserTempDTO> result = userTempService.partialUpdate(userTempDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, userTempDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /user-temps} : get all the userTemps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of userTemps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<UserTempDTO>> getAllUserTemps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of UserTemps");
        Page<UserTempDTO> page = userTempService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /user-temps/:id} : get the "id" userTemp.
     *
     * @param id the id of the userTempDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the userTempDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<UserTempDTO> getUserTemp(@PathVariable("id") Long id) {
        LOG.debug("REST request to get UserTemp : {}", id);
        Optional<UserTempDTO> userTempDTO = userTempService.findOne(id);
        return ResponseUtil.wrapOrNotFound(userTempDTO);
    }

    /**
     * {@code DELETE  /user-temps/:id} : delete the "id" userTemp.
     *
     * @param id the id of the userTempDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUserTemp(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete UserTemp : {}", id);
        userTempService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
