package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AccuteProgramVariable;
import com.mycompany.myapp.repository.AccuteProgramVariableRepository;
import com.mycompany.myapp.service.dto.AccuteProgramVariableDTO;
import com.mycompany.myapp.service.mapper.AccuteProgramVariableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AccuteProgramVariable}.
 */
@Service
@Transactional
public class AccuteProgramVariableService {

    private static final Logger LOG = LoggerFactory.getLogger(AccuteProgramVariableService.class);

    private final AccuteProgramVariableRepository accuteProgramVariableRepository;

    private final AccuteProgramVariableMapper accuteProgramVariableMapper;

    public AccuteProgramVariableService(
        AccuteProgramVariableRepository accuteProgramVariableRepository,
        AccuteProgramVariableMapper accuteProgramVariableMapper
    ) {
        this.accuteProgramVariableRepository = accuteProgramVariableRepository;
        this.accuteProgramVariableMapper = accuteProgramVariableMapper;
    }

    /**
     * Save a accuteProgramVariable.
     *
     * @param accuteProgramVariableDTO the entity to save.
     * @return the persisted entity.
     */
    public AccuteProgramVariableDTO save(AccuteProgramVariableDTO accuteProgramVariableDTO) {
        LOG.debug("Request to save AccuteProgramVariable : {}", accuteProgramVariableDTO);
        AccuteProgramVariable accuteProgramVariable = accuteProgramVariableMapper.toEntity(accuteProgramVariableDTO);
        accuteProgramVariable = accuteProgramVariableRepository.save(accuteProgramVariable);
        return accuteProgramVariableMapper.toDto(accuteProgramVariable);
    }

    /**
     * Update a accuteProgramVariable.
     *
     * @param accuteProgramVariableDTO the entity to save.
     * @return the persisted entity.
     */
    public AccuteProgramVariableDTO update(AccuteProgramVariableDTO accuteProgramVariableDTO) {
        LOG.debug("Request to update AccuteProgramVariable : {}", accuteProgramVariableDTO);
        AccuteProgramVariable accuteProgramVariable = accuteProgramVariableMapper.toEntity(accuteProgramVariableDTO);
        accuteProgramVariable = accuteProgramVariableRepository.save(accuteProgramVariable);
        return accuteProgramVariableMapper.toDto(accuteProgramVariable);
    }

    /**
     * Partially update a accuteProgramVariable.
     *
     * @param accuteProgramVariableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AccuteProgramVariableDTO> partialUpdate(AccuteProgramVariableDTO accuteProgramVariableDTO) {
        LOG.debug("Request to partially update AccuteProgramVariable : {}", accuteProgramVariableDTO);

        return accuteProgramVariableRepository
            .findById(accuteProgramVariableDTO.getId())
            .map(existingAccuteProgramVariable -> {
                accuteProgramVariableMapper.partialUpdate(existingAccuteProgramVariable, accuteProgramVariableDTO);

                return existingAccuteProgramVariable;
            })
            .map(accuteProgramVariableRepository::save)
            .map(accuteProgramVariableMapper::toDto);
    }

    /**
     * Get all the accuteProgramVariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AccuteProgramVariableDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AccuteProgramVariables");
        return accuteProgramVariableRepository.findAll(pageable).map(accuteProgramVariableMapper::toDto);
    }

    /**
     * Get one accuteProgramVariable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AccuteProgramVariableDTO> findOne(Long id) {
        LOG.debug("Request to get AccuteProgramVariable : {}", id);
        return accuteProgramVariableRepository.findById(id).map(accuteProgramVariableMapper::toDto);
    }

    /**
     * Delete the accuteProgramVariable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AccuteProgramVariable : {}", id);
        accuteProgramVariableRepository.deleteById(id);
    }
}
