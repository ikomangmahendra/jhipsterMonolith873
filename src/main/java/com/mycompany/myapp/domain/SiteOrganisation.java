package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A SiteOrganisation.
 */
@Entity
@Table(name = "site_organisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteOrganisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "siteOrganisation")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "siteOrganisation", "role" }, allowSetters = true)
    private Set<UserSiteOrganisation> userSiteOrganisations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SiteOrganisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<UserSiteOrganisation> getUserSiteOrganisations() {
        return this.userSiteOrganisations;
    }

    public void setUserSiteOrganisations(Set<UserSiteOrganisation> userSiteOrganisations) {
        if (this.userSiteOrganisations != null) {
            this.userSiteOrganisations.forEach(i -> i.setSiteOrganisation(null));
        }
        if (userSiteOrganisations != null) {
            userSiteOrganisations.forEach(i -> i.setSiteOrganisation(this));
        }
        this.userSiteOrganisations = userSiteOrganisations;
    }

    public SiteOrganisation userSiteOrganisations(Set<UserSiteOrganisation> userSiteOrganisations) {
        this.setUserSiteOrganisations(userSiteOrganisations);
        return this;
    }

    public SiteOrganisation addUserSiteOrganisation(UserSiteOrganisation userSiteOrganisation) {
        this.userSiteOrganisations.add(userSiteOrganisation);
        userSiteOrganisation.setSiteOrganisation(this);
        return this;
    }

    public SiteOrganisation removeUserSiteOrganisation(UserSiteOrganisation userSiteOrganisation) {
        this.userSiteOrganisations.remove(userSiteOrganisation);
        userSiteOrganisation.setSiteOrganisation(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteOrganisation)) {
            return false;
        }
        return getId() != null && getId().equals(((SiteOrganisation) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteOrganisation{" +
            "id=" + getId() +
            "}";
    }
}
