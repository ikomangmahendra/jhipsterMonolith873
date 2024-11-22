package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A ProgramVersion.
 */
@Entity
@Table(name = "program_version")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgramVersion implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "version", nullable = false)
    private Integer version;

    @NotNull
    @Column(name = "is_active", nullable = false)
    private Boolean isActive;

    @Column(name = "publish_date")
    private LocalDate publishDate;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "version")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "version" }, allowSetters = true)
    private Set<AccuteProgramVariable> programVariables = new HashSet<>();

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "version")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "version", "site" }, allowSetters = true)
    private Set<AdditionalVariable> additionalVariables = new HashSet<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "versions", "coordinators" }, allowSetters = true)
    private Program program;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public ProgramVersion id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return this.version;
    }

    public ProgramVersion version(Integer version) {
        this.setVersion(version);
        return this;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return this.isActive;
    }

    public ProgramVersion isActive(Boolean isActive) {
        this.setIsActive(isActive);
        return this;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getPublishDate() {
        return this.publishDate;
    }

    public ProgramVersion publishDate(LocalDate publishDate) {
        this.setPublishDate(publishDate);
        return this;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public Set<AccuteProgramVariable> getProgramVariables() {
        return this.programVariables;
    }

    public void setProgramVariables(Set<AccuteProgramVariable> accuteProgramVariables) {
        if (this.programVariables != null) {
            this.programVariables.forEach(i -> i.setVersion(null));
        }
        if (accuteProgramVariables != null) {
            accuteProgramVariables.forEach(i -> i.setVersion(this));
        }
        this.programVariables = accuteProgramVariables;
    }

    public ProgramVersion programVariables(Set<AccuteProgramVariable> accuteProgramVariables) {
        this.setProgramVariables(accuteProgramVariables);
        return this;
    }

    public ProgramVersion addProgramVariables(AccuteProgramVariable accuteProgramVariable) {
        this.programVariables.add(accuteProgramVariable);
        accuteProgramVariable.setVersion(this);
        return this;
    }

    public ProgramVersion removeProgramVariables(AccuteProgramVariable accuteProgramVariable) {
        this.programVariables.remove(accuteProgramVariable);
        accuteProgramVariable.setVersion(null);
        return this;
    }

    public Set<AdditionalVariable> getAdditionalVariables() {
        return this.additionalVariables;
    }

    public void setAdditionalVariables(Set<AdditionalVariable> additionalVariables) {
        if (this.additionalVariables != null) {
            this.additionalVariables.forEach(i -> i.setVersion(null));
        }
        if (additionalVariables != null) {
            additionalVariables.forEach(i -> i.setVersion(this));
        }
        this.additionalVariables = additionalVariables;
    }

    public ProgramVersion additionalVariables(Set<AdditionalVariable> additionalVariables) {
        this.setAdditionalVariables(additionalVariables);
        return this;
    }

    public ProgramVersion addAdditionalVariables(AdditionalVariable additionalVariable) {
        this.additionalVariables.add(additionalVariable);
        additionalVariable.setVersion(this);
        return this;
    }

    public ProgramVersion removeAdditionalVariables(AdditionalVariable additionalVariable) {
        this.additionalVariables.remove(additionalVariable);
        additionalVariable.setVersion(null);
        return this;
    }

    public Program getProgram() {
        return this.program;
    }

    public void setProgram(Program program) {
        this.program = program;
    }

    public ProgramVersion program(Program program) {
        this.setProgram(program);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramVersion)) {
            return false;
        }
        return getId() != null && getId().equals(((ProgramVersion) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramVersion{" +
            "id=" + getId() +
            ", version=" + getVersion() +
            ", isActive='" + getIsActive() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            "}";
    }
}
