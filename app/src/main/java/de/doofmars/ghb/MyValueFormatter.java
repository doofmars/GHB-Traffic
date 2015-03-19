package de.doofmars.ghb;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class MyValueFormatter implements ValueFormatter {

    private DecimalFormat mFormat;
    private final long KB_FACTOR = 1024;
    private final long MB_FACTOR = 1048576;
    private final long GB_FACTOR = 1073741824;

    public MyValueFormatter() {
        mFormat = new DecimalFormat("#####.00");
    }
    
    @Override
    public String getFormattedValue(float value) {
        if (value > GB_FACTOR) {
            return mFormat.format(value / GB_FACTOR) + " GB";
        } else if (value > MB_FACTOR) {
            return mFormat.format(value / MB_FACTOR) + " MB";
        } else {
            return mFormat.format(value / KB_FACTOR) + " KB";
        }
    }

}
