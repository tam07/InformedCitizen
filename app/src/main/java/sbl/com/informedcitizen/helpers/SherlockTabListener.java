package sbl.com.informedcitizen.helpers;

import android.support.v4.app.FragmentTransaction;
import android.util.Log;

import com.actionbarsherlock.app.ActionBar.Tab;
import com.actionbarsherlock.app.ActionBar.TabListener;
import com.actionbarsherlock.app.SherlockFragment;
import com.actionbarsherlock.app.SherlockFragmentActivity;

import sbl.com.informedcitizen.activities.ListActivity;
import sbl.com.informedcitizen.fragments.ListFragment;

public class SherlockTabListener<T extends SherlockFragment> implements TabListener {
    private SherlockFragment mFragment;
    private final SherlockFragmentActivity mActivity;
    private final String mTag;
    private final Class<T> mClass;
    private final int mfragmentContainerId;

    // new SherlockTabListener<SomeFragment>(R.id.flContent, this, "first", SomeFragment.class))
    public SherlockTabListener(int fragmentContainerId, SherlockFragmentActivity activity,
                               String tag, Class<T> clz) {
        mActivity = activity;
        mTag = tag;
        mClass = clz;
        mfragmentContainerId = fragmentContainerId;
    }

    public void onTabSelected(Tab tab, FragmentTransaction ft) {
        // if the fragment exists doesn't exist yet, create it and specify the section of the list
        if (mFragment == null) {
            if (mTag.equals("AH"))
                mFragment = ListFragment.newInstance("AH");
            else if (mTag.equals("IQ"))
                mFragment = ListFragment.newInstance("IQ");
            else
                mFragment = ListFragment.newInstance("RZ");
            //ft.add(mfragmentContainerId, mFragment, mTag);
           // mFragment.setRetainInstance(true);
            ft.replace(mfragmentContainerId, mFragment, "listFrag");

        } else {
            Log.d("TAB", "mFragment is not null, attaching instead of replacing");
            ft.attach(mFragment);
        }
    }


    public void onTabUnselected(Tab tab, FragmentTransaction ft) {
        if (mFragment != null) {
            // Detach the fragment, because another one is being attached
            ft.detach(mFragment);
        }
    }

    public void onTabReselected(Tab tab, FragmentTransaction ft) {
        // User selected the already selected tab. Usually do nothing.
    }
}
