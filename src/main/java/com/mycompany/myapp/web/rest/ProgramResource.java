package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.repository.ProgramRepository;
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
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.mycompany.myapp.domain.Program}.
 */
@RestController
@RequestMapping("/api/programs")
@Transactional
public class ProgramResource {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramResource.class);

    private static final String ENTITY_NAME = "program";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ProgramRepository programRepository;

    public ProgramResource(ProgramRepository programRepository) {
        this.programRepository = programRepository;
    }

    /**
     * {@code POST  /programs} : Create a new program.
     *
     * @param program the program to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new program, or with status {@code 400 (Bad Request)} if the program has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<Program> createProgram(@Valid @RequestBody Program program) throws URISyntaxException {
        LOG.debug("REST request to save Program : {}", program);
        if (program.getId() != null) {
            throw new BadRequestAlertException("A new program cannot already have an ID", ENTITY_NAME, "idexists");
        }
        program = programRepository.save(program);
        return ResponseEntity.created(new URI("/api/programs/" + program.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, program.getId().toString()))
            .body(program);
    }

    /**
     * {@code PUT  /programs/:id} : Updates an existing program.
     *
     * @param id the id of the program to save.
     * @param program the program to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated program,
     * or with status {@code 400 (Bad Request)} if the program is not valid,
     * or with status {@code 500 (Internal Server Error)} if the program couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/{id}")
    public ResponseEntity<Program> updateProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Program program
    ) throws URISyntaxException {
        LOG.debug("REST request to update Program : {}, {}", id, program);
        if (program.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, program.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        program = programRepository.save(program);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, program.getId().toString()))
            .body(program);
    }

    /**
     * {@code PATCH  /programs/:id} : Partial updates given fields of an existing program, field will ignore if it is null
     *
     * @param id the id of the program to save.
     * @param program the program to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated program,
     * or with status {@code 400 (Bad Request)} if the program is not valid,
     * or with status {@code 404 (Not Found)} if the program is not found,
     * or with status {@code 500 (Internal Server Error)} if the program couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Program> partialUpdateProgram(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Program program
    ) throws URISyntaxException {
        LOG.debug("REST request to partial update Program partially : {}, {}", id, program);
        if (program.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, program.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!programRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Program> result = programRepository
            .findById(program.getId())
            .map(existingProgram -> {
                if (program.getName() != null) {
                    existingProgram.setName(program.getName());
                }
                if (program.getType() != null) {
                    existingProgram.setType(program.getType());
                }
                if (program.getStartDate() != null) {
                    existingProgram.setStartDate(program.getStartDate());
                }
                if (program.getEndDate() != null) {
                    existingProgram.setEndDate(program.getEndDate());
                }
                if (program.getStatus() != null) {
                    existingProgram.setStatus(program.getStatus());
                }
                if (program.getExternalSystemLkp() != null) {
                    existingProgram.setExternalSystemLkp(program.getExternalSystemLkp());
                }
                if (program.getIsEnableFollowUp() != null) {
                    existingProgram.setIsEnableFollowUp(program.getIsEnableFollowUp());
                }
                if (program.getIsNsfSurveyAccess() != null) {
                    existingProgram.setIsNsfSurveyAccess(program.getIsNsfSurveyAccess());
                }
                if (program.getIsOptOutAllowed() != null) {
                    existingProgram.setIsOptOutAllowed(program.getIsOptOutAllowed());
                }

                return existingProgram;
            })
            .map(programRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, program.getId().toString())
        );
    }

    /**
     * {@code GET  /programs} : get all the programs.
     *
     * @param eagerload flag to eager load entities from relationships (This is applicable for many-to-many).
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of programs in body.
     */
    @GetMapping("")
    public List<Program> getAllPrograms(@RequestParam(name = "eagerload", required = false, defaultValue = "true") boolean eagerload) {
        LOG.debug("REST request to get all Programs");
        if (eagerload) {
            return programRepository.findAllWithEagerRelationships();
        } else {
            return programRepository.findAll();
        }
    }

    /**
     * {@code GET  /programs/:id} : get the "id" program.
     *
     * @param id the id of the program to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the program, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Program> getProgram(@PathVariable("id") Long id) {
        LOG.debug("REST request to get Program : {}", id);
        Optional<Program> program = programRepository.findOneWithEagerRelationships(id);
        return ResponseUtil.wrapOrNotFound(program);
    }

    /**
     * {@code DELETE  /programs/:id} : delete the "id" program.
     *
     * @param id the id of the program to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProgram(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete Program : {}", id);
        programRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
