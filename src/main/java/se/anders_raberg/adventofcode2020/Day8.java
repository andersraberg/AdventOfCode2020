package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day8 {
    private static final Logger LOGGER = Logger.getLogger(Day8.class.getName());
    private static final Pattern PATTERN = Pattern.compile("(nop|acc|jmp) ([\\+|\\-]\\d+)");

    private Day8() {
    }

    private static final List<Pair<String, Integer>> INSTRUCTIONS = new ArrayList<>();

    public static void run() throws IOException {
        List<String> instructions = Files.readAllLines(Paths.get("inputs/input8.txt")).stream() //
                .collect(Collectors.toList());

        for (String string : instructions) {
            Matcher m = PATTERN.matcher(string);

            if (m.matches()) {
                INSTRUCTIONS.add(new Pair<>(m.group(1), Integer.parseInt(m.group(2))));
            } else {
                throw new IllegalArgumentException();
            }
        }

        LOGGER.info(() -> "Part 1 : " + executeProgram(INSTRUCTIONS));

        for (int i = 0; i < INSTRUCTIONS.size(); i++) {
            List<Pair<String, Integer>> changedInstr = new ArrayList<>(INSTRUCTIONS);
            changedInstr.set(i, new Pair<>(swap(changedInstr.get(i).first()), changedInstr.get(i).second()));
            executeProgram(changedInstr);
        }

    }

    private static String swap(String str) {
        return str.equals("nop") ? "jmp" : "nop";
    }

    private static int executeProgram(List<Pair<String, Integer>> instructions) {
        Set<Integer> visitedProgramcounter = new HashSet<>();
        int accumulator = 0;
        int programCounter = 0;

        boolean cont = true;
        while (cont) {
            Pair<String, Integer> instr = instructions.get(programCounter);
            switch (instr.first()) {
            case "nop":
                programCounter++;
                break;
            case "acc":
                accumulator += instr.second();
                programCounter++;
                break;
            case "jmp":
                programCounter += instr.second();
                break;
            default:
                throw new IllegalArgumentException("Unexpected value: [" + instr.first() + "]");
            }

            cont = visitedProgramcounter.add(programCounter);

            if (programCounter == instructions.size()) {
                LOGGER.info("Part 2 : " + accumulator);
                break;
            }
        }
        return accumulator;
    }
}
