package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AdditionalVariable} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditionalVariableDTO implements Serializable {

    private Long id;

    @NotNull
    private Integer programVersionId;

    @Lob
    private String jsonSchema;

    private ProgramVersionDTO version;

    private SiteOrgTempDTO site;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProgramVersionId() {
        return programVersionId;
    }

    public void setProgramVersionId(Integer programVersionId) {
        this.programVersionId = programVersionId;
    }

    public String getJsonSchema() {
        return jsonSchema;
    }

    public void setJsonSchema(String jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    public ProgramVersionDTO getVersion() {
        return version;
    }

    public void setVersion(ProgramVersionDTO version) {
        this.version = version;
    }

    public SiteOrgTempDTO getSite() {
        return site;
    }

    public void setSite(SiteOrgTempDTO site) {
        this.site = site;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalVariableDTO)) {
            return false;
        }

        AdditionalVariableDTO additionalVariableDTO = (AdditionalVariableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, additionalVariableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditionalVariableDTO{" +
            "id=" + getId() +
            ", programVersionId=" + getProgramVersionId() +
            ", jsonSchema='" + getJsonSchema() + "'" +
            ", version=" + getVersion() +
            ", site=" + getSite() +
            "}";
    }
}
