package sbl.com.informedcitizen.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.fragments.MyAlertDialogFragment;
import sbl.com.informedcitizen.helpers.APIclient;
import sbl.com.informedcitizen.models.Contact;

//import io.realm.RealmList;
//import io.realm.Realm;



public class SearchActivity extends FragmentActivity {

    private static final long CACHE_WAIT = 1500;
    EditText stateET;
    FragmentManager supportFM;
    ProgressBar pb;
    TextView waitTV;

    ArrayList<Contact> athruh;
    ArrayList<Contact> ithruq;
    ArrayList<Contact> rthruz;

    //Realm realm;

    private static final int REQUEST_CODE = 20;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        // REQUEST_CODE is defined above
        if (resultCode == RESULT_OK && requestCode == REQUEST_CODE) {
            /*Extract name value from result extras
            String name = data.getExtras().getString("name");
            // Toast the name to display temporarily on screen
            Toast.makeText(this, name, Toast.LENGTH_SHORT).show();*/
            pb.setVisibility(View.GONE);
            waitTV.setVisibility(View.GONE);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        /*try {
            realm = new Realm(getFilesDir());
        }
        catch(Exception e) {
            e.printStackTrace();
        }*/
        pb = (ProgressBar)findViewById(R.id.progressSpinner);
        stateET = (EditText)findViewById(R.id.stateET);
        waitTV = (TextView) findViewById(R.id.waitMessage);
        supportFM = getSupportFragmentManager();
        athruh = new ArrayList<Contact>();
        ithruq = new ArrayList<Contact>();
        rthruz = new ArrayList<Contact>();
    }


    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        /*if (id == R.id.action_settings) {
            return true;
        }*/
        return super.onOptionsItemSelected(item);
    }

    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();

        if(activeNetworkInfo == null)
            return false;
        boolean isConnected = activeNetworkInfo.isConnectedOrConnecting();
        if(!isConnected)
            return false;
        return true;
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        hideKeyboard();
        // TODO Auto-generated method stub
        return super.onTouchEvent(event);
    }


    public void hideKeyboard() {
        stateET.clearFocus();
        // hide soft keyboard upon search click
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(stateET.getWindowToken(), 0);
    }


    public void onSearchClick(View v) {
        hideKeyboard();
        final String enteredState = stateET.getText().toString();
        /* if internet connection is available, load progress bar and launch list activity
           otherwise show error diaog
         */
        if(!isNetworkAvailable()) {

            //RealmList<Contact> cachedContacts = getCachedContacts(enteredState);
            List<Contact> cachedContacts = Contact.getCachedContactsByState(enteredState);
            //nothing cached for this state
            if(cachedContacts.size() == 0) {
                MyAlertDialogFragment noConnDialog = MyAlertDialogFragment.newInstance("No Internet Connection",
                        "Connect your device to the internet to search");
                noConnDialog.show(supportFM, "no_conn_dialog");
            }
            else {
                pb.setVisibility(View.VISIBLE);
                waitTV.setText("Getting from cache....");
                waitTV.setVisibility(View.VISIBLE);

                for (Contact cachedContact : cachedContacts) {
                    String[] nameParts = cachedContact.getName().split(" ");
                    String lastname = nameParts[nameParts.length - 1];

                    // A thru H
                    if (lastname.compareToIgnoreCase("I") < 0)
                        athruh.add(cachedContact);
                        // bigger than H, but less than R (I-Q)
                    else if (lastname.compareToIgnoreCase("R") < 0)
                        ithruq.add(cachedContact);
                    else
                        rthruz.add(cachedContact);
                }

                Collections.sort(athruh);
                Collections.sort(ithruq);
                Collections.sort(rthruz);

                final Handler handler = new Handler();
                // Create and start a new Thread
                new Thread(new Runnable() {
                    public void run() {
                        try{
                            Thread.sleep(CACHE_WAIT);

                            Intent i = new Intent(SearchActivity.this, ListActivity.class);
                            i.putExtra("first", athruh);
                            i.putExtra("second", ithruq);
                            i.putExtra("third", rthruz);
                            i.putExtra("state", enteredState);

                            startActivityForResult(i, REQUEST_CODE);
                        }
                        catch (Exception e) { } // Just catch the InterruptedException

                        // Now we use the Handler to post back to the main thread
                        handler.post(new Runnable() {
                            public void run() {
                                // Set the View's visibility back on the main UI Thread
                                pb.setVisibility(View.INVISIBLE);
                                waitTV.setVisibility(View.GONE);
                            }
                        });
                    }
                }).start();
                // this runs before onCreate of next activity
                //pb.setVisibility(View.GONE);
                //waitTV.setVisibility(View.GONE);
            }
        }
        else {
            pb.setVisibility(View.VISIBLE);
            // if you fetch from cache, then connect to the internet make sure the right msg shows
            waitTV.setText("Finding Legislators....");
            waitTV.setVisibility(View.VISIBLE);


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
                    public void onSuccess(int statusCode, Header[] headers, JSONObject contactsJson) {
                        // get rid of these and test back button press
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        ArrayList<Contact> contacts = Contact.fromJSON(contactsJson);

                        try {
                            // clear existing cache entries for this state and add the new one
                            /*RealmList<Contact> existingStateEntries = getCachedContacts(enteredState);

                            if(existingStateEntries != null)
                                existingStateEntries.clear();*/
                            List<Contact> existingStateEntries = Contact.getCachedContactsByState(enteredState);
                            if(existingStateEntries.size() != 0)
                                Contact.clearCachedContactsByState(enteredState);
                        }
                        catch(Exception e) {
                            e.printStackTrace();
                        }

                        for (Contact currLeg : contacts) {
                            currLeg.setState(enteredState);
                            try {
                                /*realm.beginWrite();
                                Contact cachedContact = realm.create(Contact.class);*/

                                Contact cachedContact = new Contact();
                                cachedContact.setState(enteredState);
                                cachedContact.setCandID(currLeg.getCandID());
                                cachedContact.setName(currLeg.getName());
                                cachedContact.setChamber(currLeg.getChamber());
                                cachedContact.setParty(currLeg.getParty());
                                cachedContact.setAddress(currLeg.getAddress());
                                cachedContact.setPhoneNo(currLeg.getPhoneNo());
                                cachedContact.setFacebook(currLeg.getFacebook());
                                cachedContact.setTwitter(currLeg.getTwitter());
                                cachedContact.setYoutube(currLeg.getYoutube());
                                cachedContact.save();
                                //realm.commit();
                            }
                            catch(Exception e) {
                                e.printStackTrace();
                            }
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
                        i.putExtra("state", enteredState);
                        startActivity(i);
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable arg0, JSONArray arg1) {
                        super.onFailure(statusCode, headers, arg0, arg1);
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "getLegislators failed with JSONArray arg!", Toast.LENGTH_LONG).show();
                    }

                    // If it fails it fails here where arg1 is the error message(dev inactive)
                    @Override
                    public void onFailure(int statusCode, Header[] headers, String responseStr, Throwable arg0) {
                        super.onFailure(statusCode, headers, responseStr, arg0);

                        MyAlertDialogFragment notFoundDialog = MyAlertDialogFragment.newInstance("Legislators Unavailable",
                                "The service could not find legislators in the specified state!");
                        notFoundDialog.show(supportFM, "enter_state_dialog");
                        pb.setVisibility(View.GONE);
                        waitTV.setVisibility(View.GONE);
                        Toast.makeText(getApplicationContext(),
                                "getLegislators failed with String arg!",
                                Toast.LENGTH_LONG).show();
                    }


                });
            }
        }
    }

    /*private RealmList<Contact> getCachedContacts(String enteredState) {
        return realm.where(Contact.class).equalTo("state", enteredState).findAll();
    }*/




    @Override
    public void onResume() {
        super.onResume();
        athruh.clear();
        ithruq.clear();
        rthruz.clear();
    }
}