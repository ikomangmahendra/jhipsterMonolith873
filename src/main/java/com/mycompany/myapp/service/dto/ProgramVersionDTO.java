package com.mycompany.myapp.service.dto;

import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.ProgramVersion} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ProgramVersionDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer version;

    @NotNull
    private Boolean isActive;

    private LocalDate publishDate;

    private ProgramDTO program;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public LocalDate getPublishDate() {
        return publishDate;
    }

    public void setPublishDate(LocalDate publishDate) {
        this.publishDate = publishDate;
    }

    public ProgramDTO getProgram() {
        return program;
    }

    public void setProgram(ProgramDTO program) {
        this.program = program;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ProgramVersionDTO)) {
            return false;
        }

        ProgramVersionDTO programVersionDTO = (ProgramVersionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, programVersionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ProgramVersionDTO{" +
            "id=" + getId() +
            ", version=" + getVersion() +
            ", isActive='" + getIsActive() + "'" +
            ", publishDate='" + getPublishDate() + "'" +
            ", program=" + getProgram() +
            "}";
    }
}
