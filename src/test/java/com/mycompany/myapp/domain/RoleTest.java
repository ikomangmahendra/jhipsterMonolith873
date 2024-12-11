package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.RoleTestSamples.*;
import static com.mycompany.myapp.domain.UserSiteOrganisationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import java.util.HashSet;
import java.util.Set;
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
    void hashCodeVerifier() {
        Role role = new Role();
        assertThat(role.hashCode()).isZero();

        Role role1 = getRoleSample1();
        role.setId(role1.getId());
        assertThat(role).hasSameHashCodeAs(role1);
    }

    @Test
    void userSiteOrganisationTest() {
        Role role = getRoleRandomSampleGenerator();
        UserSiteOrganisation userSiteOrganisationBack = getUserSiteOrganisationRandomSampleGenerator();

        role.addUserSiteOrganisation(userSiteOrganisationBack);
        assertThat(role.getUserSiteOrganisations()).containsOnly(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getRole()).isEqualTo(role);

        role.removeUserSiteOrganisation(userSiteOrganisationBack);
        assertThat(role.getUserSiteOrganisations()).doesNotContain(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getRole()).isNull();

        role.userSiteOrganisations(new HashSet<>(Set.of(userSiteOrganisationBack)));
        assertThat(role.getUserSiteOrganisations()).containsOnly(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getRole()).isEqualTo(role);

        role.setUserSiteOrganisations(new HashSet<>());
        assertThat(role.getUserSiteOrganisations()).doesNotContain(userSiteOrganisationBack);
        assertThat(userSiteOrganisationBack.getRole()).isNull();
    }
}
