package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class AccuteProgramVariableTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AccuteProgramVariable getAccuteProgramVariableSample1() {
        return new AccuteProgramVariable().id(1L).sectionId("sectionId1").sectionName("sectionName1").orderIndex(1);
    }

    public static AccuteProgramVariable getAccuteProgramVariableSample2() {
        return new AccuteProgramVariable().id(2L).sectionId("sectionId2").sectionName("sectionName2").orderIndex(2);
    }

    public static AccuteProgramVariable getAccuteProgramVariableRandomSampleGenerator() {
        return new AccuteProgramVariable()
            .id(longCount.incrementAndGet())
            .sectionId(UUID.randomUUID().toString())
            .sectionName(UUID.randomUUID().toString())
            .orderIndex(intCount.incrementAndGet());
    }
}
