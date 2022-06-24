package com.nic.tnebnewwatersource.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.DashboardBinding;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

public class Dashboard extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener{
    private DashboardBinding dashboardBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    private ProgressHUD progressHUD;
    String localLanguage ="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dashboardBinding = DataBindingUtil.setContentView(this, R.layout.dashboard);
        dashboardBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        localLanguage =prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);

        dashboardBinding.activityTneb.setEnabled(false);
        dashboardBinding.activityTnebUpdate.setEnabled(false);
        dashboardBinding.activityBankDetails.setEnabled(false);
        dashboardBinding.activityComplaintMonitoring.setEnabled(false);

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void connectionListActivity() {
        Intent intent = new Intent(this, ConnectionCheckActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void missedConnectionActivity() {
        Intent intent = new Intent(this, MissedConnectionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void bankDetailActivity() {
        Intent intent = new Intent(this, BankDetailActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void complaintMonitoringActivity() {
        Intent intent = new Intent(this, ComplaintMonitoringActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void waterSupplyActivity() {
        Intent intent = new Intent(this, DrinkingWaterSourceSave.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
    public void newWaterSupplyActivity() {
        Intent intent = new Intent(this, NewWaterSupplyStatusEntryForm.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }



    @Override
    public void onBackPressed() {
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        //overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        gotoHomePage();
    }

    public void gotoHomePage(){
        Intent intent=new Intent(Dashboard.this,HomePage.class);
        intent.putExtra("Home", "Login");
        startActivity(intent);
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    @Override
    protected void onResume() {
        super.onResume();

    }
}
