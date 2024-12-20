package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class FormVariableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(FormVariableDTO.class);
        FormVariableDTO formVariableDTO1 = new FormVariableDTO();
        formVariableDTO1.setId(1L);
        FormVariableDTO formVariableDTO2 = new FormVariableDTO();
        assertThat(formVariableDTO1).isNotEqualTo(formVariableDTO2);
        formVariableDTO2.setId(formVariableDTO1.getId());
        assertThat(formVariableDTO1).isEqualTo(formVariableDTO2);
        formVariableDTO2.setId(2L);
        assertThat(formVariableDTO1).isNotEqualTo(formVariableDTO2);
        formVariableDTO1.setId(null);
        assertThat(formVariableDTO1).isNotEqualTo(formVariableDTO2);
    }
}
