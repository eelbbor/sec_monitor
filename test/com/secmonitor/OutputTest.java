package com.secmonitor;

import org.testng.annotations.Test;

import static org.testng.Assert.assertTrue;

/**
 * Created by rlee on 5/31/14.
 */
@Test
public class OutputTest {
    public void passingTest() {
        assertTrue(true);
    }

    public void failingTest() {
        assertTrue(false);
    }
}
