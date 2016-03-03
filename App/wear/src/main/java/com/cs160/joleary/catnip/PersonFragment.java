package com.cs160.joleary.catnip;


import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
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
public class PersonFragment extends Fragment implements View.OnClickListener{

    private ImageView imgview_projectimage;
    private TextView name, party;
    private View mView;
    private LinearLayout content;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private static final String ARG_PARAM3 = "param3";
    private int id;

    public static PersonFragment newInstance(String param1, String param2, int img_id) {
        PersonFragment f = new PersonFragment();
        // Supply sensorType as an argument
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        args.putInt(ARG_PARAM3, img_id);
        f.setArguments(args);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mView = inflater.inflate(R.layout.myactivity, container, false);
        Bundle b = getArguments();
        id = b.getInt(ARG_PARAM3);
        String a = b.getString(ARG_PARAM1);
        getUiInitialization(b);
        return mView;
    }

    public void getUiInitialization(Bundle b) {
        imgview_projectimage=(ImageView) mView.findViewById(R.id.person);
        name = (TextView) mView.findViewById(R.id.name);
        party = (TextView) mView.findViewById(R.id.party);
        content = (LinearLayout) mView.findViewById(R.id.info);
        if (b.getString(ARG_PARAM1).equals("presvote") ||
                b.getString(ARG_PARAM1).equals("presvote2")) {
            name.setText("2012 Presidential Vote");
        } else {
            name.setText(b.getString(ARG_PARAM1));
            party.setText(b.getString(ARG_PARAM2));
        }
        imgview_projectimage.setImageResource(id);
        imgview_projectimage.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Log.v("PersonFragment", "name is: " + name.getText().toString());
        Globals.repName = name.getText().toString();
        if (!Globals.repName.equals("2012 Presidential Vote")) {
            getActivity().startService(new Intent(getActivity(), WatchToPhoneService.class));
        }
    }
}
