package com.mycompany.myapp.service.mapper;

import static com.mycompany.myapp.domain.SiteOrgTempAsserts.*;
import static com.mycompany.myapp.domain.SiteOrgTempTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class SiteOrgTempMapperTest {

    private SiteOrgTempMapper siteOrgTempMapper;

    @BeforeEach
    void setUp() {
        siteOrgTempMapper = new SiteOrgTempMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getSiteOrgTempSample1();
        var actual = siteOrgTempMapper.toEntity(siteOrgTempMapper.toDto(expected));
        assertSiteOrgTempAllPropertiesEquals(expected, actual);
    }
}
