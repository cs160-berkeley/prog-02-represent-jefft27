package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.twitter.sdk.android.Twitter;
import com.twitter.sdk.android.core.TwitterAuthConfig;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Locale;

import io.fabric.sdk.android.Fabric;

public class MainActivity extends Activity {

    // Note: Your consumer key and secret should be obfuscated in your source code before shipping.
    private static final String TWITTER_KEY = "vp1K0BPj2HXSNqRBFmpP1A7uT";
    private static final String TWITTER_SECRET = "AxxgxIN6jLnfXaFryr2LlCVjacBakdsHgNxS8yw3EvaGuiCbtS";

    private RadioButton zipCode, currLocation;
    private LinearLayout field;
    private RadioGroup radioGroup;
    private Button btn;
    private String zipCodeEntered, usedToBe;
    private EditText eTxt;
    private boolean usingCurrLoc;
    private Geocoder geocoder;
    private List<Address> addresses;
    private double latitude, longitude;
    private double [] lat_long = new double[2];
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            TwitterAuthConfig authConfig = new TwitterAuthConfig(TWITTER_KEY, TWITTER_SECRET);
            Fabric.with(this, new Twitter(authConfig));
            setContentView(R.layout.activity_main);

            header = (TextView) findViewById(R.id.actionbar);
            header.setBackgroundColor(Color.parseColor("#2F80ED"));
            initializeUI();

            radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    RadioButton rb = (RadioButton) findViewById(checkedId);
                    boolean a = rb.equals(zipCode);
                    if (a) {
                        field.setVisibility(View.VISIBLE);
                        usingCurrLoc = false;
                    } else {
                        setHideSoftKeyboard(eTxt);
                        field.setVisibility(View.GONE);
                        usingCurrLoc = true;
                    }
                }
            });

            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    JSONObject result = null;
                    if (usingCurrLoc) {
                        LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        zipCodeEntered = getCurrLocation(lm);
                    } else {
                        eTxt = (EditText) findViewById(R.id.findZipCode);
                        zipCodeEntered = eTxt.getText().toString();
                        getLatLong();
                    }

                    if (radioGroup.getCheckedRadioButtonId() != -1
                            && zipCodeEntered != null
                            && zipCodeEntered.length() == 5) {

                        usedToBe = null;
                        Toast.makeText(getApplicationContext(), "Loading...", Toast.LENGTH_SHORT).show();
                        MakeAPICalls obj = new MakeAPICalls(zipCodeEntered, getApplicationContext(), lat_long[0], lat_long[1]);
                        obj.setUpConnection();
                    } else {
                        Toast.makeText(getBaseContext(), "Enter in valid zip code", Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    private void setHideSoftKeyboard(EditText editText){
        if (eTxt != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(editText.getWindowToken(), 0);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initializeUI(){
        btn = (Button) findViewById(R.id.btn);
        zipCode = (RadioButton) findViewById(R.id.zipCode);
        currLocation = (RadioButton) findViewById(R.id.currLocation);
        radioGroup = (RadioGroup) findViewById(R.id.whereAreYou);
        field = (LinearLayout) findViewById(R.id.typeZipCodeField);
        field.setVisibility(LinearLayout.GONE);
        eTxt = (EditText) findViewById(R.id.findZipCode);
    }

    private String getCurrLocation(LocationManager lm){
        Location loc = lm.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        double longitude = loc.getLongitude();
        double latitude = loc.getLatitude();
        lat_long[0] = latitude;
        lat_long[1] = longitude;
        Locale locale = new Locale("en", "us");
        geocoder = new Geocoder(this, locale);
        try {
            addresses = geocoder.getFromLocation(latitude, longitude, 1);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        final String postalCode = addresses.get(0).getPostalCode();
        return postalCode;
    }

    private void getLatLong() {
        geocoder = new Geocoder(getApplicationContext());
        try {
            double [] coord = new double[2];
            List<Address> addresses = geocoder.getFromLocationName(zipCodeEntered, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address address = addresses.get(0);
                lat_long[0] = address.getLatitude();
                lat_long[1] = address.getLongitude();
            }
        } catch (IOException e) {
            // handle exception
        }
    }

    /* Turns the json data into a string to parse later. */
        /* Turns the json data into a string to parse later. */
    protected String getVoteData() {
        BufferedReader reader = null;
        String json_str = "";
        try {
            reader = new BufferedReader(
                    new InputStreamReader(getApplicationContext().getAssets().open("election_results_2012.json"), "UTF-8"));

            // do reading, usually loop until end of file reading
            String mLine;
            while ((mLine = reader.readLine()) != null) {
                //process line
                json_str += mLine + "\n";

            }
            return json_str;
        } catch (IOException e) {
            //log the exception
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    //log the exception
                }
            }
        }
        return json_str;
    }

    /* Parses the json data and extracts the necessary information. */
    protected String getResults(String county_name){
        String result_str= "";
        try {
            JSONObject obj = new JSONObject(getVoteData());
            JSONArray data = obj.getJSONArray("results");
            for (int i = 0; i < 10; i += 1) {
                JSONObject jsonArr2 = new JSONObject(data.getString(i));
                Log.v("jsonArr2: ", "" + jsonArr2);
            }
        } catch(JSONException e) {
            Log.d("Mobile MainActivity", "could not parse json string in votes");
        }
        return result_str;
    }

}
