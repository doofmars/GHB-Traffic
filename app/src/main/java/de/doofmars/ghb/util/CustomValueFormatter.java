package de.doofmars.ghb.util;

import com.github.mikephil.charting.utils.ValueFormatter;

import java.text.DecimalFormat;

public class CustomValueFormatter implements com.github.mikephil.charting.utils.ValueFormatter {

    private DecimalFormat mFormat;
    public static final long KB_FACTOR = 1024;
    public static final long MB_FACTOR = 1048576;
    public static final long GB_FACTOR = 1073741824;

    public CustomValueFormatter() {
        mFormat = new DecimalFormat("#0.00");
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
