package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AccuteProgramVariable.
 */
@Entity
@Table(name = "accute_program_variable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AccuteProgramVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 50)
    @Column(name = "section_id", length = 50, nullable = false)
    private String sectionId;

    @NotNull
    @Size(max = 50)
    @Column(name = "section_name", length = 50, nullable = false)
    private String sectionName;

    @NotNull
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    @Lob
    @Column(name = "json_schema", nullable = false)
    private String jsonSchema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "programVariables", "additionalVariables", "program" }, allowSetters = true)
    private ProgramVersion version;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AccuteProgramVariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionId() {
        return this.sectionId;
    }

    public AccuteProgramVariable sectionId(String sectionId) {
        this.setSectionId(sectionId);
        return this;
    }

    public void setSectionId(String sectionId) {
        this.sectionId = sectionId;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public AccuteProgramVariable sectionName(String sectionName) {
        this.setSectionName(sectionName);
        return this;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public AccuteProgramVariable orderIndex(Integer orderIndex) {
        this.setOrderIndex(orderIndex);
        return this;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    public String getJsonSchema() {
        return this.jsonSchema;
    }

    public AccuteProgramVariable jsonSchema(String jsonSchema) {
        this.setJsonSchema(jsonSchema);
        return this;
    }

    public void setJsonSchema(String jsonSchema) {
        this.jsonSchema = jsonSchema;
    }

    public ProgramVersion getVersion() {
        return this.version;
    }

    public void setVersion(ProgramVersion programVersion) {
        this.version = programVersion;
    }

    public AccuteProgramVariable version(ProgramVersion programVersion) {
        this.setVersion(programVersion);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AccuteProgramVariable)) {
            return false;
        }
        return getId() != null && getId().equals(((AccuteProgramVariable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AccuteProgramVariable{" +
            "id=" + getId() +
            ", sectionId='" + getSectionId() + "'" +
            ", sectionName='" + getSectionName() + "'" +
            ", orderIndex=" + getOrderIndex() +
            ", jsonSchema='" + getJsonSchema() + "'" +
            "}";
    }
}
