package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AdditionalVariableTestSamples.*;
import static com.mycompany.myapp.domain.ProgramVersionTestSamples.*;
import static com.mycompany.myapp.domain.SiteOrgTempTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AdditionalVariableTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AdditionalVariable.class);
        AdditionalVariable additionalVariable1 = getAdditionalVariableSample1();
        AdditionalVariable additionalVariable2 = new AdditionalVariable();
        assertThat(additionalVariable1).isNotEqualTo(additionalVariable2);

        additionalVariable2.setId(additionalVariable1.getId());
        assertThat(additionalVariable1).isEqualTo(additionalVariable2);

        additionalVariable2 = getAdditionalVariableSample2();
        assertThat(additionalVariable1).isNotEqualTo(additionalVariable2);
    }

    @Test
    void versionTest() {
        AdditionalVariable additionalVariable = getAdditionalVariableRandomSampleGenerator();
        ProgramVersion programVersionBack = getProgramVersionRandomSampleGenerator();

        additionalVariable.setVersion(programVersionBack);
        assertThat(additionalVariable.getVersion()).isEqualTo(programVersionBack);

        additionalVariable.version(null);
        assertThat(additionalVariable.getVersion()).isNull();
    }

    @Test
    void siteTest() {
        AdditionalVariable additionalVariable = getAdditionalVariableRandomSampleGenerator();
        SiteOrgTemp siteOrgTempBack = getSiteOrgTempRandomSampleGenerator();

        additionalVariable.setSite(siteOrgTempBack);
        assertThat(additionalVariable.getSite()).isEqualTo(siteOrgTempBack);

        additionalVariable.site(null);
        assertThat(additionalVariable.getSite()).isNull();
    }
}
