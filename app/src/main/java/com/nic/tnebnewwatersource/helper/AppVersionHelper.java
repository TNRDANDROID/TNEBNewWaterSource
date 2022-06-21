package com.nic.tnebnewwatersource.helper;

import android.app.Activity;
import android.util.Log;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;


import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by user on 31-08-2016.
 */
public class AppVersionHelper implements Api.ServerResponseListener {
    private Activity mContext;
    public myAppVersionInterface myListener;
    private PrefManager prefManager;
    private static int SPLASH_TIME_OUT = 2000;


    public AppVersionHelper(Activity context, myAppVersionInterface myListener) {
        this.mContext = context;
        this.myListener = myListener;
        prefManager = new PrefManager(this.mContext);

    }

    public void callAppVersionCheckApi() {
        new ApiService(mContext).makeRequest("versionCheck", Api.Method.POST, UrlGenerator.getLoginUrl(), versionCheckParams(), "not cache", this);

    }

    public Map<String, String> versionCheckParams() {
        Map<String, String> params = new HashMap<>();

        params.put(AppConstant.KEY_SERVICE_ID, AppConstant.KEY_VERSION_CHECK);
        params.put(AppConstant.KEY_APP_CODE, "EB");

        return params;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {

            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("versionCheck".equals(urlType) && responseObj != null) {
                String version = responseObj.getString("version");

                if (responseObj.getString(AppConstant.KEY_APP_CODE).equalsIgnoreCase("EB") && (!version.equalsIgnoreCase(Utils.getVersionName(mContext)))) {
                    myListener.onAppVersionCallback("Update");
                } else {
                    myListener.onAppVersionCallback("Don't update");
                }
                Log.d("version_check", "" + responseObj);
            } else {
                myListener.onAppVersionCallback("Don't update");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void OnError(VolleyError volleyError) {
        Log.i("Error", "Volley error");
    }

    // This is my interface //
    public interface myAppVersionInterface {
        void onAppVersionCallback(String value);
    }
}
