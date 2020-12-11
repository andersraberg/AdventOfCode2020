package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Day10 {
    private static final Logger LOGGER = Logger.getLogger(Day10.class.getName());

    private Day10() {
    }

    public static void run() throws IOException {
        List<Integer> data = Files.readAllLines(Paths.get("inputs/input10.txt")).stream() //
                .map(Integer::valueOf) //
                .collect(Collectors.toList());

        data.add(0, 0);
        data.add(Collections.max(data) + 3);
        Collections.sort(data);

        LOGGER.info(() -> "Part 1: " + count(diffs(data), 1) * count(diffs(data), 3));
        LOGGER.info(() -> "Part 2: " + pathsToLastAdapter(0, data, new HashMap<>()));
    }

    private static long pathsToLastAdapter(int fromAdapter, List<Integer> data, Map<Integer, Long> alreadyCountedPaths) {
        int lastAdapter = data.size() - 1;
        int maxDiff = 3;

        if (fromAdapter == lastAdapter) {
            return 1;
        }

        if (alreadyCountedPaths.containsKey(fromAdapter)) {
            return alreadyCountedPaths.get(fromAdapter);
        }

        long totalpaths = IntStream.rangeClosed(fromAdapter + 1, lastAdapter)
                .filter(i -> data.get(i) - data.get(fromAdapter) <= maxDiff)
                .mapToLong(w -> pathsToLastAdapter(w, data, alreadyCountedPaths)) //
                .reduce(0, Long::sum);

        alreadyCountedPaths.put(fromAdapter, totalpaths);
        return totalpaths;
    }

    private static List<Integer> diffs(List<Integer> input) {
        List<Integer> result = new ArrayList<>();
        for (int i = 1; i < input.size(); i++) {
            result.add(input.get(i) - input.get(i - 1));
        }
        return result;
    }

    private static long count(List<Integer> valueList, int value) {
        return valueList.stream().filter(d -> d.equals(value)).count();
    }
}
