package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProgramTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Program getProgramSample1() {
        return new Program().id(1L).name("name1").externalSystemLkp(1);
    }

    public static Program getProgramSample2() {
        return new Program().id(2L).name("name2").externalSystemLkp(2);
    }

    public static Program getProgramRandomSampleGenerator() {
        return new Program()
            .id(longCount.incrementAndGet())
            .name(UUID.randomUUID().toString())
            .externalSystemLkp(intCount.incrementAndGet());
    }
}
