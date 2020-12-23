package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Triple;

public class Day17Part1 {
    private static final int CYCLES = 6;
    private static final Logger LOGGER = Logger.getLogger(Day17Part1.class.getName());
    private static Map<Triple<Integer, Integer, Integer>, Boolean> cubes = new HashMap<>();
    private static Map<Triple<Integer, Integer, Integer>, Boolean> nexCubes = new HashMap<>();

    private Day17Part1() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input17.txt")).stream() //
                .collect(Collectors.toList());

        int sizeX = lines.get(0).length();
        int sizeY = lines.size();
        int sizeZ = 1;

        for (int y = 0; y < lines.size(); y++) {
            for (int x = 0; x < lines.get(y).length(); x++) {
                if (lines.get(y).charAt(x) == '#') {
                    cubes.put(new Triple<>(x, y, 0), true);
                }
            }
        }

        for (int i = 0; i < CYCLES; i++) {
            for (int x = -CYCLES; x < sizeX + CYCLES; x++) {
                for (int y = -CYCLES; y < sizeY + CYCLES; y++) {
                    for (int z = -CYCLES; z < sizeZ + CYCLES; z++) {
                        Triple<Integer, Integer, Integer> triple = new Triple<>(x, y, z);
                        int countActiveNeighbors = countActiveNeighbors(triple);
                        if (Boolean.TRUE.equals(cubes.get(triple))) {
                            nexCubes.put(triple, countActiveNeighbors == 2 || countActiveNeighbors == 3);
                        } else {
                            nexCubes.put(triple, countActiveNeighbors == 3);
                        }
                    }
                }
            }
            cubes = nexCubes;
            nexCubes = new HashMap<>();
        }
        LOGGER.info(() -> "Part 1: " + cubes.values().stream().filter(x -> x).count());
    }

    private static int countActiveNeighbors(Triple<Integer, Integer, Integer> cube) {
        int count = 0;
        for (int x = cube.first() - 1; x <= cube.first() + 1; x++) {
            for (int y = cube.second() - 1; y <= cube.second() + 1; y++) {
                for (int z = cube.third() - 1; z <= cube.third() + 1; z++) {
                    Triple<Integer, Integer, Integer> triple = new Triple<>(x, y, z);
                    if (!triple.equals(cube)) {
                        count = count + (Boolean.TRUE.equals(cubes.get(triple)) ? 1 : 0);
                    }
                }
            }
        }
        return count;
    }

}
