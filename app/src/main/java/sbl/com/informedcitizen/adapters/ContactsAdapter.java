package sbl.com.informedcitizen.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.JsonHttpResponseHandler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageAware;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import sbl.com.informedcitizen.R;
import sbl.com.informedcitizen.activities.DetailActivity;
import sbl.com.informedcitizen.helpers.APIclient;
import sbl.com.informedcitizen.helpers.Constants;
import sbl.com.informedcitizen.models.Contact;

/**
 * Created by atam on 8/15/2014.
 */
public class ContactsAdapter extends ArrayAdapter<Contact> {

    // view cache to avoid refinding view that exists
    private static class ViewHolder {
        ImageView profileIV;
        TextView nameTV;
        TextView chamberTV;
        TextView partyTV;
    }


    public ContactsAdapter(Context context, ArrayList<Contact> contacts) {
        // CALLING WRONG PARENT CONSTRUCTOR, needs 3 ARGUMENTS!
        //super(context, R.layout.route_item);
        super(context, R.layout.contact_item, contacts);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // current model instance
        final Contact currContact = getItem(position);

        final ViewHolder viewHolder;
        if (convertView == null) {
            Log.d("MY_TRACE", "convertView is null, current contact is " + currContact.getName());
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.contact_item, parent, false);

            viewHolder.profileIV = (ImageView) convertView.findViewById(R.id.profileIV);
            viewHolder.nameTV = (TextView) convertView.findViewById(R.id.nameTV);
            viewHolder.chamberTV = (TextView) convertView.findViewById(R.id.chamberTV);
            viewHolder.partyTV = (TextView) convertView.findViewById(R.id.partyTV);
            // tag row with inflated views
            convertView.setTag(viewHolder);
        } else {
            Log.d("MY_TRACE", "convertView is NOT null, current contact is " + currContact.getName());
            viewHolder = (ViewHolder) convertView.getTag();
        }

        String encodedName = Uri.encode(currContact.getName());
        String googleUrl = Constants.IMG_SEARCH + encodedName;
        APIclient.getImageJson(getContext(), googleUrl, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(JSONObject imgJson) {
                try {
                    JSONObject responseDataValue = imgJson.getJSONObject("responseData");
                    JSONArray resultsValue = responseDataValue.getJSONArray("results");
                    JSONObject result = resultsValue.getJSONObject(0);
                    String imgUrl = result.getString("url");

                    ImageAware imageAware = new ImageViewAware(viewHolder.profileIV, false);
                    ImageLoader.getInstance().displayImage(imgUrl, imageAware);
                    //ImageLoader.getInstance().displayImage(imgUrl, viewHolder.profileIV);
                }
                catch(Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Throwable arg0, JSONArray arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getContext(),
                        "getImageJson failed with JSONArray 2nd arg!", Toast.LENGTH_LONG).show();
            }

            // If it fails it fails here where arg1 is the error message(dev inactive)
            @Override
            public void onFailure(Throwable arg0, String arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getContext(),
                        "getImageJson failed with String 2nd arg!",
                        Toast.LENGTH_LONG).show();
            }

            @Override
            public void onFailure(Throwable arg0, JSONObject arg1) {
                super.onFailure(arg0, arg1);
                Toast.makeText(getContext(),
                        "getImageJson failed with JSONObject 2nd arg!", Toast.LENGTH_LONG).show();
            }
        } // end anon class jsonhttpresponsehandler
        );
        //viewHolder.profileIV.setBi
        viewHolder.nameTV.setText(currContact.getName());
        viewHolder.chamberTV.setText(currContact.getChamber());
        viewHolder.partyTV.setText(currContact.getParty());

        convertView.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                // set background color of v to light blue
                return false;
            }
        });
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getContext(), DetailActivity.class);
                i.putExtra(Constants.DETAIL_INTENT_KEY, currContact);
                getContext().startActivity(i);
            }
        });
        return convertView;
    }
}
