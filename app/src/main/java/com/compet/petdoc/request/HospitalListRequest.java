package com.compet.petdoc.request;

import android.content.Context;
import android.util.Log;

import com.compet.petdoc.data.HospitalItem;
import com.compet.petdoc.manager.NetworkRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Mu on 2016-11-30.
 */

public class HospitalListRequest extends NetworkRequest implements NetworkRequest.AsyncResponse {

    private String url;

    private String[] objectName;

    public HospitalListRequest(Context context, String URL, String method, String startIndex, String endIndex) {
        super(context);
        setMethod(method);

        objectName = URL.split("/");
        for (String s : objectName) {
            Log.d("split", s);
        }

        url = String.format(URL, startIndex, endIndex);

        Log.d("url", "url : " + url.toString());

        Log.d("Send", "method : " + method + " " + "startIndex : " + startIndex + " " + "endIndex : " + endIndex);

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

    List<HospitalItem> list = new ArrayList<>();

    @Override
    public void successResult(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            JSONObject region = jsonObject.getJSONObject(objectName[5]);
            JSONObject result = region.getJSONObject("RESULT");
            String code = result.getString("CODE");
            String message = result.getString("MESSAGE");
            if (code.equals("INFO-000")) {

                JSONArray jsonArray = region.getJSONArray("row");
                for (int i = 0; i < jsonArray.length(); i++) {
                    HospitalItem hospitalItem = new HospitalItem();
                    JSONObject hospital = jsonArray.getJSONObject(i);
                    if (hospital.getString("TRD_STATE_GBN").equals("0000")) {
                        String name = hospital.getString("WRKP_NM");
                        String address = hospital.getString("SITE_ADDR");
                        String phoneNumber = hospital.getString("SITE_TEL");
                        if (!address.equals("    ")) {
                            hospitalItem.setHosName(name);
                            hospitalItem.setAddress(address);
                            hospitalItem.setPhoneNumber(phoneNumber);
                            list.add(hospitalItem);
                        }

                    }
                }
                listener.onSuccess(this, list);
            } else {
                listener.onFail(this, -1, message);
            }

        } catch (JSONException e) {
            e.printStackTrace();
            listener.onFail(this, -1, "실패");
        }

    }
}
