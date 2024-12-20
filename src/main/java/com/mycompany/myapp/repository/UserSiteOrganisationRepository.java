package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserSiteOrganisation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the UserSiteOrganisation entity.
 *
 * When extending this class, extend UserSiteOrganisationRepositoryWithBagRelationships too.
 * For more information refer to https://github.com/jhipster/generator-jhipster/issues/17990.
 */
@Repository
public interface UserSiteOrganisationRepository
    extends UserSiteOrganisationRepositoryWithBagRelationships, JpaRepository<UserSiteOrganisation, Long> {
    default Optional<UserSiteOrganisation> findOneWithEagerRelationships(Long id) {
        return this.fetchBagRelationships(this.findById(id));
    }

    default List<UserSiteOrganisation> findAllWithEagerRelationships() {
        return this.fetchBagRelationships(this.findAll());
    }

    default Page<UserSiteOrganisation> findAllWithEagerRelationships(Pageable pageable) {
        return this.fetchBagRelationships(this.findAll(pageable));
    }
}
