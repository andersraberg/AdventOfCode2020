package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day24 {
    private static final int DAYS = 100;
    private static final int MAX_GROWTH = 140;
    private static final Logger LOGGER = Logger.getLogger(Day24.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(e|se|sw|w|nw|ne)");

    private Day24() {
    }

    public static void run() throws IOException {
        List<String> directions = Files.readAllLines(Paths.get("inputs/input24.txt")).stream() //
                .collect(Collectors.toList());

        // Tile color: true => black, false => white
        Map<Pair<Integer, Integer>, Boolean> tiles = new HashMap<>();
        for (String dir : directions) {
            Matcher m = PATTERN.matcher(dir);
            int x = 0;
            int y = 0;
            while (m.find()) {
                switch (m.group(1)) {
                case "e":
                    x += 2;
                    break;
                case "se":
                    x += 1;
                    y -= 2;
                    break;
                case "sw":
                    x -= 1;
                    y -= 2;
                    break;
                case "w":
                    x -= 2;
                    break;
                case "nw":
                    x -= 1;
                    y += 2;
                    break;
                case "ne":
                    x += 1;
                    y += 2;
                    break;
                default:
                    throw new IllegalArgumentException(m.group(1));
                }
            }
            tiles.merge(new Pair<>(x, y), Boolean.TRUE, Boolean::logicalXor);
        }
        LOGGER.info("Part 1: " + tiles.values().stream().filter(t -> t).count());

        // Fill floor with white(false) tiles
        for (int x = -MAX_GROWTH; x <= MAX_GROWTH; x++) {
            for (int y = -MAX_GROWTH; y <= MAX_GROWTH; y++) {
                tiles.putIfAbsent(new Pair<>(x, y), Boolean.FALSE);
            }
        }

        Map<Pair<Integer, Integer>, Boolean> nextDaytiles = null;
        for (int i = 0; i < DAYS; i++) {
            nextDaytiles = new HashMap<>();
            for (Entry<Pair<Integer, Integer>, Boolean> tile : tiles.entrySet()) {
                Set<Pair<Integer, Integer>> adjacentTiles = findAdjacentTiles(tile.getKey());
                Boolean newColorIsBlack = calculateNewColor(tile.getValue(), adjacentTiles, tiles);
                nextDaytiles.put(tile.getKey(), newColorIsBlack);
            }
            tiles = nextDaytiles;
        }
        LOGGER.info("Part 2: " + nextDaytiles.values().stream().filter(t -> t).count());

    }

    private static Set<Pair<Integer, Integer>> findAdjacentTiles(Pair<Integer, Integer> tile) {
        return Set.of( //
                add(tile, new Pair<>(+2, 0)), //
                add(tile, new Pair<>(+1, -2)), //
                add(tile, new Pair<>(-1, -2)), //
                add(tile, new Pair<>(-2, 0)), //
                add(tile, new Pair<>(-1, +2)), //
                add(tile, new Pair<>(+1, +2))); //
    }

    private static Pair<Integer, Integer> add(Pair<Integer, Integer> a, Pair<Integer, Integer> b) {
        return new Pair<>(a.first() + b.first(), //
                a.second() + b.second());
    }

    private static boolean calculateNewColor(boolean currentColorBlack, Set<Pair<Integer, Integer>> adjecentTiles,
            Map<Pair<Integer, Integer>, Boolean> tiles) {
        long adjacentBlackTiles = adjecentTiles.stream().map(tiles::get).filter(Boolean.TRUE::equals).count();
        if (currentColorBlack) {
            return adjacentBlackTiles > 0 && adjacentBlackTiles < 3;
        }
        return adjacentBlackTiles == 2;
    }

}
