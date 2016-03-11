package com.cs160.joleary.catnip;


import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by jefftan on 3/2/16.
 */
public class PersonFragment extends Fragment{

    private ImageView imgview_projectimage;
    private TextView title, name, party, obama_vote, romney_vote;
    private View mView;
    private LinearLayout content;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    public static PersonFragment newInstance(String param1, String param2) {
        PersonFragment f = new PersonFragment();
        // Supply sensorType as an argument
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.myactivity, container, false);
        Bundle b = getArguments();
        String a = b.getString(ARG_PARAM1);
        getUiInitialization(b);
        return mView;
    }

    public void getUiInitialization(Bundle b) {
        title = (TextView) mView.findViewById(R.id.title);
        name = (TextView) mView.findViewById(R.id.name);
        party = (TextView) mView.findViewById(R.id.party);
        content = (LinearLayout) mView.findViewById(R.id.info);
        obama_vote = (TextView) mView.findViewById(R.id.obama_vote);
        romney_vote = (TextView) mView.findViewById(R.id.romney_vote);

        name.setText(b.getString(ARG_PARAM1));
        party.setText(b.getString(ARG_PARAM2));

        if (party.getText().toString().equals("D")) {
            Log.v("PersonFragment", "set color for democrat!");
            party.setText("Democrat");
            content.setBackgroundColor(Color.parseColor("#D4E4F9"));
        } else if (party.getText().toString().equals("R")) {
            Log.v("PersonFragment", "set color for republican!");
            content.setBackgroundColor(Color.parseColor("#F5D6DB"));
            party.setText("Republican");
        }

        if (name.getText().toString().indexOf("County") > -1) {
            title.setText("2012 Presidental Vote");
            Log.v("PersonFragment", "name is: " + name);
            Log.v("PersonFragment", "party is: " + party);
            String county_state = name.getText().toString();
            String votes = party.getText().toString();
            name.setText(county_state.split(",")[0]);
            party.setText(county_state.toString().split(",")[1]);
            obama_vote.setText("Obama %: " + votes.split(";")[0]);
            romney_vote.setText("Romney %: " + votes.split(";")[1]);
        }
        content.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.v("PersonFragment", "name is: " + name.getText().toString());
                Globals.repName = name.getText().toString();
                if (Globals.repName.indexOf("County") == -1) {
                    getActivity().startService(new Intent(getActivity(), WatchToPhoneService.class));
                }
            }
        });
    }
    
    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte= Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}
