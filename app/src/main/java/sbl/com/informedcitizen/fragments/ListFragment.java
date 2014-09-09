package sbl.com.informedcitizen.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.actionbarsherlock.app.SherlockFragment;

import java.util.ArrayList;
import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.activities.ListActivity;
import sbl.com.informedcitizen.adapters.ContactsAdapter;
import sbl.com.informedcitizen.models.Contact;


public class ListFragment extends SherlockFragment {

    public static ListFragment newInstance(String listSection) {
        ListFragment fragment = new ListFragment();
        Bundle args = new Bundle();
        args.putSerializable("section", listSection);
        fragment.setArguments(args);

        return fragment;
    }


    public ListFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_list, container, false);

        final ListView contactsLV = (ListView) rootView.findViewById(R.id.contactsLV);
        String listSection = getArguments().getString("section");
        ListActivity la = (ListActivity)getActivity();
        ArrayList<Contact> contacts;
        if(listSection.equals("AH"))
            contacts = la.getFirstList();
        else if(listSection.equals("IQ"))
            contacts = la.getSecondList();
        else
            contacts = la.getThirdList();

        ContactsAdapter ca = new ContactsAdapter(getActivity(), contacts);
        contactsLV.setAdapter(ca);

        return rootView;
    }
}
