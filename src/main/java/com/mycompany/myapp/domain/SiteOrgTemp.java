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
 * A SiteOrgTemp.
 */
@Entity
@Table(name = "site_org_temp")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteOrgTemp implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "site")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "version", "site" }, allowSetters = true)
    private Set<AdditionalVariable> additionalVariables = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public SiteOrgTemp id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AdditionalVariable> getAdditionalVariables() {
        return this.additionalVariables;
    }

    public void setAdditionalVariables(Set<AdditionalVariable> additionalVariables) {
        if (this.additionalVariables != null) {
            this.additionalVariables.forEach(i -> i.setSite(null));
        }
        if (additionalVariables != null) {
            additionalVariables.forEach(i -> i.setSite(this));
        }
        this.additionalVariables = additionalVariables;
    }

    public SiteOrgTemp additionalVariables(Set<AdditionalVariable> additionalVariables) {
        this.setAdditionalVariables(additionalVariables);
        return this;
    }

    public SiteOrgTemp addAdditionalVariable(AdditionalVariable additionalVariable) {
        this.additionalVariables.add(additionalVariable);
        additionalVariable.setSite(this);
        return this;
    }

    public SiteOrgTemp removeAdditionalVariable(AdditionalVariable additionalVariable) {
        this.additionalVariables.remove(additionalVariable);
        additionalVariable.setSite(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteOrgTemp)) {
            return false;
        }
        return getId() != null && getId().equals(((SiteOrgTemp) o).getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteOrgTemp{" +
            "id=" + getId() +
            "}";
    }
}
