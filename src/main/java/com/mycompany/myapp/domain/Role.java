package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Role.
 */
@Entity
@Table(name = "role")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Role implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "roles", "siteOrganisations" }, allowSetters = true)
    private UserTemp user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "roles", "user", "siteOrganisation" }, allowSetters = true)
    private UserSiteOrganisation userSiteOrganisation;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Role id(Long id) {
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

    public Role user(UserTemp userTemp) {
        this.setUser(userTemp);
        return this;
    }

    public UserSiteOrganisation getUserSiteOrganisation() {
        return this.userSiteOrganisation;
    }

    public void setUserSiteOrganisation(UserSiteOrganisation userSiteOrganisation) {
        this.userSiteOrganisation = userSiteOrganisation;
    }

    public Role userSiteOrganisation(UserSiteOrganisation userSiteOrganisation) {
        this.setUserSiteOrganisation(userSiteOrganisation);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Role)) {
            return false;
        }
        return getId() != null && getId().equals(((Role) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Role{" +
            "id=" + getId() +
            "}";
    }
}
