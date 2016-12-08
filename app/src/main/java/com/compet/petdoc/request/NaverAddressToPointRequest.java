package com.compet.petdoc.request;

import android.content.Context;
import android.util.Log;

import com.compet.petdoc.manager.NetworkRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Mu on 2016-11-30.
 */

public class NaverAddressToPointRequest extends NetworkRequest implements NetworkRequest.AsyncResponse {

    private String url;

    private Context mContext;

    public NaverAddressToPointRequest(Context context, String URL, String method, String address) {
        super(context);
        setMethod(method);
        String addr = null;
        mContext = context;
        try {
            addr = URLEncoder.encode(address, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        url = String.format(URL, addr);

        Log.d("url", "url : " + url.toString());

        Log.d("Send", "method : " + method + " " + "address : " + address);

    }

    @Override
    public URL getURL() throws MalformedURLException {
        return new URL(url);
    }

    @Override
    protected void setRequestProperty(HttpURLConnection conn) {
        super.setRequestProperty(conn);
        String clientId = "xk8H5Ynsv3lwcXVrCmAy";
        String clientSecret = "POQJe0OjGw";
        conn.setRequestProperty("Accept", "application/json");
        conn.setRequestProperty("X-Naver-Client-Id", clientId);
        conn.setRequestProperty("X-Naver-Client-Secret", clientSecret);

    }

    @Override
    public void successResult(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            JSONObject result = jsonObject.getJSONObject("result");
            JSONArray items = result.getJSONArray("items");
            JSONObject jItems = (JSONObject)items.get(0);
            JSONObject point = jItems.getJSONObject("point");
            double latitude = point.getDouble("x");
            double longitude = point.getDouble("y");

            Log.d("Receive", "latitude : " + latitude + " " + "longitude : " + longitude);

            //            double latitude = point.getDouble("x");
            //            double longitude = point.getDouble("y");
            Map<String, Double> map = new HashMap<>();
            map.put("latitude", latitude);
            map.put("longitude", longitude);
            //            Log.d("Receive", "latitude : " + latitude + " " + "longitude : " + longitude);

            listener.onSuccess(this, map);

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFail(this, -1, "실패");
        }

    }
}
