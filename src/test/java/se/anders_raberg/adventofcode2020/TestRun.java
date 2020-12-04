package se.anders_raberg.adventofcode2020;

import static org.junit.Assert.assertTrue;

import java.io.IOException;

import org.junit.Test;

public class TestRun {
    DaysMain testee = new DaysMain();

    @Test
    public void testMain() throws IOException {
        DaysMain.main(null);
        assertTrue(true);
    }

}
