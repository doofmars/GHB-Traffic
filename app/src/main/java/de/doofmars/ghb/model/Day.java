package de.doofmars.ghb.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * The Day class represents a single entry parsed from the XML Api
 */
public class Day implements Comparable<Day> {
    public final static SimpleDateFormat DF = new SimpleDateFormat("d.MM.yyyy");
    private long total;
    private long host_id;
    private String hostname;
    private int day;
    private int month;
    private int year;
    private Date date;

    /**
     * Construct a day element, will do the transformation from String to float, int and Date
     *
     * @param total
     * @param host_id
     * @param hostname
     * @param day
     * @param month
     * @param year
     * @param dateString
     * @throws NumberFormatException parsing error with a number value
     * @throws ParseException parsing error with a number Date
     */
    public Day(String total, String host_id, String hostname, String day, String month, String year, String dateString) throws NumberFormatException, ParseException {
        this.total = Long.valueOf(total);
        this.host_id = Long.valueOf(host_id);
        this.hostname = hostname;
        this.day = Integer.valueOf(day);
        this.month = Integer.valueOf(month);
        this.year = Integer.valueOf(year);
        this.date = DF.parse(dateString);

    }

    public long getTotal() {
        return total;
    }

    public long getHost_id() {
        return host_id;
    }

    public String getHostname() {
        return hostname;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    public Date getDate() {
        return date;
    }

    @Override
    public String toString() {
        return "Day{" +
                "total=" + total +
                ", host_id=" + host_id +
                ", hostname='" + hostname + '\'' +
                ", day=" + day +
                ", month=" + month +
                ", year=" + year +
                ", date=" + date +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Day day1 = (Day) o;

        if (day != day1.day) return false;
        if (host_id != day1.host_id) return false;
        if (month != day1.month) return false;
        if (total != day1.total) return false;
        if (year != day1.year) return false;
        if (date != null ? !date.equals(day1.date) : day1.date != null) return false;
        if (hostname != null ? !hostname.equals(day1.hostname) : day1.hostname != null)
            return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = (int) (total ^ (total >>> 32));
        result = 31 * result + (int) (host_id ^ (host_id >>> 32));
        result = 31 * result + (hostname != null ? hostname.hashCode() : 0);
        result = 31 * result + day;
        result = 31 * result + month;
        result = 31 * result + year;
        result = 31 * result + (date != null ? date.hashCode() : 0);
        return result;
    }

    @Override
    public int compareTo(Day that) {
        if (this.getDate().compareTo(that.getDate()) != 0) {
            return this.getDate().compareTo(that.getDate());
        }
        if (this.getHostname().compareTo(that.getHostname()) != 0) {
            return this.getHostname().compareTo(that.getHostname());
        }
        return 0;
    }
}
