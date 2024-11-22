package com.mycompany.myapp.service.dto;

import com.mycompany.myapp.domain.enumeration.ProgramStatus;
import com.mycompany.myapp.domain.enumeration.ProgramType;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.Instant;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.Program} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgramDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String name;

    @NotNull
    private ProgramType type;

    private Instant startDate;

    private Instant endDate;

    @NotNull
    private ProgramStatus status;

    @NotNull
    private Integer externalSystemLkp;

    @NotNull
    private Boolean isEnableFollowUp;

    @NotNull
    private Boolean isNsfSurveyAccess;

    @NotNull
    private Boolean isOptOutAllowed;

    private Set<UserTempDTO> coordinators = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ProgramType getType() {
        return type;
    }

    public void setType(ProgramType type) {
        this.type = type;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public ProgramStatus getStatus() {
        return status;
    }

    public void setStatus(ProgramStatus status) {
        this.status = status;
    }

    public Integer getExternalSystemLkp() {
        return externalSystemLkp;
    }

    public void setExternalSystemLkp(Integer externalSystemLkp) {
        this.externalSystemLkp = externalSystemLkp;
    }

    public Boolean getIsEnableFollowUp() {
        return isEnableFollowUp;
    }

    public void setIsEnableFollowUp(Boolean isEnableFollowUp) {
        this.isEnableFollowUp = isEnableFollowUp;
    }

    public Boolean getIsNsfSurveyAccess() {
        return isNsfSurveyAccess;
    }

    public void setIsNsfSurveyAccess(Boolean isNsfSurveyAccess) {
        this.isNsfSurveyAccess = isNsfSurveyAccess;
    }

    public Boolean getIsOptOutAllowed() {
        return isOptOutAllowed;
    }

    public void setIsOptOutAllowed(Boolean isOptOutAllowed) {
        this.isOptOutAllowed = isOptOutAllowed;
    }

    public Set<UserTempDTO> getCoordinators() {
        return coordinators;
    }

    public void setCoordinators(Set<UserTempDTO> coordinators) {
        this.coordinators = coordinators;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramDTO)) {
            return false;
        }

        ProgramDTO programDTO = (ProgramDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramDTO{" +
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
            ", coordinators=" + getCoordinators() +
            "}";
    }
}
