package com.nic.tnebnewwatersource.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.BankAccountDetailsAdapter;
import com.nic.tnebnewwatersource.adapter.PendingScreenAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ActivityPendingScreenBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import es.dmoral.toasty.Toasty;

public class PendingScreen extends AppCompatActivity implements Api.ServerResponseListener {
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private ActivityPendingScreenBinding activityPendingScreenBinding;
    String connection_number;
    ArrayList<TNEBSystem> pendingLists = new ArrayList<>();
    ArrayList<TNEBSystem> pendingBankLists = new ArrayList<>();
    PendingScreenAdapter pendingAdapter;
    BankAccountDetailsAdapter bankAccountDetailsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityPendingScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_pending_screen);
        activityPendingScreenBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String localLanguage = prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        activityPendingScreenBinding.recyclerView.setLayoutManager(mLayoutManager);
        activityPendingScreenBinding.recyclerView.setItemAnimator(new DefaultItemAnimator());
        activityPendingScreenBinding.recyclerView.setHasFixedSize(true);
        activityPendingScreenBinding.recyclerView.setNestedScrollingEnabled(false);
        activityPendingScreenBinding.recyclerView.setFocusable(false);

        RecyclerView.LayoutManager mLayoutManager1 = new GridLayoutManager(getApplicationContext(),1);
        activityPendingScreenBinding.bankRecyclerView.setLayoutManager(mLayoutManager1);
        activityPendingScreenBinding.bankRecyclerView.setItemAnimator(new DefaultItemAnimator());
        activityPendingScreenBinding.bankRecyclerView.setHasFixedSize(true);
        activityPendingScreenBinding.bankRecyclerView.setNestedScrollingEnabled(false);
        activityPendingScreenBinding.bankRecyclerView.setFocusable(false);

        new fetchPendingTask().execute();
        activityPendingScreenBinding.recyclerView.setVisibility(View.VISIBLE);
        activityPendingScreenBinding.bankRecyclerView.setVisibility(View.GONE);
        activityPendingScreenBinding.syncAllBtn.setVisibility(View.GONE);
        activityPendingScreenBinding.viewViewId.setVisibility(View.GONE);
        activityPendingScreenBinding.addViewId.setVisibility(View.VISIBLE);
        activityPendingScreenBinding.connectionDetailsText.setTextColor(getResources().getColor(R.color.white));
        activityPendingScreenBinding.bankDetailsText.setTextColor(getResources().getColor(R.color.grey_5));

        activityPendingScreenBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHomePage();
            }
        });
        activityPendingScreenBinding.homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHomePage();
            }
        });
        activityPendingScreenBinding.connectionDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new fetchPendingTask().execute();
                activityPendingScreenBinding.recyclerView.setVisibility(View.VISIBLE);
                activityPendingScreenBinding.bankRecyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.syncAllBtn.setVisibility(View.GONE);
                activityPendingScreenBinding.viewViewId.setVisibility(View.GONE);
                activityPendingScreenBinding.addViewId.setVisibility(View.VISIBLE);
                activityPendingScreenBinding.connectionDetailsText.setTextColor(getResources().getColor(R.color.white));
                activityPendingScreenBinding.bankDetailsText.setTextColor(getResources().getColor(R.color.grey_5));
            }
        });
        activityPendingScreenBinding.bankDetailsLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new fetchPendingBankTask().execute();
                activityPendingScreenBinding.recyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.syncAllBtn.setVisibility(View.GONE);
                activityPendingScreenBinding.bankRecyclerView.setVisibility(View.VISIBLE);
                activityPendingScreenBinding.viewViewId.setVisibility(View.VISIBLE);
                activityPendingScreenBinding.addViewId.setVisibility(View.GONE);
                activityPendingScreenBinding.bankDetailsText.setTextColor(getResources().getColor(R.color.white));
                activityPendingScreenBinding.connectionDetailsText.setTextColor(getResources().getColor(R.color.grey_5));
            }
        });

        activityPendingScreenBinding.syncAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                new toUploadTankCentreImageTask().execute();
            }
        });
    }
    public void gotoHomePage(){
        Intent homepage=new Intent(this,HomePage.class);
        homepage.putExtra("Home", "Login");
        startActivity(homepage);
        finish();
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }

    public class fetchPendingTask extends AsyncTask<Void, Void,
            ArrayList<TNEBSystem>> {
        @Override
        protected ArrayList<TNEBSystem> doInBackground(Void... params) {
            dbData.open();
            pendingLists = new ArrayList<>();
            pendingLists = dbData.getSavedWorkDetailsNew();
            Log.d("pending_count", String.valueOf(pendingLists.size()));
            return pendingLists;
        }

        @Override
        protected void onPostExecute(ArrayList<TNEBSystem> pendingLists) {
            super.onPostExecute(pendingLists);
            if(pendingLists.size()>0) {
                activityPendingScreenBinding.recyclerView.setVisibility(View.VISIBLE);
                activityPendingScreenBinding.syncAllBtn.setVisibility(View.GONE);
                activityPendingScreenBinding.noDataIcon.setVisibility(View.GONE);
                //activityPendingScreenBinding.noDataIconNew.setVisibility(View.GONE);
                activityPendingScreenBinding.bankRecyclerView.setVisibility(View.GONE);
                pendingAdapter = new PendingScreenAdapter(PendingScreen.this, pendingLists, dbData);
                activityPendingScreenBinding.recyclerView.setAdapter(pendingAdapter);
            }
            else {
                activityPendingScreenBinding.recyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.bankRecyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.syncAllBtn.setVisibility(View.GONE);
                activityPendingScreenBinding.noDataIcon.setVisibility(View.VISIBLE);
                //activityPendingScreenBinding.noDataIconNew.setVisibility(View.VISIBLE);
            }
        }
    }
    public class fetchPendingBankTask extends AsyncTask<Void, Void,
            ArrayList<TNEBSystem>> {
        @Override
        protected ArrayList<TNEBSystem> doInBackground(Void... params) {
            dbData.open();
            pendingBankLists = new ArrayList<>();
            pendingBankLists = dbData.getAllBankDetails("");
            Log.d("pending_count", String.valueOf(pendingBankLists.size()));
            return pendingBankLists;
        }

        @Override
        protected void onPostExecute(ArrayList<TNEBSystem> pendingLists) {
            super.onPostExecute(pendingLists);
            if(pendingLists.size()>0) {
                activityPendingScreenBinding.recyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.bankRecyclerView.setVisibility(View.VISIBLE);
                activityPendingScreenBinding.noDataIcon.setVisibility(View.GONE);
                //activityPendingScreenBinding.noDataIconNew.setVisibility(View.GONE);
                activityPendingScreenBinding.syncAllBtn.setVisibility(View.VISIBLE);
                bankAccountDetailsAdapter = new BankAccountDetailsAdapter(PendingScreen.this, pendingLists, dbData);
                activityPendingScreenBinding.bankRecyclerView.setAdapter(bankAccountDetailsAdapter);
            }
            else {
                activityPendingScreenBinding.bankRecyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.recyclerView.setVisibility(View.GONE);
                activityPendingScreenBinding.syncAllBtn.setVisibility(View.GONE);
                activityPendingScreenBinding.noDataIcon.setVisibility(View.VISIBLE);
                //activityPendingScreenBinding.noDataIconNew.setVisibility(View.VISIBLE);
            }
        }
    }


    public void save_and_delete_alert(JSONObject saveDetailsDataSet,String connection_number1,String save_delete){
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if(save_delete.equals("save")) {
                text.setText(getResources().getString(R.string.do_u_want_to_upload));
            }
            else if(save_delete.equals("delete")){
                text.setText(getResources().getString(R.string.do_u_want_to_delete));
            }

            Button yesButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button noButton = (Button) dialog.findViewById(R.id.btn_cancel);
            noButton.setVisibility(View.VISIBLE);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(save_delete.equals("save")) {
                        syncTrackData(saveDetailsDataSet, connection_number1);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        db.delete(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW, "connection_number = ?", new String[]{connection_number1});
                        db.delete(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES, "connection_number = ?", new String[]{connection_number1});
                        new fetchPendingTask().execute();
                        pendingAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void save_and_delete_bank_alert(String account_id,String save_delete){
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if(save_delete.equals("save")) {
                text.setText(getResources().getString(R.string.do_u_want_to_upload));
            }
            else if(save_delete.equals("delete")){
                text.setText(getResources().getString(R.string.do_u_want_to_delete));
            }

            Button yesButton = (Button) dialog.findViewById(R.id.btn_ok);
            Button noButton = (Button) dialog.findViewById(R.id.btn_cancel);
            noButton.setVisibility(View.VISIBLE);
            noButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
            yesButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(save_delete.equals("save")) {
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        db.delete(DBHelper.TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE, "account_id = ?", new String[]{account_id});
                        new fetchPendingBankTask().execute();
                        bankAccountDetailsAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public JSONObject syncTrackData(JSONObject saveDetailsDataSet,String connection_number1) {
        connection_number=connection_number1;
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), saveDetailsDataSet.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

            new ApiService(this).makeJSONObjectRequest("saveTrackDataList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), dataSet, "not cache", this);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveTrackDataList", "" + saveDetailsDataSet);
        Log.d("request", "" + dataSet);
        return dataSet;
    }

    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("saveTrackDataList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, getResources().getString(R.string.your_track_data_synchronized));
                    db.delete(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW, "connection_number = ?", new String[]{connection_number});
                    db.delete(DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES, "connection_number = ?", new String[]{connection_number});
                    new fetchPendingTask().execute();
                    pendingAdapter.notifyDataSetChanged();
                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                    new fetchPendingTask().execute();
                    pendingAdapter.notifyDataSetChanged();
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }

            if ("SaveBankInfo".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                String profile="";
                String no_of_bank_details_count="";
                Log.d("SaveBankInfo", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {

                    Utils.showAlert(this, getResources().getString(R.string.your_track_data_synchronized));
                    dbData.delete_TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE();
                    new fetchPendingBankTask().execute();
                    bankAccountDetailsAdapter.notifyDataSetChanged();

                }
                else {
                    Utils.showAlert(this, getResources().getString(R.string.failed));
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void OnError(VolleyError volleyError) {

    }


    public JSONObject saveDetails(JSONObject bankDetails){

        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), bankDetails.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);

        }catch (JSONException e){

        }
        Log.d("BankDetailsSave", "" + dataSet);
        return dataSet;

    }

    public void saveBankDetails(JSONObject  Bankdetails) {
        try {
            new ApiService(this).makeJSONObjectRequest("SaveBankInfo", Api.Method.POST, UrlGenerator.getBankServiceUrl(), saveDetails(Bankdetails), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public class toUploadTankCentreImageTask extends AsyncTask<String, Void,
            JSONObject> {
        JSONObject dataSet = new JSONObject();
        @Override
        protected JSONObject doInBackground(String... params) {
            //dbData.open();
            JSONArray track_data = new JSONArray();
            ArrayList<TNEBSystem> tanks = dbData.getAllBankDetails("");
//            ArrayList<MITank> tanks = dbData.getAllCenterImageData(prefManager.getDistrictCode(),prefManager.getBlockCode());

            if (tanks.size() > 0) {
                for (int i = 0; i < tanks.size(); i++) {
                    JSONObject jsonObject = new JSONObject();
                    try {
                        jsonObject.put("account_id",tanks.get(i).getAccount_id());
                        jsonObject.put("bank_id",tanks.get(i).getBank_id());
                        jsonObject.put("branch_id",tanks.get(i).getBranch_id());
                        jsonObject.put("account_no",tanks.get(i).getAccount_no());
                        jsonObject.put("ifsc_code",tanks.get(i).getIfsc_code());



                        track_data.put(jsonObject);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                dataSet = new JSONObject();

                try {
                    dataSet.put(AppConstant.KEY_SERVICE_ID,"village_bank_accounts_save");
                    dataSet.put("account_list",track_data);
                    Log.d("BankData",dataSet.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            return dataSet;
        }

        @Override
        protected void onPostExecute(JSONObject dataset) {
            super.onPostExecute(dataset);

            if(dataset.length()>0){
                saveBankDetails(dataset);
            }
            else {
                Utils.showAlert(PendingScreen.this,getResources().getString(R.string.failed));
            }

        }
    }
}
