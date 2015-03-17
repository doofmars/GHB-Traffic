package de.doofmars.ghb.model;

import junit.framework.TestCase;

import org.junit.Test;

public class DayTest extends TestCase {

    @Test
    public void testCompareTo() {
        Day one = new Day("0", "123", "test", "01", "02", "2015", "01.02.2015");
        Day oneA = new Day("0", "123", "aTestA", "01", "02", "2015", "01.02.2015");
        Day oneB = new Day("0", "123", "testB", "01", "02", "2015", "01.02.2015");
        Day two = new Day("0", "123", "test", "01", "01", "2015", "01.01.2015");
        Day three = new Day("0", "123", "test", "01", "03", "2015", "01.03.2015");
        assertEquals(0, one.compareTo(one));
        assertTrue(one.compareTo(oneA) > 0);
        assertTrue(one.compareTo(oneB) < 0);
        assertTrue(one.compareTo(two) > 0);
        assertTrue(one.compareTo(three) < 0);
    }
}