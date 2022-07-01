package com.nic.tnebnewwatersource.dataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.util.Log;

import com.nic.tnebnewwatersource.constant.AppConstant;
import com.nic.tnebnewwatersource.model.TNEBSystem;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

/**
 * Created by J.DILEEPKUMAR on 22/10/2021.
 */
public class dbData {
    private SQLiteDatabase db;
    private SQLiteOpenHelper dbHelper;
    private Context context;

    public dbData(Context context){
        this.dbHelper = new DBHelper(context);
        this.context = context;
    }

    public void open() {
        db = dbHelper.getWritableDatabase();
    }

    public void close() {
        if(dbHelper != null) {
            dbHelper.close();
        }
    }

    /****** DISTRICT TABLE *****/
    public TNEBSystem insertDistrict(TNEBSystem TNEBSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, TNEBSystem.getDistictCode());
        values.put(AppConstant.DISTRICT_NAME, TNEBSystem.getDistrictName());

        long id = db.insert(DBHelper.DISTRICT_TABLE_NAME,null,values);
        Log.d("Inserted_id_district", String.valueOf(id));

        return TNEBSystem;
    }

    public ArrayList<TNEBSystem> getAll_District() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DISTRICT_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setDistrictName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** BLOCKTABLE *****/
    public TNEBSystem insertBlock(TNEBSystem TNEBSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, TNEBSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, TNEBSystem.getBlockCode());
        values.put(AppConstant.BLOCK_NAME, TNEBSystem.getBlockName());

        long id = db.insert(DBHelper.BLOCK_TABLE_NAME,null,values);
        Log.d("Inserted_id_block", String.valueOf(id));

        return TNEBSystem;
    }

    public ArrayList<TNEBSystem> getAll_Block() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.BLOCK_TABLE_NAME,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setBlockName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /****** VILLAGE TABLE *****/
    public TNEBSystem insertVillage(TNEBSystem TNEBSystem) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, TNEBSystem.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, TNEBSystem.getBlockCode());
        values.put(AppConstant.PV_CODE, TNEBSystem.getPvCode());
        values.put(AppConstant.PV_NAME, TNEBSystem.getPvName());

        long id = db.insert(DBHelper.VILLAGE_TABLE_NAME,null,values);
        Log.d("Inserted_id_village", String.valueOf(id));

        return TNEBSystem;
    }

    public ArrayList<TNEBSystem> getAll_Village(String type,String dcode, String bcode) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            if(type.equals("")){
                cursor = db.rawQuery("select * from "+DBHelper.VILLAGE_TABLE_NAME+" order by pvname asc",null);
            }
            else {
                cursor = db.rawQuery("select * from "+DBHelper.VILLAGE_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by pvname asc",null);

            }

            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public TNEBSystem insertHabitation(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.DISTRICT_CODE, miTank.getDistictCode());
        values.put(AppConstant.BLOCK_CODE, miTank.getBlockCode());
        values.put(AppConstant.PV_CODE, miTank.getPvCode());
        values.put(AppConstant.HABB_CODE, miTank.getHabitation_code());
        values.put(AppConstant.HABITATION_NAME, miTank.getHabitation_name());
        values.put(AppConstant.HABITATION_NAME_TA, miTank.getHabitation_name_ta());

        long id = db.insert(DBHelper.HABITATION_TABLE_NAME,null,values);
        Log.d("Inserted_id_habitation", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insertConnectionStatus(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_CONNECTION_ID, miTank.getConncetion_id());
        values.put(AppConstant.KEY_CONNECTION_STATUS, miTank.getConnection_status());

        long id = db.insert(DBHelper.CONNECTION_STATUS,null,values);
        Log.d("Inserted_id_Con_status", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insertMotorTypeList(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ID, miTank.getId());
        values.put(AppConstant.KEY_MOTOR_TYPE, miTank.getMotor_type());

        long id = db.insert(DBHelper.MOTOR_TYPE_LIST,null,values);
        Log.d("Inserted_id_motor_type", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insert_tneb_motor_hpList(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put("tneb_hp_id", miTank.getTneb_hp_id());
        values.put("horse_power", miTank.getTneb_horse_power());

        long id = db.insert(DBHelper.TNEB_MOTOR_HP_POWER_LIST,null,values);
        Log.d("Inserted_id_hp_list", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insert_tneb_tank_capacity_List(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put("tneb_tank_capacity__id", miTank.getTneb_tank_capacity__id());
        values.put("purpose_id", miTank.getTneb_tank_capacity_purpose_id());
        values.put("tneb_tank_capacity", miTank.getTneb_tank_capacity());

        long id = db.insert(DBHelper.TNEB_TANK_CAPACITY_LIST,null,values);
        Log.d("Insert_id_tank_capacity", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insertConnectionPurpose(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_CONNECTION_ID, miTank.getConncetion_id());
        values.put(AppConstant.KEY_CONNECTION_NAME, miTank.getConncetion_name());
        values.put(AppConstant.KEY_CONNECTION_NAME_TA, miTank.getConnection_name_ta());

        long id = db.insert(DBHelper.CONNECTION_PURPOSE_LIST,null,values);
        Log.d("Inserted_id_ConnPurpose", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insertConnectionList(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put(AppConstant.KEY_ID, miTank.getId());
        values.put(AppConstant.KEY_REGION_CODE, miTank.getRegion_code());
        values.put(AppConstant.KEY_CIRCLE_CODE, miTank.getCircle_code());
        values.put(AppConstant.KEY_SECTION_CODE, miTank.getSection_code());
        values.put(AppConstant.KEY_DISTRIBUTION_CODE, miTank.getDistribution_code());
        values.put(AppConstant.KEY_CONSUMER_CODE, miTank.getConsumer_code());
        values.put(AppConstant.KEY_CUSCODE, miTank.getCuscode());
        values.put(AppConstant.KEY_CONSUMER_NAME, miTank.getConsumer_name());
        values.put(AppConstant.KEY_CONNECTION_NO, miTank.getConnection_number());
        values.put(AppConstant.KEY_LOCATION, miTank.getLocation());
        values.put(AppConstant.KEY_TARIFF, miTank.getTariff());
        values.put(AppConstant.KEY_TARIFF_DESC, miTank.getTariff_desc());
        values.put(AppConstant.KEY_PURPOSE_AS_PER_TNEB, miTank.getPurpose_as_per_tneb());
        values.put(AppConstant.PV_CODE, miTank.getPvCode());
        values.put(AppConstant.PHOTO_SAVED_STATUS, miTank.getPHOTO_SAVED_STATUS());

        long id = db.insert(DBHelper.CONNECTION_LIST,null,values);
        Log.d("Inserted_id_Connection", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insertBankTypeList(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put("account_id", miTank.getAccount_id());
        values.put("account_name", miTank.getAccount_name());
        values.put("account_id_display_order", miTank.getAccount_id_display_order());
        values.put("account_saved_status", miTank.getAccount_saved_status());


        long id = db.insert(DBHelper.TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE,null,values);
        Log.d("Inserted_id_bank_type", String.valueOf(id));

        return miTank;
    }
    public TNEBSystem insertSavedBankList(TNEBSystem miTank) {

        ContentValues values = new ContentValues();
        values.put("bank_account_id", miTank.getBank_account_id());
        values.put("account_id", miTank.getAccount_id());
        values.put("bank_id", miTank.getBank_id());
        values.put("branch_id", miTank.getBranch_id());
        values.put("ifsc_code", miTank.getIfsc_code());
        values.put("account_no", miTank.getAccount_no());
        values.put("dcode", miTank.getDistictCode());
        values.put("bcode", miTank.getBlockCode());
        values.put("pvcode", miTank.getPvCode());
        values.put("bank_name", miTank.getBank_name());
        values.put("branchname", miTank.getBranch_name());


        long id = db.insert(DBHelper.TN_EB_BANK_SERVER_DETAILS_TABLE,null,values);
        Log.d("Inserted_id_bank_list", String.valueOf(id));

        return miTank;
    }
    public void Insert_drinking_water_source_type(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("water_source_type_id", tnebSystem.getWater_source_type_id());
        values.put("water_source_type_name", tnebSystem.getWater_source_type_name());
        //values.put("is_others", tnebSystem.getIs_others());

        long id = db.insert(DBHelper.DRINKING_WATER_SOURCE_TABLE,null,values);
        Log.d("Insert_id_drink_water", String.valueOf(id));

    }
    public void Insert_drinking_water_source_server_data(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("water_source_details_id", tnebSystem.getWater_source_details_id());
        values.put("dcode", tnebSystem.getDistictCode());
        values.put("bcode", tnebSystem.getBlockCode());
        values.put("pv_code", tnebSystem.getPvCode());
        values.put("hab_code", tnebSystem.getHabitation_code());
        values.put("hab_name", tnebSystem.getHabitation_name());
        values.put("water_source_type_id", tnebSystem.getWater_source_type_id());
        values.put("water_source_type_name", tnebSystem.getWater_source_type_name());
        //values.put("is_others", tnebSystem.getIs_others());
        values.put("landmark", tnebSystem.getKEY_LAND_MARK());
        values.put("image_1", stringToByte(tnebSystem.getImage_1()));
        values.put("image_1_lat", tnebSystem.getImage_1_lat());
        values.put("image_1_long", tnebSystem.getImage_1_long());
        values.put("image_2", stringToByte(tnebSystem.getImage_2()));
        values.put("image_2_lat", tnebSystem.getImage_2_lat());
        values.put("image_2_long", tnebSystem.getImage_2_long());

        long id = db.insert(DBHelper.DRINKING_WATER_SOURCE_SERVER_DATA,null,values);
        Log.d("Insert_id_server_data", String.valueOf(id));

    }

    //////New Water Supply Status Details
    public void Insert_drinking_water_supply_timing(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("supply_timing_id", tnebSystem.getSupply_timing_id());
        values.put("supply_timing", tnebSystem.getSupply_timing());

        long id = db.insert(DBHelper.DRINKING_WATER_TIMING_DETAILS,null,values);
        Log.d("Insert_id_water_timing", String.valueOf(id));

    }
    public void Insert_drinking_water_session(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("session_id", tnebSystem.getSession_id());
        values.put("session_name", tnebSystem.getSession_name());

        long id = db.insert(DBHelper.DRINKING_WATER_SESSION,null,values);
        Log.d("Insert_id_water_session", String.valueOf(id));

    }
    public void Insert_drinking_water_type(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("water_type_id", tnebSystem.getWater_type_id());
        values.put("water_type", tnebSystem.getWater_type());

        long id = db.insert(DBHelper.DRINKING_WATER_TYPE,null,values);
        Log.d("Insert_id_water_type", String.valueOf(id));

    }
    public void Insert_water_supply_reason(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("id", tnebSystem.getId());
        values.put("reason_for_supply", tnebSystem.getReason_for_supply());
        values.put("type", tnebSystem.getReason_type());

        long id = db.insert(DBHelper.DRINKING_WATER_SUPPLY_REASON,null,values);
        Log.d("Insert_id_supply_reason", String.valueOf(id));

    }
    public void Insert_minimum_maximum_date(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("minimum_date", tnebSystem.getMinimum_date());
        values.put("maximum_date", tnebSystem.getMaximum_date());

        long id = db.insert(DBHelper.MINIMUM_MAXIMUM_DATE,null,values);
        Log.d("Insert_id_m_m_date", String.valueOf(id));

    }
    public void Insert_new_drinking_water_details_server1(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();

        values.put("entry_date", tnebSystem.getEntry_date());
        values.put("dcode", tnebSystem.getDistictCode());
        values.put("bcode", tnebSystem.getBlockCode());
        values.put("pv_code", tnebSystem.getPvCode());
        values.put("hab_code", tnebSystem.getHabitation_code());
        values.put("water_supply_status_id", tnebSystem.getWater_supply_status_id());
        values.put("no_supply_reason_id", tnebSystem.getWater_supplied_reason_id());
        values.put("session_fn_water_type_id", tnebSystem.getSession_fn_water_type_id());
        values.put("session_an_water_type_id", tnebSystem.getSession_an_water_type_id());
        values.put("session_an_timing_id", tnebSystem.getSession_an_timing_id());
        values.put("session_fn_timing_id", tnebSystem.getSession_fn_timing_id());
        values.put("session_fn_src_id", tnebSystem.getSession_fn_src_id());
        values.put("session_an_src_id", tnebSystem.getSession_an_src_id());

        long id = db.insert(DBHelper.NEW_DRINKING_WATER_DETAILS_SERVER_1,null,values);
        Log.d("Insert_details_server1", String.valueOf(id));

    }
    public void Insert_menu_access_control(TNEBSystem tnebSystem) {

        ContentValues values = new ContentValues();
        values.put("menu_id", tnebSystem.getMenu_id());
        values.put("menu_name", tnebSystem.getMenu_name());
        values.put("menu_access_control", tnebSystem.getMenu_access_control());

        long id = db.insert(DBHelper.MENU_ACCESS_CONTROL,null,values);
        Log.d("Insert_id_menu", String.valueOf(id));

    }

    //////////////

    public ArrayList<TNEBSystem> get_new_drinking_water_details_server1(String type,String entry_date,String dcode,String bcode,String pv_code,String hab_code ) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.NEW_DRINKING_WATER_DETAILS_SERVER_1,null);
            }
            else {
                selection = "entry_date = ? and dcode = ? and bcode = ? and pv_code = ? and hab_code = ? ";
                selectionArgs = new String[]{entry_date,dcode,bcode,pv_code,hab_code};
                cursor = db.query(DBHelper.NEW_DRINKING_WATER_DETAILS_SERVER_1,new String[]{"*"},
                        selection, selectionArgs, null, null, null);
                // cursor = db.rawQuery("select * from "+DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL+" where hab_code = "+hab_code,null);

            }


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setNew_water_details_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("new_water_details_server_1_primary_id")));
                    card.setEntry_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("entry_date")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("bcode")));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pv_code")));
                    card.setHabitation_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_code")));
                    card.setWater_supply_status_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_status_id")));
                    card.setWater_supplied_reason_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("no_supply_reason_id"))));
                    card.setSession_fn_water_type_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("session_fn_water_type_id"))));
                    card.setSession_an_water_type_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("session_an_water_type_id"))));
                    card.setSession_an_timing_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("session_an_timing_id"))));
                    card.setSession_fn_timing_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("session_fn_timing_id"))));
                    card.setSession_fn_src_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("session_fn_src_id")));
                    card.setSession_an_src_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("session_an_src_id"))));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TNEBSystem> getAll_Menu_Access_Control() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MENU_ACCESS_CONTROL,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setMenu_control_primary_key(cursor.getInt(cursor
                            .getColumnIndexOrThrow("menu_control_primary_key")));
                    card.setMenu_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("menu_id")));
                    card.setMenu_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("menu_name")));
                    card.setMenu_access_control(cursor.getString(cursor
                            .getColumnIndexOrThrow("menu_access_control")));


                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
            e.printStackTrace();
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TNEBSystem> getAll_BankTypes() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setAccount_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_id")));
                    card.setAccount_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_name")));
                    card.setAccount_id_display_order(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_id_display_order")));
                    card.setAccount_saved_status(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_saved_status")));


                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
            e.printStackTrace();
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_Habitation(String dcode, String bcode) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.HABITATION_TABLE_NAME+" where dcode = "+dcode+" and bcode = "+bcode+" order by habitation_name asc",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.DISTRICT_CODE)));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.BLOCK_CODE)));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.PV_CODE)));
                    card.setHabitation_code(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABB_CODE)));
                    card.setHabitation_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.HABITATION_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_ConnectionStatus() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.CONNECTION_STATUS,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setConncetion_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_ID)));
                    card.setConnection_status(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_STATUS)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_MotorTypeList() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MOTOR_TYPE_LIST,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setId(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setMotor_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_ConnectionPurposeList() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.CONNECTION_PURPOSE_LIST,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setConncetion_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_ID)));
                    card.setConncetion_name(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NAME)));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getSaveParticularBankDetails(String account_id) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
//             cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE,null);
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE+" where account_id = "+account_id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setAccount_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_id")));
                    card.setBank_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("bank_id")));
                    card.setBranch_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("branch_id")));
                    card.setAccount_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_no")));
                    card.setIfsc_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("ifsc_code")));
                    card.setBranch_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("branch_name")));
                    card.setBank_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("bank_name")));


                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAllBankDetails(String account_id) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
//             cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE,null);
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setAccount_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_id")));
                    card.setBank_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("bank_id")));
                    card.setBranch_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("branch_id")));
                    card.setAccount_no(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_no")));
                    card.setIfsc_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("ifsc_code")));
                    card.setBranch_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("branch_name")));
                    card.setBank_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("bank_name")));
                    card.setAccount_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("account_name")));



                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TNEBSystem> getAll_Tneb_Horse_Power_List() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.TNEB_MOTOR_HP_POWER_LIST,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setTneb_hp_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("tneb_hp_id")));
                    card.setTneb_horse_power(cursor.getString(cursor
                            .getColumnIndexOrThrow("horse_power")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_Tneb_Tank_Capacity_List() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.TNEB_TANK_CAPACITY_LIST,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setTneb_tank_capacity__id(cursor.getString(cursor
                            .getColumnIndexOrThrow("tneb_tank_capacity__id")));
                    card.setTneb_tank_capacity_purpose_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("purpose_id")));
                    card.setTneb_tank_capacity(cursor.getString(cursor
                            .getColumnIndexOrThrow("tneb_tank_capacity")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> get_Particular_Tneb_Tank_Capacity_List(String id) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            //cursor = db.rawQuery("select * from "+DBHelper.TNEB_TANK_CAPACITY_LIST,null);
            cursor = db.rawQuery("select * from "+DBHelper.TNEB_TANK_CAPACITY_LIST+" where purpose_id = "+id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setTneb_tank_capacity__id(cursor.getString(cursor
                            .getColumnIndexOrThrow("tneb_tank_capacity__id")));
                    card.setTneb_tank_capacity_purpose_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("purpose_id")));
                    card.setTneb_tank_capacity(cursor.getString(cursor
                            .getColumnIndexOrThrow("tneb_tank_capacity")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_drinking_water_source_type() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SOURCE_TABLE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setWater_source_type_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_id")));
                    card.setWater_source_type_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_name")));
                    /*card.setIs_others(cursor.getString(cursor
                            .getColumnIndexOrThrow("is_others")));*/

                    cards.add(card);
                }
            }
        } catch (Exception e){
               Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    /////**************New Water Supply Status Details
    public ArrayList<TNEBSystem> getAll_drinking_water_supply_timing() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_TIMING_DETAILS+" order by supply_timing_id asc ",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setSupply_timing_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("supply_timing_id")));
                    card.setSupply_timing(cursor.getString(cursor
                            .getColumnIndexOrThrow("supply_timing")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_drinking_water_session() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SESSION+" order by session_id asc ",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setSession_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("session_id")));
                    card.setSession_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("session_name")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_drinking_water_type() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_TYPE+" order by water_type_id asc ",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setWater_type_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_type_id")));
                    card.setWater_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_type")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_water_supply_reason() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SUPPLY_REASON+" order by id asc ",null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setId(cursor.getString(cursor
                            .getColumnIndexOrThrow("id")));
                    card.setReason_for_supply(cursor.getString(cursor
                            .getColumnIndexOrThrow("reason_for_supply")));
                    card.setReason_type(cursor.getString(cursor
                            .getColumnIndexOrThrow("type")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getAll_minimum_max_date() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            cursor = db.rawQuery("select * from "+DBHelper.MINIMUM_MAXIMUM_DATE,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();

                    card.setMinimum_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("minimum_date")));
                    card.setMaximum_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("maximum_date")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    ///////**********

    public ArrayList<TNEBSystem> getSavedWorkDetailsNew() {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String query ="select * from tn_eb_connection_details_save_table_new";

        try {
            cursor = db.rawQuery(query,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setConncetion_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setCuscode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CUSCODE)));
                    card.setConnection_number(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO)));
                    card.setKEY_CONNECTION_STATUS_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_STATUS_ID)));
                    card.setKEY_HAB_CODE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HAB_CODE)));
                    card.setKEY_METER_IMAGE_LAT(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_IMAGE_LAT)));
                    card.setKEY_METER_IMAGE_LONG(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_IMAGE_LONG)));
                    card.setKEY_PURPOSE_IDS(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PURPOSE_IDS)));
                    card.setMotor_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE)));
                    card.setHorse_power(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HORSE_POWER)));
                    card.setTank_capacity(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_TANK_CAPACITY)));
                    card.setStreet_light_count(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_STREET_LIGHT_COUNT)));


                    card.setKEY_MINI_OHT_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_LATITUDE)));
                    card.setKEY_OHT_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_LATITUDE)));
                    card.setKEY_MINI_OHT_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_LANGTITUDE)));
                    card.setKEY_OHT_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_LANGTITUDE)));
                    card.setKEY_OHT_TANK_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_LATITUDE)));
                    card.setKEY_OHT_TANK_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_LANGTITUDE)));
                    card.setKEY_MINI_OHT_TANK_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_LATITUDE)));
                    card.setKEY_MINI_OHT_TANK_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_LANGTITUDE)));
                    card.setKEY_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LAND_MARK)));
                    card.setKEY_MINI_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_TYPE)));
                    card.setKEY_MINI_MOTOR_HP(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_HP)));
                    card.setKEY_MINI_MOTOR_TANK_CAPACITY(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_TANK_CAPACITY)));
                    card.setKEY_MINI_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_LAND_MARK)));
                    card.setMini_oht_tank_count(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_TANK_COUNT)));
                    card.setOht_tank_count(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_MOTOR_TANK_COUNT)));
                    card.setMeter_available(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_AVAILABLE)));


///GLR Motor
                    card.setKEY_GLR_MOTOR_TANK_COUNT(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_TANK_COUNT)));
                    card.setKEY_GLR_TANK_CAPACITY(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_TANK_CAPACITY)));
                    card.setKEY_GLR_HORSE_POWER(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_HORSE_POWER)));
                    card.setKEY_GLR_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_TYPE)));
                    card.setKEY_GLR_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_LAND_MARK)));
                    card.setKEY_GLR_LONGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_LONGTITUDE)));
                    card.setKEY_GLR_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_LATITUDE)));

                    ///Power Pump
                    card.setKEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT)));
                    card.setKEY_MINI_POWER_PUMP_TANK_CAPACITY(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_TANK_CAPACITY)));
                    card.setKEY_MINI_POWER_PUMP_HORSE_POWER(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_HORSE_POWER)));
                    card.setKEY_MINI_POWER_PUMP_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TYPE)));
                    card.setKEY_MINI_POWER_PUMP_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_LAND_MARK)));
                    card.setKEY_MINI_POWER_PUMP_LONGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_LONGTITUDE)));
                    card.setKEY_MINI_POWER_PUMP_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_LATITUDE)));

                    ////Mini With Out OHT
                    card.setKEY_MINI_WITH_OUT_OHT_HORSE_POWER(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_WITH_OUT_OHT_HORSE_POWER)));
                    card.setKEY_mini_with_out_oht_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_MOTOR_TYPE)));
                    card.setKEY_mini_with_out_oht_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_LAND_MARK)));
                    card.setKEY_mini_with_out_oht_LONGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_LONGTITUDE)));
                    card.setKEY_mini_with_out_oht_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_LATITUDE)));


                    cards.add(card);
                }
            }
        } catch (Exception e){
               Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getSavedWorkDetailsImages(String connection_number) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
//             cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE,null);
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES+" where connection_number = "+connection_number,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setConncetion_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setCuscode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CUSCODE)));
                    card.setConnection_number(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO)));
                    card.setKEY_CONNECTION_STATUS_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_STATUS_ID)));
                    card.setKEY_HAB_CODE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HAB_CODE)));
                    card.setKEY_METER_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_IMAGE)))));
                    card.setKEY_LAST_BILL_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LAST_BILL_IMAGE)))));
                    card.setKEY_EB_CARD_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_EB_CARD_IMAGE)))));
                    card.setKEY_MINI_OHT_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_MOTOR_IMAGE)))));
                    card.setKEY_MINI_OHT_TANK_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_IMAGE)))));
                    card.setKEY_OHT_TANK_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_IMAGE)))));
                    card.setKEY_OHT_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_MOTOR_IMAGE)))));
                    card.setKEY_GLR_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_IMAGE)))));
                    card.setKEY_MINI_POWER_PUMP_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_IMAGE)))));
                    card.setKEY_mini_with_out_oht_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_MOTOR_IMAGE)))));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            //   Log.d(DEBUG_TAG, "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TNEBSystem> getConnectionDetailsNew(String id) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
//             cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE,null);
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW+" where id = "+id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setConncetion_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setCuscode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CUSCODE)));
                    card.setConnection_number(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO)));
                    card.setKEY_CONNECTION_STATUS_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_STATUS_ID)));
                    card.setKEY_HAB_CODE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HAB_CODE)));
                    /*card.setKEY_LAST_BILL_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LAST_BILL_IMAGE)));
                    card.setKEY_EB_CARD_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_EB_CARD_IMAGE)));*/
                    card.setKEY_METER_IMAGE_LAT(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_IMAGE_LAT)));
                    card.setKEY_METER_IMAGE_LONG(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_IMAGE_LONG)));
                    card.setKEY_PURPOSE_IDS(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_PURPOSE_IDS)));
                    card.setMotor_type(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MOTOR_TYPE)));
                    card.setHorse_power(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HORSE_POWER)));
                    card.setTank_capacity(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_TANK_CAPACITY)));
                    card.setStreet_light_count(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_STREET_LIGHT_COUNT)));

                    /*card.setKEY_MINI_OHT_MOTOR_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_MOTOR_IMAGE)));
                    card.setKEY_MINI_OHT_TANK_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_IMAGE)));
                    card.setKEY_OHT_TANK_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_IMAGE)));
                    card.setKEY_OHT_MOTOR_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_MOTOR_IMAGE)));*/
                    card.setKEY_MINI_OHT_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_LATITUDE)));
                    card.setKEY_OHT_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_LATITUDE)));
                    card.setKEY_MINI_OHT_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_LANGTITUDE)));
                    card.setKEY_OHT_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_LANGTITUDE)));
                    card.setKEY_OHT_TANK_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_LATITUDE)));
                    card.setKEY_OHT_TANK_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_LANGTITUDE)));
                    card.setKEY_MINI_OHT_TANK_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_LATITUDE)));
                    card.setKEY_MINI_OHT_TANK_LANGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_LANGTITUDE)));
                    card.setKEY_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LAND_MARK)));
                    card.setKEY_MINI_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_TYPE)));
                    card.setKEY_MINI_MOTOR_HP(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_HP)));
                    card.setKEY_MINI_MOTOR_TANK_CAPACITY(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_TANK_CAPACITY)));
                    card.setKEY_MINI_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_LAND_MARK)));
                    card.setMini_oht_tank_count(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_MOTOR_TANK_COUNT)));
                    card.setOht_tank_count(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_MOTOR_TANK_COUNT)));
                    card.setMeter_available(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_AVAILABLE)));

                    ///GLR Motor
                    card.setKEY_GLR_MOTOR_TANK_COUNT(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_TANK_COUNT)));
                    card.setKEY_GLR_TANK_CAPACITY(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_TANK_CAPACITY)));
                    card.setKEY_GLR_HORSE_POWER(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_HORSE_POWER)));
                    card.setKEY_GLR_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_TYPE)));
                    card.setKEY_GLR_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_LAND_MARK)));
                    card.setKEY_GLR_LONGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_LONGTITUDE)));
                    card.setKEY_GLR_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_LATITUDE)));
                    /*card.setKEY_GLR_MOTOR_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_IMAGE)));
*/
                    ///Power Pump
                    card.setKEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT)));
                    card.setKEY_MINI_POWER_PUMP_TANK_CAPACITY(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_TANK_CAPACITY)));
                    card.setKEY_MINI_POWER_PUMP_HORSE_POWER(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_HORSE_POWER)));
                    card.setKEY_MINI_POWER_PUMP_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_TYPE)));
                    card.setKEY_MINI_POWER_PUMP_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_LAND_MARK)));
                    card.setKEY_MINI_POWER_PUMP_LONGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_LONGTITUDE)));
                    card.setKEY_MINI_POWER_PUMP_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_LATITUDE)));
                   /* card.setKEY_MINI_POWER_PUMP_MOTOR_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_IMAGE)));*/


                    ////Mini With Out OHT
                    card.setKEY_MINI_WITH_OUT_OHT_HORSE_POWER(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_WITH_OUT_OHT_HORSE_POWER)));
                    card.setKEY_mini_with_out_oht_MOTOR_TYPE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_MOTOR_TYPE)));
                    card.setKEY_mini_with_out_oht_LAND_MARK(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_LAND_MARK)));
                    card.setKEY_mini_with_out_oht_LONGTITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_LONGTITUDE)));
                    card.setKEY_mini_with_out_oht_LATITUDE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_LATITUDE)));
                   /* card.setKEY_mini_with_out_oht_MOTOR_IMAGE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_MOTOR_IMAGE)));*/
//

                    /*if(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO)).equalsIgnoreCase(connection_number))
                    {
                        cards.add(card);
                    }*/
                    cards.add(card);
                }
            }
        } catch (Exception e){
               Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getConnectionDetailsImages(String id) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
//             cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE,null);
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES+" where id = "+id,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setConncetion_id(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_ID)));
                    card.setCuscode(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CUSCODE)));
                    card.setConnection_number(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_NO)));
                    card.setKEY_CONNECTION_STATUS_ID(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_CONNECTION_STATUS_ID)));
                    card.setKEY_HAB_CODE(cursor.getString(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_HAB_CODE)));
                    card.setKEY_METER_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_METER_IMAGE)))));
                    card.setKEY_LAST_BILL_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_LAST_BILL_IMAGE)))));
                    card.setKEY_EB_CARD_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_EB_CARD_IMAGE)))));
                    card.setKEY_MINI_OHT_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_MOTOR_IMAGE)))));
                    card.setKEY_MINI_OHT_TANK_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_OHT_TANK_IMAGE)))));
                    card.setKEY_OHT_TANK_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_TANK_IMAGE)))));
                    card.setKEY_OHT_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_OHT_MOTOR_IMAGE)))));
                    card.setKEY_GLR_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_GLR_MOTOR_IMAGE)))));
                    card.setKEY_MINI_POWER_PUMP_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_MINI_POWER_PUMP_MOTOR_IMAGE)))));
                    card.setKEY_mini_with_out_oht_MOTOR_IMAGE(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow(AppConstant.KEY_mini_with_out_oht_MOTOR_IMAGE)))));
                    cards.add(card);
                }
            }
        } catch (Exception e){
               Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TNEBSystem> getWaterSupplyDetails(String hab_code) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
//             cursor = db.rawQuery("select * from "+DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE,null);
            cursor = db.rawQuery("select * from "+DBHelper.TN_EB_WATER_SUPPLY_DETAILS_TABLE+" where hab_code = "+hab_code,null);
            // cursor = db.query(CardsDBHelper.TABLE_CARDS,
            //       COLUMNS, null, null, null, null, null);
            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pv_code")));
                    card.setHabitation_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_code")));
                    card.setHabitation_name_ta(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_name_ta")));
                    card.setHabitation_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_name")));
                    card.setStatus_type_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("status_type_id")));
                    card.setWater_supplied_reason_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supplied_reason_id")));
                    card.setWater_supplied_reason_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supplied_reason_name")));
                    card.setWater_supplied_through_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supplied_through_id")));
                    card.setWater_supplied_through_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supplied_through_name")));
                    card.setTotal_no_of_trips(cursor.getString(cursor
                            .getColumnIndexOrThrow("total_no_of_trips")));
                    card.setWater_supply_litter(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_litter")));
                    card.setAmount(cursor.getString(cursor
                            .getColumnIndexOrThrow("amount")));

                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getDrinkingWaterDetailsImages(String water_source_details_id,String type) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SOURCE_VILLAGE_LEVEL,null);

            }
            else {
                cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SOURCE_VILLAGE_LEVEL+" where water_source_details_id = "+water_source_details_id,null);

            }

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setWater_source_details_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("water_source_details_primary_id")));
                    card.setWater_source_details_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_details_id")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("bcode")));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pv_code")));
                    card.setHabitation_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_code")));
                    card.setHabitation_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_name")));
                    card.setNo_of_photos(cursor.getString(cursor
                            .getColumnIndexOrThrow("no_of_photos")));
                    card.setWater_source_type_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_id")));
                    card.setWater_source_type_name((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_name"))));
                    card.setIs_others((cursor.getString(cursor
                            .getColumnIndexOrThrow("is_others"))));
                    card.setKEY_LAND_MARK((cursor.getString(cursor
                            .getColumnIndexOrThrow("landmark"))));
                    card.setImage_1(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow("image_1")))));
                    card.setImage_1_lat((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_1_lat"))));
                    card.setImage_1_long((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_1_long"))));
                    card.setImage_2(BitMapToString(ByteArraytoBitmap(cursor.getBlob(cursor
                            .getColumnIndexOrThrow("image_2")))));
                    card.setImage_2_lat((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_2_lat"))));
                    card.setImage_2_long((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_2_long"))));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public ArrayList<TNEBSystem> getDrinkingWaterServerDetailsImages(String type,String hab_code) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;

        try {
            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SOURCE_SERVER_DATA,null);
            }
            else {
                cursor = db.rawQuery("select * from "+DBHelper.DRINKING_WATER_SOURCE_SERVER_DATA+" where hab_code = "+hab_code,null);

            }


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setServer_water_source_details_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("server_water_source_details_primary_id")));
                    card.setWater_source_details_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_details_id")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("bcode")));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pv_code")));
                    card.setHabitation_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_code")));
                    card.setHabitation_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_name")));
                    card.setWater_source_type_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_id")));
                    card.setWater_source_type_name((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_name"))));
                    /*card.setIs_others((cursor.getString(cursor
                            .getColumnIndexOrThrow("is_others"))));*/
                    card.setKEY_LAND_MARK((cursor.getString(cursor
                            .getColumnIndexOrThrow("landmark"))));
                    card.setImage_1(byteToString((cursor.getBlob(cursor
                            .getColumnIndexOrThrow("image_1")))));
                    card.setImage_1_lat((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_1_lat"))));
                    card.setImage_1_long((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_1_long"))));
                    card.setImage_2(byteToString((cursor.getBlob(cursor
                            .getColumnIndexOrThrow("image_2")))));
                    card.setImage_2_lat((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_2_lat"))));
                    card.setImage_2_long((cursor.getString(cursor
                            .getColumnIndexOrThrow("image_2_long"))));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }
    public ArrayList<TNEBSystem> getNewDrinkingWaterDetailsLocal(String type,String entry_date,String dcode,String bcode,String pv_code,String hab_code,String water_session_id ) {

        ArrayList<TNEBSystem> cards = new ArrayList<>();
        Cursor cursor = null;
        String selection;
        String[] selectionArgs;
        try {
            if(type.equals("All")){
                cursor = db.rawQuery("select * from "+DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL,null);
            }
            else {
                selection = "entry_date = ? and dcode = ? and bcode = ? and pv_code = ? and hab_code = ? and water_session_id = ? ";
                selectionArgs = new String[]{entry_date,dcode,bcode,pv_code,hab_code,water_session_id};
                cursor = db.query(DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL,new String[]{"*"},
                        selection, selectionArgs, null, null, null);
               // cursor = db.rawQuery("select * from "+DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL+" where hab_code = "+hab_code,null);

            }


            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    TNEBSystem card = new TNEBSystem();
                    card.setNew_water_details_primary_id(cursor.getInt(cursor
                            .getColumnIndexOrThrow("new_water_details_primary_id")));
                    card.setEntry_date(cursor.getString(cursor
                            .getColumnIndexOrThrow("entry_date")));
                    card.setDistictCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("dcode")));
                    card.setBlockCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("bcode")));
                    card.setPvCode(cursor.getString(cursor
                            .getColumnIndexOrThrow("pv_code")));
                    card.setPvName(cursor.getString(cursor
                            .getColumnIndexOrThrow("pv_name")));
                    card.setHabitation_code(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_code")));
                    card.setHabitation_name(cursor.getString(cursor
                            .getColumnIndexOrThrow("hab_name")));
                    card.setWater_supply_status_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_supply_status_id")));
                    card.setWater_supplied_reason_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("no_supply_reason_id"))));
                    card.setReason_for_supply((cursor.getString(cursor
                            .getColumnIndexOrThrow("no_supply_reason_name"))));
                    card.setWater_type_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_type_id"))));
                    card.setWater_type((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_type_name"))));
                    card.setSession_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_session_id"))));
                    card.setSession_name((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_session_name"))));
                    card.setWater_source_details_id(cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_details_id")));
                    card.setWater_source_type_name((cursor.getString(cursor
                            .getColumnIndexOrThrow("water_source_type_name"))));
                    card.setMorning_water_supply_timing_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("morning_water_supply_timing_id"))));
                    card.setMorning_water_supply_timing_name((cursor.getString(cursor
                            .getColumnIndexOrThrow("morning_water_supply_timing_name"))));
                    card.setEvening_water_supply_timing_id((cursor.getString(cursor
                            .getColumnIndexOrThrow("evening_water_supply_timing_id"))));
                    card.setEvening_water_supply_timing_name((cursor.getString(cursor
                            .getColumnIndexOrThrow("evening_water_supply_timing_name"))));
                    cards.add(card);
                }
            }
        } catch (Exception e){
            Log.d("DEBUG_TAG", "Exception raised with a value of " + e);
        } finally{
            if (cursor != null) {
                cursor.close();
            }
        }
        return cards;
    }

    public String BitMapToString(Bitmap bitmap){
        String temp="";
        try {
            ByteArrayOutputStream baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
            byte [] b=baos.toByteArray();
            temp= Base64.encodeToString(b, Base64.DEFAULT);
        }
        catch (Exception e){
        }
        return temp;
    }
    public Bitmap ByteArraytoBitmap(byte[] byteArray){
        Bitmap bitmap=null;
        if(byteArray!=null){
             bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.length);
            return bitmap;
        }
        else {
            byteArray=new byte[]{};
            return bitmap;
        }

    }

    private byte[] stringToByte(String data){
        byte[] byteArr =new byte[]{};
        if(!data.equals("")){
            return  byteArr = data.getBytes();
        }
        else {
            return  byteArr;
        }
        // print the byte[] elements
    }

    private String byteToString(byte[] data){
        String str = new String(data);
        return str;
    }

    //Delete Tables
    public void deleteDistrictTable() {
        db.execSQL("delete from " + DBHelper.DISTRICT_TABLE_NAME);
    }
    public void deleteBlockTable() {
        db.execSQL("delete from " + DBHelper.BLOCK_TABLE_NAME);
    }
    public void deleteVillageTable() {
        db.execSQL("delete from " + DBHelper.VILLAGE_TABLE_NAME);
    }
    ///Delete Tables
    public void deleteConnectionStatusListTable() {
        db.execSQL("delete from " + DBHelper.CONNECTION_STATUS);
    }
    public void deleteHabitationListTable() {
        db.execSQL("delete from " + DBHelper.HABITATION_TABLE_NAME);
    }
    public void deleteMotorTypeListTable() {
        db.execSQL("delete from " + DBHelper.MOTOR_TYPE_LIST);
    }
    public void deleteConnectionPurposeListTable() {
        db.execSQL("delete from " + DBHelper.CONNECTION_PURPOSE_LIST);
    }
    public void deleteConnectionTable() {
        db.execSQL("delete from " + DBHelper.CONNECTION_LIST);
    }
    public void delete_tn_eb_connection_details_save_tableTable() {
        db.execSQL("delete from " + DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW);
    }
    public void delete_tn_eb_connection_details_images_save_tableTable() {
        db.execSQL("delete from " + DBHelper.TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES);
    }
    public void delete_tn_eb_type_of_bank_details_save_tableTable() {
        db.execSQL("delete from " + DBHelper.TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE);
    }
    public void delete_TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE() {
        db.execSQL("delete from " + DBHelper.TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE);
    }
    public void delete_TN_EB_BANK_SERVER_DETAILS_TABLE() {
        db.execSQL("delete from " + DBHelper.TN_EB_BANK_SERVER_DETAILS_TABLE);
    }
    public void delete_TNEB_MOTOR_HP_POWER_LIST() {
        db.execSQL("delete from " + DBHelper.TNEB_MOTOR_HP_POWER_LIST);
    }
    public void delete_TNEB_TANK_CAPACITY_LIST() {
        db.execSQL("delete from " + DBHelper.TNEB_TANK_CAPACITY_LIST);
    }
    public void delete_TN_EB_WATER_SUPPLY_DETAILS_TABLE() {
        db.execSQL("delete from " + DBHelper.TN_EB_WATER_SUPPLY_DETAILS_TABLE);
    }
    public void delete_DRINKING_WATER_SOURCE_TABLE() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_SOURCE_TABLE);
    }
    public void delete_DRINKING_WATER_SOURCE_VILLAGE_LEVEL() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_SOURCE_VILLAGE_LEVEL);
    }
    public void delete_DRINKING_WATER_SOURCE_SERVER_DATA() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_SOURCE_SERVER_DATA);
    }
    public void delete_DRINKING_WATER_TIMING_DETAILS() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_TIMING_DETAILS);
    }
    public void delete_DRINKING_WATER_SESSION() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_SESSION);
    }
    public void delete_DRINKING_WATER_TYPE() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_TYPE);
    }
    public void delete_DRINKING_WATER_SUPPLY_REASON() {
        db.execSQL("delete from " + DBHelper.DRINKING_WATER_SUPPLY_REASON);
    }
    public void delete_MINIMUM_MAXIMUM_DATE() {
        db.execSQL("delete from " + DBHelper.MINIMUM_MAXIMUM_DATE);
    }
    public void delete_NEW_DRINKING_WATER_DETAILS_LOCAL() {
        db.execSQL("delete from " + DBHelper.NEW_DRINKING_WATER_DETAILS_LOCAL);
    }
    public void delete_NEW_DRINKING_WATER_DETAILS_SERVER_1() {
        db.execSQL("delete from " + DBHelper.NEW_DRINKING_WATER_DETAILS_SERVER_1);
    }
    public void delete_MENU_ACCESS_CONTROL() {
        db.execSQL("delete from " + DBHelper.MENU_ACCESS_CONTROL);
    }


    public void deleteAll() {
        deleteDistrictTable();
        deleteBlockTable();
        deleteVillageTable();
        deleteConnectionStatusListTable();
        deleteHabitationListTable();
        deleteMotorTypeListTable();
        deleteConnectionPurposeListTable();
        deleteConnectionTable();
        delete_tn_eb_connection_details_save_tableTable();
        delete_tn_eb_connection_details_images_save_tableTable();
        delete_tn_eb_type_of_bank_details_save_tableTable();
        delete_TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE();
        delete_TN_EB_BANK_SERVER_DETAILS_TABLE();
        delete_TNEB_MOTOR_HP_POWER_LIST();
        delete_TNEB_TANK_CAPACITY_LIST();
        delete_TN_EB_WATER_SUPPLY_DETAILS_TABLE();
        delete_DRINKING_WATER_SOURCE_TABLE();
        delete_DRINKING_WATER_SOURCE_VILLAGE_LEVEL();
        delete_DRINKING_WATER_SOURCE_SERVER_DATA();

        ////*********New Water Supply Status Details
        delete_DRINKING_WATER_TIMING_DETAILS();
        delete_DRINKING_WATER_SESSION();
        delete_DRINKING_WATER_TYPE();
        delete_DRINKING_WATER_SUPPLY_REASON();
        delete_MINIMUM_MAXIMUM_DATE();
        delete_NEW_DRINKING_WATER_DETAILS_LOCAL();
        delete_NEW_DRINKING_WATER_DETAILS_SERVER_1();
        delete_MENU_ACCESS_CONTROL();
        ////
        /*deleteDistrictTable();
        deleteBlockTable();
        deleteVillageTable();
        delete_tn_eb_connection_details_save_tableTable();
        deleteConnectionStatusListTable();
        deleteHabitationListTable();
        deleteConnectionPurposeListTable();
        deleteConnectionTable();
        deleteMotorTypeListTable();
        delete_TN_EB_BANK_SERVER_DETAILS_TABLE();
        delete_TNEB_MOTOR_HP_POWER_LIST();
        delete_TNEB_TANK_CAPACITY_LIST();
        delete_tn_eb_type_of_bank_details_save_tableTable();*/
    }



}
