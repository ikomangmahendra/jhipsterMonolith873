package com.mycompany.myapp.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class RoleAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleAllPropertiesEquals(Role expected, Role actual) {
        assertRoleAutoGeneratedPropertiesEquals(expected, actual);
        assertRoleAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleAllUpdatablePropertiesEquals(Role expected, Role actual) {
        assertRoleUpdatableFieldsEquals(expected, actual);
        assertRoleUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleAutoGeneratedPropertiesEquals(Role expected, Role actual) {
        assertThat(expected)
            .as("Verify Role auto generated properties")
            .satisfies(e -> assertThat(e.getId()).as("check id").isEqualTo(actual.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUpdatableFieldsEquals(Role expected, Role actual) {
        // empty method

    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertRoleUpdatableRelationshipsEquals(Role expected, Role actual) {
        // empty method
    }
}
