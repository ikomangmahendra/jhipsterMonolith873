package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdditionalVariableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdditionalVariableDTO.class);
        AdditionalVariableDTO additionalVariableDTO1 = new AdditionalVariableDTO();
        additionalVariableDTO1.setId(1L);
        AdditionalVariableDTO additionalVariableDTO2 = new AdditionalVariableDTO();
        assertThat(additionalVariableDTO1).isNotEqualTo(additionalVariableDTO2);
        additionalVariableDTO2.setId(additionalVariableDTO1.getId());
        assertThat(additionalVariableDTO1).isEqualTo(additionalVariableDTO2);
        additionalVariableDTO2.setId(2L);
        assertThat(additionalVariableDTO1).isNotEqualTo(additionalVariableDTO2);
        additionalVariableDTO1.setId(null);
        assertThat(additionalVariableDTO1).isNotEqualTo(additionalVariableDTO2);
    }
}
