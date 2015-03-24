package de.doofmars.ghb.model;

import junit.framework.TestCase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import de.doofmars.ghb.util.CustomValueFormatter;

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
        assertEquals(600f, days.get(0));
        assertEquals(546f, days.get(1));
        assertEquals(746f, days.get(2));
        assertEquals(600f, days.get(3));
    }

    public void testGetHosts() throws Exception {
        ArrayList<String> hosts = mockReport.getHosts();
        float hostFree = mockReport.getTrafficByHost(hosts.get(0));
        float hostA = mockReport.getTrafficByHost(hosts.get(1));
        float hostB = mockReport.getTrafficByHost(hosts.get(2));
        assertEquals(CustomValueFormatter.GB_FACTOR * 25f, hostFree + hostA + hostB);
    }
}