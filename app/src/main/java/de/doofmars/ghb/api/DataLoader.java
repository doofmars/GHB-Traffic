package de.doofmars.ghb.api;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.HttpVersion;
import org.apache.http.StatusLine;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.params.HttpProtocolParams;
import org.apache.http.protocol.HTTP;
import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.security.KeyStore;

import de.doofmars.ghb.R;
import de.doofmars.ghb.Statistics;
import de.doofmars.ghb.model.TrafficReport;

/**
 * Class to Load data from the XML interface
 */
public class DataLoader extends AsyncTask<String, Void, TrafficReport> {
    Statistics caller;

    public DataLoader(Statistics caller) {
        this.caller = caller;
    }

    /**
     * Async data loading
     *
     * @param urls to parse
     * @return a Traffic Report
     */
    protected TrafficReport doInBackground(String... urls) {
        Log.i("DataLoader", "Load data from " + urls[0]);
        StringBuilder builder = new StringBuilder();
        HttpClient client = new DefaultHttpClient();
        HttpGet httpGet = new HttpGet(urls[0]);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            Log.i("DataLoader", "Data received with status code " + statusCode);
            if (statusCode == 200) {
                //Success we have the data
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                GHBTrafficApiParser ghbTrafficApiParser = new GHBTrafficApiParser(builder.toString(), caller);

                //release connection and return data
                response.getEntity().consumeContent();
                return ghbTrafficApiParser.getReport();
            } else {
                response.getEntity().consumeContent();
                //report connection error and return empty report
                return new TrafficReport(caller.getString(R.string.error_fetch));
            }
        } catch (ClientProtocolException e) {
            Log.e("Getter", "ClientProtocolException", e);
        } catch (IOException e) {
            Log.e("Getter", "IOException", e);
        } catch (XmlPullParserException e) {
            Log.e("Getter", "XmlPullParserException", e);
        }
        return new TrafficReport(caller.getString(R.string.error_fetch));

    }

    @Override
    protected void onPostExecute(TrafficReport report) {
        caller.onBackgroundTaskCompleted(report);
    }
}
