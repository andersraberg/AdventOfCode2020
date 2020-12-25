package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.commons.collections4.map.HashedMap;

import com.google.common.collect.Sets;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day21 {
    private static final Logger LOGGER = Logger.getLogger(Day21.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(.*) \\(contains (.*)\\)");

    private Day21() {
    }

    public static void run() throws IOException {
        List<String> foods = Files.readAllLines(Paths.get("inputs/input21.txt")).stream() //
                .collect(Collectors.toList());

        Map<String, Set<String>> possibleSourceOfAllergen = new HashedMap<>();
        List<String> totalIngredients = new ArrayList<>();
        for (String food : foods) {
            Matcher m = PATTERN.matcher(food);
            if (m.matches()) {
                Set<String> ingredients = new HashSet<>(Arrays.asList(m.group(1).split(" ")));
                totalIngredients.addAll(ingredients);
                Arrays.stream(m.group(2).split(", "))
                        .forEach(allergen -> possibleSourceOfAllergen.merge(allergen, ingredients, Sets::intersection));
            } else {
                throw new IllegalArgumentException();
            }
        }
        Set<String> ingredientsWithAllergen = possibleSourceOfAllergen.values().stream().reduce(Collections.emptySet(),
                Sets::union);

        totalIngredients.removeIf(ingredientsWithAllergen::contains);
        LOGGER.info(() -> "Part 1: " + totalIngredients.size());

        List<Pair<String, String>> allergenIngredientsPairs = new ArrayList<>();
        while (!possibleSourceOfAllergen.values().stream().allMatch(Set::isEmpty)) {
            Pair<String, String> singleAllergenPair = possibleSourceOfAllergen.entrySet().stream()
                    .filter(e -> e.getValue().size() == 1)
                    .map(e -> new Pair<>(e.getKey(), e.getValue().iterator().next())) //
                    .findFirst() //
                    .orElseThrow();

            allergenIngredientsPairs.add(singleAllergenPair);
            possibleSourceOfAllergen.replaceAll((k, v) -> Sets.difference(v, Set.of(singleAllergenPair.second())));
        }

        LOGGER.info(() -> "Part 2: " + allergenIngredientsPairs.stream() //
                .sorted((a, b) -> a.first().compareTo(b.first())) //
                .map(Pair<String, String>::second) //
                .reduce("", (a, b) -> a + "," + b));
    }

}
