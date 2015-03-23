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
 * TrafficReport class to collect the day data and
 * calculate the information necessary for the graphs
 *
 * Created by Jan on 17.03.2015.
 */
public class TrafficReport implements Serializable {
    private List<Day> days;
    private String message;

    private final static String FREE_TRAFFIC = "Free Traffic";
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

    /**
     * Get all days
     *
     * @return
     */
    public List<Day> getDays() {
        return days;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Function to replace the days list
     *
     * @param days
     */
    public void setDays(List<Day> days) {
        this.days = days;
        Collections.sort(days);
    }

    /**
     * Function to add a single day
     *
     * @param day
     */
    public void addDay(Day day) {
        if (!this.days.contains(day)) {
            this.days.add(day);
            Collections.sort(days);
        }
    }

    /**
     * Get the formatted name of each day as String Format: "E"
     *
     * @return
     */
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

    /**
     * Gets the total traffic for each individual day
     *
     * @return total traffic list
     */
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

    /**
     * Gets the total amount of traffic used
     *
     * @return total traffic
     */
    public float getTotal() {
        float total = 0;

        for (Day day : this.days) {
            total += day.getTotal();
        }
        return total;
    }

    /**
     * Gets a list of distinct hostnames
     *
     * @return
     */
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

    /**
     * Get the hostname with the position i
     *
     * @param i the position
     * @return the host with id i from getHosts()
     */
    public String getHost(int i) {
        ArrayList<String> hosts = getHosts();
        if (i < hosts.size()) {
            return hosts.get(i);
        } else {
            return UNKNOWN_HOST;
        }
    }

    /**
     * Returns the total traffic for the given hostname
     *
     * @param host the given host
     * @return the traffic for the host or zero if host is unknown
     */
    public float getTrafficByHost(String host) {
        float output = 0;
        //Special case, we have the free traffic
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

    /**
     * Number of different days in the days list
     *
     * @return
     */
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

    /**
     * Gets a list of R.colours to display in the bar-chart
     *
     * @return colour ids
     */
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
