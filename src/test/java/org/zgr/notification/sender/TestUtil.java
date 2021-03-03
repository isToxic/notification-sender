package org.zgr.notification.sender;

import org.apache.commons.lang3.RandomStringUtils;

public class TestUtil {

    public static String getRandomString() {
        return RandomStringUtils.random(6);
    }

    public static Long getLongIn(long start, long end) {
        return start + (long) (Math.random() * (end - start));
    }
}
