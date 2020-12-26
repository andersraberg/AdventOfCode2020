package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.collect.Lists;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day22 {
    private static final Logger LOGGER = Logger.getLogger(Day22.class.getName());

    private Day22() {
    }

    public static void run() throws IOException {
        String[] fileSections = new String(Files.readAllBytes(Paths.get("inputs/input22.txt"))).split("\n\n");

        List<Integer> player1deck = parsePlayerDeck(fileSections[0]);
        List<Integer> player2deck = parsePlayerDeck(fileSections[1]);

        LOGGER.info(() -> "Part 1: " + calculateScore(gamePart1(player1deck, player2deck).second()));
        LOGGER.info(() -> "Part 2: " + calculateScore(gamePart2(player1deck, player2deck).second()));
    }

    private static Pair<Integer, List<Integer>> gamePart1(List<Integer> deck1, List<Integer> deck2) {
        List<Integer> tmpDeck1 = new ArrayList<>(deck1);
        List<Integer> tmpDeck2 = new ArrayList<>(deck2);
        while (!deck1.isEmpty() && !tmpDeck2.isEmpty()) {
            Integer card1 = tmpDeck1.remove(0);
            Integer card2 = tmpDeck2.remove(0);
            if (card2 > card1) {
                tmpDeck2.add(card2);
                tmpDeck2.add(card1);
            } else {
                tmpDeck1.add(card1);
                tmpDeck1.add(card2);
            }
        }
        return tmpDeck1.isEmpty() ? new Pair<>(2, tmpDeck2) : new Pair<>(1, tmpDeck1);

    }

    private static Pair<Integer, List<Integer>> gamePart2(List<Integer> deck1, List<Integer> deck2) {
        List<Integer> tmpDeck1 = new ArrayList<>(deck1);
        List<Integer> tmpDeck2 = new ArrayList<>(deck2);
        Set<List<Integer>> prevSeenDeck1 = new HashSet<>();
        Set<List<Integer>> prevSeenDeck2 = new HashSet<>();
        while (!tmpDeck1.isEmpty() && !tmpDeck2.isEmpty()) {
            if (prevSeenDeck1.contains(tmpDeck1) || prevSeenDeck2.contains(tmpDeck2)) {
                return new Pair<>(1, deck1);
            }
            prevSeenDeck1.add(new ArrayList<>(tmpDeck1));
            prevSeenDeck2.add(new ArrayList<>(tmpDeck2));
            Integer card1 = tmpDeck1.remove(0);
            Integer card2 = tmpDeck2.remove(0);

            if (tmpDeck1.size() >= card1 && tmpDeck2.size() >= card2) {
                Pair<Integer, List<Integer>> winner = gamePart2(tmpDeck1.subList(0, card1),
                        tmpDeck2.subList(0, (int) card2.longValue()));
                if (winner.first() == 1) {
                    tmpDeck1.add(card1);
                    tmpDeck1.add(card2);
                } else {
                    tmpDeck2.add(card2);
                    tmpDeck2.add(card1);
                }
            } else {
                if (card2 > card1) {
                    tmpDeck2.add(card2);
                    tmpDeck2.add(card1);
                } else {
                    tmpDeck1.add(card1);
                    tmpDeck1.add(card2);
                }
            }
        }
        return tmpDeck1.isEmpty() ? new Pair<>(2, tmpDeck2) : new Pair<>(1, tmpDeck1);
    }

    private static List<Integer> parsePlayerDeck(String data) {
        return Arrays.stream(data.split("\n")) //
                .filter(m -> m.matches("\\d+")) //
                .map(Integer::parseInt) //
                .collect(Collectors.toList());
    }

    private static Integer calculateScore(List<Integer> deck) {
        int sum = 0;
        List<Integer> tmpDeck = Lists.reverse(deck);
        for (int i = 0; i < tmpDeck.size(); i++) {
            sum += tmpDeck.get(i) * (i + 1);
        }
        return sum;
    }

}
