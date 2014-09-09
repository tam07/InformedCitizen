package sbl.com.informedcitizen.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.models.Contact;

public class ContactFragment extends Fragment {
    private static final String ARG_PARAM1 = "contact";

    private TextView addrTV;
    private TextView phoneTV;
    private TextView fbTV;
    private TextView ytTV;
    private TextView twitterTV;

    public static ContactFragment newInstance(Contact thisContact) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_PARAM1, thisContact);
        fragment.setArguments(args);
        return fragment;
    }


    public ContactFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // inflate row view
        View v = inflater.inflate(R.layout.fragment_contact, container, false);

        // get individual views
        addrTV = (TextView)v.findViewById(R.id.addressTV);
        phoneTV = (TextView)v.findViewById(R.id.phoneNumberTV);
        fbTV = (TextView)v.findViewById(R.id.facebookTV);
        twitterTV = (TextView)v.findViewById(R.id.twitterTV);
        ytTV = (TextView)v.findViewById(R.id.youtubeTV);

        // set view values
        Contact contact = (Contact)getArguments().getSerializable(ARG_PARAM1);
        addrTV.setText(contact.getAddress());
        phoneTV.setText(contact.getPhoneNo());
        fbTV.setText(contact.getFacebook());
        twitterTV.setText(contact.getTwitter());
        ytTV.setText(contact.getYoutube());

        return v;
    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}
