package com.nic.tnebnewwatersource.activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.speech.RecognizerIntent;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import com.nic.tnebnewwatersource.ImageZoom.ImageMatrixTouchHandler;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.CommonAdapter;
import com.nic.tnebnewwatersource.adapter.NewConnetionSavedListAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ApiService;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.MissedConnectionActivityBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.support.MyLocationListener;
import com.nic.tnebnewwatersource.support.ProgressHUD;
import com.nic.tnebnewwatersource.utils.CameraUtils;
import com.nic.tnebnewwatersource.utils.UrlGenerator;
import com.nic.tnebnewwatersource.utils.Utils;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Objects;

import es.dmoral.toasty.Toasty;
import in.mayanknagwanshi.imagepicker.ImageSelectActivity;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.CAMERA;
import static android.os.Build.VERSION_CODES.N;
import static com.bumptech.glide.load.resource.bitmap.TransformationUtils.rotateImage;


public class MissedConnectionActivity extends AppCompatActivity implements Api.ServerResponseListener, View.OnClickListener {
    private MissedConnectionActivityBinding missedConnectionActivityBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;

    private ProgressHUD progressHUD;
    String localLanguage = "";
    private List<TNEBSystem> connectionPurposeList = new ArrayList<>();
    private List<TNEBSystem> Habitation = new ArrayList<>();
    List<TNEBSystem> SavedConnectionList;
    String purpose_id = "";
    String habitation_id = "";
    private static String imageStoragePath;
    String image_flag = "";
    String eb_meter_image_string = "";
    public static final int BITMAP_SAMPLE_SIZE = 8;
    public static final int MEDIA_TYPE_IMAGE = 1;
    private static final int PERMISSION_REQUEST_CODE = 200;
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 2500;
    private static final int LAND_MARK_SPEECH_REQUEST_CODE = 103;
    private static final int NAME_SPEECH_REQUEST_CODE = 101;
    LocationManager mlocManager = null;
    LocationListener mlocListener;
    Double offlatTextValue = 0.0, offlongTextValue = 0.0;
    NewConnetionSavedListAdapter newConnetionSavedListAdapter;

    private FusedLocationProviderClient mFusedLocationClient;
    private LocationRequest locationRequest;
    private LocationCallback locationCallback;

    Double wayLatitude = 0.0, wayLongitude = 0.0;

    private static final int CAPTURE_MEDIA = 358;
    private AppCompatActivity activity;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        missedConnectionActivityBinding = DataBindingUtil.setContentView(this, R.layout.missed_connection_activity);
        missedConnectionActivityBinding.setActivity(this);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        localLanguage = prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage, this);
        loadConnectionPurposeList();
        habitationFilterSpinner(prefManager.getDistrictCode(), prefManager.getBlockCode(), prefManager.getPvCode());
        missedConnectionActivityBinding.connectionNoEt1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 2) {
                    missedConnectionActivityBinding.connectionNoEt2.requestFocus();
                } else {
                    missedConnectionActivityBinding.connectionNoEt1.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        missedConnectionActivityBinding.connectionNoEt2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 3) {
                    missedConnectionActivityBinding.connectionNoEt3.requestFocus();
                } else {
                    missedConnectionActivityBinding.connectionNoEt2.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        missedConnectionActivityBinding.connectionNoEt3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 3) {
                    missedConnectionActivityBinding.connectionNoEt4.requestFocus();
                } else {
                    missedConnectionActivityBinding.connectionNoEt3.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        missedConnectionActivityBinding.connectionNoEt4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (charSequence.length() == 4) {
                    // missedConnectionActivityBinding.nameEt.requestFocus();
                } else {
                    missedConnectionActivityBinding.connectionNoEt4.requestFocus();
                }


            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        missedConnectionActivityBinding.purposeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    purpose_id = connectionPurposeList.get(missedConnectionActivityBinding.purposeSpinner.getSelectedItemPosition()).getConncetion_id();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        missedConnectionActivityBinding.habitaionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i > 0) {
                    habitation_id = Habitation.get(missedConnectionActivityBinding.habitaionSpinner.getSelectedItemPosition()).getHabitation_code();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        missedConnectionActivityBinding.capturedEbMeterLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_flag = "capture_eb_meter_layout";
                if (eb_meter_image_string == "") {
                    //getLatLong();
                    getExactLocation();
                } else {
                    if (missedConnectionActivityBinding.ebMeterIcon.getVisibility() == View.VISIBLE) {
                        ExpandedImage(missedConnectionActivityBinding.ebMeterIcon.getDrawable());
                    }
                }

            }
        });
        missedConnectionActivityBinding.editEbMeterIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                image_flag = "capture_eb_meter_layout";
                //getLatLong();
                getExactLocation();

            }
        });

        missedConnectionActivityBinding.listItemRecyclerRl.setVisibility(View.GONE);
        missedConnectionActivityBinding.scrollView.setVisibility(View.VISIBLE);
        missedConnectionActivityBinding.nameCloseIcon.setVisibility(View.GONE);
        missedConnectionActivityBinding.locationCloseIcon.setVisibility(View.GONE);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        missedConnectionActivityBinding.missedConnectionRecycler.setLayoutManager(mLayoutManager);
        missedConnectionActivityBinding.missedConnectionRecycler.setItemAnimator(new DefaultItemAnimator());
        missedConnectionActivityBinding.missedConnectionRecycler.setHasFixedSize(true);
        missedConnectionActivityBinding.missedConnectionRecycler.setNestedScrollingEnabled(false);
        missedConnectionActivityBinding.missedConnectionRecycler.setFocusable(false);

        missedConnectionActivityBinding.listIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                missedConnectionActivityBinding.listItemRecyclerRl.setVisibility(View.VISIBLE);
                missedConnectionActivityBinding.scrollView.setVisibility(View.GONE);
                getNewConnectionList();
            }
        });

        missedConnectionActivityBinding.nameAudioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechToText("NAME");
            }
        });

        missedConnectionActivityBinding.locationAudioIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                speechToText("LOCATION");
            }
        });
        missedConnectionActivityBinding.locationCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missedConnectionActivityBinding.locationCloseIcon.setVisibility(View.GONE);
                missedConnectionActivityBinding.locationAudioIcon.setVisibility(View.VISIBLE);
                missedConnectionActivityBinding.locationEt.setText("");
            }
        });
        missedConnectionActivityBinding.nameCloseIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                missedConnectionActivityBinding.nameCloseIcon.setVisibility(View.GONE);
                missedConnectionActivityBinding.nameAudioIcon.setVisibility(View.VISIBLE);
                missedConnectionActivityBinding.nameEt.setText("");
            }
        });


    }

    private void getNewConnectionList() {
        try {
            new ApiService(this).makeJSONObjectRequest("NewConnectionList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), connectionListJsonParams(), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject connectionListJsonParams() throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), Utils.newConnectionListJsonParams().toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("districtList", "" + authKey);
        return dataSet;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {

        }
    }

    public void gotoHomePage() {
        Intent homepage = new Intent(this, HomePage.class);
        homepage.putExtra("Home", "Login");
        startActivity(homepage);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }

    public void saveDetails() {
        if (missedConnectionActivityBinding.habitaionSpinner.getSelectedItemPosition() > 0) {
            if (!missedConnectionActivityBinding.connectionNoEt1.getText().toString().equals("")) {
                if (!missedConnectionActivityBinding.connectionNoEt2.getText().toString().equals("")) {
                    if (!missedConnectionActivityBinding.connectionNoEt3.getText().toString().equals("")) {
                        if (!missedConnectionActivityBinding.connectionNoEt4.getText().toString().equals("")) {
                            if (!missedConnectionActivityBinding.nameEt.getText().toString().equals("")) {
                                if (!missedConnectionActivityBinding.locationEt.getText().toString().equals("")) {
                                    if (missedConnectionActivityBinding.purposeSpinner.getSelectedItemPosition() > 0) {
                                        //if (missedConnectionActivityBinding.habitaionSpinner.getSelectedItemPosition() > 0) {
                                        if (!eb_meter_image_string.equals("")) {
                                            if (Utils.isOnline()) {
                                                submitConnDetail();
                                            } else {
                                                Utils.showAlert(this, getResources().getString(R.string.no_internet_connection));

                                            }
                                        } else {
                                            Utils.showAlert(this, getResources().getString(R.string.please_capture_eb_meter));
                                        }
                                        /*} else {
                                            Utils.showAlert(this, getResources().getString(R.string.select_habitation));
                                        }*/

                                    } else {
                                        Utils.showAlert(this, getResources().getString(R.string.enter_purpose_as_per_tneb));
                                    }

                                } else {
                                    Utils.showAlert(this, getResources().getString(R.string.enter_location));
                                }

                            } else {
                                Utils.showAlert(this, getResources().getString(R.string.enter_name));
                            }
                        } else {
                            Utils.showAlert(this, getResources().getString(R.string.enter_connection_no));
                        }
                    } else {
                        Utils.showAlert(this, getResources().getString(R.string.enter_connection_no));
                    }
                } else {
                    Utils.showAlert(this, getResources().getString(R.string.enter_connection_no));
                }
            } else {
                Utils.showAlert(this, getResources().getString(R.string.enter_connection_no));
            }
        } else {
            Utils.showAlert(this, getResources().getString(R.string.select_habitation));
        }

    }

    private JSONObject submitConnDetail() {
        JSONObject jsonObject = new JSONObject();
        String connection_number_1 = missedConnectionActivityBinding.connectionNoEt1.getText().toString();
        String connection_number_2 = missedConnectionActivityBinding.connectionNoEt2.getText().toString();
        String connection_number_3 = missedConnectionActivityBinding.connectionNoEt3.getText().toString();
        String connection_number_4 = missedConnectionActivityBinding.connectionNoEt4.getText().toString();
        String connection_number = connection_number_1 + "-" + connection_number_2 + "-" + connection_number_3 + "-" + connection_number_4;
        try {
            jsonObject.put(AppConstant.KEY_SERVICE_ID, "new_connection_details_save");
            jsonObject.put("region_code", connection_number_1);
            jsonObject.put("section_code", connection_number_2);
            jsonObject.put("distribution_code", connection_number_3);
            jsonObject.put("consumer_code", connection_number_4);
            jsonObject.put("consumer_name", missedConnectionActivityBinding.nameEt.getText().toString());
            jsonObject.put("landmark", missedConnectionActivityBinding.locationEt.getText().toString());
            jsonObject.put("purpose_of_conn", purpose_id);
            jsonObject.put("hab_code", habitation_id);
            jsonObject.put("meter_image_lat", offlatTextValue);
            jsonObject.put("meter_image_long", offlongTextValue);
            jsonObject.put("meter_image", eb_meter_image_string);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), jsonObject.toString());
        JSONObject dataSet = new JSONObject();
        try {
            dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
            dataSet.put(AppConstant.DATA_CONTENT, authKey);
            if (Utils.isOnline()) {
                new ApiService(this).makeJSONObjectRequest("MissedConnection", Api.Method.POST, UrlGenerator.getMainServiceUrl(), dataSet, "not cache", this);

            } else {
                Utils.showAlert(MissedConnectionActivity.this, getResources().getString(R.string.no_internet_connection));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("saveTrackDataList", "" + jsonObject);
        Log.d("request", "" + dataSet);

        return dataSet;
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();

            if ("MissedConnection".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, getResources().getString(R.string.your_track_data_synchronized));
                    eb_meter_image_string = "";
                    missedConnectionActivityBinding.ebMeterIcon.setVisibility(View.GONE);
                    missedConnectionActivityBinding.ebMeterIcon.setImageBitmap(null);
                    missedConnectionActivityBinding.previewIcon.setVisibility(View.VISIBLE);
                    missedConnectionActivityBinding.editEbMeterIcon.setVisibility(View.GONE);
                    missedConnectionActivityBinding.habitaionSpinner.setSelection(0);
                    missedConnectionActivityBinding.purposeSpinner.setSelection(0);
                    missedConnectionActivityBinding.connectionNoEt1.setText("");
                    missedConnectionActivityBinding.connectionNoEt2.setText("");
                    missedConnectionActivityBinding.connectionNoEt3.setText("");
                    missedConnectionActivityBinding.connectionNoEt4.setText("");
                    missedConnectionActivityBinding.nameEt.setText("");
                    missedConnectionActivityBinding.locationEt.setText("");


                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                }
                Log.d("saved_response", "" + responseDecryptedBlockKey);
            } else if ("DeleteConnectionList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    Utils.showAlert(this, getResources().getString(R.string.connection_deleted_successfully));
                    getNewConnectionList();


                } else if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("FAIL")) {
                    Toasty.error(this, jsonObject.getString("MESSAGE"), Toast.LENGTH_LONG, true).show();
                }
                Log.d("DeleteConnectionList", "" + responseDecryptedBlockKey);
            } else if ("NewConnectionList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                Log.d("List", responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
                    JSONArray jsonArray = new JSONArray();
                    SavedConnectionList = new ArrayList<>();
                    try {
                        jsonArray = jsonObject.getJSONArray(AppConstant.JSON_DATA);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    for (int i = 0; i < jsonArray.length(); i++) {
                        TNEBSystem ListValue = new TNEBSystem();
                        try {

                            ListValue.setId(jsonArray.getJSONObject(i).getString("id"));
                            ListValue.setRegion_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_REGION_CODE));
                            ListValue.setSection_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_SECTION_CODE));
                            ListValue.setDistribution_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_DISTRIBUTION_CODE));
                            ListValue.setConsumer_code(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CONSUMER_CODE));
                            ListValue.setCuscode(jsonArray.getJSONObject(i).getString(AppConstant.KEY_CUSCODE));
                            ListValue.setConsumer_name(jsonArray.getJSONObject(i).getString("consumer_name"));
                            ListValue.setLocation(jsonArray.getJSONObject(i).getString("landmark"));
                            ListValue.setConncetion_name(jsonArray.getJSONObject(i).getString("purpose_of_conn_name"));
                            ListValue.setConnection_name_ta(jsonArray.getJSONObject(i).getString("purpose_of_conn_name_ta"));
                            ListValue.setHabitation_name(jsonArray.getJSONObject(i).getString("habitation_name"));
                            ListValue.setHabitation_name_ta(jsonArray.getJSONObject(i).getString("habitation_name_ta"));
                            if (jsonArray.getJSONObject(i).getString("meter_image_available").equals("Y")) {
                                ListValue.setKEY_METER_IMAGE(jsonArray.getJSONObject(i).getString("meter_image"));
                            }
                            ListValue.setKEY_METER_IMAGE_LAT(jsonArray.getJSONObject(i).getString("meter_image_lat"));
                            ListValue.setKEY_METER_IMAGE_LONG(jsonArray.getJSONObject(i).getString("meter_image_long"));

                            SavedConnectionList.add(ListValue);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                    if (SavedConnectionList.size() > 0) {
                        missedConnectionActivityBinding.missedConnectionRecycler.setVisibility(View.VISIBLE);
                        missedConnectionActivityBinding.noDataIcon.setVisibility(View.GONE);
                        missedConnectionActivityBinding.scrollView.setVisibility(View.GONE);
                        missedConnectionActivityBinding.listItemRecyclerRl.setVisibility(View.VISIBLE);
                        newConnetionSavedListAdapter = new NewConnetionSavedListAdapter(this, (ArrayList<TNEBSystem>) SavedConnectionList);
                        missedConnectionActivityBinding.missedConnectionRecycler.setAdapter(newConnetionSavedListAdapter);
                    } else {
                        missedConnectionActivityBinding.listItemRecyclerRl.setVisibility(View.VISIBLE);
                        missedConnectionActivityBinding.scrollView.setVisibility(View.GONE);
                        missedConnectionActivityBinding.missedConnectionRecycler.setVisibility(View.GONE);
                        missedConnectionActivityBinding.noDataIcon.setVisibility(View.VISIBLE);
                    }

                }
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("NO_RECORD")) {
                    missedConnectionActivityBinding.listItemRecyclerRl.setVisibility(View.VISIBLE);
                    missedConnectionActivityBinding.scrollView.setVisibility(View.GONE);
                    missedConnectionActivityBinding.missedConnectionRecycler.setVisibility(View.GONE);
                    missedConnectionActivityBinding.noDataIcon.setVisibility(View.VISIBLE);
                }
                Log.d("NewConnectionList", "" + responseDecryptedBlockKey);
            }


        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {
        Utils.showAlert(this, getResources().getString(R.string.failed));
    }


    @Override
    public void onBackPressed() {
        if (missedConnectionActivityBinding.listItemRecyclerRl.getVisibility() == View.VISIBLE) {
            missedConnectionActivityBinding.listItemRecyclerRl.setVisibility(View.GONE);
            missedConnectionActivityBinding.scrollView.setVisibility(View.VISIBLE);
        } else {
            super.onBackPressed();
            setResult(Activity.RESULT_CANCELED);
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
        }
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }

    public void loadConnectionPurposeList() {
        Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.CONNECTION_PURPOSE_LIST, null);
        connectionPurposeList.clear();
        TNEBSystem purpose_List = new TNEBSystem();
        purpose_List.setConncetion_id("0");
        purpose_List.setConnection_name_ta("பயன்பாட்டு வகையைத் தேர்ந்தெடுக்கவும்");
        purpose_List.setConncetion_name("Select Purpose");
        connectionPurposeList.add(purpose_List);
        final ArrayList<String> list = new ArrayList<String>();


        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem tnebSystem = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_ID));
                    String conn_name = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NAME));
                    String conn_name_ta = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NAME_TA));
                    tnebSystem.setConncetion_id(id);
                    tnebSystem.setConncetion_name(conn_name);
                    tnebSystem.setConnection_name_ta(conn_name_ta);
                    connectionPurposeList.add(tnebSystem);
                    //   Log.d("finyeardb", "" + finyearList);
                } while (cursor.moveToNext());
            }

        }

        if (connectionPurposeList.size() > 0) {
            missedConnectionActivityBinding.purposeSpinner.setAdapter(new CommonAdapter(this, connectionPurposeList, "connectionPurposeList"));

        }


    }

    public void habitationFilterSpinner(String dcode, String bcode, String pvcode) {
        Cursor HABList = null;
        HABList = db.rawQuery("SELECT * FROM " + DBHelper.HABITATION_TABLE_NAME + " where dcode = '" + dcode + "'and bcode = '" + bcode + "' and pvcode = '" + pvcode + "' order by habitation_name asc", null);

        Habitation.clear();
        TNEBSystem habitationListValue = new TNEBSystem();
        habitationListValue.setHabitation_name("Select Habitation");
        habitationListValue.setHabitation_name_ta("குக்கிராமத்தைத் தேர்ந்தெடுக்கவும்");
        Habitation.add(habitationListValue);
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
        missedConnectionActivityBinding.habitaionSpinner.setAdapter(new CommonAdapter(this, Habitation, "HabitationList"));
    }


    public Bitmap previewCapturedImage(Bitmap i) {
        Bitmap rotatedBitmap = null;
        Bitmap bitmap = null;
        try {
            // hide video preview
            if (i != null) {
                bitmap = i;
            } else {
                bitmap = CameraUtils.optimizeBitmap(BITMAP_SAMPLE_SIZE, imageStoragePath);
            }

            ExifInterface ei = null;
            try {
                ei = new ExifInterface(imageStoragePath);
            } catch (IOException e) {
                e.printStackTrace();
            }
            int orientation = ei.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_UNDEFINED);

            switch (orientation) {

                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotatedBitmap = rotateImage(bitmap, 90);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotatedBitmap = rotateImage(bitmap, 180);
                    break;

                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotatedBitmap = rotateImage(bitmap, 270);
                    break;

                case ExifInterface.ORIENTATION_NORMAL:
                default:
                    rotatedBitmap = bitmap;
            }

            if (image_flag.equals("capture_eb_meter_layout")) {
                missedConnectionActivityBinding.previewIcon.setVisibility(View.GONE);
                missedConnectionActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                missedConnectionActivityBinding.editEbMeterIcon.setVisibility(View.VISIBLE);
                missedConnectionActivityBinding.ebMeterIcon.setImageBitmap(rotatedBitmap);
                eb_meter_image_string = BitMapToString(rotatedBitmap);
            }
//            cameraScreenBinding.imageView.showImage((getImageUri(rotatedBitmap)));
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
        return rotatedBitmap;
    }

    public String BitMapToString(Bitmap bitmap) {
        String temp = "";
        try {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
        }
        return temp;
    }

    public Bitmap stringToBitmap(String encodedImage) {
        byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
        return decodedByte;
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "IMG_" + Calendar.getInstance().getTime(), null);
        return Uri.parse(path);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case CAMERA_CAPTURE_IMAGE_REQUEST_CODE:
                if (resultCode == RESULT_OK) {
                    if (Build.VERSION.SDK_INT > N) {
                        Bitmap i = (Bitmap) data.getExtras().get("data");
                        imageStoragePath = getRealPathFromURI(getImageUri(getApplicationContext(), i));
                        previewCapturedImage(i);

                      /*  if(image_flag.equals("capture_eb_meter_layout")){
                            connectionSubmitActivityBinding.previewIcon.setVisibility(View.GONE);
                            connectionSubmitActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.editEbMeterIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.ebMeterIcon.setImageBitmap(i);
                            eb_meter_image_string=BitMapToString(i);
                        }
                        else if(image_flag.equals("capture_eb_bill_layout")) {
                            connectionSubmitActivityBinding.previewIcon1.setVisibility(View.GONE);
                            connectionSubmitActivityBinding.lastEbBillIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.editLastEbBillIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.lastEbBillIcon.setImageBitmap(i);
                            last_eb_bill_image_string=BitMapToString(i);
                        }
                        else if(image_flag.equals("capture_eb_card_layout")){
                            connectionSubmitActivityBinding.previewIcon2.setVisibility(View.GONE);
                            connectionSubmitActivityBinding.lastEbCardIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.editLastEbCardIcon.setVisibility(View.VISIBLE);
                            connectionSubmitActivityBinding.lastEbCardIcon.setImageBitmap(i);
                            eb_card_image_string=BitMapToString(i);
                        }*/
                    } else {
                        // Refreshing the gallery
                        CameraUtils.refreshGallery(getApplicationContext(), imageStoragePath);

                        // successfully captured the image
                        // display it in image view
                        Bitmap i = null;
                        previewCapturedImage(i);
                        //
                    }
                } else if (resultCode == RESULT_CANCELED) {
                    // user cancelled Image capture
                    Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.user_canceled_image_capture), Toast.LENGTH_SHORT)
                            .show();
                } else {
                    // failed to capture image
                    Toast.makeText(getApplicationContext(),
                                    getResources().getString(R.string.sorry_failed_to_capture_image), Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case NAME_SPEECH_REQUEST_CODE:

                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    missedConnectionActivityBinding.nameEt.setText(
                            Objects.requireNonNull(result).get(0));
                    missedConnectionActivityBinding.nameAudioIcon.setVisibility(View.GONE);
                    missedConnectionActivityBinding.nameCloseIcon.setVisibility(View.VISIBLE);

                }

                break;
            case LAND_MARK_SPEECH_REQUEST_CODE:

                if (resultCode == RESULT_OK && data != null) {
                    ArrayList<String> result = data.getStringArrayListExtra(
                            RecognizerIntent.EXTRA_RESULTS);
                    missedConnectionActivityBinding.locationEt.setText(
                            Objects.requireNonNull(result).get(0));
                    missedConnectionActivityBinding.locationAudioIcon.setVisibility(View.GONE);
                    missedConnectionActivityBinding.locationCloseIcon.setVisibility(View.VISIBLE);
                }

                break;

            case 1213:
                if (requestCode == 1213 && resultCode == Activity.RESULT_OK) {
                    String filePath = data.getStringExtra(ImageSelectActivity.RESULT_FILE_PATH);
                    Bitmap selectedImage = BitmapFactory.decodeFile(filePath);

                    if (image_flag.equals("capture_eb_meter_layout")) {
                        missedConnectionActivityBinding.previewIcon.setVisibility(View.GONE);
                        missedConnectionActivityBinding.ebMeterIcon.setVisibility(View.VISIBLE);
                        missedConnectionActivityBinding.editEbMeterIcon.setVisibility(View.VISIBLE);
                        missedConnectionActivityBinding.ebMeterIcon.setImageBitmap(selectedImage);
                        eb_meter_image_string = BitMapToString(selectedImage);


                    }

                }

                break;
            default:
                break;
        }
    }

    private void getLatLong() {
        mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        mlocListener = new MyLocationListener();


        // permission was granted, yay! Do the
        // location-related task you need to do.
        if (ContextCompat.checkSelfPermission(MissedConnectionActivity.this,
                ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {

            //Request location updates:
            mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mlocListener);

        }

        if (mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            if (Build.VERSION.SDK_INT > N) {
                if (ActivityCompat.checkSelfPermission(MissedConnectionActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MissedConnectionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
                    requestPermissions(new String[]{CAMERA, ACCESS_FINE_LOCATION}, PERMISSION_REQUEST_CODE);
            } else {
                if (ActivityCompat.checkSelfPermission(MissedConnectionActivity.this, ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(MissedConnectionActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(MissedConnectionActivity.this, new String[]{ACCESS_FINE_LOCATION}, 1);

                }
            }
            if (MyLocationListener.latitude > 0) {
                if (Build.VERSION.SDK_INT > N) {
                    if (CameraUtils.checkPermissions(MissedConnectionActivity.this)) {
                        captureImage1();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
//                            checkPermissionForCamera();
                } else {
                    requestCameraPermission(MEDIA_TYPE_IMAGE);
                }
            } else {
                Utils.showAlert(MissedConnectionActivity.this, getResources().getString(R.string.satelite_communication_not_available));
            }
        } else {
            Utils.showAlert(MissedConnectionActivity.this, getResources().getString(R.string.gps_is_not_turned_on));
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
                        CameraUtils.openSettings(MissedConnectionActivity.this);
                    }
                })
                .setNegativeButton(getResources().getString(R.string.CANCEL), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                }).show();
    }

    private void captureImage1() {
        /*if (Build.VERSION.SDK_INT > N) {
            Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(cameraIntent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }
        else {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

            File file = CameraUtils.getOutputMediaFile(MEDIA_TYPE_IMAGE);
            if (file != null) {
                imageStoragePath = file.getAbsolutePath();
            }

            Uri fileUri = CameraUtils.getOutputMediaFileUri(this, file);

            intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

            // start the image capture Intent
            startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
        }*/
        launchCamera();
        /*if (MyLocationListener.latitude > 0) {
            if(image_flag.equals("capture_eb_meter_layout")){
                offlatTextValue = MyLocationListener.latitude;
                offlongTextValue = MyLocationListener.longitude;
            }

        }*/

        if (image_flag.equals("capture_eb_meter_layout")) {
            offlatTextValue = wayLatitude;
            offlongTextValue = wayLongitude;
        }

    }

    private void ExpandedImage(Drawable profile) {
        try {
            //We need to get the instance of the LayoutInflater, use the context of this activity
            LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            //Inflate the view from a predefined XML layout
            View ImagePopupLayout = inflater.inflate(R.layout.image_custom_layout, null);

            ImageView zoomImage = (ImageView) ImagePopupLayout.findViewById(R.id.imgZoomProfileImage);
            zoomImage.setImageDrawable(profile);

            ImageMatrixTouchHandler imageMatrixTouchHandler = new ImageMatrixTouchHandler(this);
            zoomImage.setOnTouchListener(imageMatrixTouchHandler);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(mContext, R.style.MyDialogTheme);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
            dialogBuilder.setView(ImagePopupLayout);

            final AlertDialog alert = dialogBuilder.create();
            alert.getWindow().getAttributes().windowAnimations = R.style.dialog_animation_zoomInOut;
            alert.show();
            alert.getWindow().setBackgroundDrawableResource(R.color.full_transparent);
            alert.setCanceledOnTouchOutside(true);

            zoomImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    alert.dismiss();
                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void save_and_delete_alert(String delete_id, String save_delete) {
        try {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.alert_dialog);

            TextView text = (TextView) dialog.findViewById(R.id.tv_message);
            if (save_delete.equals("save")) {
                text.setText(getResources().getString(R.string.do_u_want_to_upload));
            } else if (save_delete.equals("delete")) {
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
                    if (save_delete.equals("save")) {

                    } else if (save_delete.equals("delete")) {
                        if (Utils.isOnline()) {
                            deleteParticularNewConnectionList(delete_id);
                            dialog.dismiss();
                        } else {
                            Utils.showAlert(MissedConnectionActivity.this, getResources().getString(R.string.no_internet_connection));
                            dialog.dismiss();
                        }
                    }
                }
            });

            dialog.show();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void deleteParticularNewConnectionList(String id) {
        try {
            new ApiService(this).makeJSONObjectRequest("DeleteConnectionList", Api.Method.POST, UrlGenerator.getMainServiceUrl(), deleteParticularListJsonParams(id), "not cache", this);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public JSONObject deleteParticularListJsonParams(String id) throws JSONException {
        String authKey = Utils.encrypt(prefManager.getUserPassKey(), getResources().getString(R.string.init_vector), newConnectionListDeleteJsonParams(id).toString());
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_USER_NAME, prefManager.getUserName());
        dataSet.put(AppConstant.DATA_CONTENT, authKey);
        Log.d("districtList", "" + authKey);
        return dataSet;
    }

    public static JSONObject newConnectionListDeleteJsonParams(String id) throws JSONException {
        JSONObject dataSet = new JSONObject();
        dataSet.put(AppConstant.KEY_SERVICE_ID, "delete_new_connection_details");
        dataSet.put("id", id);
        Log.d("connectionListParam", "" + dataSet);
        return dataSet;
    }

    public void speechToText(String request_id) {
        Intent intent
                = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE,
                "ta-IND");
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak to text");

        try {

            if (request_id.equals("LOCATION")) {
                startActivityForResult(intent, LAND_MARK_SPEECH_REQUEST_CODE);
            } else if (request_id.equals("NAME")) {
                startActivityForResult(intent, NAME_SPEECH_REQUEST_CODE);
            }

        } catch (Exception e) {
            Toast
                    .makeText(MissedConnectionActivity.this, " " + e.getMessage(),
                            Toast.LENGTH_SHORT)
                    .show();
        }
    }


    public void getExactLocation() {

        // check permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION, CAMERA},
                    1000);
            // reuqest for permission

        } else {
            mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

            locationRequest = LocationRequest.create();
            locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            locationRequest.setInterval(10 * 1000);

            mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                if (location != null) {
                    wayLatitude = location.getLatitude();
                    wayLongitude = location.getLongitude();
                    Log.d("LocationAccuracy", "" + location.getAccuracy());
                    Log.d("Locations", "" + wayLatitude + "," + wayLongitude);
                    if (CameraUtils.checkPermissions(MissedConnectionActivity.this)) {
                        captureImage1();
                    } else {
                        requestCameraPermission(MEDIA_TYPE_IMAGE);
                    }
                }
            });
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
                    locationRequest.setInterval(10 * 1000);


                    if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        // TODO: Consider calling
                        //    ActivityCompat#requestPermissions
                        // here to request the missing permissions, and then overriding
                        //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                        //                                          int[] grantResults)
                        // to handle the case where the user grants the permission. See the documentation
                        // for ActivityCompat#requestPermissions for more details.
                        return;
                    }
                    mFusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                        if (location != null) {
                            wayLatitude = location.getLatitude();
                            wayLongitude = location.getLongitude();
                            Log.d("Locations", "" + wayLatitude + "," + wayLongitude);
                            if (CameraUtils.checkPermissions(MissedConnectionActivity.this)) {
                                captureImage1();
                            } else {
                                requestCameraPermission(MEDIA_TYPE_IMAGE);
                            }
                        }
                    });
                } else {
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                }


                break;
            }
        }
    }

    private void launchCamera() {

        Intent intent = new Intent(this, ImageSelectActivity.class);
        intent.putExtra(ImageSelectActivity.FLAG_COMPRESS, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CAMERA, true);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_GALLERY, false);//default is true
        intent.putExtra(ImageSelectActivity.FLAG_CROP, false);//default is false
        startActivityForResult(intent, 1213);
    }


}
