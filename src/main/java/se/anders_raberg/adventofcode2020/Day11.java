package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.BiFunction;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day11 {
    private static final Logger LOGGER = Logger.getLogger(Day11.class.getName());
    private static final int MAX_STEPS_IN_EACH_DIRECION = 50;

    private Day11() {
    }

    private static final Map<Pair<Integer, Integer>, Character> SEATS = new HashMap<>();

    public static void run() throws IOException {
        List<String> data = Files.readAllLines(Paths.get("inputs/input11.txt")).stream() //
                .collect(Collectors.toList());

        for (int i = 0; i < data.size(); i++) {
            for (int j = 0; j < data.get(i).length(); j++) {
                SEATS.put(new Pair<>(i, j), data.get(i).charAt(j));
            }
        }

        LOGGER.info(() -> "Part 1: " + iterate(Day11::findAdjacentSeats, SEATS, 4));
        LOGGER.info(() -> "Part 2: " + iterate(Day11::findObservedSeats, SEATS, 5));
    }

    private interface SeatFinder
            extends BiFunction<Pair<Integer, Integer>, Map<Pair<Integer, Integer>, Character>, List<Character>> {
    };

    private static long iterate(SeatFinder seatFinder, Map<Pair<Integer, Integer>, Character> start, int limit) {
        Map<Pair<Integer, Integer>, Character> before = start;
        while (true) {
            HashMap<Pair<Integer, Integer>, Character> hashMap = new HashMap<>(before);
            Map<Pair<Integer, Integer>, Character> after = before.entrySet().stream().collect(Collectors
                    .toMap(Entry::getKey, e -> newState(e.getValue(), seatFinder.apply(e.getKey(), hashMap), limit)));

            if (after.equals(before)) {
                return after.entrySet().stream().filter(e -> e.getValue() == '#').count();
            }
            before = after;
        }

    }

    private static Character newState(Character oldState, List<Character> adjacentSeats, int limit) {
        switch (oldState) {
        case 'L':
            return adjacentSeats.contains('#') ? 'L' : '#';

        case '#':
            return adjacentSeats.stream().filter(s -> s == '#').count() >= limit ? 'L' : '#';

        default:
            return oldState;
        }
    }

    private static List<Character> findAdjacentSeats(Pair<Integer, Integer> seat,
            Map<Pair<Integer, Integer>, Character> seats) {
        List<Character> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                Pair<Integer, Integer> pair = new Pair<>(seat.first() + i, seat.second() + j);
                Character character = seats.get(pair);
                if (character != null && !pair.equals(seat)) {
                    result.add(character);
                }
            }
        }
        return result;
    }

    private static List<Character> findObservedSeats(Pair<Integer, Integer> seat,
            Map<Pair<Integer, Integer>, Character> seats) {
        List<Character> result = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                for (int m = 1; m < MAX_STEPS_IN_EACH_DIRECION; m++) {
                    Pair<Integer, Integer> pair = new Pair<>(seat.first() + i * m, seat.second() + j * m);
                    Character character = seats.get(pair);
                    if (character != null && !pair.equals(seat)) {
                        result.add(character);
                        if (character.equals('#') || character.equals('L')) {
                            break;
                        }
                    }

                }
            }
        }
        return result;
    }

}
