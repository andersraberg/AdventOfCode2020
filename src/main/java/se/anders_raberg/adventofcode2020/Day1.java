package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day1 {
    private static final Logger LOGGER = Logger.getLogger(Day1.class.getName());
    private static final Integer SUM = 2020;

    private Day1() {
    }

    public static void run() throws IOException {
        List<Integer> data = Files.readAllLines(Paths.get("inputs/input1.txt")).stream() //
                .map(Integer::valueOf) //
                .collect(Collectors.toList());

        for (int i = 0; i < data.size(); i++) {
            for (int j = i; j < data.size(); j++) {
                if (SUM.equals(data.get(i) + data.get(j))) {
                    LOGGER.info("Part 1: " + data.get(i) * data.get(j));
                }

                for (int k = j; k < data.size(); k++) {
                    if (SUM.equals(data.get(i) + data.get(j) + data.get(k))) {
                        LOGGER.info("Part 2: " + data.get(i) * data.get(j) * data.get(k));
                    }

                }
            }
        }
    }
}
