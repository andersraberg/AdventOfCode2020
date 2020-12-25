package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.common.base.Strings;

public class Day19 {
    private static final String TARGET_RULE = "0";
    private static final int PART_2_RULE_EXPANSION_ITERATIONS = 5;
    private static final Logger LOGGER = Logger.getLogger(Day19.class.getName());
    private static final Pattern PATTERN_RULE = Pattern.compile("(\\d+):(.+)");

    private static final Map<String, String> RULES = new HashMap<>();

    private Day19() {
    }

    public static void run() throws IOException {
        String[] fileSections = new String(Files.readAllBytes(Paths.get("inputs/input19.txt"))).split("\n\n");

        for (String string : fileSections[0].split("\n")) {
            Matcher m = PATTERN_RULE.matcher(string);
            if (m.matches()) {
                RULES.put(m.group(1), m.group(2));
            } else {
                throw new IllegalArgumentException();
            }
        }

        LOGGER.info(() -> "Part 1: "
                + Arrays.stream(fileSections[1].split("\n")).filter(s -> s.matches(expandRule(TARGET_RULE))).count());

        RULES.put("8", expandRule8(PART_2_RULE_EXPANSION_ITERATIONS));
        RULES.put("11", expandRule11(PART_2_RULE_EXPANSION_ITERATIONS));
        LOGGER.info(() -> "Part 2: "
                + Arrays.stream(fileSections[1].split("\n")).filter(s -> s.matches(expandRule(TARGET_RULE))).count());
    }

    private static String expandRule8(int iterations) {
        StringBuilder sb = new StringBuilder(" 42 ");
        for (int i = 2; i <= iterations; i++) {
            sb.append(" | ").append(Strings.repeat("42 ", i));
        }
        return sb.toString();
    }

    private static String expandRule11(int iterations) {
        StringBuilder sb = new StringBuilder(" 42 31 ");
        for (int i = 2; i <= iterations; i++) {
            sb.append(" | ").append(Strings.repeat("42 ", i)).append(Strings.repeat("31 ", i));
        }
        return sb.toString();

    }

    private static String expandRule(String rule) {
        String string = RULES.get(rule);
        if (string.contains("\"")) {
            return string.replace("\"", "").trim();
        }

        String[] split = string.split("\\|");
        StringBuilder sb = new StringBuilder("(" + expandRuleList(split[0]));
        for (int i = 1; i < split.length; i++) {
            sb.append('|').append(expandRuleList(split[i]));
        }
        sb.append(')');
        return sb.toString();
        // }
    }

    private static String expandRuleList(String string) {
        return Arrays.stream(string.trim().split("\\W+")).map(Day19::expandRule).reduce("", String::concat);
    }

}
