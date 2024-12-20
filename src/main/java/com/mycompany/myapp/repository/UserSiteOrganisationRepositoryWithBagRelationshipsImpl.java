package com.mycompany.myapp.repository;

import com.mycompany.myapp.domain.UserSiteOrganisation;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

/**
 * Utility repository to load bag relationships based on https://vladmihalcea.com/hibernate-multiplebagfetchexception/
 */
public class UserSiteOrganisationRepositoryWithBagRelationshipsImpl implements UserSiteOrganisationRepositoryWithBagRelationships {

    private static final String ID_PARAMETER = "id";
    private static final String USERSITEORGANISATIONS_PARAMETER = "userSiteOrganisations";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<UserSiteOrganisation> fetchBagRelationships(Optional<UserSiteOrganisation> userSiteOrganisation) {
        return userSiteOrganisation.map(this::fetchRoles);
    }

    @Override
    public Page<UserSiteOrganisation> fetchBagRelationships(Page<UserSiteOrganisation> userSiteOrganisations) {
        return new PageImpl<>(
            fetchBagRelationships(userSiteOrganisations.getContent()),
            userSiteOrganisations.getPageable(),
            userSiteOrganisations.getTotalElements()
        );
    }

    @Override
    public List<UserSiteOrganisation> fetchBagRelationships(List<UserSiteOrganisation> userSiteOrganisations) {
        return Optional.of(userSiteOrganisations).map(this::fetchRoles).orElse(Collections.emptyList());
    }

    UserSiteOrganisation fetchRoles(UserSiteOrganisation result) {
        return entityManager
            .createQuery(
                "select userSiteOrganisation from UserSiteOrganisation userSiteOrganisation left join fetch userSiteOrganisation.roles where userSiteOrganisation.id = :id",
                UserSiteOrganisation.class
            )
            .setParameter(ID_PARAMETER, result.getId())
            .getSingleResult();
    }

    List<UserSiteOrganisation> fetchRoles(List<UserSiteOrganisation> userSiteOrganisations) {
        HashMap<Object, Integer> order = new HashMap<>();
        IntStream.range(0, userSiteOrganisations.size()).forEach(index -> order.put(userSiteOrganisations.get(index).getId(), index));
        List<UserSiteOrganisation> result = entityManager
            .createQuery(
                "select userSiteOrganisation from UserSiteOrganisation userSiteOrganisation left join fetch userSiteOrganisation.roles where userSiteOrganisation in :userSiteOrganisations",
                UserSiteOrganisation.class
            )
            .setParameter(USERSITEORGANISATIONS_PARAMETER, userSiteOrganisations)
            .getResultList();
        Collections.sort(result, (o1, o2) -> Integer.compare(order.get(o1.getId()), order.get(o2.getId())));
        return result;
    }
}
