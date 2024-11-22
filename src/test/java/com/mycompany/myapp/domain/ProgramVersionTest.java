package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AccuteProgramVariableTestSamples.*;
import static com.mycompany.myapp.domain.AdditionalVariableTestSamples.*;
import static com.mycompany.myapp.domain.ProgramTestSamples.*;
import static com.mycompany.myapp.domain.ProgramVersionTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProgramVersionTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ProgramVersion.class);
        ProgramVersion programVersion1 = getProgramVersionSample1();
        ProgramVersion programVersion2 = new ProgramVersion();
        assertThat(programVersion1).isNotEqualTo(programVersion2);

        programVersion2.setId(programVersion1.getId());
        assertThat(programVersion1).isEqualTo(programVersion2);

        programVersion2 = getProgramVersionSample2();
        assertThat(programVersion1).isNotEqualTo(programVersion2);
    }

    @Test
    void programVariablesTest() {
        ProgramVersion programVersion = getProgramVersionRandomSampleGenerator();
        AccuteProgramVariable accuteProgramVariableBack = getAccuteProgramVariableRandomSampleGenerator();

        programVersion.addProgramVariables(accuteProgramVariableBack);
        assertThat(programVersion.getProgramVariables()).containsOnly(accuteProgramVariableBack);
        assertThat(accuteProgramVariableBack.getVersion()).isEqualTo(programVersion);

        programVersion.removeProgramVariables(accuteProgramVariableBack);
        assertThat(programVersion.getProgramVariables()).doesNotContain(accuteProgramVariableBack);
        assertThat(accuteProgramVariableBack.getVersion()).isNull();

        programVersion.programVariables(new HashSet<>(Set.of(accuteProgramVariableBack)));
        assertThat(programVersion.getProgramVariables()).containsOnly(accuteProgramVariableBack);
        assertThat(accuteProgramVariableBack.getVersion()).isEqualTo(programVersion);

        programVersion.setProgramVariables(new HashSet<>());
        assertThat(programVersion.getProgramVariables()).doesNotContain(accuteProgramVariableBack);
        assertThat(accuteProgramVariableBack.getVersion()).isNull();
    }

    @Test
    void additionalVariablesTest() {
        ProgramVersion programVersion = getProgramVersionRandomSampleGenerator();
        AdditionalVariable additionalVariableBack = getAdditionalVariableRandomSampleGenerator();

        programVersion.addAdditionalVariables(additionalVariableBack);
        assertThat(programVersion.getAdditionalVariables()).containsOnly(additionalVariableBack);
        assertThat(additionalVariableBack.getVersion()).isEqualTo(programVersion);

        programVersion.removeAdditionalVariables(additionalVariableBack);
        assertThat(programVersion.getAdditionalVariables()).doesNotContain(additionalVariableBack);
        assertThat(additionalVariableBack.getVersion()).isNull();

        programVersion.additionalVariables(new HashSet<>(Set.of(additionalVariableBack)));
        assertThat(programVersion.getAdditionalVariables()).containsOnly(additionalVariableBack);
        assertThat(additionalVariableBack.getVersion()).isEqualTo(programVersion);

        programVersion.setAdditionalVariables(new HashSet<>());
        assertThat(programVersion.getAdditionalVariables()).doesNotContain(additionalVariableBack);
        assertThat(additionalVariableBack.getVersion()).isNull();
    }

    @Test
    void programTest() {
        ProgramVersion programVersion = getProgramVersionRandomSampleGenerator();
        Program programBack = getProgramRandomSampleGenerator();

        programVersion.setProgram(programBack);
        assertThat(programVersion.getProgram()).isEqualTo(programBack);

        programVersion.program(null);
        assertThat(programVersion.getProgram()).isNull();
    }
}
