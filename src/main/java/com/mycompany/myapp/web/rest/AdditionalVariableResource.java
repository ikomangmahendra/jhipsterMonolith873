package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.AdditionalVariableRepository;
import com.mycompany.myapp.service.AdditionalVariableService;
import com.mycompany.myapp.service.dto.AdditionalVariableDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.AdditionalVariable}.
 */
@RestController
@RequestMapping("/api/additional-variables")
public class AdditionalVariableResource {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalVariableResource.class);

    private static final String ENTITY_NAME = "additionalVariable";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final AdditionalVariableService additionalVariableService;

    private final AdditionalVariableRepository additionalVariableRepository;

    public AdditionalVariableResource(
        AdditionalVariableService additionalVariableService,
        AdditionalVariableRepository additionalVariableRepository
    ) {
        this.additionalVariableService = additionalVariableService;
        this.additionalVariableRepository = additionalVariableRepository;
    }

    /**
     * {@code POST  /additional-variables} : Create a new additionalVariable.
     *
     * @param additionalVariableDTO the additionalVariableDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new additionalVariableDTO, or with status {@code 400 (Bad Request)} if the additionalVariable has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<AdditionalVariableDTO> createAdditionalVariable(@Valid @RequestBody AdditionalVariableDTO additionalVariableDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save AdditionalVariable : {}", additionalVariableDTO);
        if (additionalVariableDTO.getId() != null) {
            throw new BadRequestAlertException("A new additionalVariable cannot already have an ID", ENTITY_NAME, "idexists");
        }
        additionalVariableDTO = additionalVariableService.save(additionalVariableDTO);
        return ResponseEntity.created(new URI("/api/additional-variables/" + additionalVariableDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, additionalVariableDTO.getId().toString()))
            .body(additionalVariableDTO);
    }

    /**
     * {@code PUT  /additional-variables/:id} : Updates an existing additionalVariable.
     *
     * @param id the id of the additionalVariableDTO to save.
     * @param additionalVariableDTO the additionalVariableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalVariableDTO,
     * or with status {@code 400 (Bad Request)} if the additionalVariableDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the additionalVariableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<AdditionalVariableDTO> updateAdditionalVariable(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody AdditionalVariableDTO additionalVariableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update AdditionalVariable : {}, {}", id, additionalVariableDTO);
        if (additionalVariableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalVariableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalVariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        additionalVariableDTO = additionalVariableService.update(additionalVariableDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, additionalVariableDTO.getId().toString()))
            .body(additionalVariableDTO);
    }

    /**
     * {@code PATCH  /additional-variables/:id} : Partial updates given fields of an existing additionalVariable, field will ignore if it is null
     *
     * @param id the id of the additionalVariableDTO to save.
     * @param additionalVariableDTO the additionalVariableDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated additionalVariableDTO,
     * or with status {@code 400 (Bad Request)} if the additionalVariableDTO is not valid,
     * or with status {@code 404 (Not Found)} if the additionalVariableDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the additionalVariableDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<AdditionalVariableDTO> partialUpdateAdditionalVariable(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody AdditionalVariableDTO additionalVariableDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update AdditionalVariable partially : {}, {}", id, additionalVariableDTO);
        if (additionalVariableDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, additionalVariableDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!additionalVariableRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<AdditionalVariableDTO> result = additionalVariableService.partialUpdate(additionalVariableDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, additionalVariableDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /additional-variables} : get all the additionalVariables.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of additionalVariables in body.
     */
    @GetMapping("")
    public ResponseEntity<List<AdditionalVariableDTO>> getAllAdditionalVariables(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of AdditionalVariables");
        Page<AdditionalVariableDTO> page = additionalVariableService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /additional-variables/:id} : get the "id" additionalVariable.
     *
     * @param id the id of the additionalVariableDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the additionalVariableDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<AdditionalVariableDTO> getAdditionalVariable(@PathVariable("id") Long id) {
        LOG.debug("REST request to get AdditionalVariable : {}", id);
        Optional<AdditionalVariableDTO> additionalVariableDTO = additionalVariableService.findOne(id);
        return ResponseUtil.wrapOrNotFound(additionalVariableDTO);
    }

    /**
     * {@code DELETE  /additional-variables/:id} : delete the "id" additionalVariable.
     *
     * @param id the id of the additionalVariableDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdditionalVariable(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete AdditionalVariable : {}", id);
        additionalVariableService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
