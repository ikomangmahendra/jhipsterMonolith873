package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProgramTestSamples.*;
import static com.mycompany.myapp.domain.UserTempTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class ProgramTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Program.class);
        Program program1 = getProgramSample1();
        Program program2 = new Program();
        assertThat(program1).isNotEqualTo(program2);

        program2.setId(program1.getId());
        assertThat(program1).isEqualTo(program2);

        program2 = getProgramSample2();
        assertThat(program1).isNotEqualTo(program2);
    }

    @Test
    void coordinatorsTest() {
        Program program = getProgramRandomSampleGenerator();
        UserTemp userTempBack = getUserTempRandomSampleGenerator();

        program.addCoordinators(userTempBack);
        assertThat(program.getCoordinators()).containsOnly(userTempBack);

        program.removeCoordinators(userTempBack);
        assertThat(program.getCoordinators()).doesNotContain(userTempBack);

        program.coordinators(new HashSet<>(Set.of(userTempBack)));
        assertThat(program.getCoordinators()).containsOnly(userTempBack);

        program.setCoordinators(new HashSet<>());
        assertThat(program.getCoordinators()).doesNotContain(userTempBack);
    }
}
