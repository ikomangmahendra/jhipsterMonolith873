package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ConsumerTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Consumer getConsumerSample1() {
        return new Consumer()
            .id(1L)
            .guid("guid1")
            .note("note1")
            .createdBy("createdBy1")
            .lastModifiedBy("lastModifiedBy1")
            .recordStatusId(1);
    }

    public static Consumer getConsumerSample2() {
        return new Consumer()
            .id(2L)
            .guid("guid2")
            .note("note2")
            .createdBy("createdBy2")
            .lastModifiedBy("lastModifiedBy2")
            .recordStatusId(2);
    }

    public static Consumer getConsumerRandomSampleGenerator() {
        return new Consumer()
            .id(longCount.incrementAndGet())
            .guid(UUID.randomUUID().toString())
            .note(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .lastModifiedBy(UUID.randomUUID().toString())
            .recordStatusId(intCount.incrementAndGet());
    }
}
