package sbl.com.informedcitizen.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.fragments.MyAlertDialogFragment;
import sbl.com.informedcitizen.helpers.APIclient;
import sbl.com.informedcitizen.models.Contact;


public class SearchActivity extends FragmentActivity {

    EditText stateET;
    FragmentManager supportFM;
    ProgressBar pb;
    TextView waitTV;

    ArrayList<Contact> athruh;
    ArrayList<Contact> ithruq;
    ArrayList<Contact> rthruz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        pb = (ProgressBar)findViewById(R.id.progressSpinner);
        stateET = (EditText)findViewById(R.id.stateET);
        waitTV = (TextView) findViewById(R.id.waitMessage);
        supportFM = getSupportFragmentManager();
        athruh = new ArrayList<Contact>();
        ithruq = new ArrayList<Contact>();
        rthruz = new ArrayList<Contact>();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    public void onSearchClick(View v) {
        stateET.clearFocus();

        // hide soft keyboard upon search click
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(stateET.getWindowToken(), 0);


        /* if internet connection is available, load progress bar and launch list activity
           otherwise show error diaog
         */
        if(!isNetworkAvailable()) {
            MyAlertDialogFragment noConnDialog = MyAlertDialogFragment.newInstance("No Intenet Connection",
                    "Connect your device to the internet to search");
            noConnDialog.show(supportFM, "enter_state_dialog");
        }
        else {
            pb.setVisibility(View.VISIBLE);
            waitTV.setVisibility(View.VISIBLE);

            String enteredState = stateET.getText().toString();
            if (enteredState.equals("")) {
                pb.setVisibility(View.GONE);
                waitTV.setVisibility(View.GONE);
                MyAlertDialogFragment enterStateDialog = MyAlertDialogFragment.newInstance("Input Error",
                        "Enter a state abbreviation to find legislators");
                enterStateDialog.show(supportFM, "enter_state_dialog");
            }
            else {
                APIclient.getLegislators(this, enteredState, new JsonHttpResponseHandler() {

                    @Override
                    public void onSuccess(JSONObject contactsJson) {
                        // get rid of these and test back button press
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        ArrayList<Contact> contacts = Contact.fromJSON(contactsJson);
                        for (Contact currLeg : contacts) {
                            String fullname = currLeg.getName();
                            String[] tokens = fullname.split(" ");
                            String lastname = tokens[tokens.length - 1];

                            // A thru H
                            if (lastname.compareToIgnoreCase("I") < 0)
                                athruh.add(currLeg);
                                // bigger than H, but less than R (I-Q)
                            else if (lastname.compareToIgnoreCase("R") < 0)
                                ithruq.add(currLeg);
                            else
                                rthruz.add(currLeg);
                        }
                        Collections.sort(athruh);
                        Collections.sort(ithruq);
                        Collections.sort(rthruz);

                        Intent i = new Intent(SearchActivity.this, ListActivity.class);
                        i.putExtra("first", athruh);
                        i.putExtra("second", ithruq);
                        i.putExtra("third", rthruz);

                        startActivity(i);
                    }

                    @Override
                    public void onFailure(Throwable arg0, JSONArray arg1) {
                        super.onFailure(arg0, arg1);
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "getLegislators failed with JSONArray 2nd arg!", Toast.LENGTH_LONG).show();
                    }

                    // If it fails it fails here where arg1 is the error message(dev inactive)
                    @Override
                    public void onFailure(Throwable arg0, String arg1) {
                        super.onFailure(arg0, arg1);
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "getLegislators failed with String 2nd arg!",
                                Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(Throwable arg0, JSONObject arg1) {
                        super.onFailure(arg0, arg1);
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "getLegislators failed with JSONObject 2nd arg!", Toast.LENGTH_LONG).show();
                    }
                });
            }
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        athruh.clear();
        ithruq.clear();
        rthruz.clear();
    }
}