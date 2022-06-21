package com.nic.tnebnewwatersource.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.CommonAdapter;
import com.nic.tnebnewwatersource.adapter.HabitationListAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ActivityWaterSupplyBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.ProgressHUD;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WaterSupplyActivity extends AppCompatActivity implements  Api.ServerResponseListener{
    private ActivityWaterSupplyBinding waterSupplyBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    private ProgressHUD progressHUD;
    String localLanguage ="";
    int status_type =-1;
    ArrayList<TNEBSystem> Habitation;
    ArrayList<TNEBSystem> water_supplied_throughList;
    ArrayList<TNEBSystem> water_supplied_reason;
    ArrayList<TNEBSystem> tempReasonList;
    HabitationListAdapter habitationListAdapter;
    String maximum_date ="";
    String minimum_date ="";
    String water_supplied_through_id ="";
    String water_supplied_through_name ="";
    String water_supplied_reason_id ="";
    String water_supplied_reason_name ="";
    private int mYear, mMonth, mDay;

    //dialog Layout Widgets
    Spinner reason_spinner;
    Button done_btn;
    RadioGroup water_supply_radio_group;
    Spinner water_supply_through_spinner;
    TextView habitation_name;
    EditText total_number_of_trips;
    EditText water_supply_litter;
    EditText amount;
    RadioButton full_supply;
    RadioButton partial_supply;
    RadioButton no_supply;
    LinearLayout water_supply_reason_layout;
    LinearLayout water_supply_through_layout;
    RelativeLayout total_no_of_trips_layout;
    RelativeLayout water_supply_litter_layout;
    RelativeLayout amount_layout;
    private Handler handler = new Handler();

    ////Supply Details Server View variables
    private String water_supply_status;
    private String no_partial_supply_reason;
    private String no_trips;
    private String watersupplyliter;
    private String partial_amount;
    private String partial_supply_through;
    private String reason_for_supply;
    private String water_supply_through;
    private String water_supply_hab_name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        waterSupplyBinding = DataBindingUtil.setContentView(this, R.layout.activity_water_supply);
        waterSupplyBinding.setActivity(this);
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
        waterSupplyBinding.habitationListLayout.setVisibility(View.GONE);
        waterSupplyBinding.saveBtn.setVisibility(View.GONE);
        water_supplied_reason = new ArrayList<>();
        water_supplied_throughList = new ArrayList<>();
        waterSupplyBinding.datePickerLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });

        waterSupplyBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                uploadDateToServer();
            }
        });

        fetchSpinnerValues();

    }

    private void fetchSpinnerValues() {
        waterSupplyBinding.habitationRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        habitationFilterSpinner(prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode());
        if(Utils.isOnline()){
            get_min_max_date();
        }
        else {
            Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.no_internet_connection));
        }
    }

    public void get_min_max_date() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_min_max_date", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_min_max_date_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_min_max_date_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.min_max_dateJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("min_max_date", "" + dataSet);
        return dataSet;
    }

    public void get_water_supply_reason() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_water_supply_reason", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_water_supply_reason_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_water_supply_reason_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.get_water_supply_reasonJsonParams(status_type).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_supply_reason", "" + dataSet);
        return dataSet;
    }

    public void get_water_supplied_through() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_water_supplied_through", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_water_supplied_through_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_water_supplied_through_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.water_supplied_throughJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("min_max_date", "" + dataSet);
        return dataSet;
    }

    public void habitationFilterSpinner(String dcode, String bcode, String pvcode) {
        Cursor HABList = null;
        HABList = db.rawQuery("SELECT * FROM " + DBHelper.HABITATION_TABLE_NAME + " where dcode = '" + dcode + "'and bcode = '" + bcode + "' and pvcode = '" + pvcode + "' order by habitation_name asc", null);
        Habitation = new ArrayList<>();
        Habitation.clear();
        if (HABList.getCount() > 0) {
            if (HABList.moveToFirst()) {
                do {
                    TNEBSystem habList = new TNEBSystem();
                    String districtCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String blockCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.BLOCK_CODE));
                    String pvCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String habCode = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABB_CODE));
                    String habName = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABITATION_NAME));
                    String habName_ta = HABList.getString(HABList.getColumnIndexOrThrow(AppConstant.HABITATION_NAME_TA));

                    habList.setDistictCode(districtCode);
                    habList.setBlockCode(blockCode);
                    habList.setPvCode(pvCode);
                    habList.setHabitation_code(habCode);
                    habList.setHabitation_name(habName);
                    habList.setHabitation_name_ta(habName_ta);



                    Habitation.add(habList);
                    Log.d("spinnersize", "" + Habitation.size());
                } while (HABList.moveToNext());
            }
        }
        if(Habitation.size()>0){
            waterSupplyBinding.habitationRecycler.setVisibility(View.VISIBLE);
            habitationListAdapter = new HabitationListAdapter(WaterSupplyActivity.this,Habitation,dbData);
            waterSupplyBinding.habitationRecycler.setAdapter(habitationListAdapter);
        }
        else {
            waterSupplyBinding.habitationRecycler.setVisibility(View.GONE);
        }
    }
    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("get_min_max_date".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    minimum_date = jsonObject.getString("min_date");
                    maximum_date = jsonObject.getString("max_date");
                }
                Log.d("get_min_max_date", "" + responseDecryptedBlockKey);
            }
            if ("get_water_supply_reason".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("get_water_supply_reason", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_water_supply_reason().execute(jsonObject);
                }

            }
            if ("get_water_supplied_through".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("water_supplied_through", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_water_supplied_through().execute(jsonObject);
                }

            }
            if ("daily_drinking_water_supply_status_view".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("water_supplied_through", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                   // new Insert_water_supplied_through().execute(jsonObject);
                    JSONArray jsonArray = jsonObject.getJSONArray("JSON_DATA");
                    for (int i =0;i<jsonArray.length();i++){
                        water_supply_status = jsonArray.getJSONObject(i).getString("water_supply_status");
                        no_partial_supply_reason = jsonArray.getJSONObject(i).getString("no_partial_supply_reason");
                        no_trips = jsonArray.getJSONObject(i).getString("no_trips");
                        watersupplyliter = jsonArray.getJSONObject(i).getString("watersupplyliter");
                        partial_amount = jsonArray.getJSONObject(i).getString("partial_amount");
                        partial_supply_through = jsonArray.getJSONObject(i).getString("partial_supply_through");
                        reason_for_supply = jsonArray.getJSONObject(i).getString("reason_for_supply");
                        water_supply_through = jsonArray.getJSONObject(i).getString("water_supply_through");
                        //water_supply_hab_name = jsonArray.getJSONObject(i).getString("water_supply_hab_name");
                    }
                    showPopUpSupplyDetailsView();
                }

            }
            if ("upload_water_supply_status_details".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("water_supplied_through", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(WaterSupplyActivity.this,jsonObject.getString("MESSAGE"));
                    dbData.open();
                    dbData.delete_TN_EB_WATER_SUPPLY_DETAILS_TABLE();
                    Runnable runnable = new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    };
                    handler.postDelayed(runnable, 1000);
                }
                else {
                    Utils.showAlert(WaterSupplyActivity.this,jsonObject.getString("MESSAGE"));
                }

            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public class Insert_water_supplied_through extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                    JSONArray jsonArray = new JSONArray();
                    try {
                        jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                water_supplied_throughList = new ArrayList<>();
                TNEBSystem ListValue1 = new TNEBSystem();
                ListValue1.setId("0");
                ListValue1.setWater_supply_through("Select Water Supply Through");
                water_supplied_throughList.add(ListValue1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {
                            ListValue.setId(jsonArray.getJSONObject(i).getString("id"));
                            ListValue.setWater_supply_through(jsonArray.getJSONObject(i).getString("water_supply_through"));
                            water_supplied_throughList.add(ListValue);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(water_supplied_throughList.size()>1){
                water_supply_through_spinner.setAdapter(new CommonAdapter(WaterSupplyActivity.this,water_supplied_throughList,"water_supplied_throughList"));
            }
            else {
                water_supply_through_spinner.setAdapter(null);

            }
        }
    }
    public class Insert_water_supply_reason extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                water_supplied_reason = new ArrayList<>();
                /*TNEBSystem ListValue1 = new TNEBSystem();
                ListValue1.setId("0");
                ListValue1.setReason_for_supply("Select Reason");
                ListValue1.setReason_type("");
                water_supplied_reason.add(ListValue1);*/
                for (int i = 0; i < jsonArray.length(); i++) {
                    TNEBSystem ListValue = new TNEBSystem();
                    try {
                        ListValue.setId(jsonArray.getJSONObject(i).getString("id"));
                        ListValue.setReason_for_supply(jsonArray.getJSONObject(i).getString("reason_for_supply"));
                        ListValue.setReason_type(jsonArray.getJSONObject(i).getString("type"));
                        water_supplied_reason.add(ListValue);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
           /* if(water_supplied_reason.size()>1){
                reason_spinner.setAdapter(new CommonAdapter(WaterSupplyActivity.this,water_supplied_reason,"water_supplied_reason"));
                water_supply_reason_layout.setVisibility(View.VISIBLE);
                water_supply_through_layout.setVisibility(View.GONE);
                total_no_of_trips_layout.setVisibility(View.GONE);
                water_supply_litter_layout.setVisibility(View.GONE);
                amount_layout.setVisibility(View.GONE);
            }
            else {
                reason_spinner.setAdapter(null);
                water_supply_reason_layout.setVisibility(View.GONE);
                water_supply_through_layout.setVisibility(View.GONE);
                total_no_of_trips_layout.setVisibility(View.GONE);
                water_supply_litter_layout.setVisibility(View.GONE);
                amount_layout.setVisibility(View.GONE);
            }
*/
        }
    }
    private void setWaterSupplyReasonAdapter(String type){
        tempReasonList = new ArrayList();
        TNEBSystem ListValue1 = new TNEBSystem();
        ListValue1.setId("0");
        ListValue1.setReason_for_supply("Select Reason");
        ListValue1.setReason_type(type);
        tempReasonList.add(ListValue1);
        for(int i=0;i<water_supplied_reason.size();i++){
            if(water_supplied_reason.get(i).getReason_type().equals(type)){
                TNEBSystem ListValue = new TNEBSystem();
                ListValue.setId(water_supplied_reason.get(i).getId());
                ListValue.setReason_for_supply(water_supplied_reason.get(i).getReason_for_supply());
                ListValue.setReason_type(water_supplied_reason.get(i).getReason_type());
                tempReasonList.add(ListValue);
            }
        }
        if(tempReasonList.size()>1){
            reason_spinner.setAdapter(new CommonAdapter(WaterSupplyActivity.this,tempReasonList,"water_supplied_reason"));
            water_supply_reason_layout.setVisibility(View.VISIBLE);
            water_supply_through_layout.setVisibility(View.GONE);
            total_no_of_trips_layout.setVisibility(View.GONE);
            water_supply_litter_layout.setVisibility(View.GONE);
            amount_layout.setVisibility(View.GONE);
            total_number_of_trips.setText("");
            water_supply_litter.setText("");
            amount.setText("");
            water_supply_through_spinner.setSelection(0);
            reason_spinner.setSelection(0);
        }
        else {
            water_supply_reason_layout.setVisibility(View.GONE);
            water_supply_through_layout.setVisibility(View.GONE);
            total_no_of_trips_layout.setVisibility(View.GONE);
            water_supply_litter_layout.setVisibility(View.GONE);
            amount_layout.setVisibility(View.GONE);
            total_number_of_trips.setText("");
            water_supply_litter.setText("");
            amount.setText("");
            water_supply_through_spinner.setSelection(0);
            reason_spinner.setSelection(0);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void showStartDatePickerDialog() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        waterSupplyBinding.dateText.setText(dateFormet(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                        waterSupplyBinding.habitationListLayout.setVisibility(View.VISIBLE);
                        waterSupplyBinding.saveBtn.setVisibility(View.VISIBLE);

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.getDatePicker().setMinDate(stringToDate(minimum_date));
        datePickerDialog.getDatePicker().setMaxDate(stringToDate(maximum_date));
        datePickerDialog.show();


    }
    private String dateFormet(String dateStr) {
        String myFormat="";
        java.text.SimpleDateFormat format = new java.text.SimpleDateFormat("dd-MM-yyyy");
        try {
            Date date1 = format.parse(dateStr);
            System.out.println(date1);
            String dateTime = format.format(date1);
            System.out.println("Current Date Time : " + dateTime);
            myFormat = dateTime; //In which you need put here
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return myFormat;
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    private long stringToDate(String date_text){
        DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        Date startDate = new Date();
        long milliseconds = 0;
        try {
            startDate = df.parse(date_text);
            String newDateString = df.format(startDate);
            milliseconds = startDate.getTime();
            System.out.println(newDateString);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return milliseconds;
    }

    public  void showPopUp(int position){
        try {
            final Dialog dialog = new Dialog(WaterSupplyActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.habitation_drinking_water_supply);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            habitation_name = (TextView)dialog.findViewById(R.id.habitation_name) ;
            reason_spinner   = (Spinner)dialog.findViewById(R.id.reason_spinner) ;
            water_supply_through_spinner   = (Spinner)dialog.findViewById(R.id.water_supply_through_spinner) ;
            total_number_of_trips   = (EditText) dialog.findViewById(R.id.total_number_of_trips) ;
            water_supply_litter   = (EditText) dialog.findViewById(R.id.water_supply_litter) ;
            amount   = (EditText) dialog.findViewById(R.id.amount) ;
            water_supply_radio_group = (RadioGroup) dialog.findViewById(R.id.water_supply_radio_group) ;
            full_supply   = (RadioButton) dialog.findViewById(R.id.full_supply) ;
            partial_supply   = (RadioButton) dialog.findViewById(R.id.partial_supply) ;
            no_supply   = (RadioButton) dialog.findViewById(R.id.no_supply) ;
            water_supply_reason_layout   = (LinearLayout) dialog.findViewById(R.id.water_supply_reason_layout) ;
            water_supply_through_layout   = (LinearLayout) dialog.findViewById(R.id.water_supply_through_layout) ;
            total_no_of_trips_layout   = (RelativeLayout) dialog.findViewById(R.id.total_no_of_trips_layout) ;
            water_supply_litter_layout   = (RelativeLayout) dialog.findViewById(R.id.water_supply_litter_layout) ;
            amount_layout   = (RelativeLayout) dialog.findViewById(R.id.amount_layout) ;
            done_btn   = (Button) dialog.findViewById(R.id.done_btn) ;

            water_supply_radio_group.clearCheck();


            habitation_name.setText(Habitation.get(position).getHabitation_name());

            water_supply_reason_layout.setVisibility(View.GONE);
            water_supply_through_layout.setVisibility(View.GONE);
            total_no_of_trips_layout.setVisibility(View.GONE);
            water_supply_litter_layout.setVisibility(View.GONE);
            amount_layout.setVisibility(View.GONE);

            get_water_supplied_through();
            get_water_supply_reason();

           /* ArrayList<TNEBSystem> previousList = new ArrayList<>();
            previousList.addAll(dbData.getWaterSupplyDetails(Habitation.get(position).getHabitation_code()));
            if(previousList.size()>0){
                for (int i=0;i<previousList.size();i++){
                    String status_type_= previousList.get(i).getStatus_type_id();
                    String water_supplied_reason_id_= previousList.get(i).getWater_supplied_reason_id();
                    String water_supplied_through_id_= previousList.get(i).getWater_supplied_through_id();
                    String total_number_of_trips_= previousList.get(i).getTotal_no_of_trips();
                    String water_supply_litter_= previousList.get(i).getWater_supply_litter();
                    String amount_= previousList.get(i).getAmount();

                    if(status_type_.equals("1")){
                        full_supply.setChecked(true);
                    }
                    else if(status_type_.equals("2")){
                        partial_supply.setChecked(true);
                        //setWaterSupplyReasonAdapter("P");
                    }
                    else{
                        no_supply.setChecked(true);
                        //setWaterSupplyReasonAdapter("N");
                    }

                    reason_spinner.setSelection(getReasonIndex(water_supplied_reason_id_));
                    water_supply_through_spinner.setSelection(getWaterSupplyThroughIndex(water_supplied_through_id_));
                    total_number_of_trips.setText(total_number_of_trips_);
                    water_supply_litter.setText(water_supply_litter_);
                    amount.setText(amount_);
                }
            }*/

            full_supply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if(isChecked){
                           status_type = 1;
                           water_supply_reason_layout.setVisibility(View.GONE);
                           water_supply_through_layout.setVisibility(View.GONE);
                           total_no_of_trips_layout.setVisibility(View.GONE);
                           water_supply_litter_layout.setVisibility(View.GONE);
                           amount_layout.setVisibility(View.GONE);
                           total_number_of_trips.setText("");
                           water_supply_litter.setText("");
                           amount.setText("");
                           water_supply_through_spinner.setSelection(0);
                           reason_spinner.setSelection(0);
                       }
                   }
               });
            partial_supply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                       if(isChecked){
                           status_type = 2;
                           setWaterSupplyReasonAdapter("P");
                           //get_water_supply_reason();
                       }

                   }
               });
            no_supply.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                   @Override
                   public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                      if(isChecked){
                          status_type = 0;
                          setWaterSupplyReasonAdapter("N");
                          //get_water_supply_reason();
                      }

                   }
               });

            reason_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if(position>0){
                           water_supplied_reason_id = tempReasonList.get(position).getId();
                           water_supplied_reason_name = tempReasonList.get(position).getReason_for_supply();
                           total_number_of_trips.setText("");
                           water_supply_litter.setText("");
                           amount.setText("");
                           water_supply_through_spinner.setSelection(0);
                           if(water_supplied_reason_id.equals("8")){
                               water_supply_through_layout.setVisibility(View.VISIBLE);
                               total_no_of_trips_layout.setVisibility(View.VISIBLE);
                               water_supply_litter_layout.setVisibility(View.VISIBLE);
                               amount_layout.setVisibility(View.VISIBLE);
                           }
                           else {
                               total_number_of_trips.setText("");
                               water_supply_litter.setText("");
                               amount.setText("");
                               water_supply_through_layout.setVisibility(View.GONE);
                               total_no_of_trips_layout.setVisibility(View.GONE);
                               water_supply_litter_layout.setVisibility(View.GONE);
                               amount_layout.setVisibility(View.GONE);
                           }
                       }
                       else {
                           total_number_of_trips.setText("");
                           water_supply_litter.setText("");
                           amount.setText("");
                           water_supply_through_layout.setVisibility(View.GONE);
                           total_no_of_trips_layout.setVisibility(View.GONE);
                           water_supply_litter_layout.setVisibility(View.GONE);
                           amount_layout.setVisibility(View.GONE);
                           water_supplied_reason_id ="";
                           water_supplied_reason_name = "";
                       }
                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });
            water_supply_through_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                   @Override
                   public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                       if(position>0){
                           water_supplied_through_id = water_supplied_throughList.get(position).getId();
                           water_supplied_through_name = water_supplied_throughList.get(position).getWater_supply_through();
                       }
                       else {
                           water_supplied_through_id = "";
                           water_supplied_through_name ="";
                       }

                   }

                   @Override
                   public void onNothingSelected(AdapterView<?> parent) {

                   }
               });

            done_btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(status_type!=-1){
                        if(status_type==1){
                            insertWasterSupplyDetails(position);
                            dialog.dismiss();
                        }
                        else {
                            if(!water_supplied_reason_id.equals("")){
                                if(water_supplied_reason_id.equals("8")){
                                    if(!water_supplied_through_id.equals("")){
                                        if(!total_number_of_trips.getText().toString().equals("")){
                                            if(!water_supply_litter.getText().toString().equals("")){
                                                if(!amount.getText().toString().equals("")){
                                                    insertWasterSupplyDetails(position);
                                                    dialog.dismiss();
                                                }
                                                else {
                                                    Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.amount));

                                                }
                                            }
                                            else {
                                                Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.water_supply_liter));

                                            }
                                        }
                                        else {
                                            Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.total_no_of_trips));

                                        }
                                    }
                                    else {
                                        Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.select_water_supply_through));
                                    }
                                }
                                else {
                                    insertWasterSupplyDetails(position);
                                    dialog.dismiss();
                                }
                            }
                            else {
                                Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.select_reason));
                            }
                        }
                    }
                    else {
                        Utils.showAlert(WaterSupplyActivity.this,"Please Choose Water Supply");
                    }
                }
            });

            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void insertWasterSupplyDetails(int pos){
        long insert_id =0;
        String whereClause="";
        String[] whereArgs = null;
        try {
            dbData.open();
            ContentValues contentValues = new ContentValues();
            contentValues.put("status_type_id",String.valueOf(status_type));
            contentValues.put("pv_code",prefManager.getPvCode());
            contentValues.put("hab_code",Habitation.get(pos).getHabitation_code());
            contentValues.put("hab_name",Habitation.get(pos).getHabitation_name());
            contentValues.put("hab_name_ta",Habitation.get(pos).getHabitation_name_ta());
            contentValues.put("water_supplied_reason_id",water_supplied_reason_id);
            contentValues.put("water_supplied_reason_name",water_supplied_reason_name);
            contentValues.put("water_supplied_through_id",water_supplied_through_id);
            contentValues.put("water_supplied_through_name",water_supplied_through_name);
            contentValues.put("total_no_of_trips",total_number_of_trips.getText().toString());
            contentValues.put("water_supply_litter",water_supply_litter.getText().toString());
            contentValues.put("amount",amount.getText().toString());
            whereClause = "hab_code = ? ";
            whereArgs = new String[]{Habitation.get(pos).getHabitation_code()};
            ArrayList<TNEBSystem> previousList = new ArrayList<>();
            previousList.addAll(dbData.getWaterSupplyDetails(Habitation.get(pos).getHabitation_code()));
            if(previousList.size()>0){
                insert_id = db.update(DBHelper.TN_EB_WATER_SUPPLY_DETAILS_TABLE, contentValues, whereClause, whereArgs);
                if(insert_id>0){
                    //Toasty.success(WaterSupplyActivity.this,"Updated Success",Toasty.LENGTH_SHORT,true).show();
                    habitationListAdapter.notifyDataSetChanged();
                }
            }
            else {
                insert_id = db.insert(DBHelper.TN_EB_WATER_SUPPLY_DETAILS_TABLE,null,contentValues);
                if(insert_id>0){
                    //Toasty.success(WaterSupplyActivity.this,"Inserted Success",Toasty.LENGTH_SHORT,true).show();
                    habitationListAdapter.notifyDataSetChanged();
                }
            }
        }
        catch (Exception e){
            Utils.showAlert(WaterSupplyActivity.this,"Fail");
        }
    }
    private void uploadDateToServer(){
        boolean condition_flag = false;
        int count=0;
        for(int i=0;i<Habitation.size();i++){
            ArrayList<TNEBSystem> previousList = new ArrayList<>();
            previousList.addAll(dbData.getWaterSupplyDetails(Habitation.get(i).getHabitation_code()));
            count=i;
            if(previousList.size()>0) {
                count=i;
                condition_flag = true;
            }
            else {
                condition_flag = false;
                break;
            }
        }
        if(condition_flag){
            try {
                JSONObject jsonValue = new JSONObject();
                JSONArray water_supply_status_details = new JSONArray();
                for(int i=0;i<Habitation.size();i++) {
                    ArrayList<TNEBSystem> previousList = new ArrayList<>();
                    previousList.addAll(dbData.getWaterSupplyDetails(Habitation.get(i).getHabitation_code()));
                    for(int j =0;j<previousList.size();j++){
                        JSONObject jsonObject = new JSONObject();
                        jsonObject.put("habcode",previousList.get(j).getHabitation_code());
                        jsonObject.put("water_supply_status",previousList.get(j).getStatus_type_id());
                        jsonObject.put("no_partial_supply_reason",previousList.get(j).getWater_supplied_reason_id());
                        jsonObject.put("partial_supply_through",previousList.get(j).getWater_supplied_through_id());
                        jsonObject.put("no_trips",previousList.get(j).getTotal_no_of_trips());
                        jsonObject.put("watersupplyliter",previousList.get(j).getWater_supply_litter());
                        jsonObject.put("partial_amount",previousList.get(j).getAmount());
                        water_supply_status_details.put(jsonObject);
                    }
                }
                jsonValue.put(AppConstant.KEY_SERVICE_ID,"daily_drinking_water_supply_status_save");
                jsonValue.put("entry_dt",waterSupplyBinding.dateText.getText().toString());
                jsonValue.put("water_supply_status_details",water_supply_status_details);

                Log.d("json",""+jsonValue);

                if(Utils.isOnline()){
                    upload_water_supply_status_details(jsonValue);
                }
                else {
                    Utils.showAlert(WaterSupplyActivity.this,getResources().getString(R.string.no_internet_connection));
                }
            }
            catch (JSONException e){
                e.printStackTrace();
            }

        }
        else {
            String msg = "Please Fill the Details of"+Habitation.get(count).getHabitation_name();
            Utils.showAlert(WaterSupplyActivity.this,msg);
        }

    }
    public void upload_water_supply_status_details(JSONObject jsonObject) {
        try {
            new ApiService(this).makeJSONObjectRequest("upload_water_supply_status_details", Api.Method.POST, UrlGenerator.getMainServiceUrl(), upload_water_supply_status_details_JSONParams(jsonObject), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject upload_water_supply_status_details_JSONParams(JSONObject jsonObject) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), jsonObject.toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_supply_status", "" + dataSet);
        return dataSet;
    }


    @Override
    public void onBackPressed() {
        exitDialog(getResources().getString(R.string.are_you_sure_exit));
    }
    public void exitDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(WaterSupplyActivity.this);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
        final AlertDialog alertDialog = builder.create();
        alertDialog.setView(dialogView, 0, 0, 0, 0);
        alertDialog.setCancelable(false);
        alertDialog.show();

        TextView tv_message = (TextView) dialogView.findViewById(R.id.tv_message);
        tv_message.setText(message);

        Button btnOk = (Button) dialogView.findViewById(R.id.btn_ok);
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dbData.open();
                dbData.delete_TN_EB_WATER_SUPPLY_DETAILS_TABLE();
                finish();
            }
        });
        Button btnCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        btnCancel.setVisibility(View.VISIBLE);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }

    private int getReasonIndex(String myString){

        int index = 0;

        for (int i=0;i<tempReasonList.size();i++){
            if (tempReasonList.get(i).getId().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private int getWaterSupplyThroughIndex(String myString){

        int index = 0;

        for (int i=0;i<water_supplied_throughList.size();i++){
            if (water_supplied_throughList.get(i).getId().equals(myString)){
                index = i;
            }
        }
        return index;
    }

    public void get_daily_drinking_water_supply_status_view(String hab_code,String hab_name) {
        water_supply_hab_name = hab_name;
        try {
            new ApiService(this).makeJSONObjectRequest("daily_drinking_water_supply_status_view", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_daily_drinking_water_supply_status_view_JSONParams(hab_code), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_daily_drinking_water_supply_status_view_JSONParams(String hab_code) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), daily_drinking_water_supply_status_viewJsonParams(hab_code).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("supply_status_view", "" + dataSet);
        return dataSet;
    }
    public  JSONObject daily_drinking_water_supply_status_viewJsonParams(String hab_code) throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "daily_drinking_water_supply_status_view");
        dataSet.put("entry_dt",waterSupplyBinding.dateText.getText().toString());
        dataSet.put("habcode",hab_code);
        Log.d("supply_status_view", "" + dataSet);
        return dataSet;
    }
    public  void showPopUpSupplyDetailsView(){
        try {
            final Dialog dialog = new Dialog(WaterSupplyActivity.this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.water_supply_details_view_dialog);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

            ImageView water_supply_icon = dialog.findViewById(R.id.water_supply_icon);
            ImageView close_icon = dialog.findViewById(R.id.close_icon);
            LinearLayout water_supply_reason_layout = dialog.findViewById(R.id.water_supply_reason_layout);
            LinearLayout water_supply_through_layout = dialog.findViewById(R.id.water_supply_through_layout);
            TextView habitation_name = dialog.findViewById(R.id.habitation_name);
            TextView status_name = dialog.findViewById(R.id.status_name);
            TextView reason_name = dialog.findViewById(R.id.reason_name);
            TextView total_number_of_trips = dialog.findViewById(R.id.total_number_of_trips);
            TextView water_supply_litter = dialog.findViewById(R.id.water_supply_litter);
            TextView amount = dialog.findViewById(R.id.amount);
            TextView water_supply_through_name = dialog.findViewById(R.id.water_supply_through_name);
            RelativeLayout total_no_of_trips_layout = dialog.findViewById(R.id.total_no_of_trips_layout);
            RelativeLayout water_supply_litter_layout = dialog.findViewById(R.id.water_supply_litter_layout);
            RelativeLayout amount_layout = dialog.findViewById(R.id.amount_layout);

            close_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            habitation_name.setText(water_supply_hab_name);
            if(water_supply_status.equals("1")){
                status_name.setText(getResources().getString(R.string.full_supply));
                water_supply_icon.setImageResource(R.drawable.full_water_supply);
            }
            else if(water_supply_status.equals("2")){
                status_name.setText(getResources().getString(R.string.partial_supply));
                water_supply_icon.setImageResource(R.drawable.partial_water_supply);
            }
            else {
                status_name.setText(getResources().getString(R.string.no_supply));
                water_supply_icon.setImageResource(R.drawable.no_water_supply);
            }
            if(!reason_for_supply.equals("")){
                water_supply_reason_layout.setVisibility(View.VISIBLE);
                reason_name.setText(reason_for_supply);
            }
            else {
                water_supply_reason_layout.setVisibility(View.GONE);
                reason_name.setText(reason_for_supply);
            }

            if(!water_supply_through.equals("")){
                water_supply_through_layout.setVisibility(View.VISIBLE);
                water_supply_through_name.setText(water_supply_through);
            }
            else {
                water_supply_through_layout.setVisibility(View.GONE);
                water_supply_through_name.setText(water_supply_through);
            }
            if(!no_trips.equals("")){
                total_no_of_trips_layout.setVisibility(View.VISIBLE);
                total_number_of_trips.setText(no_trips);
            }
            else {
                total_no_of_trips_layout.setVisibility(View.GONE);
                water_supply_through_name.setText(no_trips);
            }
            if(!watersupplyliter.equals("")){
                water_supply_litter_layout.setVisibility(View.VISIBLE);
                water_supply_litter.setText(watersupplyliter);
            }
            else {
                water_supply_litter_layout.setVisibility(View.GONE);
                water_supply_litter.setText(watersupplyliter);
            }
            if(!partial_amount.equals("")){
                amount_layout.setVisibility(View.VISIBLE);
                amount.setText(partial_amount);
            }
            else {
                amount_layout.setVisibility(View.GONE);
                amount.setText(partial_amount);
            }
            dialog.getWindow().getAttributes().windowAnimations = R.style.PauseDialogAnimation;
            dialog.show();
            dialog.setCanceledOnTouchOutside(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
