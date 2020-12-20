package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import com.google.common.collect.Range;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day16 {
    private static final Logger LOGGER = Logger.getLogger(Day16.class.getName());
    private static final Pattern PATTERN_FIELD = Pattern.compile("(.+): (\\d+)\\-(\\d+) or (\\d+)\\-(\\d+)");

    private static class TicketField {
        private final String _name;
        private final Range<Long> _firstRange;
        private final Range<Long> _secondRange;

        private TicketField(String name, Range<Long> firstRange, Range<Long> secondRange) {
            super();
            _name = name;
            _firstRange = firstRange;
            _secondRange = secondRange;
        }

        public String name() {
            return _name;
        }

        public boolean inRange(long value) {
            return _firstRange.contains(value) || _secondRange.contains(value);
        }
    }

    private Day16() {
    }

    public static void run() throws IOException {
        String[] fileSections = new String(Files.readAllBytes(Paths.get("inputs/input16.txt"))).split("\n\n");

        // Parse ticket fields
        //
        Set<TicketField> ticketFields = new HashSet<>();
        for (String line : fileSections[0].split("\n")) {
            Matcher m = PATTERN_FIELD.matcher(line);
            if (m.matches()) {
                ticketFields.add(new TicketField(m.group(1), //
                        Range.closed(Long.parseLong(m.group(2)), Long.parseLong(m.group(3))), //
                        Range.closed(Long.parseLong(m.group(4)), Long.parseLong(m.group(5)))));
            }
        }

        // Parse your ticket
        //
        List<Long> yourTicket = Collections.emptyList();
        for (String line : fileSections[1].split("\n")) {
            if (line.contains(",")) {
                yourTicket = parseTicket(line);
            }
        }

        // Parse nearby tickets
        //
        Set<List<Long>> nearbyTickets = new HashSet<>();
        for (String line : fileSections[2].split("\n")) {
            if (line.contains(",")) {
                nearbyTickets.add(parseTicket(line));
            }
        }

        LOGGER.info(() -> "Part 1 : " + nearbyTickets.stream() //
                .map(t -> t.stream() //
                        .filter(v -> !matchesAnyField(v, ticketFields)) //
                        .reduce(0L, Long::sum)) //
                .reduce(0L, Long::sum));

        Set<List<Long>> validTickets = nearbyTickets.stream() //
                .filter(t -> t.stream() //
                        .allMatch(v -> matchesAnyField(v, ticketFields))) //
                .collect(Collectors.toSet());

        List<Pair<Integer, TicketField>> result = new ArrayList<>();
        while (!ticketFields.isEmpty()) {
            Map<Integer, List<TicketField>> matchingFields = new HashMap<>();
            for (int i = 0; i < yourTicket.size(); i++) {
                List<TicketField> matches = new ArrayList<>();
                for (TicketField list : ticketFields) {
                    int index = i;
                    if (validTickets.stream().map(t -> t.get(index)).allMatch(list::inRange)) {
                        matches.add(list);
                    }
                }
                matchingFields.put(i, matches);
            }
            Pair<Integer, TicketField> pair = findFieldMatchingOnlyOne(matchingFields);
            result.add(pair);
            ticketFields.remove(pair.second());
        }

        List<Long> tmpList = yourTicket;
        LOGGER.info(() -> "Part 2 : " + result.stream().filter(p -> p.second().name().contains("departure"))
                .map(p -> tmpList.get(p.first())).reduce(1L, (x, y) -> x * y));
    }

    private static Pair<Integer, TicketField> findFieldMatchingOnlyOne(Map<Integer, List<TicketField>> matchingFields) {
        return matchingFields.entrySet().stream() //
                .filter(e -> e.getValue().size() == 1) //
                .map(e -> new Pair<>(e.getKey(), e.getValue().get(0))) //
                .findFirst().orElseThrow();
    }

    private static boolean matchesAnyField(long value, Set<TicketField> fields) {
        return fields.stream().anyMatch(f -> f.inRange(value));
    }

    private static List<Long> parseTicket(String ticket) {
        return Arrays.stream(ticket.split(",")).map(Long::parseLong).collect(Collectors.toList());
    }

}
