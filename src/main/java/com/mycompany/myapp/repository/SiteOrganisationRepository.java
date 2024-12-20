package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SiteOrganisation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SiteOrganisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteOrganisationRepository extends JpaRepository<SiteOrganisation, Long> {}
