package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.google.common.collect.Sets;

public class Day9 {
    private static final Logger LOGGER = Logger.getLogger(Day9.class.getName());
    private static final int PREAMBLE_SIZE = 25;

    private Day9() {
    }

    public static void run() throws IOException {
        List<Long> data = Files.readAllLines(Paths.get("inputs/input9.txt")).stream() //
                .map(Long::valueOf) //
                .collect(Collectors.toList());

        int pos = PREAMBLE_SIZE;
        long invalidNumber = -1;

        while (pos < data.size()) {
            List<Long> subList = data.subList(pos - PREAMBLE_SIZE, pos);
            Set<Long> combinations = Sets.combinations(new HashSet<>(subList), 2).stream().map(Day9::sum)
                    .collect(Collectors.toSet());
            if (!combinations.contains(data.get(pos))) {
                invalidNumber = data.get(pos);
                break;
            }
            pos++;
        }

        LOGGER.info("Part 1 : " + invalidNumber);

        int firstPos = 0;
        int lastPos = 0;

        while (firstPos < data.size()) {
            List<Long> subList = data.subList(firstPos, lastPos + 1);
            Long sublistSum = sum(subList);

            if (sublistSum.equals(invalidNumber)) {
                LOGGER.info(() -> "Part 2: " + (Collections.min(subList) + Collections.max(subList)));
                break;
            } else if (sublistSum > invalidNumber) {
                firstPos++;
            } else {
                lastPos++;
            }
        }

    }

    private static long sum(Collection<Long> collection) {
        return collection.stream().reduce(0L, Long::sum);
    }

}
