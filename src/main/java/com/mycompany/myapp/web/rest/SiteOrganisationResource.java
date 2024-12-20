package com.mycompany.myapp.web.rest;

import com.mycompany.myapp.domain.SiteOrganisation;
import com.mycompany.myapp.repository.SiteOrganisationRepository;
import com.mycompany.myapp.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
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
 * REST controller for managing {@link com.mycompany.myapp.domain.SiteOrganisation}.
 */
@RestController
@RequestMapping("/api/site-organisations")
@Transactional
public class SiteOrganisationResource {

    private static final Logger LOG = LoggerFactory.getLogger(SiteOrganisationResource.class);

    private static final String ENTITY_NAME = "siteOrganisation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final SiteOrganisationRepository siteOrganisationRepository;

    public SiteOrganisationResource(SiteOrganisationRepository siteOrganisationRepository) {
        this.siteOrganisationRepository = siteOrganisationRepository;
    }

    /**
     * {@code POST  /site-organisations} : Create a new siteOrganisation.
     *
     * @param siteOrganisation the siteOrganisation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new siteOrganisation, or with status {@code 400 (Bad Request)} if the siteOrganisation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("")
    public ResponseEntity<SiteOrganisation> createSiteOrganisation(@RequestBody SiteOrganisation siteOrganisation)
        throws URISyntaxException {
        LOG.debug("REST request to save SiteOrganisation : {}", siteOrganisation);
        if (siteOrganisation.getId() != null) {
            throw new BadRequestAlertException("A new siteOrganisation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        siteOrganisation = siteOrganisationRepository.save(siteOrganisation);
        return ResponseEntity.created(new URI("/api/site-organisations/" + siteOrganisation.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, siteOrganisation.getId().toString()))
            .body(siteOrganisation);
    }

    /**
     * {@code GET  /site-organisations} : get all the siteOrganisations.
     *
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of siteOrganisations in body.
     */
    @GetMapping("")
    public List<SiteOrganisation> getAllSiteOrganisations() {
        LOG.debug("REST request to get all SiteOrganisations");
        return siteOrganisationRepository.findAll();
    }

    /**
     * {@code GET  /site-organisations/:id} : get the "id" siteOrganisation.
     *
     * @param id the id of the siteOrganisation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the siteOrganisation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/{id}")
    public ResponseEntity<SiteOrganisation> getSiteOrganisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to get SiteOrganisation : {}", id);
        Optional<SiteOrganisation> siteOrganisation = siteOrganisationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(siteOrganisation);
    }

    /**
     * {@code DELETE  /site-organisations/:id} : delete the "id" siteOrganisation.
     *
     * @param id the id of the siteOrganisation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSiteOrganisation(@PathVariable("id") Long id) {
        LOG.debug("REST request to delete SiteOrganisation : {}", id);
        siteOrganisationRepository.deleteById(id);
        return ResponseEntity.noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString()))
            .build();
    }
}
