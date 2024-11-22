package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SiteOrgTempTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SiteOrgTemp getSiteOrgTempSample1() {
        return new SiteOrgTemp().id(1L);
    }

    public static SiteOrgTemp getSiteOrgTempSample2() {
        return new SiteOrgTemp().id(2L);
    }

    public static SiteOrgTemp getSiteOrgTempRandomSampleGenerator() {
        return new SiteOrgTemp().id(longCount.incrementAndGet());
    }
}
