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
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.ProgressHUD;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

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
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        localLanguage =prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);


        dashboardBinding.availableConnectionActivity.setEnabled(false);
        dashboardBinding.missedConnectionActivity.setEnabled(false);
        dashboardBinding.bankDetailsActivity.setEnabled(false);
        dashboardBinding.complaintMonitoringActivity.setEnabled(false);
        dashboardBinding.waterSourceEntry.setEnabled(false);
        dashboardBinding.dailyWaterSupplyDetailsEntry.setEnabled(false);


        dashboardBinding.availableConnectionActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur_bg));
        dashboardBinding.missedConnectionActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur_bg));
        dashboardBinding.bankDetailsActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur_bg));
        dashboardBinding.complaintMonitoringActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur_bg));
        dashboardBinding.waterSourceEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur_bg));
        dashboardBinding.dailyWaterSupplyDetailsEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.blur_bg));

        dashboardBinding.availableConnectionText.setTextColor(getResources().getColor(R.color.grey3));
        dashboardBinding.missedConnectionText.setTextColor(getResources().getColor(R.color.grey3));
        dashboardBinding.bankText.setTextColor(getResources().getColor(R.color.grey3));
        dashboardBinding.complaintText.setTextColor(getResources().getColor(R.color.grey3));
        dashboardBinding.waterSourceText.setTextColor(getResources().getColor(R.color.grey3));
        dashboardBinding.dailyText.setTextColor(getResources().getColor(R.color.grey3));
        accessController();
       /* dashboardBinding.availableConnectionActivity.setClickable(false);
        dashboardBinding.missedConnectionActivity.setClickable(false);
        dashboardBinding.bankDetailsActivity.setClickable(false);
        dashboardBinding.complaintMonitoringActivity.setClickable(false);
        if(prefManager.getkey_levels().equals("V")){
            dashboardBinding.waterSourceEntry.setEnabled(true);
            dashboardBinding.waterSourceEntry.setClickable(true);
        }
        else {
            dashboardBinding.waterSourceEntry.setEnabled(false);
            dashboardBinding.waterSourceEntry.setClickable(false);
        }*/

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    private void accessController(){
        ArrayList<TNEBSystem> accessList = new ArrayList<>();
        accessList.addAll(dbData.getAll_Menu_Access_Control());
        if(accessList.size()>0){
           for (int i=0;i<accessList.size();i++){
               if(accessList.get(i).getMenu_name().equals("available_connection_activity")){
                    if(accessList.get(i).getMenu_access_control().equals("Y")){
                        dashboardBinding.availableConnectionActivity.setEnabled(true);
                        dashboardBinding.availableConnectionActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.elevation_bottom_lite_bg_gradient_all_corner));
                        dashboardBinding.availableConnectionText.setTextColor(getResources().getColor(R.color.white));
                    }
               }
               else if(accessList.get(i).getMenu_name().equals("missed_connection_activity")){
                    if(accessList.get(i).getMenu_access_control().equals("Y")){
                        dashboardBinding.missedConnectionActivity.setEnabled(true);
                        dashboardBinding.missedConnectionActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.elevation_bottom_lite_bg_gradient_all_corner));
                        dashboardBinding.missedConnectionText.setTextColor(getResources().getColor(R.color.white));
                    }
               }
               else if(accessList.get(i).getMenu_name().equals("bank_details_activity")){
                    if(accessList.get(i).getMenu_access_control().equals("Y")){
                        dashboardBinding.bankDetailsActivity.setEnabled(true);
                        dashboardBinding.bankDetailsActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.elevation_bottom_lite_bg_gradient_all_corner));
                        dashboardBinding.bankText.setTextColor(getResources().getColor(R.color.white));
                    }
               }
               else if(accessList.get(i).getMenu_name().equals("complaint_monitoring_activity")){
                    if(accessList.get(i).getMenu_access_control().equals("Y")){
                        dashboardBinding.complaintMonitoringActivity.setEnabled(true);
                        dashboardBinding.complaintMonitoringActivity.setBackgroundDrawable(getResources().getDrawable(R.drawable.elevation_bottom_lite_bg_gradient_all_corner));
                        dashboardBinding.complaintText.setTextColor(getResources().getColor(R.color.white));
                    }
               }
               else if(accessList.get(i).getMenu_name().equals("water_source_entry")){
                    if(accessList.get(i).getMenu_access_control().equals("Y")){
                        dashboardBinding.waterSourceEntry.setEnabled(true);
                        dashboardBinding.waterSourceEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.elevation_bottom_lite_bg_gradient_all_corner));
                        dashboardBinding.waterSourceText.setTextColor(getResources().getColor(R.color.white));
                    }
               }
               else {
                    if(accessList.get(i).getMenu_access_control().equals("Y")){
                        dashboardBinding.dailyWaterSupplyDetailsEntry.setEnabled(true);
                        dashboardBinding.dailyWaterSupplyDetailsEntry.setBackgroundDrawable(getResources().getDrawable(R.drawable.elevation_bottom_lite_bg_gradient_all_corner));
                        dashboardBinding.dailyText.setTextColor(getResources().getColor(R.color.white));
                    }
               }
           }
        }
        else {

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
