package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Day18Part2 {
    private static final Logger LOGGER = Logger.getLogger(Day18Part2.class.getName());

    private Day18Part2() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input18.txt")).stream() //
                .map(l -> l.replaceAll("\\s", "")).collect(Collectors.toList());

        LOGGER.info(() -> "Part 2: " + lines.stream() //
                .map(Day18Part2::evaluate) //
                .mapToLong(Long::parseLong) //
                .sum());
    }

    private static String evaluate(String str) {
        for (int i = 0; i < str.length(); i++) {
            if (str.charAt(i) == '(') {
                int endIndex = findMatchingParenthesis(str.substring(i));
                String before = str.substring(0, i);
                String subexpr = str.substring(i + 1, i + endIndex);
                String after = str.substring(i + endIndex + 1);
                return evaluate(before + evaluate(subexpr) + after);
            }
        }
        return evalFlatExpression(str);
    }

    private static int findMatchingParenthesis(String str) {
        int counter = 0;
        for (int i = 0; i < str.length(); i++) {
            char charAt = str.charAt(i);
            if (charAt == '(') {
                counter++;
            }
            if (charAt == ')') {
                counter--;
                if (counter == 0) {
                    return i;
                }
            }
        }
        throw new IllegalArgumentException("No matching parenthesis");
    }

    private static String evalFlatExpression(String str) {
        String tmpStr = str;
        Pattern pattern = Pattern.compile("([0-9]+\\+[0-9]+)");
        boolean matches = true;
        while (matches) {
            Matcher m = pattern.matcher(tmpStr);
            matches = m.find();
            if (matches) {
                tmpStr = replaceSlice(tmpStr, m.start(), m.end(), evalSum(m.group()));
            }
        }
        return evalProd(tmpStr);
    }

    private static String replaceSlice(String source, int start, int stop, String replacement) {
        return source.substring(0, start) + replacement + source.substring(stop);
    }

    private static String evalSum(String str) {
        return String.valueOf(Arrays.stream(str.split("\\+")).mapToLong(Long::valueOf).sum());
    }

    private static String evalProd(String str) {
        return String.valueOf(Arrays.stream(str.split("\\*")).mapToLong(Long::valueOf).reduce(1, Math::multiplyExact));
    }
}
