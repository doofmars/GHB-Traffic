package de.doofmars.ghb.model;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * Created by Jan on 17.03.2015.
 */
public class TrafficReport {
    List<Day> days;

    public TrafficReport() {
        days = new ArrayList<Day>();
    }

    public TrafficReport(List<Day> days) {
        this.days = days;
        Collections.sort(days);
    }

    public List<Day> getDays() {
        return days;
    }

    public void setDays(List<Day> days) {
        this.days = days;
        Collections.sort(days);
    }

    public void addDay(Day day) {
        if (!this.days.contains(day)) {
            this.days.add(day);
            Collections.sort(days);
        }
    }

    public List<String> getDaysText() {
        List<String> output = new ArrayList<String>();
        SimpleDateFormat df = new SimpleDateFormat("E");
        Date currentDate = null;

        for (Day day : this.days) {
            if (currentDate == null) {
                currentDate = day.getDate();
                output.add(df.format(currentDate));
            }
            if (currentDate.before(day.getDate())) {
                currentDate = day.getDate();
                output.add(df.format(currentDate));
            }
        }
        return output;
    }

    public List<Float> getDaysTotal() {
        List<Float> output = new ArrayList<Float>();
        Date currentDate = null;

        for (Day day : this.days) {
            if (currentDate == null) {
                currentDate = day.getDate();
                output.add(new Float(day.getTotal())/100);
            } else if (currentDate.before(day.getDate())) {
                currentDate = day.getDate();
                output.add(new Float(day.getTotal())/100);
            } else {
                output.set(output.size() - 1, output.get(output.size() - 1) + new Float(day.getTotal())/ 100);
            }
        }
        return output;
    }

    @Override
    public String toString() {
        return "TrafficReport{" +
                "days=" + days +
                '}';
    }
}
