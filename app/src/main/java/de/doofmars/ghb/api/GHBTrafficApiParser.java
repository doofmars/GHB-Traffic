package de.doofmars.ghb.api;

import android.text.TextUtils;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.ParseException;
import java.util.Date;

import de.doofmars.ghb.R;
import de.doofmars.ghb.Statistics;
import de.doofmars.ghb.model.Day;
import de.doofmars.ghb.model.TrafficReport;

/**
 * Parser to convert the XML into an TrafficReport object
 */
public class GHBTrafficApiParser {
    private TrafficReport report;
    public final static String XML_TAG = "day";
    public final static String XML_STATUS = "status";
    public final static String XML_STATUS_SUCCESS = "success";
    public final static String XML_STATUS_UNKNOWN = "0";
    public final static long XML_VALIDITY = 1209600000l; //2 weeks in milliseconds
    //XML_DATE has to be after (now - 2 weeks)

    public final static int XML_ATTRIBUTE_COUNT = 7;
    public final static String XML_TOTAL = "total";
    public final static String XML_HOST_ID = "host_id";
    public final static String XML_HOSTNAME = "hostname";
    public final static String XML_DAY = "day";
    public final static String XML_MONTH = "month";
    public final static String XML_YEAR = "year";
    public final static String XML_DATE = "DATE";

    /**
     * Constructor parses xml and fills the report
     * @param xml the xml to parse
     * @param parser the parser
     * @param caller the calling class for string resources
     */
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
                        //check for error status (e.g. wrong key)
                        if (eventType == XmlPullParser.TEXT && parser.getText().equals(XML_STATUS_UNKNOWN)) {
                            report.setMessage(caller.getString(R.string.error_key));
                            return;
                        } else {
                            report.setMessage(caller.getString(R.string.error_parse));
                            return;
                        }
                    }
                }
                if (success && parser.getName() != null && parser.getName().equals(XML_TAG)) {
                    if (validate(parser)) {
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
                    parser.next();
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
        } catch (NumberFormatException | ParseException e) {
            Log.e("XML-Parser", "Invalid argument value", e);
            for (int i = 0; i < parser.getAttributeCount(); i++) {
                Log.e("XML-Parser", "Attribute " + i + ": " + parser.getAttributeValue(i));
            }
            report.setMessage(caller.getString(R.string.error_parse));
        }
    }

    /**
     * Validator to validate the date xml entry
     * @param parser the parser with the current location
     * @return true if each necessary attribute is set and valid
     */
    private boolean validate(XmlPullParser parser) {
        //Test if the right attribute count is set
        if (parser.getAttributeCount() == XML_ATTRIBUTE_COUNT) {
            //Test if every attribute is set and has a value
            if (!TextUtils.isEmpty(parser.getAttributeValue(null, XML_TOTAL)) &&
                    !TextUtils.isEmpty(parser.getAttributeValue(null, XML_HOST_ID)) &&
                    !TextUtils.isEmpty(parser.getAttributeValue(null, XML_HOSTNAME)) &&
                    !TextUtils.isEmpty(parser.getAttributeValue(null, XML_DAY)) &&
                    !TextUtils.isEmpty(parser.getAttributeValue(null, XML_MONTH)) &&
                    !TextUtils.isEmpty(parser.getAttributeValue(null, XML_YEAR)) &&
                    !TextUtils.isEmpty(parser.getAttributeValue(null, XML_DATE))) {
                try {
                    //Test if the date can be formatted and is after (now - 2 weeks)
                    Date d = Day.DF.parse(parser.getAttributeValue(null, XML_DATE));
                    if (d.getTime() > (System.currentTimeMillis() - XML_VALIDITY)) {
                        return true;
                    } else {
                        Log.e("XML-Parser", "Validation error, Date attribute not after (now - 2 weeks)");
                    }
                } catch (Exception e) {
                    Log.e("XML-Parser", "Exception during validation", e);
                }
            } else {
                Log.e("XML-Parser", "Validation error, argument without Value");
            }
        } else {
            Log.e("XML-Parser", "Validation error, wrong argument count");
        }

        //Error case: log each argument
        for (int i = 0; i < parser.getAttributeCount(); i++) {
            Log.e("XML-Parser", "Attribute " + i + ": " + parser.getAttributeValue(i));
        }
        return false;
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
