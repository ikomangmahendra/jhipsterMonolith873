package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccuteProgramVariableTestSamples.*;
import static com.mycompany.myapp.domain.ProgramVersionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AccuteProgramVariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AccuteProgramVariable.class);
        AccuteProgramVariable accuteProgramVariable1 = getAccuteProgramVariableSample1();
        AccuteProgramVariable accuteProgramVariable2 = new AccuteProgramVariable();
        assertThat(accuteProgramVariable1).isNotEqualTo(accuteProgramVariable2);

        accuteProgramVariable2.setId(accuteProgramVariable1.getId());
        assertThat(accuteProgramVariable1).isEqualTo(accuteProgramVariable2);

        accuteProgramVariable2 = getAccuteProgramVariableSample2();
        assertThat(accuteProgramVariable1).isNotEqualTo(accuteProgramVariable2);
    }

    @Test
    void versionTest() {
        AccuteProgramVariable accuteProgramVariable = getAccuteProgramVariableRandomSampleGenerator();
        ProgramVersion programVersionBack = getProgramVersionRandomSampleGenerator();

        accuteProgramVariable.setVersion(programVersionBack);
        assertThat(accuteProgramVariable.getVersion()).isEqualTo(programVersionBack);

        accuteProgramVariable.version(null);
        assertThat(accuteProgramVariable.getVersion()).isNull();
    }
}
