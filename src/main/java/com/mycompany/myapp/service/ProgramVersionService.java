package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.ProgramVersion;
import com.mycompany.myapp.repository.ProgramVersionRepository;
import com.mycompany.myapp.service.dto.ProgramVersionDTO;
import com.mycompany.myapp.service.mapper.ProgramVersionMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.ProgramVersion}.
 */
@Service
@Transactional
public class ProgramVersionService {

    private static final Logger LOG = LoggerFactory.getLogger(ProgramVersionService.class);

    private final ProgramVersionRepository programVersionRepository;

    private final ProgramVersionMapper programVersionMapper;

    public ProgramVersionService(ProgramVersionRepository programVersionRepository, ProgramVersionMapper programVersionMapper) {
        this.programVersionRepository = programVersionRepository;
        this.programVersionMapper = programVersionMapper;
    }

    /**
     * Save a programVersion.
     *
     * @param programVersionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgramVersionDTO save(ProgramVersionDTO programVersionDTO) {
        LOG.debug("Request to save ProgramVersion : {}", programVersionDTO);
        ProgramVersion programVersion = programVersionMapper.toEntity(programVersionDTO);
        programVersion = programVersionRepository.save(programVersion);
        return programVersionMapper.toDto(programVersion);
    }

    /**
     * Update a programVersion.
     *
     * @param programVersionDTO the entity to save.
     * @return the persisted entity.
     */
    public ProgramVersionDTO update(ProgramVersionDTO programVersionDTO) {
        LOG.debug("Request to update ProgramVersion : {}", programVersionDTO);
        ProgramVersion programVersion = programVersionMapper.toEntity(programVersionDTO);
        programVersion = programVersionRepository.save(programVersion);
        return programVersionMapper.toDto(programVersion);
    }

    /**
     * Partially update a programVersion.
     *
     * @param programVersionDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<ProgramVersionDTO> partialUpdate(ProgramVersionDTO programVersionDTO) {
        LOG.debug("Request to partially update ProgramVersion : {}", programVersionDTO);

        return programVersionRepository
            .findById(programVersionDTO.getId())
            .map(existingProgramVersion -> {
                programVersionMapper.partialUpdate(existingProgramVersion, programVersionDTO);

                return existingProgramVersion;
            })
            .map(programVersionRepository::save)
            .map(programVersionMapper::toDto);
    }

    /**
     * Get all the programVersions.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<ProgramVersionDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all ProgramVersions");
        return programVersionRepository.findAll(pageable).map(programVersionMapper::toDto);
    }

    /**
     * Get one programVersion by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<ProgramVersionDTO> findOne(Long id) {
        LOG.debug("Request to get ProgramVersion : {}", id);
        return programVersionRepository.findById(id).map(programVersionMapper::toDto);
    }

    /**
     * Delete the programVersion by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete ProgramVersion : {}", id);
        programVersionRepository.deleteById(id);
    }
}
