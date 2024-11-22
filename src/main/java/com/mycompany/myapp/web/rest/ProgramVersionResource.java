package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.ProgramVersionRepository;
import com.mycompany.myapp.service.ProgramVersionService;
import com.mycompany.myapp.service.dto.ProgramVersionDTO;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.ProgramVersion}.
 */
@RestController
@RequestMapping("/api/program-versions")
public class ProgramVersionResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramVersionResource.class);

    private static final String ENTITY_NAME = "programVersion";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramVersionService programVersionService;

    private final ProgramVersionRepository programVersionRepository;

    public ProgramVersionResource(ProgramVersionService programVersionService, ProgramVersionRepository programVersionRepository) {
        this.programVersionService = programVersionService;
        this.programVersionRepository = programVersionRepository;
    }

    /**
     * {@code POST  /program-versions} : Create a new programVersion.
     *
     * @param programVersionDTO the programVersionDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new programVersionDTO, or with status {@code 400 (Bad Request)} if the programVersion has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<ProgramVersionDTO> createProgramVersion(@Valid @RequestBody ProgramVersionDTO programVersionDTO)
        throws URISyntaxException {
        LOG.debug("REST request to save ProgramVersion : {}", programVersionDTO);
        if (programVersionDTO.getId() != null) {
            throw new BadRequestAlertException("A new programVersion cannot already have an ID", ENTITY_NAME, "idexists");
        }
        programVersionDTO = programVersionService.save(programVersionDTO);
        return ResponseEntity.created(new URI("/api/program-versions/" + programVersionDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, programVersionDTO.getId().toString()))
            .body(programVersionDTO);
    }

    /**
     * {@code PUT  /program-versions/:id} : Updates an existing programVersion.
     *
     * @param id the id of the programVersionDTO to save.
     * @param programVersionDTO the programVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programVersionDTO,
     * or with status {@code 400 (Bad Request)} if the programVersionDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the programVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<ProgramVersionDTO> updateProgramVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ProgramVersionDTO programVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to update ProgramVersion : {}, {}", id, programVersionDTO);
        if (programVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        programVersionDTO = programVersionService.update(programVersionDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, programVersionDTO.getId().toString()))
            .body(programVersionDTO);
    }

    /**
     * {@code PATCH  /program-versions/:id} : Partial updates given fields of an existing programVersion, field will ignore if it is null
     *
     * @param id the id of the programVersionDTO to save.
     * @param programVersionDTO the programVersionDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated programVersionDTO,
     * or with status {@code 400 (Bad Request)} if the programVersionDTO is not valid,
     * or with status {@code 404 (Not Found)} if the programVersionDTO is not found,
     * or with status {@code 500 (Internal Server Error)} if the programVersionDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ProgramVersionDTO> partialUpdateProgramVersion(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ProgramVersionDTO programVersionDTO
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update ProgramVersion partially : {}, {}", id, programVersionDTO);
        if (programVersionDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, programVersionDTO.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programVersionRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ProgramVersionDTO> result = programVersionService.partialUpdate(programVersionDTO);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, programVersionDTO.getId().toString())
        );
    }

    /**
     * {@code GET  /program-versions} : get all the programVersions.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programVersions in body.
     */
    @GetMapping("")
    public ResponseEntity<List<ProgramVersionDTO>> getAllProgramVersions(
        @org.springdoc.core.annotations.ParameterObject Pageable pageable
    ) {
        LOG.debug("REST request to get a page of ProgramVersions");
        Page<ProgramVersionDTO> page = programVersionService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /program-versions/:id} : get the "id" programVersion.
     *
     * @param id the id of the programVersionDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the programVersionDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProgramVersionDTO> getProgramVersion(@PathVariable("id") Long id) {
        LOG.debug("REST request to get ProgramVersion : {}", id);
        Optional<ProgramVersionDTO> programVersionDTO = programVersionService.findOne(id);
        return ResponseUtil.wrapOrNotFound(programVersionDTO);
    }

    /**
     * {@code DELETE  /program-versions/:id} : delete the "id" programVersion.
     *
     * @param id the id of the programVersionDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgramVersion(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete ProgramVersion : {}", id);
        programVersionService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
