package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class ProgramVersionTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static ProgramVersion getProgramVersionSample1() {
        return new ProgramVersion().id(1L).version(1);
    }

    public static ProgramVersion getProgramVersionSample2() {
        return new ProgramVersion().id(2L).version(2);
    }

    public static ProgramVersion getProgramVersionRandomSampleGenerator() {
        return new ProgramVersion().id(longCount.incrementAndGet()).version(intCount.incrementAndGet());
    }
}
