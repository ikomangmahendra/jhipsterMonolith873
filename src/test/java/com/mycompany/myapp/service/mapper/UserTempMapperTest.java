package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.UserTempAsserts.*;
import static com.mycompany.myapp.domain.UserTempTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class UserTempMapperTest {

    private UserTempMapper userTempMapper;

    @BeforeEach
    void setUp() {
        userTempMapper = new UserTempMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getUserTempSample1();
        var actual = userTempMapper.toEntity(userTempMapper.toDto(expected));
        assertUserTempAllPropertiesEquals(expected, actual);
    }
}
