package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.ConsumerAsserts.*;
import static com.mycompany.myapp.domain.ConsumerTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ConsumerMapperTest {

    private ConsumerMapper consumerMapper;

    @BeforeEach
    void setUp() {
        consumerMapper = new ConsumerMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getConsumerSample1();
        var actual = consumerMapper.toEntity(consumerMapper.toDto(expected));
        assertConsumerAllPropertiesEquals(expected, actual);
    }
}
