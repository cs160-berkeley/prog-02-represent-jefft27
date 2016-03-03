package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.HashMap;

public class DetailedInfoActivity extends Activity {

    private HashMap<String, HashMap<String, String>> persons_info;
    private HashMap<String, String> representative;
    private ImageView image, btn;
    private TextView title, t0, t1, t2, t3, t4, t5, t6, t7;
    private LinearLayout l1;
    private boolean orientation;
    private String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_info);
        orientation = true;

        image = (ImageView) findViewById(R.id.person);
        btn = (ImageView) findViewById(R.id.moreInfoBtn);
        title = (TextView) findViewById(R.id.title);
        t0 = (TextView) findViewById(R.id.position);
        t1 = (TextView) findViewById(R.id.party);
        t2 = (TextView) findViewById(R.id.email);
        t3 = (TextView) findViewById(R.id.website);
        t4 = (TextView) findViewById(R.id.tweet);
        t5 = (TextView) findViewById(R.id.end_of_term);
        t6 = (TextView) findViewById(R.id.committees);
        t7 = (TextView) findViewById(R.id.bills_passed);
        l1 = (LinearLayout) findViewById(R.id.info);

        l1.setVisibility(LinearLayout.GONE);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            DummyInfo d = new DummyInfo(extras.getString("94801"));
            representative = d.getPersons(extras.getString("name"));
            if (extras.getString("getInfo") != null) {
                orientation = false;
                btn.setImageResource(getResources().getIdentifier("up_arrow", "drawable", getPackageName()));
                l1.setVisibility(LinearLayout.VISIBLE);
            }
        }

        String img_src = representative.get("picture");
        image.setImageResource(getResources().getIdentifier(img_src, "drawable", getPackageName()));
        title.setText(representative.get("name"));
        String t0_text = t0.getText().toString();
        String t1_text = t1.getText().toString();
        String t2_text = t2.getText().toString();
        String t3_text = t3.getText().toString();
        String t4_text = t4.getText().toString();
        String t5_text = t5.getText().toString();
        String t6_text = t6.getText().toString();
        String t7_text = t7.getText().toString();
        t0.setText(t0_text + " " + representative.get("position"));
        t1.setText(t1_text + " " + representative.get("party"));
        t2.setText(t2_text + " " + representative.get("email"));
        t3.setText(t3_text + " " + representative.get("website"));
        String tweet = "The same day POTUS announces a \"plan\" to close #Guantanamo Bay, " +
                "a former detainee is arrested for links to #ISIS. http://1.usa.gov/1XL0Ddq ";
        t4.setText(tweet);
        t5.setText(t5_text + " " + representative.get("end_of_term"));
        String committees_str = "  Intelligence \n" +
                "  Appropriations \n" +
                "  Foreign Trade";
        t6.setText(t6_text + ":\n" + committees_str);
        String recent_bills = "  Abortion, 20 week ban - 5/13/15\n" +
                "  Cybersecurity - 7/13/15\n" +
                "  Health care law, repeal - 9/22/15";
        t7.setText(t7_text + "\n" + recent_bills + "\n");

        btn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (orientation) {
                    btn.setImageResource(getResources().getIdentifier("up_arrow", "drawable", getPackageName()));
                    orientation = false;
                    l1.setVisibility(LinearLayout.VISIBLE);
                } else {
                    btn.setImageResource(getResources().getIdentifier("down_arrow", "drawable", getPackageName()));
                    orientation = true;
                    l1.setVisibility(LinearLayout.GONE);
                }
            }
        });
    }

    public void openEmail(View v){
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL  , new String[]{"recipient@example.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "subject of email");
        i.putExtra(Intent.EXTRA_TEXT   , "body of email");
        try {
            startActivity(Intent.createChooser(i, "Send mail..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    public void openWebBrowser(View v){
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.google.com"));
        startActivity(browserIntent);
    }
}