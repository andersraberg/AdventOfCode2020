package se.anders_raberg.adventofcode2020;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import se.anders_raberg.adventofcode2020.utilities.Pair;

public class PairTest {

    @BeforeClass
    public static void setUpBeforeClass() throws Exception {
    }

    @AfterClass
    public static void tearDownAfterClass() throws Exception {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testPair() {
        Pair<String, Integer> testee = new Pair<>("Hello", 42);
        assertEquals("Hello", testee.first());
        assertEquals((Integer)42, testee.second());
        assertEquals("(Hello, 42)", testee.toString());
        assertTrue(testee.equals(new Pair<>("Hello", 42)));
        assertTrue(testee.equals(testee));
        assertFalse(testee.equals(null));
        assertFalse(testee.equals(0));
    }

}
