package sbl.com.informedcitizen.fragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.*;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.helpers.APIclient;



public class SummaryFragment extends Fragment {

    private static final String ARG_PARAM1 = "candID";
    private TextView firstElectedTV;
    private TextView nextElectionTV;
    private TextView totalTV;
    private TextView spentTV;
    private TextView cashTV;
    private TextView debtTV;
    private TextView lastUpdatedTV;

    private OnFragmentInteractionListener mListener;


    public static SummaryFragment newInstance(String candidateID) {
        SummaryFragment fragment = new SummaryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, candidateID);
        fragment.setArguments(args);
        return fragment;
    }


    public SummaryFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        /*if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
        }*/

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_summary, container, false);
        firstElectedTV = (TextView)v.findViewById(R.id.firstElectedTV);
        nextElectionTV = (TextView)v.findViewById(R.id.nextElectionTV);
        totalTV = (TextView)v.findViewById(R.id.totalTV);
        spentTV = (TextView)v.findViewById(R.id.spentTV);
        cashTV = (TextView)v.findViewById(R.id.cashTV);
        debtTV = (TextView)v.findViewById(R.id.debtTV);
        lastUpdatedTV = (TextView)v.findViewById(R.id.lastUpdatedTV);

        String candID = getArguments().getString(ARG_PARAM1);
        APIclient.getSummary(getActivity(), candID, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject jsonResp) {
                try {
                    JSONObject responseValue = jsonResp.getJSONObject("response");
                    JSONObject summaryValue = responseValue.getJSONObject("summary");
                    JSONObject attributesValue = summaryValue.getJSONObject("@attributes");

                    String firstElected = attributesValue.getString("first_elected");
                    firstElectedTV.setText(firstElected);

                    String nextElection = attributesValue.getString("next_election");
                    nextElectionTV.setText(nextElection);

                    String total = attributesValue.getString("total");
                    totalTV.setText("$" + total);

                    String spent = attributesValue.getString("spent");
                    spentTV.setText("$" + spent);

                    String cash = attributesValue.getString("cash_on_hand");
                    cashTV.setText("$" + cash);

                    String debt = attributesValue.getString("debt");
                    debtTV.setText("$" + debt);

                    String lastUpdated = attributesValue.getString("last_updated");
                    lastUpdatedTV.setText(lastUpdated);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable arg0, JSONArray arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getActivity(),
                        "getSummary failed with JSONArray 2nd arg!", Toast.LENGTH_LONG).show();
            }

            // If it fails it fails here where arg1 is the error message(dev inactive)
            @Override
            public void onFailure(Throwable arg0, String arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getActivity(),
                        "getSummary failed with String 2nd arg!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable arg0, JSONObject arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getActivity(),
                        "getSummary failed with JSONObject 2nd arg!", Toast.LENGTH_LONG).show();
            }
        });

        return v;
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }*/
    }


    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        public void onFragmentInteraction(Uri uri);
    }
}
