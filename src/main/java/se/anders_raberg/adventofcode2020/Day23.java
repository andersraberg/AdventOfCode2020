package se.anders_raberg.adventofcode2020;

import java.util.List;
import java.util.logging.Logger;

import se.anders_raberg.adventofcode2020.Day23CircularList.Node;

public class Day23 {
    private static final Logger LOGGER = Logger.getLogger(Day23.class.getName());
    private static final List<Integer> INPUT = List.of(9, 7, 4, 6, 1, 8, 3, 5, 2);
    private static final Day23CircularList CIRCLE_PART_1 = new Day23CircularList(INPUT);
    private static final Day23CircularList CIRCLE_PART_2 = new Day23CircularList(INPUT);
    private static final int NO_OF_CUPS_PART_1 = 9;
    private static final int NO_OF_CUPS_PART_2 = 1_000_000;
    private static final int ROUNDS_PART_1 = 100;
    private static final int ROUNDS_PART_2 = 10_000_000;
    private static final int START_CUP = 9;

    private Day23() {
    }

    public static void run() {
        // Part 1
        //
        runGame(CIRCLE_PART_1, START_CUP, NO_OF_CUPS_PART_1, ROUNDS_PART_1);
        Node node = CIRCLE_PART_1.next(CIRCLE_PART_1.get(1));
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < NO_OF_CUPS_PART_1 - 1; i++) {
            sb.append(node.value());
            node = CIRCLE_PART_1.next(node);

        }
        LOGGER.info(() -> "Part 1: " + sb);

        // Part 2
        //
        for (int i = 10; i <= NO_OF_CUPS_PART_2; i++) {
            CIRCLE_PART_2.add(i);
        }
        runGame(CIRCLE_PART_2, START_CUP, NO_OF_CUPS_PART_2, ROUNDS_PART_2);
        node = CIRCLE_PART_2.get(1);
        long value1 = CIRCLE_PART_2.removeAfter(node);
        long value2 = CIRCLE_PART_2.removeAfter(node);
        LOGGER.info(() -> "Part 2: " + value1 * value2);
    }

    private static void runGame(Day23CircularList input, int startCup, int noOfCups, int rounds) {
        Node currentCupNode = input.get(startCup);
        for (int i = 0; i < rounds; i++) {
            int p1 = input.removeAfter(currentCupNode);
            int p2 = input.removeAfter(currentCupNode);
            int p3 = input.removeAfter(currentCupNode);

            int destinationCup = currentCupNode.value() == 1 ? noOfCups : currentCupNode.value() - 1;
            while (List.of(p1, p2, p3).contains(destinationCup)) {
                destinationCup = destinationCup == 1 ? noOfCups : destinationCup - 1;
            }

            Node destinationCupNode = input.get(destinationCup);
            input.insertAfter(p3, destinationCupNode);
            input.insertAfter(p2, destinationCupNode);
            input.insertAfter(p1, destinationCupNode);
            currentCupNode = input.next(currentCupNode);
        }
    }

}
