package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Quadruple;

public class Day17Part2 {
    private static final int CYCLES = 6;
    private static final Logger LOGGER = Logger.getLogger(Day17Part2.class.getName());
    private static Map<Quadruple<Integer, Integer, Integer, Integer>, Boolean> cubes = new HashMap<>();
    private static Map<Quadruple<Integer, Integer, Integer, Integer>, Boolean> nextCubes = new HashMap<>();

    private Day17Part2() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input17.txt")).stream() //
                .collect(Collectors.toList());

        int sizeX = lines.get(0).length();
        int sizeY = lines.size();
        int sizeZ = 1;
        int sizeW = 1;

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) == '#') {
                    cubes.put(new Quadruple<>(x, y, 0, 0), true);
                }
            }
        }

        for (int i = 0; i < CYCLES; i++) {
            for (int x = -CYCLES; x < sizeX + CYCLES; x++) {
                for (int y = -CYCLES; y < sizeY + CYCLES; y++) {
                    for (int z = -CYCLES; z < sizeZ + CYCLES; z++) {
                        for (int w = -CYCLES; w < sizeW + CYCLES; w++) {
                            Quadruple<Integer, Integer, Integer, Integer> quadruple = new Quadruple<>(x, y, z, w);
                            int countActiveNeighbors = countActiveNeighbors(quadruple);
                            if (Boolean.TRUE.equals(cubes.get(quadruple))) {
                                nextCubes.put(quadruple, countActiveNeighbors == 2 || countActiveNeighbors == 3);
                            } else {
                                nextCubes.put(quadruple, countActiveNeighbors == 3);
                            }
                        }
                    }
                }
            }
            cubes = nextCubes;
            nextCubes = new HashMap<>();
        }
        LOGGER.info(() -> "Part 1: " + cubes.values().stream().filter(x -> x).count());
    }

    private static int countActiveNeighbors(Quadruple<Integer, Integer, Integer, Integer> cube) {
        int count = 0;
        for (int x = cube.first() - 1; x <= cube.first() + 1; x++) {
            for (int y = cube.second() - 1; y <= cube.second() + 1; y++) {
                for (int z = cube.third() - 1; z <= cube.third() + 1; z++) {
                    for (int w = cube.fourth() - 1; w <= cube.fourth() + 1; w++) {
                        Quadruple<Integer, Integer, Integer, Integer> quadruple = new Quadruple<>(x, y, z, w);
                        if (!quadruple.equals(cube)) {
                            count = count + (Boolean.TRUE.equals(cubes.get(quadruple)) ? 1 : 0);
                        }
                    }
                }
            }
        }
        return count;
    }

}
