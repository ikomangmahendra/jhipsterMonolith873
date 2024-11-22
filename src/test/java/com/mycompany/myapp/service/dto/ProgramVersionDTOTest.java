package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ProgramVersionDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramVersionDTO.class);
        ProgramVersionDTO programVersionDTO1 = new ProgramVersionDTO();
        programVersionDTO1.setId(1L);
        ProgramVersionDTO programVersionDTO2 = new ProgramVersionDTO();
        assertThat(programVersionDTO1).isNotEqualTo(programVersionDTO2);
        programVersionDTO2.setId(programVersionDTO1.getId());
        assertThat(programVersionDTO1).isEqualTo(programVersionDTO2);
        programVersionDTO2.setId(2L);
        assertThat(programVersionDTO1).isNotEqualTo(programVersionDTO2);
        programVersionDTO1.setId(null);
        assertThat(programVersionDTO1).isNotEqualTo(programVersionDTO2);
    }
}
