package com.compet.petdoc.request;

import android.content.Context;
import android.util.Log;

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

public class GwangjinHospitalListRequest extends NetworkRequest implements NetworkRequest.AsyncResponse {

    private static final String URL = "http://openAPI.gwangjin.go.kr:8088/" + AUTH_KEY + "/json/GwangjinAnimalHospital/%s/5";
    private String url;

    public GwangjinHospitalListRequest(Context context, String method, String pageNum) {
        super(context);
        setMethod(method);
        url = String.format(URL, pageNum);

        Log.d("url", "url : " + url.toString());
        Log.d("Send", "method : " + method + "" + "pageNum : " + pageNum);

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

    List<String> list = new ArrayList<>();

    @Override
    public void successResult(String output) {
        try {
            JSONObject jsonObject = new JSONObject(output);
            JSONObject gwangJin = jsonObject.getJSONObject("GwangjinAnimalHospital");
            JSONObject result = gwangJin.getJSONObject("RESULT");
            String code = result.getString("CODE");
            String message = result.getString("MESSAGE");
            if(code.equals("INFO-000")) {

                JSONArray jsonArray = gwangJin.getJSONArray("row");
                for(int i = 0; i < jsonArray.length(); i++) {
                    JSONObject hospital = jsonArray.getJSONObject(i);
                    String hospitalName = hospital.getString("WRKP_NM");
                    Log.d("hospital", "병원 이름 : " + hospitalName);
                    list.add(hospitalName);
                }
                listener.onSuccess(this, list);
            } else {
                listener.onFail(this, -1, message);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
