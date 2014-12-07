package sbl.com.informedcitizen.helpers;

import android.content.Context;
import android.net.Uri;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Header;
import org.apache.http.entity.StringEntity;
import org.apache.http.message.BasicHeader;
import org.json.JSONObject;

/**
 * Created by atam on 8/13/2014.
 */
public class APIclient {
    private static AsyncHttpClient client = new AsyncHttpClient();
    //private static SyncHttpClient blockingClient = new SyncHttpClient();

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


    public static void getImageJson(Context context, String name, AsyncHttpResponseHandler respHandler) {
        String encodedQueryStr = Uri.encode(Constants.CONGRESS_QTERM + name);
        // rsz=1&start=0&v=1.0&imgsz=icon&imgtype=face
        RequestParams params = new RequestParams();
        params.put("rsz", "1");
        params.put("start", "0");
        params.put("v", "1.0");
        //params.put("imgsz", "");
        params.put("imgtype", "face");
        params.put("q", "congress " + name);
        get(context, "imageJson", params, respHandler);
    }


    public static void get(Context context, String suffix, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        String requestUrl = getRequestUrl(suffix);
        if(requestUrl.startsWith("https://ajax")) {
            Log.d("TRACE", "FUCK ANDROID STUDIO");
            requestUrl += "";
        }
        try {
            Log.d("DEBUG", "About to GET " + suffix);
            client.get(context, requestUrl, params, responseHandler);
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }


    /*public static void getImageJsonSynch(Context context, String url, AsyncHttpResponseHandler respHandler) {
        Log.d("DEBUG", "About to GET " + url);
        blockingClient.get(context, url, respHandler);
    }*/


    private static String getRequestUrl(String suffix) {
        if(suffix != "imageJson")
            return Constants.BASE_URL + suffix + Constants.KEY_PARAM + Constants.API_KEY +
                   Constants.FORMAT_PARAM + Constants.RESPONSE_FORMAT;
        return Constants.IMG_SEARCH;
    }
}