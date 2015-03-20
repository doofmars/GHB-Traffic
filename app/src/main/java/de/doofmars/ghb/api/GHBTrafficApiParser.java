package de.doofmars.ghb.api;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import de.doofmars.ghb.model.Day;
import de.doofmars.ghb.model.TrafficReport;

/**
 * Created by Jan on 14.03.2015.
 */
public class GHBTrafficApiParser {
    private StringReader input;
    TrafficReport report = new TrafficReport();
    public final static String XML_TAG = "day";
    public final static String XML_TOTAL = "total";
    public final static String XML_HOST_ID = "host_id";
    public final static String XML_HOSTNAME = "hostname";
    public final static String XML_DAY = "day";
    public final static String XML_MONTH = "month";
    public final static String XML_YEAR = "year";
    public final static String XML_DATE = "DATE";

    public GHBTrafficApiParser(String xml, XmlPullParser parser) {
        this.input = new StringReader(xml);
        try {
            parser.setInput(input);
            int eventType = parser.getEventType();
            while (eventType != XmlPullParser.END_DOCUMENT) {
                if (parser.getName() != null && parser.getName().equals(XML_TAG)) {
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
        } catch (IOException e) {
            Log.e("XML-Parser", "IOException", e);
        } catch (ParseException e) {
            Log.e("XML-Parser", "Invalid argument value", e);
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                Log.e("XML-Parser", "Attribute " + i + ": " +  parser.getAttributeValue(i));
            }
        }
    }

    public GHBTrafficApiParser(String xml) throws XmlPullParserException {
        this(xml, XmlPullParserFactory.newInstance().newPullParser());
    }

    public TrafficReport getReport() {
        return report;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
