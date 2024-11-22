package com.mycompany.myapp.service.dto;

import jakarta.persistence.Lob;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.AccuteProgramVariable} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccuteProgramVariableDTO implements Serializable {

    private Long id;

    @NotNull
    @Size(max = 50)
    private String sectionId;

    @NotNull
    @Size(max = 50)
    private String sectionName;

    @NotNull
    private Integer orderIndex;

    @Lob
    private String jsonSchema;

    private ProgramVersionDTO version;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionId() {
        return sectionId;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return sectionName;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getOrderIndex() {
        return orderIndex;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccuteProgramVariableDTO)) {
            return false;
        }

        AccuteProgramVariableDTO accuteProgramVariableDTO = (AccuteProgramVariableDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, accuteProgramVariableDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccuteProgramVariableDTO{" +
            "id=" + getId() +
            ", sectionId='" + getSectionId() + "'" +
            ", sectionName='" + getSectionName() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", jsonSchema='" + getJsonSchema() + "'" +
            ", version=" + getVersion() +
            "}";
    }
}
