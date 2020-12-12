package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day12Part2 {
    private static final Logger LOGGER = Logger.getLogger(Day12Part2.class.getName());
    private static final String UNEXPECTED_VALUE = "Unexpected value: ";

    private Day12Part2() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input12.txt")).stream() //
                .collect(Collectors.toList());

        int nsShipPos = 0;
        int ewShipPos = 0;
        int nsWaypointRelPos = 1;
        int ewWaypointRelPos = 10;

        for (String line : lines) {
            char action = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));

            switch (action) {
            case 'N':
                nsWaypointRelPos += value;
                break;
            case 'S':
                nsWaypointRelPos -= value;
                break;
            case 'E':
                ewWaypointRelPos += value;
                break;
            case 'W':
                ewWaypointRelPos -= value;
                break;
            case 'L':
            case 'R':
                Pair<Integer, Integer> res = rotate(new Pair<>(ewWaypointRelPos, nsWaypointRelPos),
                        value * (action == 'L' ? 1 : -1));
                ewWaypointRelPos = res.first();
                nsWaypointRelPos = res.second();
                break;
            case 'F':
                nsShipPos += value * nsWaypointRelPos;
                ewShipPos += value * ewWaypointRelPos;
                break;
            default:
                throw new IllegalArgumentException(UNEXPECTED_VALUE + action);
            }
        }

        LOGGER.info("Part 2: " + (Math.abs(nsShipPos) + Math.abs(ewShipPos)));
    }

    private static Pair<Integer, Integer> rotate(Pair<Integer, Integer> in, int angle) {
        double tmpAngle = Math.toRadians(angle);
        return new Pair<>( //
                (int) Math.round(in.first() * Math.cos(tmpAngle) - in.second() * Math.sin(tmpAngle)),
                (int) Math.round(in.first() * Math.sin(tmpAngle) + in.second() * Math.cos(tmpAngle)));
    }

}
