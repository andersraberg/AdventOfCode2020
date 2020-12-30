package se.anders_raberg.adventofcode2020;

import java.util.ArrayDeque;
import java.util.List;
import java.util.logging.Logger;

public class Day23Part1 {
    private static final int MOVES = 100;
    private static final Logger LOGGER = Logger.getLogger(Day23Part1.class.getName());
    private static final ArrayDeque<Integer> CIRCLE = new ArrayDeque<>(List.of(9, 7, 4, 6, 1, 8, 3, 5, 2));
    private static final int NO_OF_CUPS = CIRCLE.size();

    private Day23Part1() {
    }

    public static void run() {

        for (int i = 0; i < MOVES; i++) {
            int currentCup = CIRCLE.removeFirst();
            List<Integer> removedList = List.of(CIRCLE.removeFirst(), CIRCLE.removeFirst(), CIRCLE.removeFirst());
            CIRCLE.addFirst(currentCup);
            int destinationCup = currentCup == 1 ? NO_OF_CUPS : currentCup - 1;
            while (removedList.contains(destinationCup)) {
                destinationCup = destinationCup == 1 ? NO_OF_CUPS : destinationCup - 1;
            }

            // Put destination cup last
            while (CIRCLE.peekLast() != destinationCup) {
                CIRCLE.addLast(CIRCLE.removeFirst());
            }
            CIRCLE.addAll(removedList);

            // Put current cup last
            while (CIRCLE.peekLast() != currentCup) {
                CIRCLE.addLast(CIRCLE.removeFirst());
            }
        }
        while (CIRCLE.peekFirst() != 1) {
            CIRCLE.addLast(CIRCLE.removeFirst());
        }
        CIRCLE.removeFirst();
        LOGGER.info(() -> "Part 1: " + CIRCLE.stream().map(c -> c.toString()).reduce("", String::concat));
    }

}
