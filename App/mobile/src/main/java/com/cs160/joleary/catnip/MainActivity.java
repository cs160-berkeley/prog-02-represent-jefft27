package com.cs160.joleary.catnip;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

public class MainActivity extends Activity {

    private RadioButton zipCode;
    private RadioButton currLocation;
    private LinearLayout field;
    private RadioGroup radioGroup;
    private Button btn;
    private String zipCodeEntered;
    private EditText eTxt;
    private boolean usingCurrLoc;
    private String usedToBe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.v("Mobile MainActivity", "whatsup");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btn = (Button) findViewById(R.id.btn);
        zipCode = (RadioButton) findViewById(R.id.zipCode);
        currLocation = (RadioButton) findViewById(R.id.currLocation);
        radioGroup = (RadioGroup) findViewById(R.id.whereAreYou);
        field = (LinearLayout) findViewById(R.id.typeZipCodeField);
        field.setVisibility(LinearLayout.GONE);

        eTxt = (EditText) findViewById(R.id.findZipCode);

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
                if (field.getVisibility() == View.VISIBLE) {
                    eTxt = (EditText) findViewById(R.id.findZipCode);
                    zipCodeEntered = eTxt.getText().toString();
                }
                if (usingCurrLoc) {
                    zipCodeEntered = "94801";
                }
                if (radioGroup.getCheckedRadioButtonId() != -1 && zipCodeEntered.length() == 5) {
                    DummyInfo info = new DummyInfo(zipCodeEntered);
                    Intent sendToMobile = new Intent(getBaseContext(), DisplayRepsActivity.class);

                    if (!usingCurrLoc && !zipCodeEntered.equals("94801")) { // this is the line to fix.
                        usedToBe = zipCodeEntered;
                        zipCodeEntered = "10001";
                    }
                    if (usedToBe != null) {
                        sendToMobile.putExtra("ZIPCODE", usedToBe);
                    } else {
                        sendToMobile.putExtra("ZIPCODE", zipCodeEntered);

                    }
                    sendToMobile.putExtra("dict", info.getMap());

                    Intent sendToWear = new Intent(getBaseContext(), PhoneToWatchService.class);
                    sendToWear.putExtra("CAT_NAME", zipCodeEntered);
                    startService(sendToWear);

                    Log.d("NOTICE:", "sent to phone to watch service class");

                    startActivity(sendToMobile);
                    usedToBe = null;
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
}
