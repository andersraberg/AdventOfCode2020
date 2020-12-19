package se.anders_raberg.adventofcode2020;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class Day15 {
    private static final Logger LOGGER = Logger.getLogger(Day15.class.getName());
    private static final Integer NUMBER_GOAL_PART_1 = 2020;
    private static final Integer NUMBER_GOAL_PART_2 = 30_000_000;
    private static final List<Integer> PUZZLE_INPUT = List.of(16, 1, 0, 18, 12, 14, 19);

    private Day15() {
    }

    public static void run() {
        LOGGER.info(() -> "Part 1: " + runGame(NUMBER_GOAL_PART_1));
        LOGGER.info(() -> "Part 2: " + runGame(NUMBER_GOAL_PART_2));
    }

    private static int runGame(Integer goal) {
        Map<Integer, Integer> game = new HashMap<>();
        for (int i = 0; i < PUZZLE_INPUT.size() - 1; i++) {
            game.put(PUZZLE_INPUT.get(i), i);
        }

        Integer last = PUZZLE_INPUT.get(PUZZLE_INPUT.size() - 1);
        for (int i = PUZZLE_INPUT.size() - 1; i < goal - 1; i++) {
            Integer posOfLast = game.get(last);
            game.put(last, i);
            if (posOfLast != null) {
                last = i - posOfLast;
            } else {
                last = 0;
            }
        }
        return last;
    }
}
