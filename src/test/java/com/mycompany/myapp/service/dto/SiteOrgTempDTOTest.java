package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class SiteOrgTempDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(SiteOrgTempDTO.class);
        SiteOrgTempDTO siteOrgTempDTO1 = new SiteOrgTempDTO();
        siteOrgTempDTO1.setId(1L);
        SiteOrgTempDTO siteOrgTempDTO2 = new SiteOrgTempDTO();
        assertThat(siteOrgTempDTO1).isNotEqualTo(siteOrgTempDTO2);
        siteOrgTempDTO2.setId(siteOrgTempDTO1.getId());
        assertThat(siteOrgTempDTO1).isEqualTo(siteOrgTempDTO2);
        siteOrgTempDTO2.setId(2L);
        assertThat(siteOrgTempDTO1).isNotEqualTo(siteOrgTempDTO2);
        siteOrgTempDTO1.setId(null);
        assertThat(siteOrgTempDTO1).isNotEqualTo(siteOrgTempDTO2);
    }
}
