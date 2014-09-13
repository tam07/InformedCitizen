package sbl.com.informedcitizen.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.apache.http.Header;
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
    private ProgressBar pb;
    private TextView waitMsg;

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
        pb = (ProgressBar)v.findViewById(R.id.progressContr);
        waitMsg = (TextView)v.findViewById(R.id.msgContr);
        String candidateID = getArguments().getString(ARG_PARAM1);
        APIclient.getContributors(getActivity(), candidateID, new JsonHttpResponseHandler() {
            @Override
            //public void onSuccess(JSONObject jsonResp) {
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResp) {
                try {
                    JSONObject responseValue = jsonResp.getJSONObject("response");
                    JSONObject contributorsValue = responseValue.getJSONObject("contributors");
                    JSONArray contributorValue = contributorsValue.getJSONArray("contributor");
                    ArrayList<Contributor> contributorList = Contributor.fromJSON(contributorValue);
                    ContributorsAdapter contribAdapter = new ContributorsAdapter(getActivity(),
                                                                                 contributorList);

                    waitMsg.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    contributorsLV.setVisibility(View.VISIBLE);
                    contributorsLV.setAdapter(contribAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // If it fails it fails here where arg1 is the error message(dev inactive)
            @Override
            //public void onFailure(Throwable arg0, String arg1) {
                //super.onFailure(arg0, arg1);
            public void onFailure(int statusCode, Header[] headers, String responseStr, Throwable arg0) {
                super.onFailure(statusCode, headers, responseStr, arg0);
                pb.setVisibility(View.GONE);
                waitMsg.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        "getContributors failed with String 2nd arg!",
                        Toast.LENGTH_LONG).show();
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
