package com.mycompany.myapp.service;

import com.mycompany.myapp.domain.SiteOrgTemp;
import com.mycompany.myapp.repository.SiteOrgTempRepository;
import com.mycompany.myapp.service.dto.SiteOrgTempDTO;
import com.mycompany.myapp.service.mapper.SiteOrgTempMapper;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link com.mycompany.myapp.domain.SiteOrgTemp}.
 */
@Service
@Transactional
public class SiteOrgTempService {

    private static final Logger LOG = LoggerFactory.getLogger(SiteOrgTempService.class);

    private final SiteOrgTempRepository siteOrgTempRepository;

    private final SiteOrgTempMapper siteOrgTempMapper;

    public SiteOrgTempService(SiteOrgTempRepository siteOrgTempRepository, SiteOrgTempMapper siteOrgTempMapper) {
        this.siteOrgTempRepository = siteOrgTempRepository;
        this.siteOrgTempMapper = siteOrgTempMapper;
    }

    /**
     * Save a siteOrgTemp.
     *
     * @param siteOrgTempDTO the entity to save.
     * @return the persisted entity.
     */
    public SiteOrgTempDTO save(SiteOrgTempDTO siteOrgTempDTO) {
        LOG.debug("Request to save SiteOrgTemp : {}", siteOrgTempDTO);
        SiteOrgTemp siteOrgTemp = siteOrgTempMapper.toEntity(siteOrgTempDTO);
        siteOrgTemp = siteOrgTempRepository.save(siteOrgTemp);
        return siteOrgTempMapper.toDto(siteOrgTemp);
    }

    /**
     * Get all the siteOrgTemps.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Transactional(readOnly = true)
    public Page<SiteOrgTempDTO> findAll(Pageable pageable) {
        LOG.debug("Request to get all SiteOrgTemps");
        return siteOrgTempRepository.findAll(pageable).map(siteOrgTempMapper::toDto);
    }

    /**
     * Get one siteOrgTemp by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Transactional(readOnly = true)
    public Optional<SiteOrgTempDTO> findOne(Long id) {
        LOG.debug("Request to get SiteOrgTemp : {}", id);
        return siteOrgTempRepository.findById(id).map(siteOrgTempMapper::toDto);
    }

    /**
     * Delete the siteOrgTemp by id.
     *
     * @param id the id of the entity.
     */
    public void delete(Long id) {
        LOG.debug("Request to delete SiteOrgTemp : {}", id);
        siteOrgTempRepository.deleteById(id);
    }
}
