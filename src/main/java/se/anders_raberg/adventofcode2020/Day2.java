package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day2 {
    private static final Logger LOGGER = Logger.getLogger(Day2.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(\\d+)-(\\d+) ([a-z]): ([a-z]+)");

    private Day2() {
    }

    @FunctionalInterface
    private interface ValidFunction {
        boolean valid(String character, String password, int a, int b);
    }

    public static void run() throws IOException {
        List<String> passwords = Files.readAllLines(Paths.get("inputs/input2.txt")).stream() //
                .collect(Collectors.toList());

        LOGGER.info(() -> "Part 1 : " + count(passwords, Day2::validPart1));
        LOGGER.info(() -> "Part 2 : " + count(passwords, Day2::validPart2));
    }

    private static long count(List<String> passwords, ValidFunction function) {
        return passwords.stream().filter(p -> {
            Matcher m = PATTERN.matcher(p);
            m.find();
            return function.valid(m.group(3), m.group(4), Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)));
        }).count();
    }

    private static boolean validPart1(String character, String password, int min, int max) {
        long count = Arrays.stream(password.split("")).filter(c -> c.equals(character)).count();
        return count >= min && count <= max;

    }

    private static boolean validPart2(String character, String password, int pos1, int pos2) {
        boolean first = password.substring(pos1 - 1, pos1).equals(character);
        boolean second = password.substring(pos2 - 1, pos2).equals(character);
        return first ^ second;
    }

}
