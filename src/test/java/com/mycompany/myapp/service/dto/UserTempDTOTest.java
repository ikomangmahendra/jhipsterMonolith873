package com.mycompany.myapp.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class UserTempDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(UserTempDTO.class);
        UserTempDTO userTempDTO1 = new UserTempDTO();
        userTempDTO1.setId(1L);
        UserTempDTO userTempDTO2 = new UserTempDTO();
        assertThat(userTempDTO1).isNotEqualTo(userTempDTO2);
        userTempDTO2.setId(userTempDTO1.getId());
        assertThat(userTempDTO1).isEqualTo(userTempDTO2);
        userTempDTO2.setId(2L);
        assertThat(userTempDTO1).isNotEqualTo(userTempDTO2);
        userTempDTO1.setId(null);
        assertThat(userTempDTO1).isNotEqualTo(userTempDTO2);
    }
}
