package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AdditionalVariableAsserts.*;
import static com.mycompany.myapp.domain.AdditionalVariableTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AdditionalVariableMapperTest {

    private AdditionalVariableMapper additionalVariableMapper;

    @BeforeEach
    void setUp() {
        additionalVariableMapper = new AdditionalVariableMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAdditionalVariableSample1();
        var actual = additionalVariableMapper.toEntity(additionalVariableMapper.toDto(expected));
        assertAdditionalVariableAllPropertiesEquals(expected, actual);
    }
}
