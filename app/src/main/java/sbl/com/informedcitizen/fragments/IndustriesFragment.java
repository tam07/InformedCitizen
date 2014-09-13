package sbl.com.informedcitizen.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.adapters.ContributorsAdapter;
import sbl.com.informedcitizen.adapters.IndustriesAdapter;
import sbl.com.informedcitizen.helpers.APIclient;
import sbl.com.informedcitizen.models.Contributor;
import sbl.com.informedcitizen.models.Industry;

public class IndustriesFragment extends Fragment {
    private static final String ARG_PARAM1 = "candID";
    private ListView industryLV;
    private ProgressBar pb;
    private TextView waitMsg;

    public static IndustriesFragment newInstance(String candidateID) {
        IndustriesFragment fragment = new IndustriesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, candidateID);
        fragment.setArguments(args);
        return fragment;
    }


    public IndustriesFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_industries, container, false);
        industryLV = (ListView)v.findViewById(R.id.industriesLV);
        pb = (ProgressBar)v.findViewById(R.id.progressIndust);
        waitMsg = (TextView)v.findViewById(R.id.msgIndust);
        String candidateID = getArguments().getString(ARG_PARAM1);
        APIclient.getIndustries(getActivity(), candidateID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject jsonResp) {
                try {
                    JSONObject responseValue = jsonResp.getJSONObject("response");
                    JSONObject industriesValue = responseValue.getJSONObject("industries");
                    JSONArray industryValue = industriesValue.getJSONArray("industry");
                    ArrayList<Industry> industryList = Industry.fromJSON(industryValue);
                    IndustriesAdapter industryAdapter = new IndustriesAdapter(getActivity(),
                            industryList);
                    waitMsg.setVisibility(View.GONE);
                    pb.setVisibility(View.GONE);
                    industryLV.setVisibility(View.VISIBLE);
                    industryLV.setAdapter(industryAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            // If it fails it fails here where arg1 is the error message(dev inactive)
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseStr, Throwable arg0) {
                super.onFailure(statusCode, headers, responseStr, arg0);
                pb.setVisibility(View.GONE);
                waitMsg.setVisibility(View.GONE);
                Toast.makeText(getActivity(),
                        "getIndustries failed with String 2nd arg!",
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
