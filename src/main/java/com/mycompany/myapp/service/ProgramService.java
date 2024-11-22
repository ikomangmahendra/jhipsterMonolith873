package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.Program;
import com.mycompany.myapp.repository.ProgramRepository;
import com.mycompany.myapp.service.dto.ProgramDTO;
import com.mycompany.myapp.service.mapper.ProgramMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.Program}.
 */
@Service
@Transactional
public class ProgramService {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramService.class);

    private final ProgramRepository programRepository;

    private final ProgramMapper programMapper;

    public ProgramService(ProgramRepository programRepository, ProgramMapper programMapper) {
        this.programRepository = programRepository;
        this.programMapper = programMapper;
    }

    /**
     * Save a program.
     *
     * @param programDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgramDTO save(ProgramDTO programDTO) {
        LOG.debug("Request to save Program : {}", programDTO);
        Program program = programMapper.toEntity(programDTO);
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    /**
     * Update a program.
     *
     * @param programDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgramDTO update(ProgramDTO programDTO) {
        LOG.debug("Request to update Program : {}", programDTO);
        Program program = programMapper.toEntity(programDTO);
        program = programRepository.save(program);
        return programMapper.toDto(program);
    }

    /**
     * Partially update a program.
     *
     * @param programDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProgramDTO> partialUpdate(ProgramDTO programDTO) {
        LOG.debug("Request to partially update Program : {}", programDTO);

        return programRepository
            .findById(programDTO.getId())
            .map(existingProgram -> {
                programMapper.partialUpdate(existingProgram, programDTO);

                return existingProgram;
            })
            .map(programRepository::save)
            .map(programMapper::toDto);
    }

    /**
     * Get all the programs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProgramDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all Programs");
        return programRepository.findAll(pageable).map(programMapper::toDto);
    }

    /**
     * Get all the programs with eager load of many-to-many relationships.
     *
     * @return the list of entities.
     */
    public Page<ProgramDTO> findAllWithEagerRelationships(Pageable pageable) {
        return programRepository.findAllWithEagerRelationships(pageable).map(programMapper::toDto);
    }

    /**
     * Get one program by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProgramDTO> findOne(Long id) {
        LOG.debug("Request to get Program : {}", id);
        return programRepository.findOneWithEagerRelationships(id).map(programMapper::toDto);
    }

    /**
     * Delete the program by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete Program : {}", id);
        programRepository.deleteById(id);
    }
}
