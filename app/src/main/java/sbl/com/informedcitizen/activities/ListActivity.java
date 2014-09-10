package sbl.com.informedcitizen.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import java.util.ArrayList;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.fragments.ListFragment;
import sbl.com.informedcitizen.helpers.SherlockTabListener;
import sbl.com.informedcitizen.models.Contact;

public class ListActivity extends SherlockFragmentActivity {
    ArrayList<Contact> firstList;
    ArrayList<Contact> secondList;
    ArrayList<Contact> thirdList;

    public ProgressBar getLoadDetailPB() {
        return loadDetailPB;
    }

    public TextView getLoadDetailMsg() {
        return loadDetailMsg;
    }

    ProgressBar loadDetailPB;
    TextView loadDetailMsg;


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
        setContentView(R.layout.activity_list);

        loadDetailPB = (ProgressBar)findViewById(R.id.detailPB);
        loadDetailMsg = (TextView)findViewById(R.id.detailWaitMsg);

        firstList = (ArrayList<Contact>)getIntent().getSerializableExtra("first");
        secondList = (ArrayList<Contact>)getIntent().getSerializableExtra("second");
        thirdList = (ArrayList<Contact>)getIntent().getSerializableExtra("third");

        int selectIndex = 0;
        if(savedInstanceState != null)
            selectIndex = savedInstanceState.getInt("tIndex");
        setupTabs(selectIndex);
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


    @Override
    public void onActivityResult(int reqCode, int resCode, Intent data) {
        loadDetailPB.setVisibility(View.GONE);
        loadDetailMsg.setVisibility(View.GONE);
    }


}
