package se.anders_raberg.adventofcode2020;

import java.util.logging.Logger;

public class Day25 {
    private static final int DIVISOR = 20201227;
    private static final long SUBJECT_NUMBER = 7;
    private static final Logger LOGGER = Logger.getLogger(Day25.class.getName());
    private static final long CARD_KEY = 2069194;
    private static final long DOOR_KEY = 16426071;

    private Day25() {
    }

    public static void run() {
        LOGGER.info(() -> "Part 1: " + calcKey(findLoopSize(CARD_KEY), DOOR_KEY));
    }

    private static long findLoopSize(long publicKey) {
        int loopSize = 0;
        long value = 1;
        while (value != publicKey) {
            value = value * SUBJECT_NUMBER % DIVISOR;
            loopSize++;
        }
        return loopSize;
    }

    private static long calcKey(long loopSize, long publicKey) {
        long value = 1;
        for (int i = 0; i < loopSize; i++) {
            value = value * publicKey % DIVISOR;
        }
        return value;
    }

}
