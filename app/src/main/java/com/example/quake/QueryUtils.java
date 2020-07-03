package com.example.quake;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Helper methods related to requesting and receiving earthquake data from USGS.
 */
public final class QueryUtils {

    public static final String LOG_TAG = MainActivity.class.getName();

    /**
     * Return a list of {@link details} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<details> extractEarthquakes(String SAMPLE_JSON_RESPONSE) {

        // Create an empty ArrayList that we can start adding earthquakes to
        ArrayList<details> earthquakes = new ArrayList<>();

        // Try to parse the SAMPLE_JSON_RESPONSE. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // TODO: Parse the response given by the SAMPLE_JSON_RESPONSE string and
            // build up a list of Earthquake objects with the corresponding data.
            JSONObject temp = new JSONObject(SAMPLE_JSON_RESPONSE);
            JSONArray quakes = temp.optJSONArray("features");
            for(int i=0;i<quakes.length();i++){
                JSONObject temp1=quakes.getJSONObject(i);
                JSONObject properties = temp1.getJSONObject("properties");
                double m =properties.getDouble("mag");
                long t=properties.getLong("time");

                earthquakes.add(new details(m,properties.getString("place"),t,properties.getString("url")));
            }


        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of earthquakes
        return earthquakes;
    }
    static URL createUrl(String urls) {
        URL url;
        try {
            url = new URL(urls);
        } catch (MalformedURLException exception) {
            Log.e(LOG_TAG, "Error with creating URL", exception);
            return null;
        }
        return url;
    }


    static String makeHttprequest(URL url) throws IOException {
        String json = null;
        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            if (url != null) {
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000);
                urlConnection.setConnectTimeout(15000);
                urlConnection.connect();
                if (urlConnection.getResponseCode() == 200) {
                    inputStream = urlConnection.getInputStream();
                    json = readFromStream(inputStream);
                }
            }
        } catch (ProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return json;
    }

    static String readFromStream(InputStream is) throws IOException {
        StringBuilder builder = new StringBuilder();
        if (is != null) {
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader bfr = new BufferedReader(isr);
            String line = bfr.readLine();
            while (line != null) {
                builder.append(line);
                line = bfr.readLine();
            }
        }
        return builder.toString();
    }
    public static List<details> fetchlist(String urls) throws IOException {
        String json = makeHttprequest(createUrl(urls));
        List<details> earthquakes = extractEarthquakes(json);
        return earthquakes;

    }
}