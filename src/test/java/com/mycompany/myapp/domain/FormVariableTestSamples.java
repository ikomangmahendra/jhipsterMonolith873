package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class FormVariableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static FormVariable getFormVariableSample1() {
        return new FormVariable().id(1L).sectionCode("sectionCode1").sectionName("sectionName1").orderIndex(1);
    }

    public static FormVariable getFormVariableSample2() {
        return new FormVariable().id(2L).sectionCode("sectionCode2").sectionName("sectionName2").orderIndex(2);
    }

    public static FormVariable getFormVariableRandomSampleGenerator() {
        return new FormVariable()
            .id(longCount.incrementAndGet())
            .sectionCode(UUID.randomUUID().toString())
            .sectionName(UUID.randomUUID().toString())
            .orderIndex(intCount.incrementAndGet());
    }
}
