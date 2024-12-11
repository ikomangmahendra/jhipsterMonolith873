package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.SiteOrganisationTestSamples.*;
import static com.mycompany.myapp.domain.UserSiteOrganisationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
import org.junit.jupiter.api.Test;

class SiteOrganisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteOrganisation.class);
        SiteOrganisation siteOrganisation1 = getSiteOrganisationSample1();
        SiteOrganisation siteOrganisation2 = new SiteOrganisation();
        assertThat(siteOrganisation1).isNotEqualTo(siteOrganisation2);

        siteOrganisation2.setId(siteOrganisation1.getId());
        assertThat(siteOrganisation1).isEqualTo(siteOrganisation2);

        siteOrganisation2 = getSiteOrganisationSample2();
        assertThat(siteOrganisation1).isNotEqualTo(siteOrganisation2);
    }

    @Test
    void hashCodeVerifier() {
        SiteOrganisation siteOrganisation = new SiteOrganisation();
        assertThat(siteOrganisation.hashCode()).isZero();

        SiteOrganisation siteOrganisation1 = getSiteOrganisationSample1();
        siteOrganisation.setId(siteOrganisation1.getId());
        assertThat(siteOrganisation).hasSameHashCodeAs(siteOrganisation1);
    }

    @Test
    void userSiteOrganisationTest() {
        SiteOrganisation siteOrganisation = getSiteOrganisationRandomSampleGenerator();
        UserSiteOrganisation userSiteOrganisationBack = getUserSiteOrganisationRandomSampleGenerator();

        siteOrganisation.addUserSiteOrganisation(userSiteOrganisationBack);
        assertThat(siteOrganisation.getUserSiteOrganisations()).containsOnly(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getSiteOrganisation()).isEqualTo(siteOrganisation);

        siteOrganisation.removeUserSiteOrganisation(userSiteOrganisationBack);
        assertThat(siteOrganisation.getUserSiteOrganisations()).doesNotContain(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getSiteOrganisation()).isNull();

        siteOrganisation.userSiteOrganisations(new HashSet<>(Set.of(userSiteOrganisationBack)));
        assertThat(siteOrganisation.getUserSiteOrganisations()).containsOnly(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getSiteOrganisation()).isEqualTo(siteOrganisation);

        siteOrganisation.setUserSiteOrganisations(new HashSet<>());
        assertThat(siteOrganisation.getUserSiteOrganisations()).doesNotContain(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getSiteOrganisation()).isNull();
    }
}
