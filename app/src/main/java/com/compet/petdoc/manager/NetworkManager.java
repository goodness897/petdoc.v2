package com.compet.petdoc.manager;

/**
 * Created by Mu on 2016-11-09.
 */

public class NetworkManager {
    private static NetworkManager instance;

    public static NetworkManager getInstance() {
        if(instance == null) {
            instance = new NetworkManager();
        }
        return instance;
    }
    private NetworkManager() {

    }
    public interface OnResultListener {
        public void onSuccess(NetworkRequest request, Object result);

        public void onFail(NetworkRequest request, int errorCode, String errorMessage);
    }

    public void getNetworkData(NetworkRequest request, OnResultListener listener) {
        request.setOnResultListener(listener);
        request.execute();
    }

}
