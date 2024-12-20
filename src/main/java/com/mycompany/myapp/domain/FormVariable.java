package com.mycompany.myapp.domain;

import com.mycompany.myapp.domain.enumeration.FormVariableType;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.io.Serializable;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FormVariable.
 */
@Entity
@Table(name = "form_variable")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@SuppressWarnings("common-java:DuplicatedBlocks")
public class FormVariable implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @NotNull
    @Size(max = 10)
    @Column(name = "section_code", length = 10, nullable = false)
    private String sectionCode;

    @NotNull
    @Size(max = 50)
    @Column(name = "section_name", length = 50, nullable = false)
    private String sectionName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "form_variable_type", nullable = false)
    private FormVariableType formVariableType;

    @NotNull
    @Column(name = "order_index", nullable = false)
    private Integer orderIndex;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FormVariable id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSectionCode() {
        return this.sectionCode;
    }

    public FormVariable sectionCode(String sectionCode) {
        this.setSectionCode(sectionCode);
        return this;
    }

    public void setSectionCode(String sectionCode) {
        this.sectionCode = sectionCode;
    }

    public String getSectionName() {
        return this.sectionName;
    }

    public FormVariable sectionName(String sectionName) {
        this.setSectionName(sectionName);
        return this;
    }

    public void setSectionName(String sectionName) {
        this.sectionName = sectionName;
    }

    public FormVariableType getFormVariableType() {
        return this.formVariableType;
    }

    public FormVariable formVariableType(FormVariableType formVariableType) {
        this.setFormVariableType(formVariableType);
        return this;
    }

    public void setFormVariableType(FormVariableType formVariableType) {
        this.formVariableType = formVariableType;
    }

    public Integer getOrderIndex() {
        return this.orderIndex;
    }

    public FormVariable orderIndex(Integer orderIndex) {
        this.setOrderIndex(orderIndex);
        return this;
    }

    public void setOrderIndex(Integer orderIndex) {
        this.orderIndex = orderIndex;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FormVariable)) {
            return false;
        }
        return getId() != null && getId().equals(((FormVariable) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FormVariable{" +
            "id=" + getId() +
            ", sectionCode='" + getSectionCode() + "'" +
            ", sectionName='" + getSectionName() + "'" +
            ", formVariableType='" + getFormVariableType() + "'" +
            ", orderIndex=" + getOrderIndex() +
            "}";
    }
}
