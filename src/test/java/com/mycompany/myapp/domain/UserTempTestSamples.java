package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserTempTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserTemp getUserTempSample1() {
        return new UserTemp().id(1L);
    }

    public static UserTemp getUserTempSample2() {
        return new UserTemp().id(2L);
    }

    public static UserTemp getUserTempRandomSampleGenerator() {
        return new UserTemp().id(longCount.incrementAndGet());
    }
}
