package sbl.com.informedcitizen.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.List;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.adapters.ContributorsAdapter;
import sbl.com.informedcitizen.helpers.APIclient;
import sbl.com.informedcitizen.models.Contributor;

public class ContributorsFragment extends Fragment {
    private static final String ARG_PARAM1 = "candID";
    private ListView contributorsLV;

    public static ContributorsFragment newInstance(String candidateID) {
        ContributorsFragment fragment = new ContributorsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, candidateID);
        fragment.setArguments(args);
        return fragment;
    }


    public ContributorsFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_contributors, container, false);
        contributorsLV = (ListView)v.findViewById(R.id.contributorsLV);

        String candidateID = getArguments().getString(ARG_PARAM1);
        APIclient.getContributors(getActivity(), candidateID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonResp) {
                try {
                    JSONObject responseValue = jsonResp.getJSONObject("response");
                    JSONObject contributorsValue = responseValue.getJSONObject("contributors");
                    JSONArray contributorValue = contributorsValue.getJSONArray("contributor");
                    ArrayList<Contributor> contributorList = Contributor.fromJSON(contributorValue);
                    ContributorsAdapter contribAdapter = new ContributorsAdapter(getActivity(),
                                                                                 contributorList);
                    contributorsLV.setAdapter(contribAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

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
