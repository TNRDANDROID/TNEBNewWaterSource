package com.nic.tnebnewwatersource.dataBase;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by J.DILEEPKUMAR on 22/10/2021.
 */

public class DBHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "TNEB_Water";
    private static final int DATABASE_VERSION = 1;

    public static final String DISTRICT_TABLE_NAME = "DistrictTable";
    public static final String BLOCK_TABLE_NAME = " BlockTable";
    public static final String VILLAGE_TABLE_NAME = " villageTable";
    public static final String HABITATION_TABLE_NAME = "habitaionTable";
    public static final String CONNECTION_STATUS = "connection_status";
    public static final String CONNECTION_PURPOSE_LIST = "connectionPurposeList";
    public static final String CONNECTION_LIST = "connection_list";
    public static final String MOTOR_TYPE_LIST = "motorTypeList";
    public static final String TNEB_MOTOR_HP_POWER_LIST = "tneb_motor_hp_power_list";
    public static final String TNEB_TANK_CAPACITY_LIST = "tneb_tank_capacity_list";
    public static final String TN_EB_CONNECTION_DETAILS_SAVE_TABLE = "tn_eb_connection_details_save_table";
    public static final String TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW = "tn_eb_connection_details_save_table_new";
    public static final String TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES = "tn_eb_connection_details_save_table_images";
    public static final String TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE = "tn_eb_bank_type__details_save_table";
    public static final String TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE = "tn_eb_bank_save__details_save_table";
    public static final String TN_EB_BANK_SERVER_DETAILS_TABLE = "tn_eb_bank_server_details_table";
    public static final String TN_EB_WATER_SUPPLY_DETAILS_TABLE = "tn_eb_water_supply_details_table";
    public static final String DRINKING_WATER_SOURCE_TABLE = "m_drinking_water_source";
    public static final String DRINKING_WATER_SOURCE_VILLAGE_LEVEL = "m_driking_water_source_village_level";
    public static final String DRINKING_WATER_SOURCE_SERVER_DATA = "m_drinking_water_source_server_data";

    private Context context;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;

    }

    //creating tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + DISTRICT_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "dname TEXT)");

        db.execSQL("CREATE TABLE " + BLOCK_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "bname TEXT)");

        db.execSQL("CREATE TABLE " + VILLAGE_TABLE_NAME + " ("
                + "dcode INTEGER," +
                "bcode INTEGER," +
                "pvcode INTEGER," +
                "pvname TEXT)");

        db.execSQL("CREATE TABLE " + HABITATION_TABLE_NAME + " ("
                + "dcode TEXT," +
                "bcode TEXT," +
                "pvcode TEXT," +
                "habitation_code TEXT," +
                "habitation_name_ta TEXT," +
                "habitation_name TEXT)");

        db.execSQL("CREATE TABLE " + CONNECTION_STATUS + " ("
                + "id TEXT," +
                "status TEXT)");
        db.execSQL("CREATE TABLE " + CONNECTION_PURPOSE_LIST + " ("
                + "id TEXT," +
                "connection_name_ta TEXT," +
                "connection_name TEXT)");
        db.execSQL("CREATE TABLE " + MOTOR_TYPE_LIST + " ("
                + "id TEXT," +
                "motor_type TEXT)");

        db.execSQL("CREATE TABLE " + TNEB_MOTOR_HP_POWER_LIST + " ("
                + "tneb_hp_id TEXT," +
                "horse_power TEXT)");

        db.execSQL("CREATE TABLE " + TNEB_TANK_CAPACITY_LIST + " ("
                + "tneb_tank_capacity__id TEXT," +
                "purpose_id TEXT," +
                "tneb_tank_capacity TEXT)");

        db.execSQL("CREATE TABLE " + CONNECTION_LIST + " ("
                + "id TEXT," +
                "pvcode TEXT," +
                "region_code TEXT," +
                "circle_code TEXT," +
                "section_code TEXT," +
                "distribution_code TEXT," +
                "consumer_code TEXT," +
                "cuscode TEXT," +
                "connection_number TEXT," +
                "consumer_name TEXT," +
                "location TEXT," +
                "tariff TEXT," +
                "tariff_desc TEXT," +
                "photo_saved_status TEXT," +
                "purpose_as_per_tneb TEXT)");

        db.execSQL("CREATE TABLE " + TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW + " ("
                + "id TEXT," +
                "cuscode TEXT," +
                "connection_number TEXT," +
                "connection_status_id TEXT," +
                "habcode TEXT," +
                "meter_image_lat TEXT," +
                "meter_image_long TEXT," +
                "mini_with_out_oht_lat_TextValue TEXT," +
                "mini_with_out_oht_long_TextValue TEXT," +
                "mini_with_out_oht_land_mark TEXT," +
                "mini_with_out_oht_motor_type TEXT," +
                "mini_with_out_oht_horse_power TEXT," +
                "mini_oht_lat_TextValue TEXT," +
                "oht_lat_TextValue TEXT," +
                "glr_lat_TextValue TEXT," +
                "mini_oht_long_TextValue TEXT," +
                "oht_long_TextValue TEXT," +
                "glr_long_TextValue TEXT," +
                "oht_tank_lat_Value TEXT," +
                "oht_tank_long_Value TEXT," +
                "mini_oht_tank_lat_Value TEXT," +
                "mini_oht_tank_long_Value TEXT," +
                "land_mark TEXT," +
                "glr_land_mark TEXT," +
                "mini_land_mark TEXT," +
                "mini_motor_type TEXT," +
                "mini_motor_hp TEXT," +
                "mini_motor_tank_capacity TEXT," +
                "street_light_count TEXT," +
                "motor_type TEXT," +
                "glr_motor_type TEXT," +
                "horse_power TEXT," +
                "glr_horse_power TEXT," +
                "meter_available TEXT," +
                "tank_capacity TEXT," +
                "glr_tank_capacity TEXT," +
                "mini_motor_tank_count TEXT," +
                "oht_motor_tank_count TEXT," +
                "glr_motor_tank_count TEXT," +
                "mini_power_pump_motor_tank_count TEXT," +
                "mini_power_pump_tank_capacity TEXT," +
                "mini_power_pump_horse_power TEXT," +
                "mini_power_pump_motor_type TEXT," +
                "mini_power_pump_land_mark TEXT," +
                "mini_power_pump_long_TextValue TEXT," +
                "mini_power_pump_lat_TextValue TEXT," +
                "purpose_ids TEXT)");
        db.execSQL("CREATE TABLE " + TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES + " ("
                + "id TEXT," +
                "cuscode TEXT," +
                "connection_number TEXT," +
                "connection_status_id TEXT," +
                "habcode TEXT," +
                "meter_image BLOB," +
                "last_bill_image BLOB," +
                "eb_card_image BLOB," +
                "mini_oht_motor_image BLOB," +
                "mini_oht_tank_image BLOB," +
                "oht_tank_image BLOB," +
                "oht_motor_image BLOB," +
                "glr_motor_image BLOB," +
                "mini_with_out_oht_motor_image BLOB," +
                "mini_power_pump_motor_image BLOB)");

        db.execSQL("CREATE TABLE " + TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE + " ("
                + "account_id TEXT," +
                "account_name TEXT," +
                "account_id_display_order TEXT," +
                "account_saved_status TEXT)");

        db.execSQL("CREATE TABLE " + TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE + " ("
                + "account_id TEXT," +
                 "account_name TEXT," +
                "bank_id TEXT," +
                "branch_id TEXT," +
                "account_no TEXT," +
                "branch_name TEXT," +
                "bank_name TEXT," +
                "ifsc_code TEXT)");

        db.execSQL("CREATE TABLE " + TN_EB_BANK_SERVER_DETAILS_TABLE + " ("
                + "bank_account_id TEXT," +
                 "account_id TEXT," +
                "bank_id TEXT," +
                "branch_id TEXT," +
                "branchname TEXT," +
                "bank_name TEXT," +
                "account_no TEXT," +
                "dcode TEXT," +
                "bcode TEXT," +
                "pvcode TEXT," +
                "ifsc_code TEXT)");

        db.execSQL("CREATE TABLE " + TN_EB_WATER_SUPPLY_DETAILS_TABLE + " ("
                + "status_type_id TEXT," +
                 "pv_code TEXT," +
                 "hab_code TEXT," +
                 "hab_name TEXT," +
                 "hab_name_ta TEXT," +
                 "water_supplied_reason_id TEXT," +
                "water_supplied_reason_name TEXT," +
                "water_supplied_through_id TEXT," +
                "water_supplied_through_name TEXT," +
                "total_no_of_trips TEXT," +
                "water_supply_litter TEXT," +
                "amount TEXT)");

        db.execSQL("CREATE TABLE " + DRINKING_WATER_SOURCE_TABLE + " ("
                + "water_source_type_id_primary_key INTEGER PRIMARY KEY AUTOINCREMENT," +
                 "water_source_type_id TEXT," +
                 "water_source_type_name TEXT)");

        db.execSQL("CREATE TABLE " + DRINKING_WATER_SOURCE_VILLAGE_LEVEL + " ("
                + "water_source_details_primary_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "water_source_details_id TEXT," +
                "dcode TEXT," +
                "bcode TEXT," +
                "pv_code TEXT," +
                "hab_code TEXT," +
                "hab_name TEXT," +
                "no_of_photos TEXT," +
                "water_source_type_id TEXT," +
                "water_source_type_name TEXT," +
                "landmark TEXT," +
                "image_1 BLOB," +
                "image_1_lat TEXT," +
                "image_1_long TEXT," +
                "image_2 BLOB," +
                "image_2_lat TEXT," +
                "image_2_long TEXT)");

        db.execSQL("CREATE TABLE " + DRINKING_WATER_SOURCE_SERVER_DATA + " ("
                + "server_water_source_details_primary_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "water_source_details_id TEXT," +
                "dcode TEXT," +
                "bcode TEXT," +
                "pv_code TEXT," +
                "hab_code TEXT," +
                "hab_name TEXT," +
                "water_source_type_id TEXT," +
                "water_source_type_name TEXT," +
                "landmark TEXT," +
                "image_1 BLOB," +
                "image_1_lat TEXT," +
                "image_1_long TEXT," +
                "image_2 BLOB," +
                "image_2_lat TEXT," +
                "image_2_long TEXT)");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion >= newVersion) {
            //drop table if already exists
            db.execSQL("DROP TABLE IF EXISTS " + DISTRICT_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + BLOCK_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + VILLAGE_TABLE_NAME);
            db.execSQL("DROP TABLE IF EXISTS " + HABITATION_TABLE_NAME);

            db.execSQL("DROP TABLE IF EXISTS " + CONNECTION_STATUS);
            db.execSQL("DROP TABLE IF EXISTS " + CONNECTION_PURPOSE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + CONNECTION_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TN_EB_CONNECTION_DETAILS_SAVE_TABLE_NEW);
            db.execSQL("DROP TABLE IF EXISTS " + TN_EB_CONNECTION_DETAILS_SAVE_TABLE_IMAGES);
            db.execSQL("DROP TABLE IF EXISTS " + MOTOR_TYPE_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TN_EB_BANK_TYPE_DETAILS_SAVE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TN_EB_BANK_SAVE_DETAILS_SAVE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TN_EB_BANK_SERVER_DETAILS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + TNEB_TANK_CAPACITY_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TNEB_MOTOR_HP_POWER_LIST);
            db.execSQL("DROP TABLE IF EXISTS " + TN_EB_WATER_SUPPLY_DETAILS_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DRINKING_WATER_SOURCE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + DRINKING_WATER_SOURCE_VILLAGE_LEVEL);
            db.execSQL("DROP TABLE IF EXISTS " + DRINKING_WATER_SOURCE_SERVER_DATA);


            onCreate(db);
        }
    }


}
