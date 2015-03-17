package de.doofmars.ghb.model;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

public class TrafficReportTest extends TestCase {

    TrafficReport mockReport;

    public void setUp() throws Exception {
        super.setUp();
        mockReport = new TrafficReport();
        mockReport.addDay(new Day("100", "123", "testA", "01", "02", "2015", "01.02.2015"));
        mockReport.addDay(new Day("500", "123", "testB", "01", "02", "2015", "01.02.2015"));
        mockReport.addDay(new Day("123", "123", "testA", "02", "02", "2015", "02.02.2015"));
        mockReport.addDay(new Day("423", "123", "testB", "02", "02", "2015", "02.02.2015"));
        mockReport.addDay(new Day("123", "123", "testA", "03", "02", "2015", "03.02.2015"));
        mockReport.addDay(new Day("623", "123", "testB", "03", "02", "2015", "03.02.2015"));
        mockReport.addDay(new Day("200", "123", "testA", "04", "02", "2015", "04.02.2015"));
        mockReport.addDay(new Day("400", "123", "testB", "04", "02", "2015", "04.02.2015"));
    }

    public void tearDown() throws Exception {
        mockReport = null;
    }

    public void testGetDaysText() throws Exception {
        List<String> days = mockReport.getDaysText();
        SimpleDateFormat df = new SimpleDateFormat("E");
        Calendar cal = GregorianCalendar.getInstance();
        cal.set(2015, 02, 01);
        assertEquals(4, days.size());
        assertEquals(df.format(cal.getTime()), days.get(0));
    }

    public void testGetDaysTotal() throws Exception {
        List<Float> days = mockReport.getDaysTotal();
        assertEquals(4, days.size());
        assertEquals(6.0f, days.get(0));
        assertEquals(5.46f, days.get(1));
        assertEquals(7.46f, days.get(2));
        assertEquals(6.0f, days.get(3));

    }
}