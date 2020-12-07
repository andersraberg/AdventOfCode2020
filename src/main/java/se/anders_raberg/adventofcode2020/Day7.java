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

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day7 {
    private static final String SHINY_GOLD = "shiny gold";
    private static final Logger LOGGER = Logger.getLogger(Day7.class.getName());
    private static final Pattern BAG_PATTERN = Pattern.compile("(.+) bags contain (.+)");
    private static final Pattern CONTENT_PATTERN = Pattern.compile("(\\d+) (.+?) bags*");
    private static final Map<String, Set<Pair<Integer, String>>> BAGS = new HashMap<>();

    private Day7() {
    }


    public static void run() throws IOException {
        List<String> rules = Files.readAllLines(Paths.get("inputs/input7.txt")).stream() //
                .collect(Collectors.toList());

        for (String rule : rules) {
            Matcher mbag = BAG_PATTERN.matcher(rule);

            if (mbag.matches()) {
                Set<Pair<Integer, String>> content = new HashSet<>();
                Matcher mContent = CONTENT_PATTERN.matcher(mbag.group(2));
                while (mContent.find()) {
                    content.add(new Pair<>(Integer.parseInt(mContent.group(1)), mContent.group(2)));
                }
                BAGS.put(mbag.group(1), content);
            } else {
                throw new IllegalArgumentException();
            }
        }

        LOGGER.info(() -> "Part 1: " + BAGS.keySet().stream() //
                .filter(b -> !b.equals(SHINY_GOLD)) //
                .filter(Day7::searchBags) //
                .count());

        LOGGER.info(() -> "Part 2 : " + (countBags(SHINY_GOLD) - 1)); // Remove the top bag itself
    }

    private static boolean searchBags(String bag) {
        if (bag.equals(SHINY_GOLD)) {
            return true;
        }

        boolean found = false;
        for (Pair<Integer, String> contents : BAGS.get(bag)) {
            found = found || searchBags(contents.second());
        }
        return found;
    }

    private static int countBags(String bag) {
        int bagCount = 1;
        for (Pair<Integer, String> contents : BAGS.get(bag)) {
            bagCount = bagCount + contents.first() * countBags(contents.second());
        }
        return bagCount;
    }

}
