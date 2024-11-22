package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.repository.SiteOrgTempRepository;
import com.mycompany.myapp.service.SiteOrgTempService;
import com.mycompany.myapp.service.dto.SiteOrgTempDTO;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SiteOrgTemp}.
 */
@RestController
@RequestMapping("/api/site-org-temps")
public class SiteOrgTempResource {

    private static final Logger LOG = LoggerFactory.getLogger(SiteOrgTempResource.class);

    private static final String ENTITY_NAME = "siteOrgTemp";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteOrgTempService siteOrgTempService;

    private final SiteOrgTempRepository siteOrgTempRepository;

    public SiteOrgTempResource(SiteOrgTempService siteOrgTempService, SiteOrgTempRepository siteOrgTempRepository) {
        this.siteOrgTempService = siteOrgTempService;
        this.siteOrgTempRepository = siteOrgTempRepository;
    }

    /**
     * {@code POST  /site-org-temps} : Create a new siteOrgTemp.
     *
     * @param siteOrgTempDTO the siteOrgTempDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteOrgTempDTO, or with status {@code 400 (Bad Request)} if the siteOrgTemp has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SiteOrgTempDTO> createSiteOrgTemp(@RequestBody SiteOrgTempDTO siteOrgTempDTO) throws URISyntaxException {
        LOG.debug("REST request to save SiteOrgTemp : {}", siteOrgTempDTO);
        if (siteOrgTempDTO.getId() != null) {
            throw new BadRequestAlertException("A new siteOrgTemp cannot already have an ID", ENTITY_NAME, "idexists");
        }
        siteOrgTempDTO = siteOrgTempService.save(siteOrgTempDTO);
        return ResponseEntity.created(new URI("/api/site-org-temps/" + siteOrgTempDTO.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, siteOrgTempDTO.getId().toString()))
            .body(siteOrgTempDTO);
    }

    /**
     * {@code GET  /site-org-temps} : get all the siteOrgTemps.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteOrgTemps in body.
     */
    @GetMapping("")
    public ResponseEntity<List<SiteOrgTempDTO>> getAllSiteOrgTemps(@org.springdoc.core.annotations.ParameterObject Pageable pageable) {
        LOG.debug("REST request to get a page of SiteOrgTemps");
        Page<SiteOrgTempDTO> page = siteOrgTempService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /site-org-temps/:id} : get the "id" siteOrgTemp.
     *
     * @param id the id of the siteOrgTempDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteOrgTempDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiteOrgTempDTO> getSiteOrgTemp(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SiteOrgTemp : {}", id);
        Optional<SiteOrgTempDTO> siteOrgTempDTO = siteOrgTempService.findOne(id);
        return ResponseUtil.wrapOrNotFound(siteOrgTempDTO);
    }

    /**
     * {@code DELETE  /site-org-temps/:id} : delete the "id" siteOrgTemp.
     *
     * @param id the id of the siteOrgTempDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiteOrgTemp(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SiteOrgTemp : {}", id);
        siteOrgTempService.delete(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
