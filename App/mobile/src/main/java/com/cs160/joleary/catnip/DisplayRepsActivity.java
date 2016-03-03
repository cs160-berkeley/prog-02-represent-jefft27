package com.cs160.joleary.catnip;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayRepsActivity extends Activity {

    ArrayList<String> repsNames, pos, keys;
    ArrayAdapter<String>adapter;
    ArrayList<Integer> photos;
    private String zip_code;
    private HashMap<String,HashMap<String, HashMap<String, String>>> persons;
    private HashMap<String, HashMap<String, String>> details;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reps);

        final ListView repsList = (ListView) findViewById(R.id.rep_view);
        repsNames = new ArrayList<String>();
        pos = new ArrayList<String>();
        keys = new ArrayList<String>();
        photos = new ArrayList<Integer>();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            persons = (HashMap<String, HashMap<String, HashMap<String, String>>>) getIntent().getSerializableExtra("dict");
            zip_code = extras.getString("ZIPCODE");
        }
        setTitle(zip_code);
        if (!zip_code.equals("94801")) {
            zip_code = "10001";
        }
        details = persons.get(zip_code);
        assembleKeys();
        populateMaps();
        adapter = new ArrayAdapter<String>(this, R.layout.list_view, repsNames);

        final RepsAdapter adapter = new RepsAdapter(this, repsNames, pos, photos, zip_code);
        repsList.setAdapter(adapter);

    }

    private void assembleKeys(){
        for ( String key : details.keySet() ) {
            keys.add(key);
        }
    }

    private void populateMaps(){
        for (int i = 0; i < keys.size(); i += 1) {
            repsNames.add(keys.get(i));
            pos.add(details.get(keys.get(i)).get("position"));
            String pic = details.get(keys.get(i)).get("picture");
            int id = getResources().getIdentifier(pic, "drawable", getPackageName());
            photos.add(id);
        }
    }
}


