package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A UserSiteOrganisation.
 */
@Entity
@Table(name = "user_site_organisation")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSiteOrganisation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "siteOrganisations" }, allowSetters = true)
    private UserTemp user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userSiteOrganisations" }, allowSetters = true)
    private SiteOrganisation siteOrganisation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "userSiteOrganisations" }, allowSetters = true)
    private Role role;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSiteOrganisation id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserTemp getUser() {
        return this.user;
    }

    public void setUser(UserTemp userTemp) {
        this.user = userTemp;
    }

    public UserSiteOrganisation user(UserTemp userTemp) {
        this.setUser(userTemp);
        return this;
    }

    public SiteOrganisation getSiteOrganisation() {
        return this.siteOrganisation;
    }

    public void setSiteOrganisation(SiteOrganisation siteOrganisation) {
        this.siteOrganisation = siteOrganisation;
    }

    public UserSiteOrganisation siteOrganisation(SiteOrganisation siteOrganisation) {
        this.setSiteOrganisation(siteOrganisation);
        return this;
    }

    public Role getRole() {
        return this.role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public UserSiteOrganisation role(Role role) {
        this.setRole(role);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSiteOrganisation)) {
            return false;
        }
        return getId() != null && getId().equals(((UserSiteOrganisation) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSiteOrganisation{" +
            "id=" + getId() +
            "}";
    }
}
