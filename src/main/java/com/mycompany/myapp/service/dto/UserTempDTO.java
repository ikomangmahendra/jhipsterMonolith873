package com.mycompany.myapp.service.dto;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * A DTO for the {@link com.mycompany.myapp.domain.UserTemp} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserTempDTO implements Serializable {

    private Long id;

    private Set<ProgramDTO> programs = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<ProgramDTO> getPrograms() {
        return programs;
    }

    public void setPrograms(Set<ProgramDTO> programs) {
        this.programs = programs;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserTempDTO)) {
            return false;
        }

        UserTempDTO userTempDTO = (UserTempDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userTempDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserTempDTO{" +
            "id=" + getId() +
            ", programs=" + getPrograms() +
            "}";
    }
}
