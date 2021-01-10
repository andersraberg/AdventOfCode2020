package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import java.util.function.LongBinaryOperator;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class Day18Part1 {
    private static final Logger LOGGER = Logger.getLogger(Day18Part1.class.getName());
    private static final Pattern NUM_PATTERN = Pattern.compile("([0-9]+)(.*)");
    private static final Pattern OPERATOR_PATTERN = Pattern.compile("([\\*,\\+])(.*)");
    private static final Pattern PARENTHESIS_PATTERN = Pattern.compile("(\\(.*)");

    private static final Map<String, LongBinaryOperator> OPERATORS = Map.of( //
            "+", (a, b) -> a + b, //
            "*", (a, b) -> a * b);

    private Day18Part1() {
    }

    public static void run() throws IOException {
        List<String> lines = Files.readAllLines(Paths.get("inputs/input18.txt")).stream() //
                .map(l -> l.replaceAll("\\s", "")).collect(Collectors.toList());

        LOGGER.info(() -> "Part 1: " + lines.stream().map(Day18Part1::evaluate).reduce(0L, Long::sum));
    }

    private static long evaluate(String expression) {
        long accumulatedValue = 0;
        LongBinaryOperator currentOperator = OPERATORS.get("+");
        String remainingExpression = expression;
        while (!remainingExpression.isEmpty()) {
            Matcher mNum = NUM_PATTERN.matcher(remainingExpression);
            Matcher mParenthesis = PARENTHESIS_PATTERN.matcher(remainingExpression);
            if (mNum.matches()) {
                accumulatedValue = currentOperator.applyAsLong(accumulatedValue, Long.parseLong(mNum.group(1)));
                remainingExpression = mNum.group(2);
            } else if (mParenthesis.matches()) {
                Pair<String, String> findMatching = matchParanthesis(mParenthesis.group());
                accumulatedValue = currentOperator.applyAsLong(accumulatedValue, evaluate(findMatching.first()));
                remainingExpression = findMatching.second();
            } else {
                throw new IllegalArgumentException(remainingExpression);
            }
            Matcher mOper = OPERATOR_PATTERN.matcher(remainingExpression);
            if (mOper.matches()) {
                currentOperator = OPERATORS.get(mOper.group(1));
                remainingExpression = mOper.group(2);
            }
        }
        return accumulatedValue;
    }

    private static Pair<String, String> matchParanthesis(String expression) {
        int counter = 0;
        int index = 0;
        do {
            char charAt = expression.charAt(index++);
            if (charAt == '(') {
                counter++;
            }
            if (charAt == ')') {
                counter--;
            }
        } while (counter > 0);

        return new Pair<>(expression.substring(1, index - 1), expression.substring(index));
    }

}
