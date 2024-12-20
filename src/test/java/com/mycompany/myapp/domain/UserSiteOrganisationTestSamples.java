package com.mycompany.myapp.domain;

import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class UserSiteOrganisationTestSamples {

    private static final Random random = new Random();
    private static final AtomicLong longCount = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    public static UserSiteOrganisation getUserSiteOrganisationSample1() {
        return new UserSiteOrganisation().id(1L);
    }

    public static UserSiteOrganisation getUserSiteOrganisationSample2() {
        return new UserSiteOrganisation().id(2L);
    }

    public static UserSiteOrganisation getUserSiteOrganisationRandomSampleGenerator() {
        return new UserSiteOrganisation().id(longCount.incrementAndGet());
    }
}
