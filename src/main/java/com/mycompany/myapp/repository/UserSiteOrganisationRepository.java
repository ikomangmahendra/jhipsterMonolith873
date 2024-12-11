package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserSiteOrganisation;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSiteOrganisation entity.
 */
@SuppressWarnings("unused")
@Repository
public interface UserSiteOrganisationRepository extends JpaRepository<UserSiteOrganisation, Long> {}
