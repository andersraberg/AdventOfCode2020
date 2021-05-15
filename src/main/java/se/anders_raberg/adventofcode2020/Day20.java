package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;
import com.google.common.math.IntMath;

import se.anders_raberg.adventofcode2020.utilities.Quadruple;

public class Day20 {
    private static final Logger LOGGER = Logger.getLogger(Day20.class.getName());
    private static final Pattern HEADING_PATTERN = Pattern.compile("Tile (\\d++)\\:");
    private static final int SIZE_WITHOUT_BORDER = 8;
    private static final int NUMBER_OF_NEIGHBOURS_FOR_CORNERS = 2;

    private Day20() {
    }

    private record MatrixWrapper(char[][] matrix) {
    }

    private static final List<String> MONSTER = List.of( //
            "                  # ", //
            "#    ##    ##    ###", //
            " #  #  #  #  #  #   ");

    public static void run() throws IOException {
        String[] fileSections = new String(Files.readAllBytes(Paths.get("inputs/input20.txt"))).split("\n\n");

        int outerSize = IntMath.sqrt(fileSections.length, RoundingMode.UNNECESSARY);

        Map<Quadruple<Integer, Integer, Integer, Integer>, Integer> idsForEdgeCombinations = new HashMap<>();
        Map<MatrixWrapper, Integer> idsForTilePermutations = new HashMap<>();

        char[][] monsterMatrix = new char[MONSTER.get(0).length()][MONSTER.size()];
        for (int y = 0; y < MONSTER.size(); y++) {
            for (int x = 0; x < MONSTER.get(0).length(); x++) {
                monsterMatrix[x][y] = MONSTER.get(y).charAt(x);
            }
        }

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

            for (char[][] permutation : flipsAndRotations(tileMatrix)) {
                idsForEdgeCombinations.put(edgeValues(permutation), tileId);
                idsForTilePermutations.put(new MatrixWrapper(permutation), tileId);
            }
        }

        Map<Integer, Set<Integer>> neighbours = new HashMap<>();
        for (Entry<Quadruple<Integer, Integer, Integer, Integer>, Integer> entry : idsForEdgeCombinations.entrySet()) {
            Set<Integer> set = idsForEdgeCombinations.entrySet().stream()
                    .filter(e -> hasCommonEdge(e.getKey(), entry.getKey()))
                    .filter(e -> !e.getValue().equals(entry.getValue())) //
                    .map(Entry::getValue) //
                    .collect(Collectors.toSet());

            neighbours.merge(entry.getValue(), set, Sets::union);
        }

        List<Integer> cornerTileIds = neighbours.entrySet() //
                .stream() //
                .filter(e -> e.getValue().size() == NUMBER_OF_NEIGHBOURS_FOR_CORNERS) //
                .map(Entry::getKey) //
                .collect(Collectors.toList());

        LOGGER.info(() -> "Part 1 : "
                + cornerTileIds.stream().mapToLong(Integer::longValue).reduce(1, Math::multiplyExact));

        MatrixWrapper[][] outerMatrix = null;

        for (int i = 0; i < 8; i++) {
            try {
                Set<Integer> unusedTiles = new HashSet<>(neighbours.keySet());
                outerMatrix = new MatrixWrapper[outerSize][outerSize];
                outerMatrix[0][0] = idsForTilePermutations.entrySet().stream()
                        .filter(e -> e.getValue().equals(cornerTileIds.get(3))).map(Entry::getKey)
                        .collect(Collectors.toList()).get(i);
                unusedTiles.remove(cornerTileIds.get(3));
                int x = 1;
                int y = 0;
                while (y < outerSize) {
                    while (x < outerSize) {
                        MatrixWrapper wrapper;
                        if (x == 0) {
                            int bottomEdgeVal = edgeValues(outerMatrix[x][y - 1].matrix()).second();
                            wrapper = idsForTilePermutations.entrySet().stream() //
                                    .filter(e -> unusedTiles.contains(e.getValue())) //
                                    .filter(e -> edgeValues(e.getKey().matrix()).first() == bottomEdgeVal) //
                                    .map(Entry::getKey) //
                                    .findFirst() //
                                    .orElseThrow();
                        } else {
                            int rightEdgeVal = edgeValues(outerMatrix[x - 1][y].matrix()).fourth();
                            wrapper = idsForTilePermutations.entrySet().stream() //
                                    .filter(e -> unusedTiles.contains(e.getValue())) //
                                    .filter(e -> edgeValues(e.getKey().matrix()).third() == rightEdgeVal) //
                                    .map(Entry::getKey) //
                                    .findFirst() //
                                    .orElseThrow();
                        }
                        outerMatrix[x][y] = wrapper;
                        unusedTiles.remove(idsForTilePermutations.get(wrapper));
                        x++;
                    }
                    x = 0;
                    y++;
                }
                break;
            } catch (NoSuchElementException e) {
                // just continue the loop.
            }
        }

        for (char[][] permutation : flipsAndRotations(image(removeAllBorder(outerMatrix)))) {
            int occurences = countOccurences(monsterMatrix, permutation);
            if (occurences > 0) {
                LOGGER.info(() -> "Part 2 : Roughness :"
                        + (countHashSigns(permutation) - occurences * countHashSigns(monsterMatrix)));
                break;
            }
        }
    }

    private static boolean hasCommonEdge(Quadruple<Integer, Integer, Integer, Integer> edges1,
            Quadruple<Integer, Integer, Integer, Integer> edges2) {
        return !Sets.intersection(Set.of(edges1.first(), edges1.second(), edges1.third(), edges1.fourth()),
                Set.of(edges2.first(), edges2.second(), edges2.third(), edges2.fourth())).isEmpty();
    }

    private static MatrixWrapper[][] removeAllBorder(MatrixWrapper[][] matrix) {
        MatrixWrapper[][] result = new MatrixWrapper[matrix.length][matrix.length];
        for (int y = 0; y < matrix.length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                result[x][y] = removeBorder(matrix[x][y]);
            }
        }
        return result;
    }

    private static MatrixWrapper removeBorder(MatrixWrapper matrixWrapper) {
        char[][] total = new char[SIZE_WITHOUT_BORDER][SIZE_WITHOUT_BORDER];
        int x = 1;
        int y = 1;
        for (int y2 = 0; y2 < SIZE_WITHOUT_BORDER; y2++) {
            for (int x2 = 0; x2 < SIZE_WITHOUT_BORDER; x2++) {
                total[x2][y2] = matrixWrapper.matrix()[x][y];
                x++;
            }
            x = 1;
            y++;
        }
        return new MatrixWrapper(total);

    }

    private static int countOccurences(char[][] monster, char[][] image) {
        int occurances = 0;
        for (int y = 0; y < image[0].length - monster[0].length; y++) {
            for (int x = 0; x < image.length - monster.length; x++) {
                boolean match = true;
                for (int y2 = 0; y2 < monster[0].length; y2++) {
                    for (int x2 = 0; x2 < monster.length; x2++) {
                        match = match
                                && (monster[x2][y2] == ' ' || monster[x2][y2] == '#' && image[x + x2][y + y2] == '#');
                    }
                }
                if (match) {
                    occurances++;
                }
            }
        }
        return occurances;
    }

    private static char[][] image(MatrixWrapper[][] matrix) {
        int sizeAll = matrix.length;
        int sizeSingle = matrix[0][0].matrix().length;
        char[][] total = new char[sizeAll * sizeSingle][sizeAll * sizeSingle];
        for (int y = 0; y < sizeAll * sizeSingle; y++) {
            for (int x = 0; x < sizeAll * sizeSingle; x++) {
                int tileX = x / sizeSingle;
                int tileY = y / sizeSingle;
                total[x][y] = matrix[tileX][tileY].matrix()[x % sizeSingle][y % sizeSingle];
            }
        }
        return total;
    }

    private static Quadruple<Integer, Integer, Integer, Integer> edgeValues(char[][] matrix) {
        StringBuilder sbTop = new StringBuilder();
        StringBuilder sbBottom = new StringBuilder();
        for (int x = 0; x < matrix[0].length; x++) {
            sbTop.append(matrix[x][0]);
            sbBottom.append(matrix[x][matrix.length - 1]);
        }
        return new Quadruple<>(parseBinaryInt(sbTop.toString()), //
                parseBinaryInt(sbBottom.toString()), //
                parseBinaryInt(new String(matrix[0])), //
                parseBinaryInt(new String(matrix[matrix.length - 1])));
    }

    private static Integer parseBinaryInt(String str) {
        return Integer.parseInt(str.replace("#", "1").replace(".", "0"), 2);
    }

    private static int countHashSigns(char[][] matrix) {
        int count = 0;
        for (int y = 0; y < matrix[0].length; y++) {
            for (int x = 0; x < matrix.length; x++) {
                count += matrix[x][y] == '#' ? 1 : 0;
            }
        }
        return count;
    }

    private static List<char[][]> flipsAndRotations(char[][] matrix) {
        List<char[][]> result = new ArrayList<>();
        char[][] tmp = matrix;
        for (int i = 0; i < 4; i++) {
            result.addAll(Set.of(tmp, flipHoriz(tmp), flipVert(tmp)));
            tmp = rotate(tmp);
        }
        return result;
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
