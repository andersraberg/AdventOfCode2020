package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day14Part2 {
    private static final Logger LOGGER = Logger.getLogger(Day14Part2.class.getName());
    private static final Pattern MASK_PATTERN = Pattern.compile("mask = ([01X]+)");
    private static final Pattern MEM_PATTERN = Pattern.compile("mem\\[(\\d+)\\] = (\\d+)");
    private static final Map<Long, Long> MEMORY = new HashMap<>();

    private Day14Part2() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input14.txt")).stream() //
                .collect(Collectors.toList());

        String mask = "";
        for (String line : lines) {
            Matcher mMask = MASK_PATTERN.matcher(line);
            if (mMask.matches()) {
                mask = mMask.group(1);
            }
            Matcher mMem = MEM_PATTERN.matcher(line);
            if (mMem.matches()) {
                String afterMaskApplication = applyMask(
                        String.format("%36s", Long.toBinaryString(Long.parseLong(mMem.group(1)))).replace(' ', '0'),
                        mask);

                generatePermutations(afterMaskApplication)
                        .forEach(p -> MEMORY.put(Long.parseLong(p, 2), Long.parseLong(mMem.group(2))));
            }
        }
        LOGGER.info(() -> "Part 2: " + MEMORY.values().stream().reduce(Long::sum).orElseThrow());
    }

    private static String applyMask(String address, String mask) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < mask.length(); i++) {
            switch (mask.charAt(i)) {
            case '0':
                sb.append(address.charAt(i));
                break;
            case '1':
                sb.append('1');
                break;
            case 'X':
                sb.append('X');
                break;
            default:
                throw new IllegalArgumentException();
            }
        }
        return sb.toString();
    }

    private static Set<String> generatePermutations(String str) {
        Set<String> result = new HashSet<>();
        if (str.contains("X")) {
            result.addAll(generatePermutations(str.replaceFirst("X", "0")));
            result.addAll(generatePermutations(str.replaceFirst("X", "1")));
        } else {
            result.add(str);
        }
        return result;
    }

}
