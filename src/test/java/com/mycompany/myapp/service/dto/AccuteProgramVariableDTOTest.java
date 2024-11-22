package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccuteProgramVariableDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccuteProgramVariableDTO.class);
        AccuteProgramVariableDTO accuteProgramVariableDTO1 = new AccuteProgramVariableDTO();
        accuteProgramVariableDTO1.setId(1L);
        AccuteProgramVariableDTO accuteProgramVariableDTO2 = new AccuteProgramVariableDTO();
        assertThat(accuteProgramVariableDTO1).isNotEqualTo(accuteProgramVariableDTO2);
        accuteProgramVariableDTO2.setId(accuteProgramVariableDTO1.getId());
        assertThat(accuteProgramVariableDTO1).isEqualTo(accuteProgramVariableDTO2);
        accuteProgramVariableDTO2.setId(2L);
        assertThat(accuteProgramVariableDTO1).isNotEqualTo(accuteProgramVariableDTO2);
        accuteProgramVariableDTO1.setId(null);
        assertThat(accuteProgramVariableDTO1).isNotEqualTo(accuteProgramVariableDTO2);
    }
}
