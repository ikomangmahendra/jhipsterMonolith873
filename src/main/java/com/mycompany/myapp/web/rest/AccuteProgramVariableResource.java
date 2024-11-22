package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AccuteProgramVariableRepository;
import com.mycompany.myapp.service.AccuteProgramVariableService;
import com.mycompany.myapp.service.dto.AccuteProgramVariableDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AccuteProgramVariable}.
 */
@RestController
@RequestMapping("/api/accute-program-variables")
public class AccuteProgramVariableResource {

    private static final Logger LOG = LoggerFactory.getLogger(AccuteProgramVariableResource.class);

    private static final String ENTITY_NAME = "accuteProgramVariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AccuteProgramVariableService accuteProgramVariableService;

    private final AccuteProgramVariableRepository accuteProgramVariableRepository;

    public AccuteProgramVariableResource(
        AccuteProgramVariableService accuteProgramVariableService,
        AccuteProgramVariableRepository accuteProgramVariableRepository
    ) {
        this.accuteProgramVariableService = accuteProgramVariableService;
        this.accuteProgramVariableRepository = accuteProgramVariableRepository;
    }

    /**
     * {@code POST  /accute-program-variables} : Create a new accuteProgramVariable.
     *
     * @param accuteProgramVariableDTO the accuteProgramVariableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new accuteProgramVariableDTO, or with status {@code 400 (Bad Request)} if the accuteProgramVariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AccuteProgramVariableDTO> createAccuteProgramVariable(
        @Valid @RequestBody AccuteProgramVariableDTO accuteProgramVariableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to save AccuteProgramVariable : {}", accuteProgramVariableDTO);
        if (accuteProgramVariableDTO.getId() != null) {
            throw new BadRequestAlertException("A new accuteProgramVariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        accuteProgramVariableDTO = accuteProgramVariableService.save(accuteProgramVariableDTO);
        return ResponseEntity.created(new URI("/api/accute-program-variables/" + accuteProgramVariableDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, accuteProgramVariableDTO.getId().toString()))
            .body(accuteProgramVariableDTO);
    }

    /**
     * {@code PUT  /accute-program-variables/:id} : Updates an existing accuteProgramVariable.
     *
     * @param id the id of the accuteProgramVariableDTO to save.
     * @param accuteProgramVariableDTO the accuteProgramVariableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accuteProgramVariableDTO,
     * or with status {@code 400 (Bad Request)} if the accuteProgramVariableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the accuteProgramVariableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AccuteProgramVariableDTO> updateAccuteProgramVariable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AccuteProgramVariableDTO accuteProgramVariableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AccuteProgramVariable : {}, {}", id, accuteProgramVariableDTO);
        if (accuteProgramVariableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accuteProgramVariableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accuteProgramVariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        accuteProgramVariableDTO = accuteProgramVariableService.update(accuteProgramVariableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accuteProgramVariableDTO.getId().toString()))
            .body(accuteProgramVariableDTO);
    }

    /**
     * {@code PATCH  /accute-program-variables/:id} : Partial updates given fields of an existing accuteProgramVariable, field will ignore if it is null
     *
     * @param id the id of the accuteProgramVariableDTO to save.
     * @param accuteProgramVariableDTO the accuteProgramVariableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated accuteProgramVariableDTO,
     * or with status {@code 400 (Bad Request)} if the accuteProgramVariableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the accuteProgramVariableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the accuteProgramVariableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AccuteProgramVariableDTO> partialUpdateAccuteProgramVariable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AccuteProgramVariableDTO accuteProgramVariableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AccuteProgramVariable partially : {}, {}", id, accuteProgramVariableDTO);
        if (accuteProgramVariableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, accuteProgramVariableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!accuteProgramVariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AccuteProgramVariableDTO> result = accuteProgramVariableService.partialUpdate(accuteProgramVariableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, accuteProgramVariableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /accute-program-variables} : get all the accuteProgramVariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of accuteProgramVariables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AccuteProgramVariableDTO>> getAllAccuteProgramVariables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AccuteProgramVariables");
        Page<AccuteProgramVariableDTO> page = accuteProgramVariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /accute-program-variables/:id} : get the "id" accuteProgramVariable.
     *
     * @param id the id of the accuteProgramVariableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the accuteProgramVariableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AccuteProgramVariableDTO> getAccuteProgramVariable(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AccuteProgramVariable : {}", id);
        Optional<AccuteProgramVariableDTO> accuteProgramVariableDTO = accuteProgramVariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(accuteProgramVariableDTO);
    }

    /**
     * {@code DELETE  /accute-program-variables/:id} : delete the "id" accuteProgramVariable.
     *
     * @param id the id of the accuteProgramVariableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAccuteProgramVariable(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AccuteProgramVariable : {}", id);
        accuteProgramVariableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
