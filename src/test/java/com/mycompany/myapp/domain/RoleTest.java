package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RoleTestSamples.*;
import static com.mycompany.myapp.domain.UserSiteOrganisationTestSamples.*;
import static com.mycompany.myapp.domain.UserTempTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class RoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Role.class);
        Role role1 = getRoleSample1();
        Role role2 = new Role();
        assertThat(role1).isNotEqualTo(role2);

        role2.setId(role1.getId());
        assertThat(role1).isEqualTo(role2);

        role2 = getRoleSample2();
        assertThat(role1).isNotEqualTo(role2);
    }

    @Test
    void userTest() {
        Role role = getRoleRandomSampleGenerator();
        UserTemp userTempBack = getUserTempRandomSampleGenerator();

        role.setUser(userTempBack);
        assertThat(role.getUser()).isEqualTo(userTempBack);

        role.user(null);
        assertThat(role.getUser()).isNull();
    }

    @Test
    void userSiteOrganisationTest() {
        Role role = getRoleRandomSampleGenerator();
        UserSiteOrganisation userSiteOrganisationBack = getUserSiteOrganisationRandomSampleGenerator();

        role.setUserSiteOrganisation(userSiteOrganisationBack);
        assertThat(role.getUserSiteOrganisation()).isEqualTo(userSiteOrganisationBack);

        role.userSiteOrganisation(null);
        assertThat(role.getUserSiteOrganisation()).isNull();
    }
}
