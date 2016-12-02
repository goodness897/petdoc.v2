package com.compet.petdoc.request;

import android.content.Context;
import android.util.Log;

import com.compet.petdoc.manager.NetworkRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by Mu on 2016-11-30.
 */

public class DaumCoordToAddressRequest extends NetworkRequest implements NetworkRequest.AsyncResponse {

    private String url;

    private String longitude = "127.10863694633468";

    private String latitude = "37.40209529907863";

    private String URL = "https://apis.daum.net/local/geo/coord2addr?apikey=bb0a31722200079de3d7097ca27a504a&longitude=%s&latitude=%s&inputCoordSystem=WGS84&output=json";

    public DaumCoordToAddressRequest(Context context, String method, String longitude, String latitude) {
        super(context);
        setMethod(method);


        url = String.format(URL, longitude, latitude);

        Log.d("url", "url : " + url.toString());

        Log.d("Send", "method : " + method + " " + "longitude : " + longitude + " " + "latitude : " + latitude);

    }

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(url);
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        conn.setRequestProperty("Accept", "application/json");
    }


    @Override
    public void successResult(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            String fullName = jsonObject.getString("fullName");
            Log.d("request", "fullname : " + fullName);
            if(!fullName.isEmpty()) {
                listener.onSuccess(this, fullName);
            } else {
                listener.onFail(this, -1, "실패");
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFail(this, -1, "실패");
        }

    }
}
