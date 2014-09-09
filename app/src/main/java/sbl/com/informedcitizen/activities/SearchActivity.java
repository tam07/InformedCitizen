package sbl.com.informedcitizen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
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
    ArrayList<Contact> athruh;
    ArrayList<Contact> ithruq;
    ArrayList<Contact> rthruz;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        stateET = (EditText)findViewById(R.id.stateET);
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


    public void onSearchClick(View v) {
        String enteredState = stateET.getText().toString();
        if(enteredState.equals(""))
        {
            MyAlertDialogFragment enterStateDialog = MyAlertDialogFragment.newInstance("Input Error",
                                                     "Enter a state abbreviation to find legislators");
            enterStateDialog.show(supportFM, "enter_state_dialog");
        }
        APIclient.getLegislators(this, enteredState, new JsonHttpResponseHandler() {

            @Override
            public void onSuccess(JSONObject contactsJson) {
                ArrayList<Contact> contacts = Contact.fromJSON(contactsJson);
                for(Contact currLeg: contacts) {
                    String fullname = currLeg.getName();
                    String[] tokens = fullname.split(" ");
                    String lastname = tokens[tokens.length-1];

                    // A thru H
                    if(lastname.compareToIgnoreCase("I") < 0)
                        athruh.add(currLeg);
                    // bigger than H, but less than R (I-Q)
                    else if(lastname.compareToIgnoreCase("R") < 0)
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
                Toast.makeText(getApplicationContext(),
                        "Failed with JSONArray 2nd arg!", Toast.LENGTH_LONG).show();
            }

            // If it fails it fails here where arg1 is the error message(dev inactive)
            @Override
            public void onFailure(Throwable arg0, String arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getApplicationContext(),
                        "getTrailerUrl failed with String 2nd arg!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable arg0, JSONObject arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getApplicationContext(),
                        "Failed with JSONObject 2nd arg!", Toast.LENGTH_LONG).show();
            }

        });
    }

    @Override
    public void onResume() {
        super.onResume();
        athruh.clear();
        ithruq.clear();
        rthruz.clear();
    }
}