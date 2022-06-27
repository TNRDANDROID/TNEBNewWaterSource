package com.nic.tnebnewwatersource.activity;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.Interface.recyclerItemClicked;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.CommonAdapter;
import com.nic.tnebnewwatersource.adapter.DrinkingWaterUploadAdapter;
import com.nic.tnebnewwatersource.adapter.EveningWaterSupplyTimimgAdapter;
import com.nic.tnebnewwatersource.adapter.HabitationAdapter;
import com.nic.tnebnewwatersource.adapter.NewDrinkingWaterUploadAdapter;
import com.nic.tnebnewwatersource.adapter.SourceOfWaterAdapter;
import com.nic.tnebnewwatersource.adapter.WaterSessionAdapter;
import com.nic.tnebnewwatersource.adapter.WaterSupplyTimingAdapter;
import com.nic.tnebnewwatersource.adapter.WaterTypeAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ActivityNewWaterSupplyStatusEntryFormBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import es.dmoral.toasty.Toasty;

public class NewWaterSupplyStatusEntryForm extends AppCompatActivity implements recyclerItemClicked ,Api.ServerResponseListener{

    ActivityNewWaterSupplyStatusEntryFormBinding waterSupplyStatusEntryFormBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public DBHelper dbHelper;
    public SQLiteDatabase db;

    ////ArrayLists
    ArrayList<TNEBSystem> minMaxDateList;
    ArrayList<TNEBSystem> villageList;
    ArrayList<TNEBSystem> habitationList;
    ArrayList<TNEBSystem> reasonList;
    ArrayList<TNEBSystem> waterSupplyTimingList;
    ArrayList<TNEBSystem> waterSessionList;
    ArrayList<TNEBSystem> waterTypeList;
    ArrayList<TNEBSystem> waterSourceList;
    ArrayList<TNEBSystem> localWaterSourceDetailsList;
    ////Recycler Adapter
    HabitationAdapter habitationAdapter;
    WaterSupplyTimingAdapter morningWaterSupplyTimingAdapter;
    EveningWaterSupplyTimimgAdapter eveningWaterSupplyTimimgAdapter;
    WaterSessionAdapter waterSessionAdapter;
    WaterTypeAdapter waterTypeAdapter;
    SourceOfWaterAdapter sourceOfWaterAdapter;
    NewDrinkingWaterUploadAdapter newDrinkingWaterUploadAdapter;

    //Radio Button items
    String water_supply_status="";

    ////Spinner Items Clicked
    String pv_code="";
    String pv_name="";
    String reason_id="";
    String reason_name="";

    ////////Date Picker Items
    private int mYear, mMonth, mDay;
    String maximum_date ="";
    String minimum_date ="";
    /////Recycler Item clicked Values
    String hab_code="";
    String hab_name="";
    String water_type_code="";
    String water_type_name="";
    String water_session_code="";
    String water_session_name="";
    String morning_water_supply_timing_code ="";
    String morning_water_supply_timing_name ="";
    String evening_water_supply_timing_code="";
    String evening_water_supply_timing_name="";
    String water_source_details_id="";
    String water_source_type_name="";

    String new_water_details_primary_id="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        waterSupplyStatusEntryFormBinding = DataBindingUtil.setContentView(this, R.layout.activity_new_water_supply_status_entry_form);
        waterSupplyStatusEntryFormBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }

        initializeUI();

        waterSupplyStatusEntryFormBinding.villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        pv_code = villageList.get(position).getPvCode();
                        pv_name = villageList.get(position).getPvName();
                        hab_code="";
                        hab_name ="";
                        habitationFilterAdapter(prefManager.getDistrictCode(),prefManager.getBlockCode(),pv_code);
                        water_supply_status="";
                        waterSupplyStatusEntryFormBinding.visibleGoneLayout.setVisibility(View.GONE);
                        waterSupplyStatusEntryFormBinding.selectReasonLayout.setVisibility(View.GONE);
                        water_type_code="";
                        water_type_name="";
                        water_session_code="";
                        water_session_name="";
                        morning_water_supply_timing_code ="";
                        morning_water_supply_timing_name ="";
                        evening_water_supply_timing_code="";
                        evening_water_supply_timing_name="";
                        water_source_details_id="";
                        water_source_type_name="";
                        setSourceOfWaterAdapter();
                        setMorningWaterSupplyTimingAdapter();
                        //setEveningWaterSupplyTimingAdapter();
                        setWaterSessionAdapter();
                        setWaterTypeAdapter();
                        setReasonSpinner();
                    }
                    else {
                        pv_code ="";
                        pv_name = "";
                        hab_code="";
                        hab_name ="";

                        waterSupplyStatusEntryFormBinding.habitationRecycler.setAdapter(null);
                        water_supply_status="";
                        waterSupplyStatusEntryFormBinding.visibleGoneLayout.setVisibility(View.GONE);
                        waterSupplyStatusEntryFormBinding.selectReasonLayout.setVisibility(View.GONE);
                        water_type_code="";
                        water_type_name="";
                        water_session_code="";
                        water_session_name="";
                        morning_water_supply_timing_code ="";
                        morning_water_supply_timing_name ="";
                        evening_water_supply_timing_code="";
                        evening_water_supply_timing_name="";
                        water_source_details_id="";
                        water_source_type_name="";
                        setSourceOfWaterAdapter();
                        setMorningWaterSupplyTimingAdapter();
                        //setEveningWaterSupplyTimingAdapter();
                        setWaterSessionAdapter();
                        setWaterTypeAdapter();
                        setReasonSpinner();
                    }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        waterSupplyStatusEntryFormBinding.reasonSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    if(position>0){
                        reason_id = reasonList.get(position).getId();
                        reason_name = reasonList.get(position).getReason_for_supply();
                    }
                    else {
                        reason_id="";
                        reason_name="";
                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        waterSupplyStatusEntryFormBinding.waterSupplyYes.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    water_supply_status="Y";
                    waterSupplyStatusEntryFormBinding.visibleGoneLayout.setVisibility(View.VISIBLE);
                    waterSupplyStatusEntryFormBinding.selectReasonLayout.setVisibility(View.GONE);
                    water_type_code="";
                    water_type_name="";
                    water_session_code="";
                    water_session_name="";
                    morning_water_supply_timing_code ="";
                    morning_water_supply_timing_name ="";
                    evening_water_supply_timing_code="";
                    evening_water_supply_timing_name="";
                    water_source_details_id="";
                    water_source_type_name="";
                    setSourceOfWaterAdapter();
                    setMorningWaterSupplyTimingAdapter();
                    //setEveningWaterSupplyTimingAdapter();
                    setWaterSessionAdapter();
                    setWaterTypeAdapter();
                    setReasonSpinner();
                }
            }
        });
        waterSupplyStatusEntryFormBinding.waterSupplyNo.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    water_supply_status="N";
                    waterSupplyStatusEntryFormBinding.visibleGoneLayout.setVisibility(View.GONE);
                    waterSupplyStatusEntryFormBinding.selectReasonLayout.setVisibility(View.VISIBLE);
                    water_type_code="";
                    water_type_name="";
                    water_session_code="";
                    water_session_name="";
                    morning_water_supply_timing_code ="";
                    morning_water_supply_timing_name ="";
                    evening_water_supply_timing_code="";
                    evening_water_supply_timing_name="";
                    water_source_details_id="";
                    water_source_type_name="";
                    setSourceOfWaterAdapter();
                    setMorningWaterSupplyTimingAdapter();
                    //setEveningWaterSupplyTimingAdapter();
                    setWaterSessionAdapter();
                    setWaterTypeAdapter();
                    setReasonSpinner();
                }
            }
        });

        waterSupplyStatusEntryFormBinding.datePickerLayout.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                showStartDatePickerDialog();
            }
        });
        waterSupplyStatusEntryFormBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onClick(View v) {
                validationForm1();
            }
        });
        waterSupplyStatusEntryFormBinding.viewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocallySavedDrinkingWaterDetails();
            }
        });


    }

    private void initializeUI(){
        waterSupplyStatusEntryFormBinding.mainDetailsLayout.setVisibility(View.GONE);
        waterSupplyStatusEntryFormBinding.selectReasonLayout.setVisibility(View.GONE);
        waterSupplyStatusEntryFormBinding.waterSuppliedStatusLayout.setVisibility(View.GONE);
        waterSupplyStatusEntryFormBinding.visibleGoneLayout.setVisibility(View.GONE);
        waterSupplyStatusEntryFormBinding.scrollView.setVisibility(View.VISIBLE);
        waterSupplyStatusEntryFormBinding.viewLayout.setVisibility(View.GONE);
        waterSupplyStatusEntryFormBinding.locallySavedRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        waterSupplyStatusEntryFormBinding.habitationRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        waterSupplyStatusEntryFormBinding.morningTimingRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        waterSupplyStatusEntryFormBinding.eveningTimingRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        waterSupplyStatusEntryFormBinding.sessionRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        waterSupplyStatusEntryFormBinding.waterTypeRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        waterSupplyStatusEntryFormBinding.sourceWaterRecycler.setLayoutManager(new GridLayoutManager(getApplicationContext(),2));
        if(prefManager.getkey_levels().equals("B")){
            waterSupplyStatusEntryFormBinding.selectVillageLayout.setVisibility(View.VISIBLE);
            setVillageSpinner();
        }
        else if(prefManager.getkey_levels().equals("V")){
            pv_code = prefManager.getPvCode();
            pv_name = prefManager.getPvName();
            waterSupplyStatusEntryFormBinding.selectVillageLayout.setVisibility(View.GONE);
            habitationFilterAdapter(prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode());
        }
        else {
            waterSupplyStatusEntryFormBinding.selectVillageLayout.setVisibility(View.GONE);
            waterSupplyStatusEntryFormBinding.habitationLayout.setVisibility(View.GONE);
            pv_code = prefManager.getPvCode();
            pv_name = prefManager.getPvName();
            hab_code = prefManager.getHabCode();
            hab_name = prefManager.getHabName();
        }
        getMinimumMaxDateList();
        setMorningWaterSupplyTimingAdapter();
        //setEveningWaterSupplyTimingAdapter();
        setWaterSessionAdapter();
        setWaterTypeAdapter();
        setReasonSpinner();

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

                        waterSupplyStatusEntryFormBinding.dateText.setText(dateFormet(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year));
                        waterSupplyStatusEntryFormBinding.mainDetailsLayout.setVisibility(View.VISIBLE);

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

    public void habitationFilterAdapter(String dcode, String bcode, String pvcode) {
        Cursor HABList = null;
        HABList = db.rawQuery("SELECT * FROM " + DBHelper.HABITATION_TABLE_NAME + " where dcode = '" + dcode + "'and bcode = '" + bcode + "' and pvcode = '" + pvcode + "' order by habitation_name asc", null);
        habitationList = new ArrayList<>();
        habitationList.clear();
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

                    habitationList.add(habList);
                    Log.d("spinnersize", "" + habitationList.size());
                } while (HABList.moveToNext());
            }
        }
        if(habitationList.size()>0){
            waterSupplyStatusEntryFormBinding.habitationRecycler.setVisibility(View.VISIBLE);
            habitationAdapter = new HabitationAdapter(NewWaterSupplyStatusEntryForm.this,habitationList,this::onItemClicked);
            waterSupplyStatusEntryFormBinding.habitationRecycler.setAdapter(habitationAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.habitationRecycler.setVisibility(View.GONE);
        }
    }
    private void getMinimumMaxDateList(){
        dbData.open();
        minMaxDateList = new ArrayList<>();
        minMaxDateList.addAll(dbData.getAll_minimum_max_date());
        if(minMaxDateList.size()>0){
            minimum_date = minMaxDateList.get(0).getMinimum_date();
            maximum_date = minMaxDateList.get(0).getMaximum_date();
        }

    }
    private void setVillageSpinner(){
        dbData.open();
        villageList = new ArrayList<>();
        villageList.addAll(dbData.getAll_Village("Particular",prefManager.getDistrictCode(),prefManager.getBlockCode()));
        if(villageList.size()>0){
            waterSupplyStatusEntryFormBinding.villageSpinner.setAdapter(new CommonAdapter(NewWaterSupplyStatusEntryForm.this, villageList,"VillageList"));
        }

    }
    private void setMorningWaterSupplyTimingAdapter(){
        dbData.open();
        waterSupplyTimingList = new ArrayList<>();
        waterSupplyTimingList.addAll(dbData.getAll_drinking_water_supply_timing());
        if(waterSupplyTimingList.size()>0){
            waterSupplyStatusEntryFormBinding.morningTimingRecycler.setVisibility(View.VISIBLE);
            morningWaterSupplyTimingAdapter = new WaterSupplyTimingAdapter(NewWaterSupplyStatusEntryForm.this,waterSupplyTimingList,this::onItemClicked);
            waterSupplyStatusEntryFormBinding.morningTimingRecycler.setAdapter(morningWaterSupplyTimingAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.morningTimingRecycler.setVisibility(View.GONE);
        }
    }
    private void setEveningWaterSupplyTimingAdapter(){
        dbData.open();
        waterSupplyTimingList = new ArrayList<>();
        waterSupplyTimingList.addAll(dbData.getAll_drinking_water_supply_timing());
        if(waterSupplyTimingList.size()>0){
            waterSupplyStatusEntryFormBinding.eveningTimingRecycler.setVisibility(View.VISIBLE);
            eveningWaterSupplyTimimgAdapter = new EveningWaterSupplyTimimgAdapter(NewWaterSupplyStatusEntryForm.this,waterSupplyTimingList,this::onItemClicked);
            waterSupplyStatusEntryFormBinding.eveningTimingRecycler.setAdapter(eveningWaterSupplyTimimgAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.eveningTimingRecycler.setVisibility(View.GONE);
        }
    }
    private void setReasonSpinner(){
        dbData.open();
        reasonList = new ArrayList<>();
        TNEBSystem tnebSystem = new TNEBSystem();
        tnebSystem.setId("0");
        tnebSystem.setReason_for_supply("Select Reason");
        tnebSystem.setReason_type("");
        reasonList.add(tnebSystem);
        reasonList.addAll(dbData.getAll_water_supply_reason());
        if(reasonList.size()>0){
            waterSupplyStatusEntryFormBinding.reasonSpinner.setAdapter(new CommonAdapter(NewWaterSupplyStatusEntryForm.this,reasonList,"water_supplied_reason"));
        }

    }
    private void setWaterSessionAdapter(){
        dbData.open();
        waterSessionList = new ArrayList<>();
        waterSessionList.addAll(dbData.getAll_drinking_water_session());
        if(waterSessionList.size()>0){
            waterSupplyStatusEntryFormBinding.sessionRecycler.setVisibility(View.VISIBLE);
            waterSessionAdapter = new WaterSessionAdapter(NewWaterSupplyStatusEntryForm.this,waterSessionList,this::onItemClicked);
            waterSupplyStatusEntryFormBinding.sessionRecycler.setAdapter(waterSessionAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.sessionRecycler.setVisibility(View.GONE);
        }
    }
    private void setWaterTypeAdapter(){
        dbData.open();
        waterTypeList = new ArrayList<>();
        waterTypeList.addAll(dbData.getAll_drinking_water_type());
        if(waterTypeList.size()>0){
            waterSupplyStatusEntryFormBinding.waterTypeRecycler.setVisibility(View.VISIBLE);
            waterTypeAdapter = new WaterTypeAdapter(NewWaterSupplyStatusEntryForm.this,waterTypeList,this::onItemClicked);
            waterSupplyStatusEntryFormBinding.waterTypeRecycler.setAdapter(waterTypeAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.waterTypeRecycler.setVisibility(View.GONE);
        }
    }
    public void setSourceOfWaterAdapter(){
        dbData.open();
        waterSourceList  = new ArrayList<>();
        waterSourceList.addAll(dbData.getDrinkingWaterServerDetailsImages("",hab_code));
        if(waterSourceList.size()>0){
            waterSupplyStatusEntryFormBinding.sourceWaterLayout.setVisibility(View.VISIBLE);
            waterSupplyStatusEntryFormBinding.sourceWaterRecycler.setVisibility(View.VISIBLE);
            sourceOfWaterAdapter = new SourceOfWaterAdapter(NewWaterSupplyStatusEntryForm.this,waterSourceList,dbData,this::onItemClicked);
            waterSupplyStatusEntryFormBinding.sourceWaterRecycler.setAdapter(sourceOfWaterAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.sourceWaterLayout.setVisibility(View.GONE);
            waterSupplyStatusEntryFormBinding.sourceWaterRecycler.setVisibility(View.GONE);
        }

    }


    @Override
    public void onItemClicked(int pos, String type) {
        if(type.equals("Habitation")){
            hab_code = habitationList.get(pos).getHabitation_code();
            hab_name = habitationList.get(pos).getHabitation_name();
            water_source_details_id="";
            water_source_type_name="";
            waterSupplyStatusEntryFormBinding.waterSuppliedStatusLayout.setVisibility(View.VISIBLE);
            waterSupplyStatusEntryFormBinding.waterSupplyStatusRadioGroup.clearCheck();
            water_supply_status="";
            waterSupplyStatusEntryFormBinding.visibleGoneLayout.setVisibility(View.GONE);
        }
        else if(type.equals("Water_Session")){
            water_session_code= waterSessionList.get(pos).getSession_id();
            water_session_name= waterSessionList.get(pos).getSession_name();
            getTheAlreadySavedDetailsAndSetTheUI();
        }
        else if(type.equals("Water_Type")){
            if(!water_session_code.equals("")){
                water_type_code = waterTypeList.get(pos).getWater_type_id();
                water_type_name = waterTypeList.get(pos).getWater_type();
            }
            else {
                setWaterTypeAdapter();
                Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Please Choose Session!");
            }

        }
        else if (type.equals("Source_Of_water")) {
            if(!water_session_code.equals("")){
                water_source_details_id  = waterSourceList.get(pos).getWater_source_details_id();
                water_source_type_name = waterSourceList.get(pos).getWater_source_type_name();
            }
            else {
                setSourceOfWaterAdapter();
                Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Please Choose Session!");
            }

        }
       /* else if(type.equals("Evening_Supply_Timing")){
            if(water_session_code.equals("1")){
                morning_water_supply_timing_code = waterSupplyTimingList.get(pos).getSupply_timing_id();
                morning_water_supply_timing_name = waterSupplyTimingList.get(pos).getSupply_timing();
            }
            else {
                evening_water_supply_timing_code = waterSupplyTimingList.get(pos).getSupply_timing_id();
                evening_water_supply_timing_name = waterSupplyTimingList.get(pos).getSupply_timing();
            }
        }*/
        else{
            if(!water_session_code.equals("")){
                if(water_session_code.equals("1")){
                    morning_water_supply_timing_code = waterSupplyTimingList.get(pos).getSupply_timing_id();
                    morning_water_supply_timing_name = waterSupplyTimingList.get(pos).getSupply_timing();
                }
                else {
                    evening_water_supply_timing_code = waterSupplyTimingList.get(pos).getSupply_timing_id();
                    evening_water_supply_timing_name = waterSupplyTimingList.get(pos).getSupply_timing();
                }
            }
            else {
                setMorningWaterSupplyTimingAdapter();
                Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Please Choose Session!");
            }

        }
    }

    public void getTheAlreadySavedDetailsAndSetTheUI(){
        String entry_date = waterSupplyStatusEntryFormBinding.dateText.getText().toString();
        String dcode = prefManager.getDistrictCode();
        String bcode = prefManager.getBlockCode();
        ArrayList<TNEBSystem> listItems = new ArrayList<>();
        dbData.open();
        listItems.addAll(dbData.get_new_drinking_water_details_server1("",entry_date,dcode,bcode,pv_code,hab_code));
        if(listItems.size()>0){
            if (water_session_code.equals("1")){
                int water_type_position = getWaterTypeIndex(listItems.get(0).getSession_fn_water_type_id());
                int water_timing_position = getTimingIndex(listItems.get(0).getSession_fn_timing_id());
                int water_source_position = getSourceWaterIndex(listItems.get(0).getSession_fn_src_id());
                waterSupplyStatusEntryFormBinding.waterTypeRecycler.findViewHolderForAdapterPosition(water_type_position).itemView.findViewById(R.id.card_view).performClick();
                waterSupplyStatusEntryFormBinding.morningTimingRecycler.findViewHolderForAdapterPosition(water_timing_position).itemView.findViewById(R.id.card_view).performClick();
                waterSupplyStatusEntryFormBinding.sourceWaterRecycler.findViewHolderForAdapterPosition(water_source_position).itemView.findViewById(R.id.card_view).performClick();
            }
            else  {
                int water_type_position = getWaterTypeIndex(listItems.get(0).getSession_an_water_type_id());
                int water_timing_position = getTimingIndex(listItems.get(0).getSession_an_timing_id());
                int water_source_position = getSourceWaterIndex(listItems.get(0).getSession_an_src_id());

                waterSupplyStatusEntryFormBinding.waterTypeRecycler.findViewHolderForAdapterPosition(water_type_position).itemView.findViewById(R.id.card_view).performClick();
                waterSupplyStatusEntryFormBinding.morningTimingRecycler.findViewHolderForAdapterPosition(water_timing_position).itemView.findViewById(R.id.card_view).performClick();
                waterSupplyStatusEntryFormBinding.sourceWaterRecycler.findViewHolderForAdapterPosition(water_source_position).itemView.findViewById(R.id.card_view).performClick();

            }
        }


    }

    private int getWaterTypeIndex(String myString){

        int index = 0;

        for (int i=0;i<waterTypeList.size();i++){
            if (waterTypeList.get(i).getWater_type_id().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private int getTimingIndex(String myString){

        int index = 0;

        for (int i=0;i<waterSupplyTimingList.size();i++){
            if (waterSupplyTimingList.get(i).getSupply_timing_id().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private int getSourceWaterIndex(String myString){

        int index = 0;

        for (int i=0;i<waterSourceList.size();i++){
            if (waterSourceList.get(i).getWater_source_details_id().equals(myString)){
                index = i;
            }
        }
        return index;
    }


    public void validationForm1(){
        if(!waterSupplyStatusEntryFormBinding.dateText.getText().toString().equals("")){
            if(!pv_code.equals("")){
                if(!hab_code.equals("")){
                    if(!water_supply_status.equals("")){
                        if(water_supply_status.equals("N")){
                            if(!reason_id.equals("")){
                                saveDataLocally();
                            }
                            else {
                                Utils.showAlert(NewWaterSupplyStatusEntryForm.this,getResources().getString(R.string.select_reason));
                            }

                        }
                        else {
                            validationForm2();
                        }
                    }
                    else {
                        Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Water Supply Status!");
                    }
                }
                else {
                    Utils.showAlert(NewWaterSupplyStatusEntryForm.this,getResources().getString(R.string.select_habitation));
                }
            }
            else {
                Utils.showAlert(NewWaterSupplyStatusEntryForm.this,getResources().getString(R.string.select_village));
            }

        }
        else {
            Utils.showAlert(NewWaterSupplyStatusEntryForm.this,getResources().getString(R.string.choose_date));
        }
    }
    public void validationForm2(){
        if(!water_type_code.equals("")){
            if(!water_session_code.equals("")){
                if(water_session_code.equals("1")){
                    if(!morning_water_supply_timing_code.equals("")){
                        if(!water_source_details_id.equals("")){
                            saveDataLocally();
                        }
                        else {
                            Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Select Source Of Water!");
                        }
                    }
                    else {
                        Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Select Water Supply Timing!");
                    }
                }
                else {
                    if(!evening_water_supply_timing_code.equals("")){
                        if(!water_source_details_id.equals("")){
                            saveDataLocally();
                        }
                        else {
                            Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Select Source Of Water!");
                        }
                    }
                    else {
                        Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Select Water Supply Timing!");
                    }
                }

            }
            else {
                Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Select Water Session!");
            }
        }
        else {
            Utils.showAlert(NewWaterSupplyStatusEntryForm.this,"Select Water Type!");
        }
    }

    private void saveDataLocally(){
        dbData.open();
        String whereClause = "";String[] whereArgs = null;
        long insert_update_id = 0;
        try {
            String entry_date = waterSupplyStatusEntryFormBinding.dateText.getText().toString();
            String dcode = prefManager.getDistrictCode();
            String bcode = prefManager.getBlockCode();

            whereClause = "entry_date = ? and dcode = ? and bcode = ? and pv_code = ? and hab_code = ?";
            whereArgs = new String[]{entry_date,dcode,bcode,pv_code,hab_code};
            ContentValues contentValues = new ContentValues();
            contentValues.put("entry_date",entry_date);
            contentValues.put("dcode",dcode);
            contentValues.put("bcode",bcode);
            contentValues.put("pv_code",pv_code);
            contentValues.put("pv_name",pv_name);
            contentValues.put("hab_code",hab_code);
            contentValues.put("hab_name",hab_name);
            if(water_supply_status.equals("N")){
                contentValues.put("water_supply_status_id","0");
                contentValues.put("no_supply_reason_id",reason_id);
                contentValues.put("no_supply_reason_name",reason_name);
                contentValues.put("water_supply_status_id","0");
                contentValues.put("water_type_id","");
                contentValues.put("water_type_name","");
                contentValues.put("water_session_id","");
                contentValues.put("water_session_name","");
                contentValues.put("morning_water_supply_timing_id","");
                contentValues.put("morning_water_supply_timing_name","");
                contentValues.put("evening_water_supply_timing_id","");
                contentValues.put("evening_water_supply_timing_name","");
                contentValues.put("water_source_details_id","");
                contentValues.put("water_source_type_name","");
            }
            else {
                contentValues.put("water_supply_status_id","1");
                contentValues.put("no_supply_reason_id","");
                contentValues.put("no_supply_reason_name","");
                contentValues.put("water_type_id",water_type_code);
                contentValues.put("water_type_name",water_type_name);
                contentValues.put("water_session_id",water_session_code);
                contentValues.put("water_session_name",water_session_name);
                contentValues.put("water_source_details_id",water_source_details_id);
                contentValues.put("water_source_type_name",water_source_type_name);
                contentValues.put("morning_water_supply_timing_id", morning_water_supply_timing_code);
                contentValues.put("morning_water_supply_timing_name", morning_water_supply_timing_name);
                contentValues.put("evening_water_supply_timing_id", evening_water_supply_timing_code);
                contentValues.put("evening_water_supply_timing_name", evening_water_supply_timing_name);
            }
            ArrayList<TNEBSystem> getCount = dbData.getNewDrinkingWaterDetailsLocal("",entry_date,dcode,bcode,pv_code,hab_code,water_session_code);
            if(getCount.size()>0){

                insert_update_id = db.update(DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL,contentValues,whereClause,whereArgs);
                if(insert_update_id>0){
                    Toasty.success(NewWaterSupplyStatusEntryForm.this,"Updated Successfully",Toasty.LENGTH_SHORT,true).show();
                    restartActivity();
                }
                else {
                    Toasty.error(NewWaterSupplyStatusEntryForm.this,"Fail",Toasty.LENGTH_SHORT,true).show();
                }
            }
            else {
                insert_update_id = db.insert(DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL,null,contentValues);
                if(insert_update_id>0){
                    Toasty.success(NewWaterSupplyStatusEntryForm.this,"Inserted Successfully",Toasty.LENGTH_SHORT,true).show();
                    restartActivity();
                }
                else {
                    Toasty.error(NewWaterSupplyStatusEntryForm.this,"Faild",Toasty.LENGTH_SHORT,true).show();
                }
            }

        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private void viewLocallySavedDrinkingWaterDetails(){
        localWaterSourceDetailsList = new ArrayList<>();
        localWaterSourceDetailsList.clear();
        dbData.open();
        localWaterSourceDetailsList.addAll(dbData.getNewDrinkingWaterDetailsLocal("All","","","","","",""));
        if(localWaterSourceDetailsList.size()>0){
            waterSupplyStatusEntryFormBinding.scrollView.setVisibility(View.GONE);
            waterSupplyStatusEntryFormBinding.viewLayout.setVisibility(View.VISIBLE);
            waterSupplyStatusEntryFormBinding.noDataFound.setVisibility(View.GONE);
            waterSupplyStatusEntryFormBinding.locallySavedRecycler.setVisibility(View.VISIBLE);
            newDrinkingWaterUploadAdapter = new NewDrinkingWaterUploadAdapter(NewWaterSupplyStatusEntryForm.this,localWaterSourceDetailsList,dbData);
            waterSupplyStatusEntryFormBinding.locallySavedRecycler.setAdapter(newDrinkingWaterUploadAdapter);
        }
        else {
            waterSupplyStatusEntryFormBinding.scrollView.setVisibility(View.GONE);
            waterSupplyStatusEntryFormBinding.viewLayout.setVisibility(View.VISIBLE);
            waterSupplyStatusEntryFormBinding.noDataFound.setVisibility(View.VISIBLE);
            waterSupplyStatusEntryFormBinding.locallySavedRecycler.setVisibility(View.GONE);
        }
    }

    public void save_and_delete_alert(JSONObject saveDetailsDataSet, String new_water_details_primary_id, String save_delete){
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
                        syncTrackData(saveDetailsDataSet, new_water_details_primary_id);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        db.delete(DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL, "new_water_details_primary_id = ?", new String[]{new_water_details_primary_id});
                        viewLocallySavedDrinkingWaterDetails();
                        newDrinkingWaterUploadAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void syncTrackData(JSONObject saveDetailsDataSet,String new_water_details_primary_id_) {
        new_water_details_primary_id =new_water_details_primary_id_;
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
                    db.delete(DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL, "new_water_details_primary_id = ?", new String[]{new_water_details_primary_id});
                    viewLocallySavedDrinkingWaterDetails();
                    newDrinkingWaterUploadAdapter.notifyDataSetChanged();
                    get_daily_drinking_water_supply_status_v2_view();

                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }

            if ("water_supply_status_v2".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("water_supply_status_v2", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_new_drinking_water_details_server1().execute(jsonObject);
                }

            }




        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void OnError(VolleyError volleyError) {

    }

    @Override
    public void onBackPressed() {
        if(waterSupplyStatusEntryFormBinding.viewLayout.getVisibility()==View.VISIBLE){
            waterSupplyStatusEntryFormBinding.scrollView.setVisibility(View.VISIBLE);
            waterSupplyStatusEntryFormBinding.viewLayout.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        }

    }

    private void restartActivity(){

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {

                Intent intent = getIntent();
                finish();
                startActivity(intent);
            }
        }, 1000);
    }

    public void get_daily_drinking_water_supply_status_v2_view() {
        try {
            new ApiService(this).makeJSONObjectRequest("water_supply_status_v2", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_daily_drinking_water_supply_status_v2_view_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_daily_drinking_water_supply_status_v2_view_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.daily_drinking_water_supply_status_v2_view_Json(this).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("water_supply_status_v2", "" + dataSet);
        return dataSet;
    }
    public class Insert_new_drinking_water_details_server1 extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_NEW_DRINKING_WATER_DETAILS_SERVER_1();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    TNEBSystem ListValue = new TNEBSystem();
                    try {
                        ListValue.setEntry_date(jsonArray.getJSONObject(i).getString("entry_dt"));
                        ListValue.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                        ListValue.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                        ListValue.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                        ListValue.setHabitation_code(jsonArray.getJSONObject(i).getString("habcode"));
                        ListValue.setWater_supply_status_id(jsonArray.getJSONObject(i).getString("water_supply_status"));
                        ListValue.setWater_supplied_reason_id(jsonArray.getJSONObject(i).getString("no_supply_reason"));
                        ListValue.setSession_fn_water_type_id(jsonArray.getJSONObject(i).getString("session_fn_water_type"));
                        ListValue.setSession_fn_timing_id(jsonArray.getJSONObject(i).getString("session_fn_timing"));
                        ListValue.setSession_fn_src_id(jsonArray.getJSONObject(i).getString("session_fn_src_id"));
                        ListValue.setSession_an_water_type_id(jsonArray.getJSONObject(i).getString("session_an_water_type"));
                        ListValue.setSession_an_timing_id(jsonArray.getJSONObject(i).getString("session_an_timing"));
                        ListValue.setSession_an_src_id(jsonArray.getJSONObject(i).getString("session_an_src_id"));

                        dbData.Insert_new_drinking_water_details_server1(ListValue);
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

        }
    }
}
