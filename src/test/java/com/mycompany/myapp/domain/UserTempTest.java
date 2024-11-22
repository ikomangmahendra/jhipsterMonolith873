package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ProgramTestSamples.*;
import static com.mycompany.myapp.domain.UserTempTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class UserTempTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTemp.class);
        UserTemp userTemp1 = getUserTempSample1();
        UserTemp userTemp2 = new UserTemp();
        assertThat(userTemp1).isNotEqualTo(userTemp2);

        userTemp2.setId(userTemp1.getId());
        assertThat(userTemp1).isEqualTo(userTemp2);

        userTemp2 = getUserTempSample2();
        assertThat(userTemp1).isNotEqualTo(userTemp2);
    }

    @Test
    void programTest() {
        UserTemp userTemp = getUserTempRandomSampleGenerator();
        Program programBack = getProgramRandomSampleGenerator();

        userTemp.addProgram(programBack);
        assertThat(userTemp.getPrograms()).containsOnly(programBack);
        assertThat(programBack.getCoordinators()).containsOnly(userTemp);

        userTemp.removeProgram(programBack);
        assertThat(userTemp.getPrograms()).doesNotContain(programBack);
        assertThat(programBack.getCoordinators()).doesNotContain(userTemp);

        userTemp.programs(new HashSet<>(Set.of(programBack)));
        assertThat(userTemp.getPrograms()).containsOnly(programBack);
        assertThat(programBack.getCoordinators()).containsOnly(userTemp);

        userTemp.setPrograms(new HashSet<>());
        assertThat(userTemp.getPrograms()).doesNotContain(programBack);
        assertThat(programBack.getCoordinators()).doesNotContain(userTemp);
    }
}
