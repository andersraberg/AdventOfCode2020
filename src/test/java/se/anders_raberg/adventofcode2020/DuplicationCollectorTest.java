package se.anders_raberg.adventofcode2020;

import static org.junit.Assert.assertEquals;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import se.anders_raberg.adventofcode2020.utilities.DuplicationCollector;

public class DuplicationCollectorTest {

    private static final List<Integer> TESTEE = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8);

    @Test
    public void test1() {
        List<Integer> expected = Arrays.asList(1, 1, 2, 2, 3, 3, 4, 4, 5, 5, 6, 6, 7, 7, 8, 8);
        List<Integer> actual = TESTEE.stream().collect(new DuplicationCollector<>(2));

        System.out.println(actual);

        assertEquals(expected, actual);
    }

    // @Test
    // public void test2() {
    // List<List<Integer>> expected = Arrays.asList(Arrays.asList(1, 2, 3, 4),
    // Arrays.asList(5, 6, 7, 8));
    // List<List<Integer>> actual = TESTEE.stream().collect(new
    // SublistCollector<>(4, false));
    //
    // assertEquals(expected, actual);
    // }

}
