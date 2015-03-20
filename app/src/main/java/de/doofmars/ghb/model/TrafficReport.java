package de.doofmars.ghb.model;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import de.doofmars.ghb.util.CustomValueFormatter;
import de.doofmars.ghb.R;

/**
 * Created by Jan on 17.03.2015.
 */
public class TrafficReport implements Serializable {
    List<Day> days;
    private final static String FREE_TRAFFIC = "Fee Traffic";
    private final static String UNKNOWN_HOST = "unknown";

    /**
     * Generates the traffic report with empty days list
     */
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
                output.add(new Float(day.getTotal()));
            } else if (currentDate.before(day.getDate())) {
                currentDate = day.getDate();
                output.add(new Float(day.getTotal()));
            } else {
                output.set(output.size() - 1, output.get(output.size() - 1) + Float.valueOf(day.getTotal()));
            }
        }
        return output;
    }

    public float getTotal() {
        float total = 0;

        for (Day day : this.days) {
            total += day.getTotal();
        }
        return total;
    }

    public ArrayList<String> getHosts() {
        ArrayList<String> output = new ArrayList<>();
        if (getTotal() < CustomValueFormatter.GB_FACTOR * 25) {
            output.add(FREE_TRAFFIC);
        }
        for (Day day : this.days) {
            if (!output.contains(day.getHostname())) {
                output.add(day.getHostname());
            }
        }
        return output;
    }

    public String getHost(int i) {
        ArrayList<String> hosts = getHosts();
        if (i < hosts.size()) {
            return hosts.get(i);
        } else {
            return UNKNOWN_HOST;
        }
    }

    public float getTrafficByHost(String host) {
        float output = 0;
        if (host.equals(FREE_TRAFFIC)) {
            return CustomValueFormatter.GB_FACTOR * 25 - getTotal();
        }
        for (Day day : this.days) {
            if (day.getHostname().equals(host)) {
                output += day.getTotal();
            }
        }
        return output;
    }

    public int getDaysSize() {
        int size = 0;
        Date currentDate = null;
        for (Day day : this.days) {
            if (currentDate == null) {
                currentDate = day.getDate();
                size++;
            } else if (currentDate.before(day.getDate())) {
                currentDate = day.getDate();
                size++;
            }
        }
        return size;
    }

    public int[] getDaysColors() {
        int[] output = new int[getDaysSize()];
        List<Float> daysTotal = getDaysTotal();
        int counter = 0;
        for (Float total : daysTotal) {
            output[counter] = colorPicker(total);
            counter++;
        }
        return output;
    }

    /**
     * Colorpicker to determine the entry color depending on used traffic
     * @param value the used traffic
     * @return a color from colors.xml
     */
    private int colorPicker(float value) {
        if (value > CustomValueFormatter.GB_FACTOR * 25) {
            return R.color.black;
        } else if (value > CustomValueFormatter.GB_FACTOR * 20) {
            return R.color.darkred;
        } else if (value > CustomValueFormatter.GB_FACTOR * 5) {
            return R.color.adarkred;
        } else if (value > CustomValueFormatter.GB_FACTOR * 4) {
            return R.color.alightred;
        } else if (value > CustomValueFormatter.GB_FACTOR * 3) {
            return R.color.adarkorange;
        } else if (value > CustomValueFormatter.GB_FACTOR * 2) {
            return R.color.alightorange;
        } else if (value > CustomValueFormatter.GB_FACTOR * 1) {
            return R.color.adarkgreen;
        } else {
            return R.color.alightgreen;
        }
    }

    @Override
    public String toString() {
        return "TrafficReport{" +
                "days=" + days +
                '}';
    }
}
