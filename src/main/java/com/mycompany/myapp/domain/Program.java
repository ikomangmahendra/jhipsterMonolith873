package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.mycompany.myapp.domain.enumeration.ProgramStatus;
import com.mycompany.myapp.domain.enumeration.ProgramType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Set;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Program.
 */
@Entity
@Table(name = "program")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Program implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "name", length = 50, nullable = false)
    private String name;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private ProgramType type;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private ProgramStatus status;

    @NotNull
    @Column(name = "external_system_lkp", nullable = false)
    private Integer externalSystemLkp;

    @NotNull
    @Column(name = "is_enable_follow_up", nullable = false)
    private Boolean isEnableFollowUp;

    @NotNull
    @Column(name = "is_nsf_survey_access", nullable = false)
    private Boolean isNsfSurveyAccess;

    @NotNull
    @Column(name = "is_opt_out_allowed", nullable = false)
    private Boolean isOptOutAllowed;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "rel_program__coordinators",
        joinColumns = @JoinColumn(name = "program_id"),
        inverseJoinColumns = @JoinColumn(name = "coordinators_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "programs" }, allowSetters = true)
    private Set<UserTemp> coordinators = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Program id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Program name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgramType getType() {
        return this.type;
    }

    public Program type(ProgramType type) {
        this.setType(type);
        return this;
    }

    public void setType(ProgramType type) {
        this.type = type;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public Program startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public Program endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public ProgramStatus getStatus() {
        return this.status;
    }

    public Program status(ProgramStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(ProgramStatus status) {
        this.status = status;
    }

    public Integer getExternalSystemLkp() {
        return this.externalSystemLkp;
    }

    public Program externalSystemLkp(Integer externalSystemLkp) {
        this.setExternalSystemLkp(externalSystemLkp);
        return this;
    }

    public void setExternalSystemLkp(Integer externalSystemLkp) {
        this.externalSystemLkp = externalSystemLkp;
    }

    public Boolean getIsEnableFollowUp() {
        return this.isEnableFollowUp;
    }

    public Program isEnableFollowUp(Boolean isEnableFollowUp) {
        this.setIsEnableFollowUp(isEnableFollowUp);
        return this;
    }

    public void setIsEnableFollowUp(Boolean isEnableFollowUp) {
        this.isEnableFollowUp = isEnableFollowUp;
    }

    public Boolean getIsNsfSurveyAccess() {
        return this.isNsfSurveyAccess;
    }

    public Program isNsfSurveyAccess(Boolean isNsfSurveyAccess) {
        this.setIsNsfSurveyAccess(isNsfSurveyAccess);
        return this;
    }

    public void setIsNsfSurveyAccess(Boolean isNsfSurveyAccess) {
        this.isNsfSurveyAccess = isNsfSurveyAccess;
    }

    public Boolean getIsOptOutAllowed() {
        return this.isOptOutAllowed;
    }

    public Program isOptOutAllowed(Boolean isOptOutAllowed) {
        this.setIsOptOutAllowed(isOptOutAllowed);
        return this;
    }

    public void setIsOptOutAllowed(Boolean isOptOutAllowed) {
        this.isOptOutAllowed = isOptOutAllowed;
    }

    public Set<UserTemp> getCoordinators() {
        return this.coordinators;
    }

    public void setCoordinators(Set<UserTemp> userTemps) {
        this.coordinators = userTemps;
    }

    public Program coordinators(Set<UserTemp> userTemps) {
        this.setCoordinators(userTemps);
        return this;
    }

    public Program addCoordinators(UserTemp userTemp) {
        this.coordinators.add(userTemp);
        return this;
    }

    public Program removeCoordinators(UserTemp userTemp) {
        this.coordinators.remove(userTemp);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Program)) {
            return false;
        }
        return getId() != null && getId().equals(((Program) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Program{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", externalSystemLkp=" + getExternalSystemLkp() +
            ", isEnableFollowUp='" + getIsEnableFollowUp() + "'" +
            ", isNsfSurveyAccess='" + getIsNsfSurveyAccess() + "'" +
            ", isOptOutAllowed='" + getIsOptOutAllowed() + "'" +
            "}";
    }
}
