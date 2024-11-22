package com.mycompany.myapp.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A AdditionalVariable.
 */
@Entity
@Table(name = "additional_variable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AdditionalVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Column(name = "program_version_id", nullable = false)
    private Integer programVersionId;

    @Lob
    @Column(name = "json_schema", nullable = false)
    private String jsonSchema;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "programVariables", "additionalVariables", "program" }, allowSetters = true)
    private ProgramVersion version;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnoreProperties(value = { "additionalVariables" }, allowSetters = true)
    private SiteOrgTemp site;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public AdditionalVariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getProgramVersionId() {
        return this.programVersionId;
    }

    public AdditionalVariable programVersionId(Integer programVersionId) {
        this.setProgramVersionId(programVersionId);
        return this;
    }

    public void setProgramVersionId(Integer programVersionId) {
        this.programVersionId = programVersionId;
    }

    public String getJsonSchema() {
        return this.jsonSchema;
    }

    public AdditionalVariable jsonSchema(String jsonSchema) {
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

    public AdditionalVariable version(ProgramVersion programVersion) {
        this.setVersion(programVersion);
        return this;
    }

    public SiteOrgTemp getSite() {
        return this.site;
    }

    public void setSite(SiteOrgTemp siteOrgTemp) {
        this.site = siteOrgTemp;
    }

    public AdditionalVariable site(SiteOrgTemp siteOrgTemp) {
        this.setSite(siteOrgTemp);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AdditionalVariable)) {
            return false;
        }
        return getId() != null && getId().equals(((AdditionalVariable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AdditionalVariable{" +
            "id=" + getId() +
            ", programVersionId=" + getProgramVersionId() +
            ", jsonSchema='" + getJsonSchema() + "'" +
            "}";
    }
}
