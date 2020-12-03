package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day3 {
    private static final Logger LOGGER = Logger.getLogger(Day3.class.getName());

    private Day3() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input3.txt")).stream() //
                .collect(Collectors.toList());

        LOGGER.info(() -> "Part 1: " + countTrees(lines, 3, 1));

        LOGGER.info(() -> "Part 2: " + countTrees(lines, 1, 1) * //
                countTrees(lines, 3, 1) * //
                countTrees(lines, 5, 1) * //
                countTrees(lines, 7, 1) * //
                countTrees(lines, 1, 2)); //

    }

    private static int countTrees(List<String> lines, int dx, int dy) {
        int y = 0;
        int x = 0;
        int count = 0;
        int lineLength = lines.get(0).length();

        while (y < lines.size()) {
            count = count + (lines.get(y).charAt(x) == '#' ? 1 : 0);
            y = y + dy;
            x = (x + dx) % lineLength;
        }

        return count;
    }

}
