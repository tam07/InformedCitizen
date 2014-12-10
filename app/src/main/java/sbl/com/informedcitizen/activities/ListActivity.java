package sbl.com.informedcitizen.activities;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import java.util.ArrayList;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.adapters.ContactsAdapter;
import sbl.com.informedcitizen.fragments.ListFragment;
import sbl.com.informedcitizen.fragments.MyAlertDialogFragment;
import sbl.com.informedcitizen.helpers.Constants;
import sbl.com.informedcitizen.helpers.SherlockTabListener;
import sbl.com.informedcitizen.models.Contact;

public class ListActivity extends SherlockFragmentActivity implements ContactsAdapter.OnContactSelectedListener {
    ArrayList<Contact> firstList;
    ArrayList<Contact> secondList;
    ArrayList<Contact> thirdList;


    public ArrayList<Contact> getFirstList() {
        return firstList;
    }


    public ArrayList<Contact> getSecondList() {
        return secondList;
    }


    public ArrayList<Contact> getThirdList() {
        return thirdList;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // to handle back btn press and make sure pb in SearchActivity disappears
        Intent data = new Intent();
        // Activity finished ok, return the data
        setResult(RESULT_OK, data);
        setContentView(R.layout.activity_list);

        String actionbarTitle = (String)getIntent().getStringExtra("state").toUpperCase() +
                                " Legislators";
        getActionBar().setTitle(actionbarTitle);
        firstList = (ArrayList<Contact>) getIntent().getSerializableExtra("first");
        secondList = (ArrayList<Contact>) getIntent().getSerializableExtra("second");
        thirdList = (ArrayList<Contact>) getIntent().getSerializableExtra("third");

        int selectIndex = 0;
        if (savedInstanceState != null)
            selectIndex = savedInstanceState.getInt("tIndex");
        setupTabs(selectIndex);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getSupportMenuInflater();
        inflater.inflate(R.menu.list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void setupTabs(int index) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayShowTitleEnabled(true);

        ActionBar.Tab tabFirst = actionBar
                .newTab()
                .setText("A-H")
                .setTabListener(
                        new SherlockTabListener<ListFragment>(R.id.fragment_placeholder, this,
                                "AH", ListFragment.class)
                );

        actionBar.addTab(tabFirst);
        //actionBar.selectTab(tabFirst);


        ActionBar.Tab tabSecond = actionBar
                .newTab()
                .setText("I-Q")
                .setTabListener(
                        new SherlockTabListener<ListFragment>(R.id.fragment_placeholder, this,
                                "IQ", ListFragment.class)
                );

        actionBar.addTab(tabSecond);

        ActionBar.Tab tabThird = actionBar
                .newTab()
                .setText("R-Z")
                .setTabListener(
                        new SherlockTabListener<ListFragment>(R.id.fragment_placeholder, this,
                                "RZ", ListFragment.class)
                );

        actionBar.addTab(tabThird);

        actionBar.setSelectedNavigationItem(index);
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        int selectedTabIndex = getSupportActionBar().getSelectedNavigationIndex();
        savedInstanceState.putInt("tIndex", selectedTabIndex);
    }


    private Boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnectedOrConnecting();
    }


    @Override
    public void onContactClicked(Contact currContact) {
        boolean connected = isNetworkAvailable();
        if(connected) {
            Intent detailIntent = new Intent(this, DetailActivity.class);
            detailIntent.putExtra(Constants.DETAIL_INTENT_KEY, currContact);
            startActivity(detailIntent);
        }
        else {
            MyAlertDialogFragment noConnDialog = MyAlertDialogFragment.newInstance("No Internet Connection",
                    "Connection needed for legislator details");
            noConnDialog.show(getSupportFragmentManager(), "no_connection_dialog");
        }
    }


    public void onHomeClick(MenuItem mi) {
       // go back
       /* finish(); inconsistent with pressing home in the DetailActivity, where the edit text is
          cleared(like it should be)
        */
        Intent i = new Intent(this, SearchActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(i);
    }


/*
    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        loadDetailPB.setVisibility(View.GONE);
        loadDetailMsg.setVisibility(View.GONE);
    }*/
}
