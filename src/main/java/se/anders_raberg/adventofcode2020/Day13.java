package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day13 {
    private static final Logger LOGGER = Logger.getLogger(Day13.class.getName());

    private Day13() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input13.txt")).stream() //
                .collect(Collectors.toList());

        int time = Integer.parseInt(lines.get(0));
        List<Integer> buses = Arrays.stream(lines.get(1).split(",")) //
                .filter(b -> b.matches("\\d+")) //
                .map(Integer::parseInt) //
                .collect(Collectors.toList());

        ArrayList<Integer> departureTimes = new ArrayList<>(buses);
        do {
            for (int i = 0; i < departureTimes.size(); i++) {
                departureTimes.set(i, departureTimes.get(i) + buses.get(i));
            }
        } while (departureTimes.stream().noneMatch(b -> b >= time));

        int closestDepartureTime = departureTimes.stream().filter(b -> b >= time).findFirst().orElseThrow();
        int busNumber = buses.get(departureTimes.indexOf(closestDepartureTime));
        LOGGER.info(() -> "Part 1: " + (closestDepartureTime - time) * busNumber);

        List<Integer> busIds = Arrays.stream(lines.get(1).split(",")) //
                .map(z -> "x".equals(z) ? "0" : z) //
                .map(Integer::parseInt) //
                .collect(Collectors.toList());

        long firstTimeMatch = 0;
        long multiplier = 1;
        for (int id : busIds) {
            if (id > 0) {
                while ((busIds.indexOf(id) + firstTimeMatch) % id != 0) {
                    firstTimeMatch = firstTimeMatch + multiplier;
                }
                multiplier = multiplier * id;
            }
        }
        LOGGER.info("Part 2: " + firstTimeMatch);
    }

}
