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
 * A UserTemp.
 */
@Entity
@Table(name = "user_temp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "user", "userSiteOrganisations" }, allowSetters = true)
    private Set<Role> roles = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "user")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "roles", "user", "siteOrganisation" }, allowSetters = true)
    private Set<UserSiteOrganisation> siteOrganisations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserTemp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Role> getRoles() {
        return this.roles;
    }

    public void setRoles(Set<Role> roles) {
        if (this.roles != null) {
            this.roles.forEach(i -> i.setUser(null));
        }
        if (roles != null) {
            roles.forEach(i -> i.setUser(this));
        }
        this.roles = roles;
    }

    public UserTemp roles(Set<Role> roles) {
        this.setRoles(roles);
        return this;
    }

    public UserTemp addRoles(Role role) {
        this.roles.add(role);
        role.setUser(this);
        return this;
    }

    public UserTemp removeRoles(Role role) {
        this.roles.remove(role);
        role.setUser(null);
        return this;
    }

    public Set<UserSiteOrganisation> getSiteOrganisations() {
        return this.siteOrganisations;
    }

    public void setSiteOrganisations(Set<UserSiteOrganisation> userSiteOrganisations) {
        if (this.siteOrganisations != null) {
            this.siteOrganisations.forEach(i -> i.setUser(null));
        }
        if (userSiteOrganisations != null) {
            userSiteOrganisations.forEach(i -> i.setUser(this));
        }
        this.siteOrganisations = userSiteOrganisations;
    }

    public UserTemp siteOrganisations(Set<UserSiteOrganisation> userSiteOrganisations) {
        this.setSiteOrganisations(userSiteOrganisations);
        return this;
    }

    public UserTemp addSiteOrganisations(UserSiteOrganisation userSiteOrganisation) {
        this.siteOrganisations.add(userSiteOrganisation);
        userSiteOrganisation.setUser(this);
        return this;
    }

    public UserTemp removeSiteOrganisations(UserSiteOrganisation userSiteOrganisation) {
        this.siteOrganisations.remove(userSiteOrganisation);
        userSiteOrganisation.setUser(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserTemp)) {
            return false;
        }
        return getId() != null && getId().equals(((UserTemp) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTemp{" +
            "id=" + getId() +
            "}";
    }
}
