package com.nic.tnebnewwatersource.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Criteria;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.CommonAdapter;
import com.nic.tnebnewwatersource.adapter.DrinkingWaterServerAdapter;
import com.nic.tnebnewwatersource.adapter.DrinkingWaterUploadAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ActivityDrinkingWaterSourceSaveBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.MyLocationListener;
import com.nic.tnebnewwatersource.utils.CameraUtils;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;

public class DrinkingWaterSourceSave extends AppCompatActivity implements  Api.ServerResponseListener{
    private ActivityDrinkingWaterSourceSaveBinding drinkingWaterSourceSaveBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public DBHelper dbHelper;
    public SQLiteDatabase db;
    ArrayList<TNEBSystem> habitationList;
    ArrayList<TNEBSystem> waterSourceTypeList;
    ArrayList<TNEBSystem> localWaterSourceDetailsList;
    ArrayList<TNEBSystem> serverWaterSourceDetailsList;

    String hab_code="";
    String hab_name="";
    String water_source_type_id="";
    String water_source_type_name="";

    String image_flag="";
    Double wayLatitude = 0.0, wayLongitude = 0.0;
    Double first_image_latitude = 0.0, first_image_longtitude = 0.0;
    Double second_image_latitude = 0.0, second_image_longtitude = 0.0;
    public static final int MEDIA_TYPE_IMAGE = 1;

    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static String imageStoragePath;
    public static final int BITMAP_SAMPLE_SIZE = 8;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    byte[] first_image_byteArray;
    byte[] second_image_byteArray;
    DrinkingWaterUploadAdapter drinkingWaterUploadAdapter;
    DrinkingWaterServerAdapter drinkingWaterServerAdapter;

    String water_source_details_primary_id ="";
    String water_source_details_id ="0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        drinkingWaterSourceSaveBinding = DataBindingUtil.setContentView(this, R.layout.activity_drinking_water_source_save);
        drinkingWaterSourceSaveBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            dbData.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        initialzeUI();

        drinkingWaterSourceSaveBinding.habitationSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    hab_code = habitationList.get(position).getHabitation_code();
                    hab_name = habitationList.get(position).getHabitation_name();


                }
                else {
                    hab_code ="";
                    hab_name ="";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        drinkingWaterSourceSaveBinding.waterSourceTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                if(position>0){
                    water_source_type_id = waterSourceTypeList.get(position).getWater_source_type_id();
                    water_source_type_name = waterSourceTypeList.get(position).getWater_source_type_name();

                }
                else {
                    water_source_type_id ="";
                    water_source_type_name ="";

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        drinkingWaterSourceSaveBinding.captureFirstImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_flag ="capture_first_image";
                getExactLocation();
            }
        });
        drinkingWaterSourceSaveBinding.captureSecondImageLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                image_flag ="capture_second_image";
                getExactLocation();
            }
        });

        drinkingWaterSourceSaveBinding.saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drinkingWaterSourceSaveBinding.saveBtn.setEnabled(false);
                fieldValidation();
            }
        });
        drinkingWaterSourceSaveBinding.viewIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewLocallySavedDrinkingWaterDetails();
            }
        });
        drinkingWaterSourceSaveBinding.viewOnlineDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                get_server_drinking_water_details();
            }
        });
    }

    private void initialzeUI() {
        if(prefManager.getkey_no_of_photos().equals("1")){
            drinkingWaterSourceSaveBinding.captureFirstImageLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.captureSecondImageLayout.setVisibility(View.GONE);
        }
        else if(prefManager.getkey_no_of_photos().equals("2")){
            drinkingWaterSourceSaveBinding.captureFirstImageLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.captureSecondImageLayout.setVisibility(View.VISIBLE);
        }
        drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.VISIBLE);
        drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.GONE);
        drinkingWaterSourceSaveBinding.locallySavedRecycler.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        habitationFilterSpinner(prefManager.getDistrictCode(),prefManager.getBlockCode(),prefManager.getPvCode());
        waterSourceTypeSpinner();
    }

    public void habitationFilterSpinner(String dcode, String bcode, String pvcode) {
        Cursor HABList = null;
        HABList = db.rawQuery("SELECT * FROM " + DBHelper.HABITATION_TABLE_NAME + " where dcode = '" + dcode + "'and bcode = '" + bcode + "' and pvcode = '" + pvcode + "' order by habitation_name asc", null);
        habitationList = new ArrayList<>();
        habitationList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setHabitation_code("0");
        habitationListValue.setHabitation_name("Select Habitation");
        habitationListValue.setHabitation_name_ta("குக்கிராமத்தைத் தேர்ந்தெடுக்கவும்");
        habitationList.add(habitationListValue);
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
        drinkingWaterSourceSaveBinding.habitationSpinner.setAdapter(new CommonAdapter(this, habitationList, "HabitationList"));
    }
    public void waterSourceTypeSpinner() {
        dbData.open();
        waterSourceTypeList = new ArrayList<>();
        waterSourceTypeList.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setWater_source_type_id("0");
        habitationListValue.setWater_source_type_name("Select Water Source");
        waterSourceTypeList.add(habitationListValue);
        waterSourceTypeList.addAll(dbData.getAll_drinking_water_source_type());
        drinkingWaterSourceSaveBinding.waterSourceTypeSpinner.setAdapter(new CommonAdapter(this, waterSourceTypeList, "waterSourceTypeList"));
    }

    ///capture methods
    private void captureImage1() {

        if(wayLatitude!=0.0 && wayLongitude!=0.0) {
            if (image_flag.equals("capture_first_image")) {
                first_image_latitude = wayLatitude;
                first_image_longtitude = wayLongitude;
            }
            if (image_flag.equals("capture_second_image")) {
                second_image_latitude = wayLatitude;
                second_image_longtitude = wayLongitude;
            }
            launchCamera();
        }
        else {
            getExactLocation();
        }

    }
    public void getExactLocation() {

        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();

        Criteria criteria = new Criteria();
        criteria.setAccuracy(Criteria.ACCURACY_FINE);
        criteria.setPowerRequirement(Criteria.POWER_HIGH);
        criteria.setAltitudeRequired(false);
        criteria.setSpeedRequired(false);
        criteria.setCostAllowed(true);
        criteria.setBearingRequired(false);

        //API level 9 and up
        criteria.setHorizontalAccuracy(Criteria.ACCURACY_HIGH);
        criteria.setVerticalAccuracy(Criteria.ACCURACY_HIGH);
        Integer gpsFreqInMillis = 1000;
        Integer gpsFreqInDistance = 1;
        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(DrinkingWaterSourceSave.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            //mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);
            mlocManager.requestLocationUpdates(gpsFreqInMillis, gpsFreqInDistance, criteria, mlocListener, null);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            // check permission
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    && ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, CAMERA},
                        1000);
                // reuqest for permission

            }
            else {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                locationRequest = LocationRequest.create();
                locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                //locationRequest.setInterval(0);

                mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        wayLatitude = location.getLatitude();
                        wayLongitude = location.getLongitude();
                        Log.d("LocationAccuracy", "" + location.getAccuracy());
                        Log.d("Locations", "" + wayLatitude + "," + wayLongitude);
                        if (CameraUtils.checkPermissions(DrinkingWaterSourceSave.this)) {
                            captureImage1();
                        } else {
                            requestCameraPermission(MEDIA_TYPE_IMAGE);
                        }
                    } else {
                        Utils.showAlert(DrinkingWaterSourceSave.this, getResources().getString(R.string.satelite_communication_not_available));

                    }
                });
            }
        }
        else {
            Utils.showAlert(DrinkingWaterSourceSave.this, getResources().getString(R.string.gps_is_not_turned_on));
        }


    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

                    locationRequest = LocationRequest.create();
                    locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
                    //locationRequest.setInterval(0);

                    mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    mlocListener = new MyLocationListener();


                    // permission was granted, yay! Do the
                    // location-related task you need to do.
                    if (ContextCompat.checkSelfPermission(DrinkingWaterSourceSave.this,
                            ACCESS_FINE_LOCATION)
                            == PackageManager.PERMISSION_GRANTED) {

                        //Request location updates:
                        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

                    }

                    if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                        mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                            if (location != null) {
                                wayLatitude = location.getLatitude();
                                wayLongitude = location.getLongitude();
                                Log.d("LocationAccuracy", "" + location.getAccuracy());
                                Log.d("Locations", "" + wayLatitude + "," + wayLongitude);
                                if (CameraUtils.checkPermissions(DrinkingWaterSourceSave.this)) {
                                    captureImage1();
                                } else {
                                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                                }
                            } else {
                                Utils.showAlert(DrinkingWaterSourceSave.this, getResources().getString(R.string.satelite_communication_not_available));
                            }
                        });
                    }
                    else {
                        Utils.showAlert(DrinkingWaterSourceSave.this, getResources().getString(R.string.gps_is_not_turned_on));
                    }
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }


                break;
            }
        }
    }
    private void requestCameraPermission(final int type) {
        Dexter.withActivity(this)
                .withPermissions(CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {

                            if (type == MEDIA_TYPE_IMAGE) {
                                // capture picture
                                captureImage1();
                            } else {
//                                captureVideo();
                            }

                        } else if (report.isAnyPermissionPermanentlyDenied()) {
                            showPermissionsAlert();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();
    }
    private void showPermissionsAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(getResources().getString(R.string.permissions_required))
                .setMessage(getResources().getString(R.string.camera_needs_few_permissions))
                .setPositiveButton(getResources().getString(R.string.go_to_settings), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        CameraUtils.openSettings(DrinkingWaterSourceSave.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }
    private void launchCamera() {

        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CROP, false);//default is false
        startActivityForResult(intent, 1213);

    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case  1213:
                if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
                    String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                    Bitmap rotatedBitmap = BitmapFactory.decodeFile(filePath);
                    //rotatedBitmap = getResizedBitmap(rotatedBitmap,700);
                    if(image_flag.equals("capture_first_image")){
                        drinkingWaterSourceSaveBinding.previewIcon.setVisibility(View.GONE);
                        drinkingWaterSourceSaveBinding.firstImageIcon.setVisibility(View.VISIBLE);
                        drinkingWaterSourceSaveBinding.firstImageIcon.setImageBitmap(rotatedBitmap);
                        first_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }
                    else if(image_flag.equals("capture_second_image")) {
                        drinkingWaterSourceSaveBinding.secondPreviewIcon.setVisibility(View.GONE);
                        drinkingWaterSourceSaveBinding.secondImageIcon.setVisibility(View.VISIBLE);
                        drinkingWaterSourceSaveBinding.secondImageIcon.setImageBitmap(rotatedBitmap);
                        second_image_byteArray=BitmaptoByteArray(rotatedBitmap);
                    }


                }
                break;

            default:
                break;
        }
    }
    public byte[] BitmaptoByteArray(Bitmap bmp){
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
        byte[] byteArray = stream.toByteArray();
        //bmp.recycle();
        return byteArray;
    }
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

    public void fieldValidation(){
        if(!hab_code.equals("")){
            if(!water_source_type_id.equals("")){
                if(!drinkingWaterSourceSaveBinding.landMarkEt.getText().toString().equals("")){
                    if(prefManager.getkey_no_of_photos().equals("1")){
                        if(drinkingWaterSourceSaveBinding.firstImageIcon.getDrawable()!=null){
                            saveLocally();
                        }
                        else {
                            drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                            Utils.showAlert(DrinkingWaterSourceSave.this,"Please Capture first image");
                        }
                    }
                    else {
                        if(drinkingWaterSourceSaveBinding.firstImageIcon.getDrawable()!=null){
                            if(drinkingWaterSourceSaveBinding.secondImageIcon.getDrawable()!=null){
                                saveLocally();
                            }
                            else {
                                drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                                Utils.showAlert(DrinkingWaterSourceSave.this,"Please Capture Second image");
                            }
                        }
                        else {
                            drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                            Utils.showAlert(DrinkingWaterSourceSave.this,"Please Capture first image");
                        }
                    }
                }
                else {
                    drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                    Utils.showAlert(DrinkingWaterSourceSave.this,"Please Enter Land Mark");
                }
            }
            else {
                drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                Utils.showAlert(DrinkingWaterSourceSave.this,"Please Select Water Source Type");
            }
        }
        else {
            drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
            Utils.showAlert(DrinkingWaterSourceSave.this,getResources().getString(R.string.select_habitation));
        }
    }
    public void saveLocally(){
        long inserted_id=0;
        dbData.open();
        try {
            String landmark = drinkingWaterSourceSaveBinding.landMarkEt.getText().toString();
            ContentValues contentValues = new ContentValues();
            contentValues.put("water_source_details_id",water_source_details_id);
            contentValues.put("dcode",prefManager.getDistrictCode());
            contentValues.put("bcode",prefManager.getBlockCode());
            contentValues.put("pv_code",prefManager.getPvCode());
            contentValues.put("hab_code",hab_code);
            contentValues.put("hab_name",hab_name);
            contentValues.put("no_of_photos",prefManager.getkey_no_of_photos());
            contentValues.put("water_source_type_id",water_source_type_id);
            contentValues.put("water_source_type_name",water_source_type_name);
            contentValues.put("landmark",landmark);
            contentValues.put("image_1",first_image_byteArray);
            contentValues.put("image_1_lat",first_image_latitude);
            contentValues.put("image_1_long",first_image_longtitude);
            if(prefManager.getkey_no_of_photos().equals("2")){
                contentValues.put("image_2",second_image_byteArray);
                contentValues.put("image_2_lat",second_image_latitude);
                contentValues.put("image_2_long",second_image_longtitude);
            }

            inserted_id = db.insert(DBHelper.DRINKING_WATER_SOURCE_VILLAGE_LEVEL,null,contentValues);
            if(inserted_id>0){
                drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                drinkingWaterSourceSaveBinding.habitationSpinner.setSelection(0);
                drinkingWaterSourceSaveBinding.waterSourceTypeSpinner.setSelection(0);
                drinkingWaterSourceSaveBinding.landMarkEt.setText("");
                drinkingWaterSourceSaveBinding.landMarkEt.setText("");
                drinkingWaterSourceSaveBinding.firstImageIcon.setImageDrawable(null);
                drinkingWaterSourceSaveBinding.secondImageIcon.setImageDrawable(null);
                drinkingWaterSourceSaveBinding.firstImageIcon.setVisibility(View.GONE);
                drinkingWaterSourceSaveBinding.previewIcon.setVisibility(View.VISIBLE);
                drinkingWaterSourceSaveBinding.secondPreviewIcon.setVisibility(View.VISIBLE);
                first_image_latitude=0.0;
                first_image_longtitude=0.0;
                second_image_latitude=0.0;
                second_image_longtitude=0.0;
                wayLatitude=0.0;
                wayLongitude=0.0;
                water_source_details_id="0";
                first_image_byteArray=null;
                second_image_byteArray=null;
                Toasty.success(DrinkingWaterSourceSave.this,getResources().getString(R.string.images_inserted_successfully),Toasty.LENGTH_SHORT,true).show();
            }
            else {
                drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
                Toasty.error(DrinkingWaterSourceSave.this,getResources().getString(R.string.failed),Toasty.LENGTH_SHORT,true).show();

            }

        }
        catch (Exception e){
            drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
            e.printStackTrace();
        }
    }

    private void viewLocallySavedDrinkingWaterDetails(){
        localWaterSourceDetailsList = new ArrayList<>();
        localWaterSourceDetailsList.clear();
        dbData.open();
        localWaterSourceDetailsList.addAll(dbData.getDrinkingWaterDetailsImages("","All"));
        if(localWaterSourceDetailsList.size()>0){
            drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.noDataFound.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.locallySavedRecycler.setVisibility(View.VISIBLE);
            drinkingWaterUploadAdapter = new DrinkingWaterUploadAdapter(DrinkingWaterSourceSave.this,localWaterSourceDetailsList,dbData);
            drinkingWaterSourceSaveBinding.locallySavedRecycler.setAdapter(drinkingWaterUploadAdapter);
        }
        else {
            drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.noDataFound.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.locallySavedRecycler.setVisibility(View.GONE);
        }
    }

    public void save_and_delete_alert(JSONObject saveDetailsDataSet, String water_source_details_primary_id, String save_delete){
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
                        syncTrackData(saveDetailsDataSet, water_source_details_primary_id);
                        dialog.dismiss();
                    }
                    else if(save_delete.equals("delete")) {
                        db.delete(DBHelper.DRINKING_WATER_SOURCE_VILLAGE_LEVEL, "water_source_details_primary_id = ?", new String[]{water_source_details_primary_id});
                        viewLocallySavedDrinkingWaterDetails();
                        drinkingWaterUploadAdapter.notifyDataSetChanged();
                        dialog.dismiss();
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    public void syncTrackData(JSONObject saveDetailsDataSet,String water_source_details_primary_id_) {
        water_source_details_primary_id =water_source_details_primary_id_;
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
                    db.delete(DBHelper.DRINKING_WATER_SOURCE_VILLAGE_LEVEL, "water_source_details_primary_id = ?", new String[]{water_source_details_primary_id});
                    viewLocallySavedDrinkingWaterDetails();
                    drinkingWaterUploadAdapter.notifyDataSetChanged();

                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            }

            if ("get_server_drinking_water_details".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("server_drinking", "" + responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    new Insert_server_drinking_water_details().execute(jsonObject);
                }
                else {
                    drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.GONE);
                    drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.VISIBLE);
                    drinkingWaterSourceSaveBinding.noDataFound.setVisibility(View.VISIBLE);
                    drinkingWaterSourceSaveBinding.locallySavedRecycler.setVisibility(View.GONE);
                }

            }


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
    @Override
    public void OnError(VolleyError volleyError) {

    }

    public void get_server_drinking_water_details() {
        try {
            new ApiService(this).makeJSONObjectRequest("get_server_drinking_water_details", Api.Method.POST, UrlGenerator.getMainServiceUrl(), get_server_drinking_water_details_JSONParams(), "not cache", this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
    public JSONObject get_server_drinking_water_details_JSONParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), get_server_drinking_water_details_normal_json().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("server_drinking", "" + dataSet);
        return dataSet;
    }
    public static JSONObject get_server_drinking_water_details_normal_json() throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "drinking_water_source_village_level_view");
        Log.d("server_drinking", "" + dataSet);
        return dataSet;
    }
    @SuppressLint("StaticFieldLeak")
    public class Insert_server_drinking_water_details extends AsyncTask<JSONObject, Void, Void> {
        @Override
        protected Void doInBackground(JSONObject... params) {
            if (params.length > 0) {
                dbData.open();
                dbData.delete_DRINKING_WATER_SOURCE_SERVER_DATA();
                JSONArray jsonArray = new JSONArray();
                try {
                    jsonArray = params[0].getJSONArray(AppConstant.JSON_DATA);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                serverWaterSourceDetailsList = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    TNEBSystem ListValue = new TNEBSystem();
                    try {
                        ListValue.setWater_source_details_id(jsonArray.getJSONObject(i).getString("water_source_details_id"));
                        ListValue.setWater_source_type_name(jsonArray.getJSONObject(i).getString("water_source_type_name"));
                        ListValue.setDistictCode(jsonArray.getJSONObject(i).getString("dcode"));
                        ListValue.setBlockCode(jsonArray.getJSONObject(i).getString("bcode"));
                        ListValue.setPvCode(jsonArray.getJSONObject(i).getString("pvcode"));
                        ListValue.setHabitation_code(jsonArray.getJSONObject(i).getString("hab_code"));
                        ListValue.setHabitation_name(jsonArray.getJSONObject(i).getString("habitation_name"));
                        ListValue.setWater_source_type_id(jsonArray.getJSONObject(i).getString("water_source_type_id"));
                        ListValue.setKEY_LAND_MARK(jsonArray.getJSONObject(i).getString("landmark"));
                        if(jsonArray.getJSONObject(i).isNull("photo_file_name_1")||jsonArray.getJSONObject(i).getString("photo_file_name_1").equals("")){
                            ListValue.setImage_1_lat("");
                            ListValue.setImage_1_long("");
                            ListValue.setImage_1("");
                        }
                        else {
                            ListValue.setImage_1_lat(jsonArray.getJSONObject(i).getString("photo_file_name_1_lat"));
                            ListValue.setImage_1_long(jsonArray.getJSONObject(i).getString("photo_file_name_1_long"));
                            ListValue.setImage_1(jsonArray.getJSONObject(i).getString("image_1"));
                        }
                        if(jsonArray.getJSONObject(i).isNull("photo_file_name_2")||jsonArray.getJSONObject(i).getString("photo_file_name_2").equals("")){
                            ListValue.setImage_2_lat("");
                            ListValue.setImage_2_long("");
                            ListValue.setImage_2("");
                        }
                        else {
                            ListValue.setImage_2_lat(jsonArray.getJSONObject(i).getString("photo_file_name_2_lat"));
                            ListValue.setImage_2_long(jsonArray.getJSONObject(i).getString("photo_file_name_2_long"));
                            ListValue.setImage_2(jsonArray.getJSONObject(i).getString("image_2"));
                        }
                        dbData.Insert_drinking_water_source_server_data(ListValue);
                        //serverWaterSourceDetailsList.add(ListValue);
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
            setAdapter();

        }
    }
    public void setAdapter(){
        dbData.open();
        serverWaterSourceDetailsList.addAll(dbData.getDrinkingWaterServerDetailsImages("All",""));
        if(serverWaterSourceDetailsList.size()>0){
            drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.noDataFound.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.locallySavedRecycler.setVisibility(View.VISIBLE);
            drinkingWaterServerAdapter = new DrinkingWaterServerAdapter(DrinkingWaterSourceSave.this,serverWaterSourceDetailsList,dbData);
            drinkingWaterSourceSaveBinding.locallySavedRecycler.setAdapter(drinkingWaterServerAdapter);
            drinkingWaterServerAdapter.notifyDataSetChanged();
        }
        else {
            drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.noDataFound.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.locallySavedRecycler.setVisibility(View.GONE);
        }

    }

    ///Edit Server Data
    public void edit_alert(int pos){
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            text.setText("Do you want to edit?");

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
                    setTheValuesFromServer(pos);
                    dialog.dismiss();
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void setTheValuesFromServer(int pos){
        drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.VISIBLE);
        drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.GONE);
        hab_code = serverWaterSourceDetailsList.get(pos).getHabitation_code();
        hab_name = serverWaterSourceDetailsList.get(pos).getHabitation_name();
        water_source_type_id = serverWaterSourceDetailsList.get(pos).getWater_source_type_id();
        water_source_type_name = serverWaterSourceDetailsList.get(pos).getWater_source_type_name();
        drinkingWaterSourceSaveBinding.landMarkEt.setText(serverWaterSourceDetailsList.get(pos).getKEY_LAND_MARK());
        if(serverWaterSourceDetailsList.get(pos).getImage_1()!=null&&!serverWaterSourceDetailsList.get(pos).getImage_1().equals("")){
            drinkingWaterSourceSaveBinding.firstImageIcon.setImageBitmap(StringToBitMap(serverWaterSourceDetailsList.get(pos).getImage_1()));
            drinkingWaterSourceSaveBinding.captureFirstImageLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.firstImageIcon.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.previewIcon.setVisibility(View.GONE);
            first_image_latitude = Double.parseDouble(serverWaterSourceDetailsList.get(pos).getImage_1_lat());
            first_image_longtitude = Double.parseDouble(serverWaterSourceDetailsList.get(pos).getImage_1_long());
            first_image_byteArray = BitmaptoByteArray(StringToBitMap(serverWaterSourceDetailsList.get(pos).getImage_1()));
        }
        else {
            drinkingWaterSourceSaveBinding.firstImageIcon.setImageDrawable(null);
            drinkingWaterSourceSaveBinding.captureFirstImageLayout.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.previewIcon.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.firstImageIcon.setVisibility(View.GONE);
            first_image_latitude = 0.0;
            first_image_longtitude = 0.0;
            first_image_byteArray = null;
        }
        if(serverWaterSourceDetailsList.get(pos).getImage_2()!=null&&!serverWaterSourceDetailsList.get(pos).getImage_2().equals("")){
            drinkingWaterSourceSaveBinding.secondImageIcon.setImageBitmap(StringToBitMap(serverWaterSourceDetailsList.get(pos).getImage_2()));
            drinkingWaterSourceSaveBinding.captureSecondImageLayout.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.secondImageIcon.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.secondPreviewIcon.setVisibility(View.GONE);
            second_image_latitude = Double.parseDouble(serverWaterSourceDetailsList.get(pos).getImage_2_lat());
            second_image_longtitude = Double.parseDouble(serverWaterSourceDetailsList.get(pos).getImage_2_long());
            second_image_byteArray = BitmaptoByteArray(StringToBitMap(serverWaterSourceDetailsList.get(pos).getImage_2()));
        }
        else {
            drinkingWaterSourceSaveBinding.secondImageIcon.setImageDrawable(null);
            drinkingWaterSourceSaveBinding.captureSecondImageLayout.setVisibility(View.GONE);
            drinkingWaterSourceSaveBinding.secondPreviewIcon.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.secondImageIcon.setVisibility(View.GONE);
            second_image_latitude = 0.0;
            second_image_longtitude = 0.0;
            second_image_byteArray = null;
        }
            water_source_details_id=serverWaterSourceDetailsList.get(pos).getWater_source_details_id();


        drinkingWaterSourceSaveBinding.saveBtn.setEnabled(true);
        drinkingWaterSourceSaveBinding.habitationSpinner.setSelection(getHabitationSpinnerIndex(hab_code));
        drinkingWaterSourceSaveBinding.waterSourceTypeSpinner.setSelection(getWaterSourceSpinnerIndex(water_source_type_id));

    }
    private int getHabitationSpinnerIndex(String myString){

        int index = 0;

        for (int i=0;i<habitationList.size();i++){
            if (habitationList.get(i).getHabitation_code().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    private int getWaterSourceSpinnerIndex(String myString){

        int index = 0;

        for (int i=0;i<waterSourceTypeList.size();i++){
            if (waterSourceTypeList.get(i).getWater_source_type_id().equals(myString)){
                index = i;
            }
        }
        return index;
    }
    public Bitmap StringToBitMap(String encodedString){
        try {
//            byte [] encodeByte=Base64.decode(encodedString,Base64.DEFAULT);
            byte [] encodeByte= Base64.decode(encodedString,0);
            Bitmap bitmap= BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch(Exception e) {
            e.getMessage();
            return null;
        }
    }
    @Override
    public void onBackPressed() {
        if(drinkingWaterSourceSaveBinding.viewLayout.getVisibility()==View.VISIBLE){
            drinkingWaterSourceSaveBinding.scrollView.setVisibility(View.VISIBLE);
            drinkingWaterSourceSaveBinding.viewLayout.setVisibility(View.GONE);
        }
        else {
            super.onBackPressed();
            overridePendingTransition(R.anim.slide_in,R.anim.slide_out);
        }

    }
}
