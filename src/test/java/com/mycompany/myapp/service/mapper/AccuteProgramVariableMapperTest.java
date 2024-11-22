package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.AccuteProgramVariableAsserts.*;
import static com.mycompany.myapp.domain.AccuteProgramVariableTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AccuteProgramVariableMapperTest {

    private AccuteProgramVariableMapper accuteProgramVariableMapper;

    @BeforeEach
    void setUp() {
        accuteProgramVariableMapper = new AccuteProgramVariableMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAccuteProgramVariableSample1();
        var actual = accuteProgramVariableMapper.toEntity(accuteProgramVariableMapper.toDto(expected));
        assertAccuteProgramVariableAllPropertiesEquals(expected, actual);
    }
}
