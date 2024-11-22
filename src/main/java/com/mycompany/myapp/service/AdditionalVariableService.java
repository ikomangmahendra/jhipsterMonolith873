package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.AdditionalVariable;
import com.mycompany.myapp.repository.AdditionalVariableRepository;
import com.mycompany.myapp.service.dto.AdditionalVariableDTO;
import com.mycompany.myapp.service.mapper.AdditionalVariableMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.AdditionalVariable}.
 */
@Service
@Transactional
public class AdditionalVariableService {

    private static final Logger LOG = LoggerFactory.getLogger(AdditionalVariableService.class);

    private final AdditionalVariableRepository additionalVariableRepository;

    private final AdditionalVariableMapper additionalVariableMapper;

    public AdditionalVariableService(
        AdditionalVariableRepository additionalVariableRepository,
        AdditionalVariableMapper additionalVariableMapper
    ) {
        this.additionalVariableRepository = additionalVariableRepository;
        this.additionalVariableMapper = additionalVariableMapper;
    }

    /**
     * Save a additionalVariable.
     *
     * @param additionalVariableDTO the entity to save.
     * @return the persisted entity.
     */
    public AdditionalVariableDTO save(AdditionalVariableDTO additionalVariableDTO) {
        LOG.debug("Request to save AdditionalVariable : {}", additionalVariableDTO);
        AdditionalVariable additionalVariable = additionalVariableMapper.toEntity(additionalVariableDTO);
        additionalVariable = additionalVariableRepository.save(additionalVariable);
        return additionalVariableMapper.toDto(additionalVariable);
    }

    /**
     * Update a additionalVariable.
     *
     * @param additionalVariableDTO the entity to save.
     * @return the persisted entity.
     */
    public AdditionalVariableDTO update(AdditionalVariableDTO additionalVariableDTO) {
        LOG.debug("Request to update AdditionalVariable : {}", additionalVariableDTO);
        AdditionalVariable additionalVariable = additionalVariableMapper.toEntity(additionalVariableDTO);
        additionalVariable = additionalVariableRepository.save(additionalVariable);
        return additionalVariableMapper.toDto(additionalVariable);
    }

    /**
     * Partially update a additionalVariable.
     *
     * @param additionalVariableDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<AdditionalVariableDTO> partialUpdate(AdditionalVariableDTO additionalVariableDTO) {
        LOG.debug("Request to partially update AdditionalVariable : {}", additionalVariableDTO);

        return additionalVariableRepository
            .findById(additionalVariableDTO.getId())
            .map(existingAdditionalVariable -> {
                additionalVariableMapper.partialUpdate(existingAdditionalVariable, additionalVariableDTO);

                return existingAdditionalVariable;
            })
            .map(additionalVariableRepository::save)
            .map(additionalVariableMapper::toDto);
    }

    /**
     * Get all the additionalVariables.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<AdditionalVariableDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all AdditionalVariables");
        return additionalVariableRepository.findAll(pageable).map(additionalVariableMapper::toDto);
    }

    /**
     * Get one additionalVariable by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<AdditionalVariableDTO> findOne(Long id) {
        LOG.debug("Request to get AdditionalVariable : {}", id);
        return additionalVariableRepository.findById(id).map(additionalVariableMapper::toDto);
    }

    /**
     * Delete the additionalVariable by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete AdditionalVariable : {}", id);
        additionalVariableRepository.deleteById(id);
    }
}
