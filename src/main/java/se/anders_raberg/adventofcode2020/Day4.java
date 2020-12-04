package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
    private static final Logger LOGGER = Logger.getLogger(Day4.class.getName());
    private static final Set<String> EYE_COLOR = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    private static final Pattern HGT_PATTERN = Pattern.compile("([0-9]*)(cm|in)");

    private enum PassportField {
        BYR, IYR, EYR, HGT, HCL, ECL, PID;

        private static final Map<PassportField, Predicate<String>> VALIDITY_PREDICATES = Map.of( //
                BYR, data -> inRange(Integer.parseInt(data), 1920, 2002), //
                IYR, data -> inRange(Integer.parseInt(data), 2010, 2020), //
                EYR, data -> inRange(Integer.parseInt(data), 2020, 2030), //
                HGT, Day4::heightInRange, //
                HCL, data -> data.matches("#[0-9,a-f]{6}"), //
                ECL, EYE_COLOR::contains, //
                PID, data -> data.matches("[0-9]{9}"));
    }

    private Day4() {
    }

    public static void run() throws IOException {
        String[] passports = new String(Files.readAllBytes(Paths.get("inputs/input4.txt"))).split("\n\n");

        // Part 1
        long counter1 = Arrays.stream(passports).filter(Day4::matchAll).count();
        LOGGER.info(() -> "Part 1 : " + counter1);

        // Part 2
        long counter2 = Arrays.stream(passports).filter(Day4::matchAll2).count();
        LOGGER.info(() -> "Part 2 : " + counter2);
    }

    private static boolean matchAll(String passport) {
        return Arrays.stream(PassportField.values()).map(f -> f.toString().toLowerCase()).allMatch(passport::contains);
    }

    private static boolean matchAll2(String passport) {
        return Arrays.stream(PassportField.values()).allMatch(f -> {
            Matcher m = Pattern.compile(f.toString().toLowerCase() + ":(\\S*)").matcher(passport);
            if (m.find()) {
                return PassportField.VALIDITY_PREDICATES.get(f).test(m.group(1));
            }
            return false;
        });
    }

    private static boolean heightInRange(String data) {
        Matcher m = HGT_PATTERN.matcher(data);
        if (m.matches()) {
            int height = Integer.parseInt(m.group(1));
            if (m.group(2).equals("cm")) {
                return inRange(height, 150, 193);
            }
            return inRange(height, 59, 76);
        }
        return false;

    }

    private static boolean inRange(int value, int min, int max) {
        return value >= min && value <= max;
    }

}
