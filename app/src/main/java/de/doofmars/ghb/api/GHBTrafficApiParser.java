package de.doofmars.ghb.api;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;

import de.doofmars.ghb.R;
import de.doofmars.ghb.Statistics;
import de.doofmars.ghb.model.Day;
import de.doofmars.ghb.model.TrafficReport;

public class GHBTrafficApiParser {
    private TrafficReport report;
    public final static String XML_TAG = "day";
    public final static String XML_STATUS = "status";
    public final static String XML_STATUS_SUCCESS = "success";
    public final static String XML_STATUS_UNKNOWN = "0";
    public final static String XML_TOTAL = "total";
    public final static String XML_HOST_ID = "host_id";
    public final static String XML_HOSTNAME = "hostname";
    public final static String XML_DAY = "day";
    public final static String XML_MONTH = "month";
    public final static String XML_YEAR = "year";
    public final static String XML_DATE = "DATE";

    public GHBTrafficApiParser(String xml, XmlPullParser parser, Statistics caller) {
        StringReader input = new StringReader(xml);
        report = new TrafficReport();
        boolean success = false;

        try {
            parser.setInput(input);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (!success && parser.getName() != null && parser.getName().equals(XML_STATUS)) {
                    eventType = parser.next();
                    if (eventType == XmlPullParser.TEXT && parser.getText().equals(XML_STATUS_SUCCESS)) {
                        success = true;
                    } else {
                        if (eventType == XmlPullParser.TEXT && parser.getText().equals(XML_STATUS_UNKNOWN)) {
                            report.setMessage(caller.getString(R.string.error_key));
                            return;
                        } else {
                            report.setMessage(caller.getString(R.string.error_parse));
                            return;
                        }
                    }
                }
                if (parser.getName() != null && parser.getName().equals(XML_TAG) && success) {
                    Day currentDay = new Day(
                            parser.getAttributeValue(null, XML_TOTAL),
                            parser.getAttributeValue(null, XML_HOST_ID),
                            parser.getAttributeValue(null, XML_HOSTNAME),
                            parser.getAttributeValue(null, XML_DAY),
                            parser.getAttributeValue(null, XML_MONTH),
                            parser.getAttributeValue(null, XML_YEAR),
                            parser.getAttributeValue(null, XML_DATE));
                    Log.v("XML-Parser", currentDay.toString());
                    report.addDay(currentDay);
                }
                eventType = parser.next();
            }
            Log.i("XML-Parser", report.toString());
        } catch (XmlPullParserException e) {
            Log.e("XML-Parser", "XmlPullParserException", e);
            report.setMessage(caller.getString(R.string.error_parse));
        } catch (IOException e) {
            Log.e("XML-Parser", "IOException", e);
            report.setMessage(caller.getString(R.string.error_parse));
        } catch (ParseException e) {
            Log.e("XML-Parser", "Invalid argument value", e);
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                Log.e("XML-Parser", "Attribute " + i + ": " + parser.getAttributeValue(i));
            }
            report.setMessage(caller.getString(R.string.error_parse));
        }
    }

    public GHBTrafficApiParser(String xml, Statistics caller) throws XmlPullParserException {
        this(xml, XmlPullParserFactory.newInstance().newPullParser(), caller);
    }

    public TrafficReport getReport() {
        return report;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
