package com.nic.tnebnewwatersource.activity;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.os.Bundle;
import android.text.InputType;
import android.text.TextPaint;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.VolleyError;
import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.adapter.CommonAdapter;
import com.nic.tnebnewwatersource.adapter.ConnectionListAdapter;
import com.nic.tnebnewwatersource.api.Api;
import com.nic.tnebnewwatersource.api.ServerResponse;
import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.dataBase.DBHelper;
import com.nic.tnebnewwatersource.dataBase.dbData;
import com.nic.tnebnewwatersource.databinding.ConnectionCheckActivityBinding;
import com.nic.tnebnewwatersource.model.TNEBSystem;
import com.nic.tnebnewwatersource.session.PrefManager;
import com.nic.tnebnewwatersource.utils.Utils;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;


public class ConnectionCheckActivity extends AppCompatActivity implements Api.ServerResponseListener {
    private ConnectionCheckActivityBinding connectionCheckActivityBinding;
    public com.nic.tnebnewwatersource.dataBase.dbData dbData = new dbData(this);
    private PrefManager prefManager;
    public static DBHelper dbHelper;
    public static SQLiteDatabase db;
    private List<TNEBSystem> Village = new ArrayList<>();

    private List<TNEBSystem> ConnectionList = new ArrayList<>();
    String pref_Village;
    private SearchView searchView;
    ConnectionListAdapter connectionListAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        connectionCheckActivityBinding = DataBindingUtil.setContentView(this, R.layout.connection_check_activity);
        connectionCheckActivityBinding.setActivity(this);
        prefManager = new PrefManager(this);
        try {
            dbHelper = new DBHelper(this);
            db = dbHelper.getWritableDatabase();
            //db = dbHelper.getReadableDatabase();
        } catch (Exception e) {
            e.printStackTrace();
        }
        setSupportActionBar(connectionCheckActivityBinding.toolbar);

        String localLanguage = prefManager.getLocalLanguage();
        //Utils.setLocale(localLanguage,this);
      /*  villageFilterSpinner(prefManager.getBlockCode());
        connectionCheckActivityBinding.villageSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {

                    pref_Village = Village.get(position).getPvName();
                    prefManager.setVillageListPvName(pref_Village);
                    prefManager.setPvCode(Village.get(position).getPvCode());
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
*/

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getApplicationContext(),1);
        connectionCheckActivityBinding.connectionListRecycler.setLayoutManager(mLayoutManager);
        connectionCheckActivityBinding.connectionListRecycler.setItemAnimator(new DefaultItemAnimator());
        connectionCheckActivityBinding.connectionListRecycler.setHasFixedSize(true);
        connectionCheckActivityBinding.connectionListRecycler.setNestedScrollingEnabled(false);
        connectionCheckActivityBinding.connectionListRecycler.setFocusable(false);

        connectionCheckActivityBinding.backImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed();
            }
        });
        connectionCheckActivityBinding.homeIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotoHomePage();
            }
        });
        connectionCheckActivityBinding.headerText.setText(getResources().getString(R.string.connection_list));

        TextPaint paint = connectionCheckActivityBinding.headerText.getPaint();
        float width = paint.measureText(getResources().getString(R.string.connection_list));
        loadAllList();

        connectionCheckActivityBinding.taken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadTakenList();
            }
        });
        connectionCheckActivityBinding.all.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadAllList();
            }
        });
        connectionCheckActivityBinding.notTaken.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadNotTakenList();
            }
        });



        Shader textShader = new LinearGradient(0, 0, width, connectionCheckActivityBinding.headerText.getTextSize(),
                new int[]{
                        Color.parseColor("#F97C3C"),
                        Color.parseColor("#FDB54E"),
                        Color.parseColor("#64B678"),
                        Color.parseColor("#478AEA"),
                        Color.parseColor("#8446CC"),
                }, null, Shader.TileMode.CLAMP);
        connectionCheckActivityBinding.headerText.getPaint().setShader(textShader);
    }

    public void gotoHomePage(){
        Intent homepage=new Intent(this,HomePage.class);
        homepage.putExtra("Home", "Login");
        startActivity(homepage);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);

    }

    public void loadAllList(){
        connectionCheckActivityBinding.all.setChecked(true);
        connectionCheckActivityBinding.taken.setChecked(false);
        connectionCheckActivityBinding.notTaken.setChecked(false);
        loadOfflineConnectionListDBValues(prefManager.getPvCode());
    }
    public void loadTakenList(){
        connectionCheckActivityBinding.all.setChecked(false);
        connectionCheckActivityBinding.taken.setChecked(true);
        connectionCheckActivityBinding.notTaken.setChecked(false);
        loadOfflineTakenConnectionListDBValues(prefManager.getPvCode(),"Y");

    }
    public void loadNotTakenList(){
        connectionCheckActivityBinding.all.setChecked(false);
        connectionCheckActivityBinding.taken.setChecked(false);
        connectionCheckActivityBinding.notTaken.setChecked(true);
        loadOfflineNotTakenConnectionListDBValues(prefManager.getPvCode(),"N");

    }

    @Override
    public void onBackPressed() {
        if (!searchView.isIconified()) {
            searchView.setIconified(true);
            return;
        }
        super.onBackPressed();
        setResult(Activity.RESULT_CANCELED);
        overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit);
    }

    public void viewConnectionDetail(List<TNEBSystem> List,int position) {
         List<TNEBSystem> ConnectionList = new ArrayList<>();
        ConnectionList=List;

        Intent intent = new Intent(this, ConnectionSubmitActivity.class);
        Bundle mBundle = new Bundle();

        mBundle.putString("id", ConnectionList.get(position).getId());
        mBundle.putString("region_code", ConnectionList.get(position).getRegion_code());
        mBundle.putString("circle_code", ConnectionList.get(position).getCircle_code());
        mBundle.putString("section_code", ConnectionList.get(position).getSection_code());
        mBundle.putString("distribution_code", ConnectionList.get(position).getDistribution_code());
        mBundle.putString("consumer_code", ConnectionList.get(position).getConsumer_code());
        mBundle.putString("cuscode", ConnectionList.get(position).getCuscode());
        mBundle.putString("consumer_name", ConnectionList.get(position).getConsumer_name());
        mBundle.putString("location", ConnectionList.get(position).getLocation());
        mBundle.putString("tariff", ConnectionList.get(position).getTariff());
        mBundle.putString("tariff_desc", ConnectionList.get(position).getTariff_desc());
        mBundle.putString("purpose_as_per_tneb", ConnectionList.get(position).getPurpose_as_per_tneb());
        mBundle.putString("connection_number", ConnectionList.get(position).getConnection_number());
        intent.putExtras(mBundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    public void viewSavedDetails(String id){
        Intent intent = new Intent(this, ViewImagesActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putString("activity_flag","ConnectionList");
        mBundle.putString("id",id);
        intent.putExtras(mBundle);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);

    }


    public void villageFilterSpinner(String filterVillage) {
        Cursor VillageList = null;
        VillageList = db.rawQuery("SELECT * FROM " + DBHelper.VILLAGE_TABLE_NAME + " where dcode = " + prefManager.getDistrictCode() + " and bcode = '" + filterVillage + "'"+ " order by pvname asc", null);

        Village.clear();
        TNEBSystem villageListValue = new TNEBSystem();
        villageListValue.setPvName(getResources().getString(R.string.select_village));
        Village.add(villageListValue);
        if (VillageList.getCount() > 0) {
            if (VillageList.moveToFirst()) {
                do {
                    TNEBSystem villageList = new TNEBSystem();
                    String districtCode = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.DISTRICT_CODE));
                    String blockCode = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.BLOCK_CODE));
                    String pvCode = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String pvname = VillageList.getString(VillageList.getColumnIndexOrThrow(AppConstant.PV_NAME));

                    villageList.setDistictCode(districtCode);
                    villageList.setBlockCode(blockCode);
                    villageList.setPvCode(pvCode);
                    villageList.setPvName(pvname);

                    Village.add(villageList);
                    Log.d("spinnersize", "" + Village.size());
                } while (VillageList.moveToNext());
            }
        }
        connectionCheckActivityBinding.villageSpinner.setAdapter(new CommonAdapter(this, Village, "VillageList"));
    }


    @Override
    public void OnMyResponse(ServerResponse serverResponse) {
        try {
            String urlType = serverResponse.getApi();
            JSONObject responseObj = serverResponse.getJsonResponse();
            if ("ConnectionList".equals(urlType) && responseObj != null) {
                String key = responseObj.getString(AppConstant.ENCODE_DATA);
                String responseDecryptedBlockKey = Utils.decrypt(prefManager.getUserPassKey(), key);
                JSONObject jsonObject = new JSONObject(responseDecryptedBlockKey);
                if (jsonObject.getString("STATUS").equalsIgnoreCase("OK") && jsonObject.getString("RESPONSE").equalsIgnoreCase("OK")) {
//                    new InsertConnectionListTask().execute(jsonObject);
                }
                Log.d("ConnectionList", "" + responseDecryptedBlockKey);
            }
        }catch (JSONException e){
            e.printStackTrace();
        }
    }

    @Override
    public void OnError(VolleyError volleyError) {

    }

    public Cursor getRawEvents(String sql, String string) {
        Cursor cursor = db.rawQuery(sql, null);
        return cursor;
    }
    public void loadOfflineConnectionListDBValues(String pvCode) {
        Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.CONNECTION_LIST+" where pvcode=" + pvCode, null);
        ConnectionList.clear();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String region_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_REGION_CODE));
                    String circle_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CIRCLE_CODE));
                    String section_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_SECTION_CODE));
                    String distribution_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_DISTRIBUTION_CODE));
                    String consumer_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONSUMER_CODE));
                    String cuscode = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CUSCODE));
                    String consumer_name = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONSUMER_NAME));
                    String connection_number = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_LOCATION));
                    String tariff = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_TARIFF));
                    String tariff_desc = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_TARIFF_DESC));
                    String purpose_as_per_tneb = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_PURPOSE_AS_PER_TNEB));
                    String PV_CODE = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String PHOTO_SAVED_STATUS = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PHOTO_SAVED_STATUS));

                    list.setId(id);
                    list.setRegion_code(region_code);
                    list.setCircle_code(circle_code);
                    list.setSection_code(section_code);
                    list.setDistribution_code(distribution_code);
                    list.setConsumer_code(consumer_code);
                    list.setCuscode(cuscode);
                    list.setConsumer_name(consumer_name);
                    list.setConnection_number(connection_number);
                    list.setLocation(location);
                    list.setTariff(tariff);
                    list.setTariff_desc(tariff_desc);
                    list.setPurpose_as_per_tneb(purpose_as_per_tneb);
                    list.setPvCode(PV_CODE);
                    list.setPHOTO_SAVED_STATUS(PHOTO_SAVED_STATUS);

                    ConnectionList.add(list);
                    //   Log.d("ConnectionList", "" + ConnectionList);
                } while (cursor.moveToNext());
            }

        }
        if(ConnectionList.size()>0){
            connectionCheckActivityBinding.connectionListRecycler.setVisibility(View.VISIBLE);
            connectionCheckActivityBinding.notFoundIv.setVisibility(View.GONE);
            connectionListAdapter=new ConnectionListAdapter(this,ConnectionList,dbData);
            connectionCheckActivityBinding.connectionListRecycler.setAdapter(connectionListAdapter);
            connectionCheckActivityBinding.noConnectionCountLayout.setVisibility(View.VISIBLE);
            connectionCheckActivityBinding.connectionCount.setText(" - "+ConnectionList.size());
        }else {
            connectionCheckActivityBinding.noConnectionCountLayout.setVisibility(View.GONE);
            connectionCheckActivityBinding.connectionListRecycler.setVisibility(View.GONE);
            connectionCheckActivityBinding.notFoundIv.setVisibility(View.VISIBLE);
        }

    }
    public void loadOfflineTakenConnectionListDBValues(String pvCode, String yes) {
       // Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.CONNECTION_LIST+" where pvcode=" + pvCode, null);
        Cursor cursor = db.rawQuery("select * from "+DBHelper.CONNECTION_LIST+" where pvcode = "+pvCode+" and photo_saved_status = '"+yes+"' order by id asc",null);

        ConnectionList.clear();
        ArrayList<TNEBSystem> ConnectionTakenList = new ArrayList<>();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String region_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_REGION_CODE));
                    String circle_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CIRCLE_CODE));
                    String section_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_SECTION_CODE));
                    String distribution_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_DISTRIBUTION_CODE));
                    String consumer_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONSUMER_CODE));
                    String cuscode = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CUSCODE));
                    String consumer_name = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONSUMER_NAME));
                    String connection_number = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_LOCATION));
                    String tariff = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_TARIFF));
                    String tariff_desc = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_TARIFF_DESC));
                    String purpose_as_per_tneb = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_PURPOSE_AS_PER_TNEB));
                    String PV_CODE = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String PHOTO_SAVED_STATUS = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PHOTO_SAVED_STATUS));

                    list.setId(id);
                    list.setRegion_code(region_code);
                    list.setCircle_code(circle_code);
                    list.setSection_code(section_code);
                    list.setDistribution_code(distribution_code);
                    list.setConsumer_code(consumer_code);
                    list.setCuscode(cuscode);
                    list.setConsumer_name(consumer_name);
                    list.setConnection_number(connection_number);
                    list.setLocation(location);
                    list.setTariff(tariff);
                    list.setTariff_desc(tariff_desc);
                    list.setPurpose_as_per_tneb(purpose_as_per_tneb);
                    list.setPvCode(PV_CODE);
                    list.setPHOTO_SAVED_STATUS(PHOTO_SAVED_STATUS);

                    ConnectionList.add(list);
                    //   Log.d("ConnectionList", "" + ConnectionList);
                } while (cursor.moveToNext());
            }

        }
      /*  for(int i=0 ; i<ConnectionTakenList.size(); i++){
            TNEBSystem list =new TNEBSystem();
            if(ConnectionTakenList.get(i).getPHOTO_SAVED_STATUS().equals("Y")){
                list.setId(ConnectionTakenList.get(i).getId());
                list.setRegion_code(ConnectionTakenList.get(i).getRegion_code());
                list.setCircle_code(ConnectionTakenList.get(i).getCircle_code());
                list.setSection_code(ConnectionTakenList.get(i).getSection_code());
                list.setDistribution_code(ConnectionTakenList.get(i).getDistribution_code());
                list.setConsumer_code(ConnectionTakenList.get(i).getConsumer_code());
                list.setCuscode(ConnectionTakenList.get(i).getCuscode());
                list.setConsumer_name(ConnectionTakenList.get(i).getConsumer_name());
                list.setConnection_number(ConnectionTakenList.get(i).getConnection_number());
                list.setLocation(ConnectionTakenList.get(i).getLocation());
                list.setTariff(ConnectionTakenList.get(i).getTariff());
                list.setTariff_desc(ConnectionTakenList.get(i).getTariff_desc());
                list.setPurpose_as_per_tneb(ConnectionTakenList.get(i).getPurpose_as_per_tneb());
                list.setPvCode(ConnectionTakenList.get(i).getPvCode());
                list.setPHOTO_SAVED_STATUS(ConnectionTakenList.get(i).getPHOTO_SAVED_STATUS());
                ConnectionList.add(list);

            }
        }*/
        if(ConnectionList.size()>0){
            connectionCheckActivityBinding.connectionListRecycler.setVisibility(View.VISIBLE);
            connectionCheckActivityBinding.notFoundIv.setVisibility(View.GONE);
            connectionListAdapter=new ConnectionListAdapter(this,ConnectionList,dbData);
            connectionCheckActivityBinding.connectionListRecycler.setAdapter(connectionListAdapter);
            connectionCheckActivityBinding.noConnectionCountLayout.setVisibility(View.VISIBLE);
            connectionCheckActivityBinding.connectionCount.setText(" - "+ConnectionList.size());
        }else {
            connectionCheckActivityBinding.noConnectionCountLayout.setVisibility(View.GONE);
            connectionCheckActivityBinding.connectionListRecycler.setVisibility(View.GONE);
            connectionCheckActivityBinding.notFoundIv.setVisibility(View.VISIBLE);
        }

    }
    public void loadOfflineNotTakenConnectionListDBValues(String pvCode, String no) {
        //Cursor cursor = getRawEvents("SELECT * FROM " + DBHelper.CONNECTION_LIST+" where pvcode=" + pvCode, null);
        Cursor cursor = db.rawQuery("select * from "+DBHelper.CONNECTION_LIST+" where pvcode = "+pvCode+" and photo_saved_status = '"+no+"' order by id asc",null);

        ConnectionList.clear();
        ArrayList<TNEBSystem> ConnectionTakenList =new ArrayList<>();

        if (cursor.getCount() > 0) {
            if (cursor.moveToFirst()) {
                do {
                    TNEBSystem list = new TNEBSystem();
                    String id = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_ID));
                    String region_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_REGION_CODE));
                    String circle_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CIRCLE_CODE));
                    String section_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_SECTION_CODE));
                    String distribution_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_DISTRIBUTION_CODE));
                    String consumer_code = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONSUMER_CODE));
                    String cuscode = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CUSCODE));
                    String consumer_name = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONSUMER_NAME));
                    String connection_number = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO));
                    String location = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_LOCATION));
                    String tariff = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_TARIFF));
                    String tariff_desc = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_TARIFF_DESC));
                    String purpose_as_per_tneb = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.KEY_PURPOSE_AS_PER_TNEB));
                    String PV_CODE = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PV_CODE));
                    String PHOTO_SAVED_STATUS = cursor.getString(cursor.getColumnIndexOrThrow(AppConstant.PHOTO_SAVED_STATUS));

                    list.setId(id);
                    list.setRegion_code(region_code);
                    list.setCircle_code(circle_code);
                    list.setSection_code(section_code);
                    list.setDistribution_code(distribution_code);
                    list.setConsumer_code(consumer_code);
                    list.setCuscode(cuscode);
                    list.setConsumer_name(consumer_name);
                    list.setConnection_number(connection_number);
                    list.setLocation(location);
                    list.setTariff(tariff);
                    list.setTariff_desc(tariff_desc);
                    list.setPurpose_as_per_tneb(purpose_as_per_tneb);
                    list.setPvCode(PV_CODE);
                    list.setPHOTO_SAVED_STATUS(PHOTO_SAVED_STATUS);

                    ConnectionList.add(list);
                    //   Log.d("ConnectionList", "" + ConnectionList);
                } while (cursor.moveToNext());
            }

        }
        /*for(int i=0 ; i<ConnectionTakenList.size(); i++){
            TNEBSystem list =new TNEBSystem();
            if(ConnectionTakenList.get(i).getPHOTO_SAVED_STATUS().equals("N")){
                list.setId(ConnectionTakenList.get(i).getId());
                list.setRegion_code(ConnectionTakenList.get(i).getRegion_code());
                list.setCircle_code(ConnectionTakenList.get(i).getCircle_code());
                list.setSection_code(ConnectionTakenList.get(i).getSection_code());
                list.setDistribution_code(ConnectionTakenList.get(i).getDistribution_code());
                list.setConsumer_code(ConnectionTakenList.get(i).getConsumer_code());
                list.setCuscode(ConnectionTakenList.get(i).getCuscode());
                list.setConsumer_name(ConnectionTakenList.get(i).getConsumer_name());
                list.setConnection_number(ConnectionTakenList.get(i).getConnection_number());
                list.setLocation(ConnectionTakenList.get(i).getLocation());
                list.setTariff(ConnectionTakenList.get(i).getTariff());
                list.setTariff_desc(ConnectionTakenList.get(i).getTariff_desc());
                list.setPurpose_as_per_tneb(ConnectionTakenList.get(i).getPurpose_as_per_tneb());
                list.setPvCode(ConnectionTakenList.get(i).getPvCode());
                list.setPHOTO_SAVED_STATUS(ConnectionTakenList.get(i).getPHOTO_SAVED_STATUS());
                ConnectionList.add(list);

            }
        }*/

        if(ConnectionList.size()>0){
            connectionCheckActivityBinding.connectionListRecycler.setVisibility(View.VISIBLE);
            connectionCheckActivityBinding.notFoundIv.setVisibility(View.GONE);
            connectionListAdapter=new ConnectionListAdapter(this,ConnectionList,dbData);
            connectionCheckActivityBinding.connectionListRecycler.setAdapter(connectionListAdapter);
            connectionCheckActivityBinding.noConnectionCountLayout.setVisibility(View.VISIBLE);
            connectionCheckActivityBinding.connectionCount.setText(" - "+ConnectionList.size());
        }else {
            connectionCheckActivityBinding.noConnectionCountLayout.setVisibility(View.GONE);
            connectionCheckActivityBinding.connectionListRecycler.setVisibility(View.GONE);
            connectionCheckActivityBinding.notFoundIv.setVisibility(View.VISIBLE);
        }

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.action_search)
                .getActionView();
        searchView.setSearchableInfo(searchManager
                .getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);

        searchView.setQueryHint(getResources().getString(R.string.search_by_connection_no));
        int id = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_src_text", null, null);
        int id2 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_button", null, null);
        int id3 = searchView.getContext()
                .getResources()
                .getIdentifier("android:id/search_close_btn", null, null);
        TextView textView = (TextView) searchView.findViewById(id);
        ImageView img = (ImageView) searchView.findViewById(id2);
        ImageView close = (ImageView) searchView.findViewById(id3);
        img.setColorFilter(Color.WHITE);
        close.setColorFilter(Color.WHITE);
        textView.setTextColor(Color.WHITE);
        textView.setHintTextColor(this.getResources().getColor(R.color.grey_6));

        // listening to search query text change
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                connectionListAdapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                connectionListAdapter.getFilter().filter(query);
                return false;
            }
        });
        return true;
    }

}
