package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;

import com.google.common.collect.Sets;

public class Day6 {
    private static final Logger LOGGER = Logger.getLogger(Day6.class.getName());
    private static final Set<String> QUESTIONS = Set.of("abcdefghijklmnopqrstuvwxyz".split(""));

    private Day6() {
    }

    public static void run() throws IOException {
        String[] groups = new String(Files.readAllBytes(Paths.get("inputs/input6.txt"))).split("\n\n");

        long sumPart1 = Arrays.stream(groups) //
                .map(g -> Arrays.stream(g.replaceAll("\\s+", "").split("")) //
                        .distinct() //
                        .count()) //
                .reduce(Long::sum) //
                .orElseThrow();

        LOGGER.info(() -> "Part 1: " + sumPart1);

        long sumPart2 = Arrays.stream(groups) //
                .map(q -> commonYesAnswers(q.split("\n"))) //
                .reduce(Long::sum) //
                .orElseThrow();

        LOGGER.info(() -> "Part 2: " + sumPart2);

    }

    private static long commonYesAnswers(String[] group) {
        return Arrays.stream(group) //
                .map(g -> Set.of(g.split(""))) //
                .reduce(QUESTIONS, Sets::intersection).size();
    }

}
