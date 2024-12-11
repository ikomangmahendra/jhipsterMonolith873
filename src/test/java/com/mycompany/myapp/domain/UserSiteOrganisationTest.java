package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RoleTestSamples.*;
import static com.mycompany.myapp.domain.SiteOrganisationTestSamples.*;
import static com.mycompany.myapp.domain.UserSiteOrganisationTestSamples.*;
import static com.mycompany.myapp.domain.UserTempTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserSiteOrganisationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserSiteOrganisation.class);
        UserSiteOrganisation userSiteOrganisation1 = getUserSiteOrganisationSample1();
        UserSiteOrganisation userSiteOrganisation2 = new UserSiteOrganisation();
        assertThat(userSiteOrganisation1).isNotEqualTo(userSiteOrganisation2);

        userSiteOrganisation2.setId(userSiteOrganisation1.getId());
        assertThat(userSiteOrganisation1).isEqualTo(userSiteOrganisation2);

        userSiteOrganisation2 = getUserSiteOrganisationSample2();
        assertThat(userSiteOrganisation1).isNotEqualTo(userSiteOrganisation2);
    }

    @Test
    void userTest() {
        UserSiteOrganisation userSiteOrganisation = getUserSiteOrganisationRandomSampleGenerator();
        UserTemp userTempBack = getUserTempRandomSampleGenerator();

        userSiteOrganisation.setUser(userTempBack);
        assertThat(userSiteOrganisation.getUser()).isEqualTo(userTempBack);

        userSiteOrganisation.user(null);
        assertThat(userSiteOrganisation.getUser()).isNull();
    }

    @Test
    void siteOrganisationTest() {
        UserSiteOrganisation userSiteOrganisation = getUserSiteOrganisationRandomSampleGenerator();
        SiteOrganisation siteOrganisationBack = getSiteOrganisationRandomSampleGenerator();

        userSiteOrganisation.setSiteOrganisation(siteOrganisationBack);
        assertThat(userSiteOrganisation.getSiteOrganisation()).isEqualTo(siteOrganisationBack);

        userSiteOrganisation.siteOrganisation(null);
        assertThat(userSiteOrganisation.getSiteOrganisation()).isNull();
    }

    @Test
    void roleTest() {
        UserSiteOrganisation userSiteOrganisation = getUserSiteOrganisationRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        userSiteOrganisation.setRole(roleBack);
        assertThat(userSiteOrganisation.getRole()).isEqualTo(roleBack);

        userSiteOrganisation.role(null);
        assertThat(userSiteOrganisation.getRole()).isNull();
    }
}
