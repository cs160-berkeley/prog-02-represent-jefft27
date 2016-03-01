package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;

public class DisplayRepsActivity extends Activity {

    private TextView t1, t2, t3, pos1, pos2, pos3;
    private ArrayList<String> keys = new ArrayList<String>();
    private HashMap<String, HashMap<String, String>> details;
    private LinearLayout p1, p2, p3;
    private ImageView i1,i2,i3;
    private String zip_code;
    HashMap<String,HashMap<String, HashMap<String, String>>> persons;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_reps);
        t1 = (TextView) findViewById(R.id.name1);
        t2 = (TextView) findViewById(R.id.name2);
        t3 = (TextView) findViewById(R.id.name3);
        i1 = (ImageView) findViewById(R.id.image1);
        i2 = (ImageView) findViewById(R.id.image2);
        i3 = (ImageView) findViewById(R.id.image3);
        p1 = (LinearLayout) findViewById(R.id.person1);
        p2 = (LinearLayout) findViewById(R.id.person2);
        p3 = (LinearLayout) findViewById(R.id.person3);
        pos1 = (TextView) findViewById(R.id.pos1);
        pos2 = (TextView) findViewById(R.id.pos2);
        pos3 = (TextView) findViewById(R.id.pos3);

        Log.d("savedInstanceState is: ", savedInstanceState + "");
        if (savedInstanceState != null) {
            zip_code = savedInstanceState.getString("ZIPCODE");
            persons = (HashMap<String, HashMap<String,HashMap<String, String>>>) savedInstanceState.getSerializable("people");

        } else {
            Bundle extras = getIntent().getExtras();
            persons = (HashMap<String, HashMap<String,HashMap<String, String>>>) getIntent().getSerializableExtra("dict");
            zip_code = extras.getString("ZIPCODE");
        }

        setTitle(zip_code);
        if (!zip_code.equals("94801")) {
            zip_code = "10001";
        }
        details = persons.get(zip_code);

        assembleKeys();
        setTextViews();
        setImages();
        Intent sendToMobile = new Intent(getBaseContext(), DetailedInfoActivity.class);

        p1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent sendInfo = new Intent(getBaseContext(), DetailedInfoActivity.class);
                sendInfo.putExtra("zip", zip_code);
                sendInfo.putExtra("name", keys.get(0));
                startActivity(sendInfo);
            }
        });

        p2.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sendInfo = new Intent(getBaseContext(), DetailedInfoActivity.class);
                sendInfo.putExtra("zip",zip_code);
                sendInfo.putExtra("name", keys.get(1));
                startActivity(sendInfo);
            }
        });

        p3.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent sendInfo = new Intent(getBaseContext(), DetailedInfoActivity.class);
                sendInfo.putExtra("zip",zip_code);
                sendInfo.putExtra("name", keys.get(2));
                startActivity(sendInfo);
            }
        });

    }
    @Override
    protected void onSaveInstanceState(Bundle icicle) {
        super.onSaveInstanceState(icicle);
        icicle.putString("ZIPCODE", getIntent().getExtras().getString("ZIPCODE"));
        icicle.putSerializable("people", details);
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        zip_code = savedInstanceState.getString("ZIPCODE");
        persons = (HashMap<String, HashMap<String,HashMap<String, String>>>) savedInstanceState.getSerializable("people");
    }

    private void assembleKeys(){
        for ( String key : details.keySet() ) {
            keys.add(key);
        }
    }

    private void setTextViews(){
        t1.setText(keys.get(0));
        t2.setText(keys.get(1));
        t3.setText(keys.get(2));
        pos1.setText(details.get(keys.get(0)).get("position"));
        pos2.setText(details.get(keys.get(1)).get("position"));
        pos3.setText(details.get(keys.get(2)).get("position"));
        Log.d("boxer is: ", keys.get(2));
    }

    private void setImages(){
        String i1_src = details.get(keys.get(0)).get("picture");
        String i2_src = details.get(keys.get(1)).get("picture");
        String i3_src = details.get(keys.get(2)).get("picture");
        i1.setImageResource(getResources().getIdentifier(i1_src, "drawable", getPackageName()));
        i2.setImageResource(getResources().getIdentifier(i2_src, "drawable", getPackageName()));
        i3.setImageResource(getResources().getIdentifier(i3_src, "drawable", getPackageName()));
    }
}


