package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class SiteOrganisationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static SiteOrganisation getSiteOrganisationSample1() {
        return new SiteOrganisation().id(1L);
    }

    public static SiteOrganisation getSiteOrganisationSample2() {
        return new SiteOrganisation().id(2L);
    }

    public static SiteOrganisation getSiteOrganisationRandomSampleGenerator() {
        return new SiteOrganisation().id(longCount.incrementAndGet());
    }
}
