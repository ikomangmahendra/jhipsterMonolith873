package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AdditionalVariableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AdditionalVariable getAdditionalVariableSample1() {
        return new AdditionalVariable().id(1L).programVersionId(1);
    }

    public static AdditionalVariable getAdditionalVariableSample2() {
        return new AdditionalVariable().id(2L).programVersionId(2);
    }

    public static AdditionalVariable getAdditionalVariableRandomSampleGenerator() {
        return new AdditionalVariable().id(longCount.incrementAndGet()).programVersionId(intCount.incrementAndGet());
    }
}
