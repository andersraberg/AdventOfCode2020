package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.apache.commons.lang3.StringUtils;

public class Day5 {
    private static final Logger LOGGER = Logger.getLogger(Day5.class.getName());
    private static final int NO_OF_SEATS = 1024;

    private Day5() {
    }

    public static void run() throws IOException {
        List<Integer> seatIds = Files.readAllLines(Paths.get("inputs/input5.txt")).stream() //
                .map(Day5::parseSeatId).collect(Collectors.toList());

        // Part 1
        LOGGER.info(() -> "Part 1: " + seatIds.stream().max(Integer::compare).orElseThrow());

        // Part 2
        LOGGER.info(() -> "Part 2: " + IntStream.range(0, NO_OF_SEATS) //
                .filter(s -> !seatIds.contains(s)) //
                .filter(s -> seatIds.contains(s - 1)) //
                .filter(s -> seatIds.contains(s + 1)) //
                .findFirst().orElseThrow());
    }

    private static int parseSeatId(String seatId) {
        return Integer.parseInt(StringUtils.replaceChars(seatId, "FBLR", "0101"), 2);
    }

}
