package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserSiteOrganisation;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;

public interface UserSiteOrganisationRepositoryWithBagRelationships {
    Optional<UserSiteOrganisation> fetchBagRelationships(Optional<UserSiteOrganisation> userSiteOrganisation);

    List<UserSiteOrganisation> fetchBagRelationships(List<UserSiteOrganisation> userSiteOrganisations);

    Page<UserSiteOrganisation> fetchBagRelationships(Page<UserSiteOrganisation> userSiteOrganisations);
}
