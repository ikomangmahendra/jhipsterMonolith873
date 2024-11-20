package com.mycompany.myapp.domain;

import static com.mycompany.myapp.domain.ConsumerTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.mycompany.myapp.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConsumerTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Consumer.class);
        Consumer consumer1 = getConsumerSample1();
        Consumer consumer2 = new Consumer();
        assertThat(consumer1).isNotEqualTo(consumer2);

        consumer2.setId(consumer1.getId());
        assertThat(consumer1).isEqualTo(consumer2);

        consumer2 = getConsumerSample2();
        assertThat(consumer1).isNotEqualTo(consumer2);
    }
}
