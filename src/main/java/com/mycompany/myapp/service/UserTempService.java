package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.UserTemp;
import com.mycompany.myapp.repository.UserTempRepository;
import com.mycompany.myapp.service.dto.UserTempDTO;
import com.mycompany.myapp.service.mapper.UserTempMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.UserTemp}.
 */
@Service
@Transactional
public class UserTempService {

    private static final Logger LOG = LoggerFactory.getLogger(UserTempService.class);

    private final UserTempRepository userTempRepository;

    private final UserTempMapper userTempMapper;

    public UserTempService(UserTempRepository userTempRepository, UserTempMapper userTempMapper) {
        this.userTempRepository = userTempRepository;
        this.userTempMapper = userTempMapper;
    }

    /**
     * Save a userTemp.
     *
     * @param userTempDTO the entity to save.
     * @return the persisted entity.
     */
    public UserTempDTO save(UserTempDTO userTempDTO) {
        LOG.debug("Request to save UserTemp : {}", userTempDTO);
        UserTemp userTemp = userTempMapper.toEntity(userTempDTO);
        userTemp = userTempRepository.save(userTemp);
        return userTempMapper.toDto(userTemp);
    }

    /**
     * Update a userTemp.
     *
     * @param userTempDTO the entity to save.
     * @return the persisted entity.
     */
    public UserTempDTO update(UserTempDTO userTempDTO) {
        LOG.debug("Request to update UserTemp : {}", userTempDTO);
        UserTemp userTemp = userTempMapper.toEntity(userTempDTO);
        userTemp = userTempRepository.save(userTemp);
        return userTempMapper.toDto(userTemp);
    }

    /**
     * Partially update a userTemp.
     *
     * @param userTempDTO the entity to update partially.
     * @return the persisted entity.
     */
    public Optional<UserTempDTO> partialUpdate(UserTempDTO userTempDTO) {
        LOG.debug("Request to partially update UserTemp : {}", userTempDTO);

        return userTempRepository
            .findById(userTempDTO.getId())
            .map(existingUserTemp -> {
                userTempMapper.partialUpdate(existingUserTemp, userTempDTO);

                return existingUserTemp;
            })
            .map(userTempRepository::save)
            .map(userTempMapper::toDto);
    }

    /**
     * Get all the userTemps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<UserTempDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all UserTemps");
        return userTempRepository.findAll(pageable).map(userTempMapper::toDto);
    }

    /**
     * Get one userTemp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<UserTempDTO> findOne(Long id) {
        LOG.debug("Request to get UserTemp : {}", id);
        return userTempRepository.findById(id).map(userTempMapper::toDto);
    }

    /**
     * Delete the userTemp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete UserTemp : {}", id);
        userTempRepository.deleteById(id);
    }
}
