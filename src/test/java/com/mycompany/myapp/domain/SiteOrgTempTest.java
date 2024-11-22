package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.AdditionalVariableTestSamples.*;
import static com.mycompany.myapp.domain.SiteOrgTempTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SiteOrgTempTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteOrgTemp.class);
        SiteOrgTemp siteOrgTemp1 = getSiteOrgTempSample1();
        SiteOrgTemp siteOrgTemp2 = new SiteOrgTemp();
        assertThat(siteOrgTemp1).isNotEqualTo(siteOrgTemp2);

        siteOrgTemp2.setId(siteOrgTemp1.getId());
        assertThat(siteOrgTemp1).isEqualTo(siteOrgTemp2);

        siteOrgTemp2 = getSiteOrgTempSample2();
        assertThat(siteOrgTemp1).isNotEqualTo(siteOrgTemp2);
    }

    @Test
    void hashCodeVerifier() {
        SiteOrgTemp siteOrgTemp = new SiteOrgTemp();
        assertThat(siteOrgTemp.hashCode()).isZero();

        SiteOrgTemp siteOrgTemp1 = getSiteOrgTempSample1();
        siteOrgTemp.setId(siteOrgTemp1.getId());
        assertThat(siteOrgTemp).hasSameHashCodeAs(siteOrgTemp1);
    }

    @Test
    void additionalVariableTest() {
        SiteOrgTemp siteOrgTemp = getSiteOrgTempRandomSampleGenerator();
        AdditionalVariable additionalVariableBack = getAdditionalVariableRandomSampleGenerator();

        siteOrgTemp.addAdditionalVariable(additionalVariableBack);
        assertThat(siteOrgTemp.getAdditionalVariables()).containsOnly(additionalVariableBack);
        assertThat(additionalVariableBack.getSite()).isEqualTo(siteOrgTemp);

        siteOrgTemp.removeAdditionalVariable(additionalVariableBack);
        assertThat(siteOrgTemp.getAdditionalVariables()).doesNotContain(additionalVariableBack);
        assertThat(additionalVariableBack.getSite()).isNull();

        siteOrgTemp.additionalVariables(new HashSet<>(Set.of(additionalVariableBack)));
        assertThat(siteOrgTemp.getAdditionalVariables()).containsOnly(additionalVariableBack);
        assertThat(additionalVariableBack.getSite()).isEqualTo(siteOrgTemp);

        siteOrgTemp.setAdditionalVariables(new HashSet<>());
        assertThat(siteOrgTemp.getAdditionalVariables()).doesNotContain(additionalVariableBack);
        assertThat(additionalVariableBack.getSite()).isNull();
    }
}
