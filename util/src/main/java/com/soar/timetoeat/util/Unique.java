package com.soar.timetoeat.util;

import java.util.Random;
import java.util.UUID;

public class Unique {

    public static long idValue() {
        return UUID.randomUUID().getMostSignificantBits() & Long.MAX_VALUE;
    }

    public static String stringValue() {
        return UUID.randomUUID().toString();
    }

    public static Double doubleValue() {
        return doubleValue(100);
    }

    public static Double doubleValue(final double max) {
        Random r = new Random();
        return 0 + (max - 0) * r.nextDouble();
    }
}
