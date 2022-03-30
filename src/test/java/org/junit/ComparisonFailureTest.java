package org.junit;

import static org.junit.Assert.*;

public class ComparisonFailureTest {

    @Test
    public void testGetActual() {
        String message = "message";
        String expected = "expected";
        String actual = "actual";
        ComparisonFailure comparaison = new ComparisonFailure(message, expected, actual);
        assertEquals(comparaison.getActual(), actual);
    }

    @Test
    public void testGetExpected() {
        String message = "message";
        String expected = "expected";
        String actual = "actual";
        ComparisonFailure comparaison = new ComparisonFailure(message, expected, actual);
        assertEquals(comparaison.getExpected(), expected);
    }

}
