package com.cs160.joleary.catnip;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DisplayRepsActivity extends Activity {

    ArrayList<String> repsNames, pos, keys, email, website, tweets;
    ArrayAdapter<String>adapter;
    ArrayList<Bitmap> photos;
    private String zip_code, image_path, bioguide_id;
    private HashMap<String,HashMap<String, HashMap<String, String>>> persons;
    private HashMap<String, HashMap<String, String>> details;
    private LinearLayout layout;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reps);

        header = (TextView) findViewById(R.id.actionbar);
        header.setBackgroundColor(Color.parseColor("#2F80ED"));

        final ListView repsList = (ListView) findViewById(R.id.rep_view);
        repsNames = new ArrayList<String>();
        pos = new ArrayList<String>();
        keys = new ArrayList<String>();
        photos = new ArrayList<Bitmap>();
        email = new ArrayList<String>();
        website = new ArrayList<String>();
        tweets = new ArrayList<String>();
        layout = (LinearLayout) findViewById(R.id.background);
        layout.setBackgroundColor(Color.parseColor("#F2F8FF"));

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            zip_code = extras.getString("ZIPCODE");
            zip_code = "94801";
            DummyInfo d = new DummyInfo(zip_code);
            persons = d.getMap();
        }
        setTitle(zip_code);
        details = persons.get(zip_code);
        new DownloadImages().execute();
    }
    // to be taken out once I get picture.
    private void assembleKeys(){
        for ( String key : details.keySet() ) {
            keys.add(key);
        }
    }

    private void populateMaps(){

        for (Map.Entry<String, HashMap<String, String>> entry : RepsInfo.getMap().entrySet()) {
            String rep_name = entry.getKey();
            String position = getFullTitle(RepsInfo.getValue(rep_name, "position"))
                              + " (" + RepsInfo.getValue(rep_name, "state") + ")";
            Bitmap image = StringToBitMap(RepsInfo.getValue(rep_name, "image"));
            repsNames.add(rep_name);
            pos.add(position);
            photos.add(image);
            email.add(RepsInfo.getValue(rep_name, "email"));
            website.add(RepsInfo.getValue(rep_name, "website"));
            tweets.add(RepsInfo.getValue(rep_name, "twitter_id"));
        }
    }

    private String getFullTitle(String abbr) {
        if (abbr.toLowerCase().equals("rep")) {
            return "Representative";
        }
        return "Senator";
    }

    private Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    /* Grabs all the images and  */
    private class DownloadImages extends AsyncTask<Void, Void, String> {
        @Override
        protected String doInBackground(Void... arg0) {
            for (Map.Entry<String, HashMap<String, String>> entry : RepsInfo.getMap().entrySet()) {
                String rep_name = entry.getKey();
                String bioguide_id = RepsInfo.getValue(rep_name, "bioguide_id");
                Bitmap m = grabImage(assembleUrl(bioguide_id));
                String bitmap = BitMapToString(grabImage(assembleUrl(bioguide_id)));
                RepsInfo.add(rep_name, "image", bitmap);
            }
            return "";
        }

        protected Bitmap grabImage(String src) {
            try {
                URL url = new URL(src);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setDoInput(true);
                connection.connect();
                InputStream input = connection.getInputStream();
                Bitmap myBitmap = BitmapFactory.decodeStream(input);
                return myBitmap;
            } catch (IOException e) {
                // Log exception
                return null;
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            final ListView repsList = (ListView) findViewById(R.id.rep_view);
            assembleKeys();
            populateMaps();
            adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.list_view, repsNames);
            final RepsAdapter adapter = new RepsAdapter(DisplayRepsActivity.this, repsNames, pos,
                                                photos, zip_code, email, website, tweets);
            repsList.setAdapter(adapter);
        }
    }

    /* Given a bitmap, converts it to a string to put inside the hashmap. */
    public String BitMapToString(Bitmap bitmap){
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
        byte [] b=baos.toByteArray();
        String temp= Base64.encodeToString(b, Base64.DEFAULT);
        return temp;
    }

    /* Assembles the image url links */
    protected String assembleUrl(String bioid){
        image_path = "https://theunitedstates.io/images/congress/original/" +
                     bioid  + ".jpg";
        return image_path;
    }

}


