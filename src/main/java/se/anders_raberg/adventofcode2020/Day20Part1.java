package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

import se.anders_raberg.adventofcode2020.utilities.Quadruple;

public class Day20Part1 {
    private static final Logger LOGGER = Logger.getLogger(Day20Part1.class.getName());
    private static final Pattern HEADING_PATTERN = Pattern.compile("Tile (\\d++)\\:");

    private Day20Part1() {
    }

    private static class Edges extends Quadruple<Integer, Integer, Integer, Integer> {
        public Edges(Integer first, Integer second, Integer third, Integer forth) {
            // first => Top, second => Bottom, third => Left, fourth => Right
            super(first, second, third, forth);

        }

        public boolean hasCommonEdge(Edges other) {
            return !Sets.intersection(Set.of(first(), second(), third(), fourth()),
                    Set.of(other.first(), other.second(), other.third(), other.fourth())).isEmpty();
        }
    }

    public static void run() throws IOException {
        String[] fileSections = new String(Files.readAllBytes(Paths.get("inputs/input20.txt"))).split("\n\n");

        Map<Edges, Integer> idsForEdgeCombinations = new HashMap<>();

        for (String tile : fileSections) {
            List<String> asList = Arrays.asList(tile.split("\n"));
            Matcher m = HEADING_PATTERN.matcher(asList.get(0));
            int tileId;
            if (m.matches()) {
                tileId = Integer.parseInt(m.group(1));
            } else {
                throw new IllegalArgumentException();
            }
            List<String> subList = asList.subList(1, asList.size());
            char[][] tileMatrix = new char[subList.size()][subList.size()];
            for (int y = 0; y < subList.size(); y++) {
                for (int x = 0; x < subList.get(0).length(); x++) {
                    tileMatrix[x][y] = subList.get(y).charAt(x);
                }

            }

            for (int i = 0; i < 4; i++) {
                tileMatrix = rotate(tileMatrix);
                char[][] flipHoriz = flipHoriz(tileMatrix);
                char[][] flipVert = flipVert(tileMatrix);
                idsForEdgeCombinations.put(edgeValues(tileMatrix), tileId);
                idsForEdgeCombinations.put(edgeValues(flipHoriz), tileId);
                idsForEdgeCombinations.put(edgeValues(flipVert), tileId);
            }
        }

        Map<Integer, Set<Integer>> neighbours = new HashMap<>();
        for (Entry<Edges, Integer> string : idsForEdgeCombinations.entrySet()) {
            Set<Integer> set = idsForEdgeCombinations.entrySet().stream()
                    .filter(e -> e.getKey().hasCommonEdge(string.getKey()))
                    .filter(e -> !e.getValue().equals(string.getValue())) //
                    .map(Entry::getValue) //
                    .collect(Collectors.toSet());

            neighbours.merge(string.getValue(), set, Sets::union);
        }

        LOGGER.info(() -> "Part 1 : " + neighbours.entrySet().stream() //
                .filter(e -> e.getValue().size() == 2) //
                .map(e -> e.getKey().longValue()) //
                .reduce(1L, Math::multiplyExact));
    }

    private static Edges edgeValues(char[][] matrix) {
        StringBuilder sbTop = new StringBuilder();
        StringBuilder sbBottom = new StringBuilder();
        for (int x = 0; x < matrix[0].length; x++) {
            sbTop.append(matrix[x][0]);
            sbBottom.append(matrix[x][matrix.length - 1]);
        }
        return new Edges(parseBinaryInt(sbTop.toString()), //
                parseBinaryInt(sbBottom.toString()), //
                parseBinaryInt(new String(matrix[0])), //
                parseBinaryInt(new String(matrix[matrix.length - 1])));
    }

    private static Integer parseBinaryInt(String str) {
        return Integer.parseInt(str.replace("#", "1").replace(".", "0"), 2);
    }

    private static String toString(char[][] matrix) {
        StringBuilder sb = new StringBuilder("\n");
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix[0].length; x++) {
                sb.append(matrix[x][y]);
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    private static char[][] flipHoriz(char[][] matrix) {
        char[][] result = new char[matrix.length][matrix.length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                result[x][y] = matrix[matrix.length - 1 - x][y];
            }
        }
        return result;
    }

    private static char[][] flipVert(char[][] matrix) {
        char[][] result = new char[matrix.length][matrix.length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                result[x][y] = matrix[x][matrix.length - 1 - y];
            }
        }
        return result;
    }

    private static char[][] rotate(char[][] matrix) {
        char[][] result = new char[matrix.length][matrix.length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                result[x][y] = matrix[matrix.length - 1 - y][x];
            }
        }
        return result;
    }

}
