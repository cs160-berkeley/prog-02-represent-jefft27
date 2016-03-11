package com.cs160.joleary.catnip;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * Created by jefftan on 3/6/16.
 */
public class MakeAPICalls{

    private static final String DEBUG_TAG = "MakeAPICalls";
    private String zipCode, SLDomain, rep_tweet_id, SL_API_key,
                   Goog_API_key, bioGuide_id,
                   county_name;
    private String legislators_path, committees_path,
                   bills_path, twitter_path, goog_path, image_path;
    private String latitude, longitude;
    private ArrayList<String> api_keys;
    private Context mContext;

    public MakeAPICalls(String zip_code, Context mContext, double lat, double lng) {
        RepsInfo.clearAll();

        zipCode = zip_code;
        county_name = "";
        api_keys = new ArrayList<String>();
        SLDomain = "https://congress.api.sunlightfoundation.com/";
        rep_tweet_id = "";
        this.mContext = mContext;
        this.bioGuide_id = "";
        latitude = Double.toString(lat);
        longitude = Double.toString(lng);
        api_keys = grabApiKeys(api_keys);

        if (api_keys.size() > 0 ) {
            SL_API_key = api_keys.get(0);
            Goog_API_key = api_keys.get(1);
        }
        legislators_path = "legislators/locate?zip=" + zipCode + "&apikey=" + SL_API_key;
        committees_path = "committees?member_ids=" + this.bioGuide_id + "&apikey=" + SL_API_key;
        bills_path = "bills?sponsor_id=" + this.bioGuide_id + "&apikey=" + SL_API_key;
        goog_path = "https://maps.googleapis.com/maps/api/geocode/json?latlng="
                     + latitude +  "," + longitude + "&key=" + Goog_API_key;
        image_path = "https://theunitedstates.io/images/congress/225x275/" + this.bioGuide_id  + ".jpg";
        twitter_path="";
    }

    /* Reads in the API keys from a text file. Hidden for security purposes */
    private ArrayList<String> grabApiKeys(ArrayList<String> arr) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(
                    new InputStreamReader(this.mContext.getAssets().open("APIkeys.txt")));
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                arr.add(mLine);
            }

        } catch (IOException e) {
            // log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return arr;
    }

    public void setUpConnection() {
        ConnectivityManager connMgr = (ConnectivityManager)
                this.mContext.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            new DownloadWebpageTask().execute();
        } else {
            Log.d(DEBUG_TAG, "No network connection available.");
        }
    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<Void, Void, ArrayList<String>> {
        @Override
        protected ArrayList<String> doInBackground(Void... arg0) {

            // params comes from the execute() call: params[0] is the url.
            try {
                ArrayList<String> jsonData = new ArrayList<String>();

                String json_str = downloadUrl(getPath("legislators"));
                jsonData.add(json_str);
                JsonParser p = new JsonParser(jsonData.get(0), "legislators", "");
                p.parseJson();

                String vote_data = downloadUrl(goog_path);
                jsonData.add(vote_data);

                for (String rep_name : RepsInfo.getMap().keySet()) {
                    String jsonString = downloadUrl(getPath("committees", rep_name));
                    jsonData.add(jsonString);
                }

                for (String rep_name : RepsInfo.getMap().keySet()) {
                    String jsonString = downloadUrl(getPath("bills", rep_name));
                    jsonData.add(jsonString);
                }
                return jsonData;

            } catch (IOException e) {
                Log.v(DEBUG_TAG, "FetchMoreData, URL invalid");
                return null;
            }
    }

        // Given a URL, establishes an HttpUrlConnection and retrieves
        // the web page content as a InputStream, which it returns as
        // a string.
        private String downloadUrl(String myurl) throws IOException {
            InputStream is = null;

            try {
                URL url = new URL(myurl);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(
                                                    new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    return stringBuilder.toString();
                }
                finally{
                    urlConnection.disconnect();
                }
            }
            catch(Exception e) {
                Log.e("ERROR", e.getMessage(), e);
                return null;
            }
    }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(ArrayList<String> result) {
            result.remove(0);
            JsonParser pData = new JsonParser();
            county_name = pData.grabCounty(result.get(0));
            result.remove(0);
            Log.v(DEBUG_TAG, "county name is: " + county_name);
            JsonParser parseData = new JsonParser(result);
            parseData.parseRestOfData();

            // Mobile activity
            Intent sendToMobile = new Intent(mContext, DisplayRepsActivity.class);
            sendToMobile.putExtra("ZIPCODE", "94801");
            sendToMobile.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(sendToMobile);
            Log.v(DEBUG_TAG, "started Mobile Activity");

            // download images and send to wear.
            Intent sendToWear = new Intent(mContext, PhoneToWatchService.class);
            sendToWear.putExtra("Data", sendToWear());
            sendToWear.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startService(sendToWear);
            Log.v(DEBUG_TAG, "started Wear Activity");

        }
    }

    /* Constructs and returns the query to get the committees
     * of a certain rep. */
    protected String getCommittees(String bioGuide_id, String repr_name) {
        this.bioGuide_id = bioGuide_id;
        committees_path = "committees?member_ids=" + this.bioGuide_id + "&apikey=" + SL_API_key;
        return SLDomain + committees_path;
    }

    /* Constructs and returns the query to get the bills
     * of a certain rep. */
    protected String getBills(String bioGuide_id) {
        this.bioGuide_id = bioGuide_id;
        bills_path = "bills?sponsor_id=" + this.bioGuide_id + "&apikey=" + SL_API_key;
        return SLDomain + bills_path;
    }

    /* Constructs and returns the query to get the legislators
     * of a particular zip code. */
    protected String getLegislators() {

        return SLDomain + legislators_path;
    }

    /* Assembles the image url links */
    protected String assembleUrl(String bioid){
        this.bioGuide_id = bioid;
        image_path = "https://theunitedstates.io/images/congress/original/" +
                      this.bioGuide_id  + ".jpg";
        return image_path;
    }

    /* Returns the URL based on the keyword typed in*/
    protected String getPath(String look_up) {
        switch (look_up) {
            case "legislators":
                return getLegislators();
            case "bills":
                return getBills(this.bioGuide_id);
            case "committees":
                return getCommittees(this.bioGuide_id, "");
            default:
                return null;
        }
    }

    /* Overloaded method  */
    protected String getPath(String look_up, String repr_name) {
        this.bioGuide_id = RepsInfo.getValue(repr_name, "bioguide_id");
        switch(look_up) {
            case "bills":
                return getBills(this.bioGuide_id);
            case "committees":
                return getCommittees(this.bioGuide_id, repr_name);
            default:
                return null;
        }
    }

    /* Message string to send from mobile to wear. To be parsed in the wear side. */
    private String sendToWear(){
        String str = (RepsInfo.getSize() * 2) + ";";
        for (Map.Entry<String, HashMap<String, String>> entry : RepsInfo.getMap().entrySet()) {
            String rep_name = entry.getKey();
            String party = RepsInfo.getValue(rep_name, "party");
            str = str + rep_name + ";" + party + ";";
        }
        String vote_percentage = getResults(county_name);
        return str + county_name + ";" + vote_percentage;
    }

     /* Turns the json data into a string to parse later. */
        /* Turns the json data into a string to parse later. */
    protected String getVoteData() {
        String jsonString = "";
        try {
            InputStream stream = mContext.getAssets().open("election_results_2012.json");
            int size = stream.available();
            byte[] buffer = new byte[size];
            stream.read(buffer);
            stream.close();
            jsonString = new String(buffer, "UTF-8");
        } catch (Exception e) {
            Log.v(DEBUG_TAG,"Unable to read all of election results");
        }
       return jsonString;
    }

    /* Parses the json data and extracts the necessary information. */
    protected String getResults(String county_name){
        String result_str = "";
        try {
            JSONObject obj = new JSONObject(getVoteData()).getJSONObject(county_name);
            System.out.println(obj);
            result_str = obj.getString("obama") + ";" + obj.getString("romney");
        } catch(JSONException e) {
            Log.d(DEBUG_TAG, "could not parse json string in votes");
        }
        Log.v(DEBUG_TAG, "result is: " + result_str);
        return result_str;
     }

}
