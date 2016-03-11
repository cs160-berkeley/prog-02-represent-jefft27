package com.cs160.joleary.catnip;

import android.app.Activity;
import android.os.Bundle;
import android.support.wearable.view.DotsPageIndicator;
import android.support.wearable.view.GridViewPager;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private String data;
    private String DEBUG_TAG = "Watch_MainActivity";
    private ArrayList<String> reps_info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        reps_info = new ArrayList<String>();
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            data = extras.getString("data");
            final String [] arr = data.split(";");
            System.out.println("data is: " + data);
            Log.v("Watch MainActivity", data);
            Log.v(DEBUG_TAG, "hello!");
            WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
            stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {

                @Override
                public void onLayoutInflated(WatchViewStub stub) {
                    final GridViewPager pager = (GridViewPager) findViewById(R.id.pager);
                    pager.setAdapter(new GridPageAdapter(MainActivity.this, getFragmentManager(), arr));

                    DotsPageIndicator indicator = (DotsPageIndicator) findViewById(R.id.indicator);
                    indicator.setPager(pager);
                }
            });
        }
    }

}
