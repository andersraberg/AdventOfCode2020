package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14Part1 {
    private static final Logger LOGGER = Logger.getLogger(Day14Part1.class.getName());
    private static final Pattern MASK_PATTERN = Pattern.compile("mask = ([01X]+)");
    private static final Pattern MEM_PATTERN = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
    private static final Map<Long, Long> MEMORY = new HashMap<>();

    private Day14Part1() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input14.txt")).stream() //
                .collect(Collectors.toList());

        long clearMask = 0;
        long setMask = 0;
        for (String line : lines) {
            Matcher mMask = MASK_PATTERN.matcher(line);
            if (mMask.matches()) {
                clearMask = Long.parseLong(mMask.group(1).replace('X', '1'), 2);
                setMask = Long.parseLong(mMask.group(1).replace('X', '0'), 2);
            }
            Matcher mMem = MEM_PATTERN.matcher(line);
            if (mMem.matches()) {
                MEMORY.put(Long.parseLong(mMem.group(1)), Long.parseLong(mMem.group(2)) & clearMask | setMask);
            }
        }
        LOGGER.info(() -> "Part 1: " + MEMORY.values().stream().reduce(Long::sum).orElseThrow());
    }

}
