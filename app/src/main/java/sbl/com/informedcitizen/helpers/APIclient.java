package sbl.com.informedcitizen.helpers;

import android.content.Context;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

/**
 * Created by atam on 8/13/2014.
 */
public class APIclient {
    private static AsyncHttpClient client = new AsyncHttpClient();

    public static void getLegislators(Context context, String state, AsyncHttpResponseHandler respHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put(Constants.ABBR_PARAM, state);

            get(context, Constants.GET_LEGISLATORS, params, respHandler);
            //String fullUrl = getRequestUrl(Constants.GET_LEGISLATORS) + "&id=" + state;
            //client.get(context, fullUrl, respHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getSummary(Context context, String id, AsyncHttpResponseHandler respHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put(Constants.ID_PARAM, id);
            params.put(Constants.CYCLE_PARAM, Constants.CYCLE);
            get(context, Constants.CAND_SUMMARY, params, respHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getContributors(Context context, String id, AsyncHttpResponseHandler respHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put(Constants.ID_PARAM, id);
            params.put(Constants.CYCLE_PARAM, Constants.CYCLE);
            get(context, Constants.CAND_CONTRIB, params, respHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void getIndustries(Context context, String id, AsyncHttpResponseHandler respHandler) {

        try {
            RequestParams params = new RequestParams();
            params.put(Constants.ID_PARAM, id);
            params.put(Constants.CYCLE_PARAM, Constants.CYCLE);
            get(context, Constants.CAND_INDUSTRIES, params, respHandler);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void get(Context context, String suffix, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String requestUrl = getRequestUrl(suffix);
        try {
            Log.d("DEBUG", "About to GET " + suffix);
            client.get(context, requestUrl, params, responseHandler);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    public static void getImageJson(Context context, String url, AsyncHttpResponseHandler respHandler) {
        Log.d("DEBUG", "About to GET " + url);
        client.get(context, url, respHandler);
    }


    private static String getRequestUrl(String suffix) {
        return Constants.BASE_URL + suffix + Constants.KEY_PARAM + Constants.API_KEY +
               Constants.FORMAT_PARAM + Constants.RESPONSE_FORMAT;
    }
}
