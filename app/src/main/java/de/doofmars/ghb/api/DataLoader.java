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

import de.doofmars.ghb.Statistics;

/**
 * Created by Jan on 14.03.2015.
 */
public class DataLoader extends AsyncTask<String, Void, String> {
    Statistics caller;

    public DataLoader(Statistics caller) {
        this.caller = caller;
    }

    private HttpClient getNewHttpClient() {
        try {
            KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            trustStore.load(null, null);

            MySSLSocketFactory sf = new MySSLSocketFactory(trustStore);
            sf.setHostnameVerifier(SSLSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);

            HttpParams params = new BasicHttpParams();
            HttpProtocolParams.setVersion(params, HttpVersion.HTTP_1_1);
            HttpProtocolParams.setContentCharset(params, HTTP.UTF_8);

            SchemeRegistry registry = new SchemeRegistry();
            registry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
            registry.register(new Scheme("https", sf, 443));

            ClientConnectionManager ccm = new ThreadSafeClientConnManager(params, registry);

            return new DefaultHttpClient(ccm, params);
        } catch (Exception e) {
            return new DefaultHttpClient();
        }
    }

    protected String doInBackground(String... urls) {
        Log.i("DataLoader", "Load data from " + urls[0]);
        StringBuilder builder = new StringBuilder();
        HttpClient client = getNewHttpClient();
        HttpGet httpGet = new HttpGet(urls[0]);

        try {
            HttpResponse response = client.execute(httpGet);
            StatusLine statusLine = response.getStatusLine();
            int statusCode = statusLine.getStatusCode();
            if (statusCode == 200) {
                HttpEntity entity = response.getEntity();
                InputStream content = entity.getContent();
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(content));
                String line;
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                GHBTrafficApiParser ghbTrafficApiParser = new GHBTrafficApiParser(builder.toString());
                return builder.toString();
            } else {
                return "Failed to access data";
            }
        } catch (ClientProtocolException e) {
            Log.e("Getter", "ClientProtocolException", e);
        } catch (IOException e) {
            Log.e("Getter", "IOException", e);
        } catch (XmlPullParserException e) {
            Log.e("Getter", "XmlPullParserException", e);
        }
        return "An exception occurred";

    }

    @Override
    protected void onPostExecute(String result) {
        caller.onBackgroundTaskCompleted(result);
    }
}
