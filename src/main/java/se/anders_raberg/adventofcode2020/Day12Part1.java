package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class Day12Part1 {
    private static final Logger LOGGER = Logger.getLogger(Day12Part1.class.getName());
    private static final String UNEXPECTED_VALUE = "Unexpected value: ";

    private Day12Part1() {
    }

    private static final List<Character> DIRECTIONS = List.of('N', 'E', 'S', 'W');
    private static final int DIR_MODULO = DIRECTIONS.size();

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input12.txt")).stream() //
                .collect(Collectors.toList());

        int nsPos = 0;
        int ewPos = 0;
        char direction = 'E';

        for (String line : lines) {
            char action = line.charAt(0);
            int value = Integer.parseInt(line.substring(1));

            switch (action) {
            case 'N':
                nsPos += value;
                break;
            case 'S':
                nsPos -= value;
                break;
            case 'E':
                ewPos += value;
                break;
            case 'W':
                ewPos -= value;
                break;
            case 'L':
            case 'R':
                direction = newDirection(direction, action, value);
                break;
            case 'F':
                switch (direction) {
                case 'N':
                    nsPos += value;
                    break;
                case 'S':
                    nsPos -= value;
                    break;
                case 'E':
                    ewPos += value;
                    break;
                case 'W':
                    ewPos -= value;
                    break;
                default:
                    throw new IllegalArgumentException(UNEXPECTED_VALUE + direction);
                }
                break;
            default:
                throw new IllegalArgumentException(UNEXPECTED_VALUE + action);
            }
        }
        LOGGER.info("Part 1: " + (Math.abs(nsPos) + Math.abs(ewPos)));
    }

    private static char newDirection(char oldDirection, char turn, int amount) {
        int index = DIRECTIONS.indexOf(oldDirection);
        switch (turn) {
        case 'L':
            index = (index + DIR_MODULO - amount / 90) % DIR_MODULO;
            break;
        case 'R':
            index = (index + amount / 90) % DIR_MODULO;
            break;

        default:
            throw new IllegalArgumentException(UNEXPECTED_VALUE + turn);
        }
        return DIRECTIONS.get(index);
    }

}
