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
        return max * r.nextDouble();
    }

    public static int intValue() {
        return intValue(100);
    }

    public static int intValue(final int max) {
        Random r = new Random();
        return max * r.nextInt();
    }
}
