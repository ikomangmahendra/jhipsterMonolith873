package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ProgramVersionAsserts.*;
import static com.mycompany.myapp.domain.ProgramVersionTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ProgramVersionMapperTest {

    private ProgramVersionMapper programVersionMapper;

    @BeforeEach
    void setUp() {
        programVersionMapper = new ProgramVersionMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getProgramVersionSample1();
        var actual = programVersionMapper.toEntity(programVersionMapper.toDto(expected));
        assertProgramVersionAllPropertiesEquals(expected, actual);
    }
}
