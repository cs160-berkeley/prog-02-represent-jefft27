package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

    private TextView mTextView;
    private Button mFeedBtn;
    private ImageView rep;
    private GestureDetector mDetector;
    private final int MAX_CLICK_DURATION = 400;
    private final int MAX_CLICK_DISTANCE = 5;
    private long startClickTime;
    private float x1;
    private float y1;
    private float x2;
    private float y2;
    private float dx;
    private float dy;
    private int pos;
    private int [] reps_pics;
    private int [] reps_pics1 = {R.drawable.df_watch, R.drawable.km_watch, R.drawable.bb_watch,
                                R.drawable.presvote};
    private int [] reps_pics2 = {R.drawable.cs_watch, R.drawable.kg_watch, R.drawable.lz_watch,
                                R.drawable.presvote2};

    private boolean which_map;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        pos = 0;
        rep =  (ImageView) findViewById(R.id.image);

        Toast.makeText(getBaseContext(), "connected from phone to watch!", Toast.LENGTH_SHORT).show();

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            if (extras.getString("map_to_use").equals("94801")) {
                which_map = true;
                reps_pics = reps_pics1;
                rep.setImageResource(reps_pics[pos]);
            } else {
                which_map = false;
                reps_pics = reps_pics2;
                rep.setImageResource(reps_pics[pos]);
            }
        }

        rep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
//                Toast.makeText(getBaseContext(), "Tap", Toast.LENGTH_SHORT).show();
                Intent sendIntent = new Intent(getBaseContext(), WatchToPhoneService.class);
                sendIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                Globals.repName = getName();
                if (Globals.repName.length() > 0) {
                    startService(sendIntent);
//                    Toast.makeText(getBaseContext(), "Done", Toast.LENGTH_SHORT).show();
                }

            }
        });

        rep.setOnTouchListener(new OnSwipeTouchListener() {
            @Override
            public boolean onSwipeRight() {
                pos = (pos + 1) % (reps_pics.length);
//                Toast.makeText(MainActivity.this, "right", Toast.LENGTH_SHORT).show();
                rep.setImageResource(reps_pics[pos]);

                return true;
            }

            @Override
            public boolean onSwipeLeft() {
                Log.d("Swipe left, pos is: ", "" + pos);
                pos = (pos - 1) % (reps_pics.length);
                if (pos < 0) {
                    pos = reps_pics.length - 1;
                }
                Log.d("after decrement: ", "" + pos);
//                Toast.makeText(MainActivity.this, "left", Toast.LENGTH_SHORT).show();
                rep.setImageResource(reps_pics[pos]);
                return true;
            }

        });
    }
    
    private String getName() {
        if (which_map) {
            if (pos == 0) {
                return "Dianne Feinstein";
            } else if (pos == 1) {
                return "Kevin McCarthy";
            } else if (pos == 2) {
                return "Barbara Boxer";
            }
        } else {
            if (pos == 0) {
                return "Chuck Schumer";
            } else if (pos == 1) {
                return "Kristen Gillibrand";
            } else if (pos == 2) {
                return "Lee Zeldin";
            }
        }

        return "";
    }
}
