package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RoleTestSamples.*;
import static com.mycompany.myapp.domain.UserSiteOrganisationTestSamples.*;
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
    void hashCodeVerifier() {
        UserTemp userTemp = new UserTemp();
        assertThat(userTemp.hashCode()).isZero();

        UserTemp userTemp1 = getUserTempSample1();
        userTemp.setId(userTemp1.getId());
        assertThat(userTemp).hasSameHashCodeAs(userTemp1);
    }

    @Test
    void rolesTest() {
        UserTemp userTemp = getUserTempRandomSampleGenerator();
        Role roleBack = getRoleRandomSampleGenerator();

        userTemp.addRoles(roleBack);
        assertThat(userTemp.getRoles()).containsOnly(roleBack);
        assertThat(roleBack.getUser()).isEqualTo(userTemp);

        userTemp.removeRoles(roleBack);
        assertThat(userTemp.getRoles()).doesNotContain(roleBack);
        assertThat(roleBack.getUser()).isNull();

        userTemp.roles(new HashSet<>(Set.of(roleBack)));
        assertThat(userTemp.getRoles()).containsOnly(roleBack);
        assertThat(roleBack.getUser()).isEqualTo(userTemp);

        userTemp.setRoles(new HashSet<>());
        assertThat(userTemp.getRoles()).doesNotContain(roleBack);
        assertThat(roleBack.getUser()).isNull();
    }

    @Test
    void siteOrganisationsTest() {
        UserTemp userTemp = getUserTempRandomSampleGenerator();
        UserSiteOrganisation userSiteOrganisationBack = getUserSiteOrganisationRandomSampleGenerator();

        userTemp.addSiteOrganisations(userSiteOrganisationBack);
        assertThat(userTemp.getSiteOrganisations()).containsOnly(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getUser()).isEqualTo(userTemp);

        userTemp.removeSiteOrganisations(userSiteOrganisationBack);
        assertThat(userTemp.getSiteOrganisations()).doesNotContain(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getUser()).isNull();

        userTemp.siteOrganisations(new HashSet<>(Set.of(userSiteOrganisationBack)));
        assertThat(userTemp.getSiteOrganisations()).containsOnly(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getUser()).isEqualTo(userTemp);

        userTemp.setSiteOrganisations(new HashSet<>());
        assertThat(userTemp.getSiteOrganisations()).doesNotContain(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getUser()).isNull();
    }
}
