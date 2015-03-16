package de.doofmars.ghb.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Day {
    private final static SimpleDateFormat DF = new SimpleDateFormat("d.MM.yyyy");
    private long total;
    private long host_id;
    private String hostname;
    private int day;
    private int month;
    private int year;
    private Date date;

    public Day(String total, String host_id, String hostname, String day, String month, String year, String dateString) {
        this.total = Long.valueOf(total);
        this.host_id = Long.valueOf(host_id);
        this.hostname = hostname;
        this.day = Integer.valueOf(day);
        this.year = Integer.valueOf(year);
        try {
            this.date = DF.parse(dateString);
        } catch (ParseException e) {
            this.date = new Date();
        }
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
}
