package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.SiteOrgTemp} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class SiteOrgTempDTO implements Serializable {

    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SiteOrgTempDTO)) {
            return false;
        }

        SiteOrgTempDTO siteOrgTempDTO = (SiteOrgTempDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, siteOrgTempDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "SiteOrgTempDTO{" +
            "id=" + getId() +
            "}";
    }
}
