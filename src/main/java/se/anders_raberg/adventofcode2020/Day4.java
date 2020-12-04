package se.anders_raberg.adventofcode2020;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day4 {
    private static final Logger LOGGER = Logger.getLogger(Day4.class.getName());
    private static final Set<String> EYE_COLOR = Set.of("amb", "blu", "brn", "gry", "grn", "hzl", "oth");
    private static final Pattern HGT_PATTERN = Pattern.compile("([0-9]*)(cm|in)");

    private enum PassportField {
        BYR, IYR, EYR, HGT, HCL, ECL, PID;

        private static boolean valid(PassportField pField, String data) {
            switch (pField) {
            case BYR:
                int parseInt = Integer.parseInt(data);
                return parseInt >= 1920 && parseInt <= 2002;
            case ECL:
                return EYE_COLOR.contains(data);
            case EYR:
                int parseInt1 = Integer.parseInt(data);
                return parseInt1 >= 2020 && parseInt1 <= 2030;
            case HCL:
                return data.matches("#[0-9,a-f]{6}");
            case HGT:
                Matcher m = HGT_PATTERN.matcher(data);
                if (m.matches()) {
                    int height = Integer.parseInt(m.group(1));
                    if (m.group(2).equals("cm")) {
                        return height >= 150 && height <= 193;
                    }
                    return height >= 59 && height <= 76;
                }
                return false;
            case IYR:
                int parseInt2 = Integer.parseInt(data);
                return parseInt2 >= 2010 && parseInt2 <= 2020;
            case PID:
                return data.matches("[0-9]{9}");
            default:
                throw new IllegalArgumentException("Unexpected value: " + pField);
            }
        }
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
                return PassportField.valid(f, m.group(1));
            }
            return false;
        });
    }

}
