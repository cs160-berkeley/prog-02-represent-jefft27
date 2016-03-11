package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

public class DetailedInfoActivity extends Activity {

    private HashMap<String, String> representative;
    private ImageView image, btn;
    private TextView title, t4, separator, separator0;
    private LinearLayout l1, whole_page;
    private Bitmap img;
    private String rep_name;
    private TextView header;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);

        header = (TextView) findViewById(R.id.actionbar);
        header.setBackgroundColor(Color.parseColor("#2F80ED"));

        initializeUI();
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            rep_name = extras.getString("name");
            representative = RepsInfo.getAllInfo(extras.getString("name"));
        }

        setFields();

        String party = RepsInfo.getValue(rep_name, "party");
        whole_page.setBackgroundColor(Color.parseColor("#EDECEC"));
        separator.setBackgroundColor(Color.parseColor("#EDECEC"));
        separator0.setBackgroundColor(Color.parseColor("#EDECEC"));

        final ListView moreStuffList = (ListView) findViewById(R.id.detailed_view);
        ArrayList<String> lst = RepsInfo.getValueForInfo(rep_name, "committees");
        final DetailedInfoAdapter adapter = new DetailedInfoAdapter(this, lst);
        moreStuffList.setAdapter(adapter);

        final ListView moreStuffBills = (ListView) findViewById(R.id.detailed_bill_view);
        ArrayList<String> bills_lst = RepsInfo.getValueForInfo(rep_name, "bills");
        final DetailedInfoAdapter adapter_bills = new DetailedInfoAdapter(this, bills_lst);
        moreStuffBills.setAdapter(adapter_bills);
    }

    public void openEmail(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT, "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openWebBrowser(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(representative.get("website")));
        startActivity(browserIntent);
    }

    protected void initializeUI() {
        image = (ImageView) findViewById(R.id.person);
        title = (TextView) findViewById(R.id.title);
        t4 = (TextView) findViewById(R.id.end_of_term);
        separator = (TextView) findViewById(R.id.separator);
        separator0 = (TextView) findViewById(R.id.separator0);
    }

    protected void setFields() {
        String img_src = representative.get("picture");
        System.out.println("rep_name is: " + rep_name);
        image.setImageBitmap(StringToBitMap(RepsInfo.getValue(rep_name, "image")));
        if (representative.get("party").equals("D")) {
            title.setTextColor(Color.parseColor("#2F80ED"));
        } else if (representative.get("party").equals("R")) {
            title.setTextColor(Color.parseColor("#E53535"));
        }
        whole_page = (LinearLayout) findViewById(R.id.view);
        title.setText(representative.get("position")
                      + " " + representative.get("name"));
        t4.setText("End of term: " + representative.get("end_of_term"));
    }

    private String getFullTitle(String abbr) {
        if (abbr.toLowerCase().equals("rep")) {
            return "Representative";
        }
        return "Senator";
    }

    private String getFullParty(String abbr) {
        if (abbr.toLowerCase().equals("d")) {
            return "Democrat";
        } else if (abbr.toLowerCase().equals("r")) {
            return "Republican";
        }
        return "Independent";
    }

    public Bitmap StringToBitMap(String encodedString){
        try {
            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
}