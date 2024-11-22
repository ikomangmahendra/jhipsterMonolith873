package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.SiteOrgTemp;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the SiteOrgTemp entity.
 */
@SuppressWarnings("unused")
@Repository
public interface SiteOrgTempRepository extends JpaRepository<SiteOrgTemp, Long> {}
