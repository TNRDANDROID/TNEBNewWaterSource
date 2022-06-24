package com.nic.tnebnewwatersource.model;

import android.graphics.Bitmap;

/**
 * Created by M.Kavitha on 22/10/2021.
 */

public class TNEBSystem {

    private String distictCode;
    private String districtName;
    private String blockCode;
    private String Latitude;
    private String PvCode;
    private String PHOTO_SAVED_STATUS;
    private String PvName;
    private String blockName;
    private String Name;
    public String workStageName;
    private String habitation_code;
    private String habitation_name;
    private String habitation_name_ta;
    private String conncetion_id;
    private String conncetion_name;
    private String connection_name_ta;
    private String connection_status;
    private String id;
    private String region_code;
    private String circle_code;
    private String section_code;
    private String distribution_code;
    private String consumer_code;
    private String cuscode;
    private String consumer_name;
    private String connection_number;
    private String location;
    private String tariff;
    private String tariff_desc;
    private String purpose_as_per_tneb;

    private String motor_type;
    private String horse_power;
    private String tank_capacity;
    private String street_light_count;

    public  String KEY_CONNECTION_STATUS_ID;
    public  String KEY_METER_IMAGE;
    public  String KEY_LAST_BILL_IMAGE;
    public  String KEY_EB_CARD_IMAGE;
    public  String KEY_METER_IMAGE_LAT;
    public  String KEY_METER_IMAGE_LONG;
    public  String KEY_PURPOSE_IDS;
    public  String KEY_HAB_CODE;

    private String imageRemark;
    private String Longitude;
    private Bitmap Image;


    public  String KEY_MINI_OHT_MOTOR_IMAGE;
    public  String KEY_MINI_OHT_TANK_IMAGE;
    public  String KEY_OHT_TANK_IMAGE;
    public  String KEY_OHT_MOTOR_IMAGE;
    public  String KEY_MINI_OHT_LATITUDE;
    public  String KEY_OHT_LATITUDE;
    public  String KEY_MINI_OHT_LANGTITUDE;
    public  String KEY_OHT_LANGTITUDE;
    public  String KEY_OHT_TANK_LATITUDE;
    public  String KEY_OHT_TANK_LANGTITUDE;
    public  String KEY_MINI_OHT_TANK_LATITUDE;
    public  String KEY_MINI_OHT_TANK_LANGTITUDE;
    public  String KEY_LAND_MARK;
    public  String KEY_MINI_MOTOR_TYPE;
    public  String KEY_MINI_MOTOR_HP;
    public  String KEY_MINI_MOTOR_TANK_CAPACITY;
    public  String KEY_MINI_LAND_MARK;
    public  String mini_oht_tank_count;
    public  String oht_tank_count;
    public  String account_id;
    public  String account_name;
    public  String account_id_display_order;
    public  String account_saved_status;
    public  String meter_available;


    public  String bank_id;
    public  String branch_id;
    public  String account_no;
    public  String ifsc_code;
    public  String branch_name;
    public  String bank_name;
    public  String bank_account_id;

    public  String tneb_hp_id;
    public  String tneb_tank_capacity__id;
    public  String tneb_tank_capacity_purpose_id;
    public  String tneb_horse_power;
    public  String tneb_tank_capacity;


    ////Mini Power Pump
    public  String KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT;
    public  String KEY_MINI_POWER_PUMP_TANK_CAPACITY;
    public  String KEY_MINI_POWER_PUMP_HORSE_POWER;
    public  String KEY_MINI_POWER_PUMP_MOTOR_TYPE;
    public  String KEY_MINI_POWER_PUMP_LAND_MARK;
    public  String KEY_MINI_POWER_PUMP_LONGTITUDE;
    public  String KEY_MINI_POWER_PUMP_LATITUDE;
    public  String KEY_MINI_POWER_PUMP_MOTOR_IMAGE;


    /////////////////


    public String getKEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT() {
        return KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT;
    }

    public void setKEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT(String KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT) {
        this.KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT = KEY_MINI_POWER_PUMP_MOTOR_TANK_COUNT;
    }

    public String getKEY_MINI_POWER_PUMP_TANK_CAPACITY() {
        return KEY_MINI_POWER_PUMP_TANK_CAPACITY;
    }

    public void setKEY_MINI_POWER_PUMP_TANK_CAPACITY(String KEY_MINI_POWER_PUMP_TANK_CAPACITY) {
        this.KEY_MINI_POWER_PUMP_TANK_CAPACITY = KEY_MINI_POWER_PUMP_TANK_CAPACITY;
    }

    public String getKEY_MINI_POWER_PUMP_HORSE_POWER() {
        return KEY_MINI_POWER_PUMP_HORSE_POWER;
    }

    public void setKEY_MINI_POWER_PUMP_HORSE_POWER(String KEY_MINI_POWER_PUMP_HORSE_POWER) {
        this.KEY_MINI_POWER_PUMP_HORSE_POWER = KEY_MINI_POWER_PUMP_HORSE_POWER;
    }

    public String getKEY_MINI_POWER_PUMP_MOTOR_TYPE() {
        return KEY_MINI_POWER_PUMP_MOTOR_TYPE;
    }

    public void setKEY_MINI_POWER_PUMP_MOTOR_TYPE(String KEY_MINI_POWER_PUMP_MOTOR_TYPE) {
        this.KEY_MINI_POWER_PUMP_MOTOR_TYPE = KEY_MINI_POWER_PUMP_MOTOR_TYPE;
    }

    public String getKEY_MINI_POWER_PUMP_LAND_MARK() {
        return KEY_MINI_POWER_PUMP_LAND_MARK;
    }

    public void setKEY_MINI_POWER_PUMP_LAND_MARK(String KEY_MINI_POWER_PUMP_LAND_MARK) {
        this.KEY_MINI_POWER_PUMP_LAND_MARK = KEY_MINI_POWER_PUMP_LAND_MARK;
    }

    public String getKEY_MINI_POWER_PUMP_LONGTITUDE() {
        return KEY_MINI_POWER_PUMP_LONGTITUDE;
    }

    public void setKEY_MINI_POWER_PUMP_LONGTITUDE(String KEY_MINI_POWER_PUMP_LONGTITUDE) {
        this.KEY_MINI_POWER_PUMP_LONGTITUDE = KEY_MINI_POWER_PUMP_LONGTITUDE;
    }

    public String getKEY_MINI_POWER_PUMP_LATITUDE() {
        return KEY_MINI_POWER_PUMP_LATITUDE;
    }

    public void setKEY_MINI_POWER_PUMP_LATITUDE(String KEY_MINI_POWER_PUMP_LATITUDE) {
        this.KEY_MINI_POWER_PUMP_LATITUDE = KEY_MINI_POWER_PUMP_LATITUDE;
    }

    public String getKEY_MINI_POWER_PUMP_MOTOR_IMAGE() {
        return KEY_MINI_POWER_PUMP_MOTOR_IMAGE;
    }

    public void setKEY_MINI_POWER_PUMP_MOTOR_IMAGE(String KEY_MINI_POWER_PUMP_MOTOR_IMAGE) {
        this.KEY_MINI_POWER_PUMP_MOTOR_IMAGE = KEY_MINI_POWER_PUMP_MOTOR_IMAGE;
    }

    ///////////////////
    /////Glr Motor
    public  String KEY_GLR_MOTOR_TANK_COUNT;
    public  String KEY_GLR_TANK_CAPACITY;
    public  String KEY_GLR_HORSE_POWER;
    public  String KEY_GLR_MOTOR_TYPE;
    public  String KEY_GLR_LAND_MARK;
    public  String KEY_GLR_LONGTITUDE;
    public  String KEY_GLR_LATITUDE;
    public  String KEY_GLR_MOTOR_IMAGE;

    public String getKEY_GLR_MOTOR_TANK_COUNT() {
        return KEY_GLR_MOTOR_TANK_COUNT;
    }

    public void setKEY_GLR_MOTOR_TANK_COUNT(String KEY_GLR_MOTOR_TANK_COUNT) {
        this.KEY_GLR_MOTOR_TANK_COUNT = KEY_GLR_MOTOR_TANK_COUNT;
    }

    public String getKEY_GLR_TANK_CAPACITY() {
        return KEY_GLR_TANK_CAPACITY;
    }

    public void setKEY_GLR_TANK_CAPACITY(String KEY_GLR_TANK_CAPACITY) {
        this.KEY_GLR_TANK_CAPACITY = KEY_GLR_TANK_CAPACITY;
    }

    public String getKEY_GLR_HORSE_POWER() {
        return KEY_GLR_HORSE_POWER;
    }

    public void setKEY_GLR_HORSE_POWER(String KEY_GLR_HORSE_POWER) {
        this.KEY_GLR_HORSE_POWER = KEY_GLR_HORSE_POWER;
    }

    public String getKEY_GLR_MOTOR_TYPE() {
        return KEY_GLR_MOTOR_TYPE;
    }

    public void setKEY_GLR_MOTOR_TYPE(String KEY_GLR_MOTOR_TYPE) {
        this.KEY_GLR_MOTOR_TYPE = KEY_GLR_MOTOR_TYPE;
    }

    public String getKEY_GLR_LAND_MARK() {
        return KEY_GLR_LAND_MARK;
    }

    public void setKEY_GLR_LAND_MARK(String KEY_GLR_LAND_MARK) {
        this.KEY_GLR_LAND_MARK = KEY_GLR_LAND_MARK;
    }

    public String getKEY_GLR_LONGTITUDE() {
        return KEY_GLR_LONGTITUDE;
    }

    public void setKEY_GLR_LONGTITUDE(String KEY_GLR_LONGTITUDE) {
        this.KEY_GLR_LONGTITUDE = KEY_GLR_LONGTITUDE;
    }

    public String getKEY_GLR_LATITUDE() {
        return KEY_GLR_LATITUDE;
    }

    public void setKEY_GLR_LATITUDE(String KEY_GLR_LATITUDE) {
        this.KEY_GLR_LATITUDE = KEY_GLR_LATITUDE;
    }

    public String getKEY_GLR_MOTOR_IMAGE() {
        return KEY_GLR_MOTOR_IMAGE;
    }

    public void setKEY_GLR_MOTOR_IMAGE(String KEY_GLR_MOTOR_IMAGE) {
        this.KEY_GLR_MOTOR_IMAGE = KEY_GLR_MOTOR_IMAGE;
    }

    ///////Mini With OUt Oht

    public  String KEY_MINI_WITH_OUT_OHT_HORSE_POWER;
    public  String KEY_mini_with_out_oht_MOTOR_TYPE;
    public  String KEY_mini_with_out_oht_LAND_MARK;
    public  String KEY_mini_with_out_oht_LONGTITUDE;
    public  String KEY_mini_with_out_oht_LATITUDE;
    public  String KEY_mini_with_out_oht_MOTOR_IMAGE;

    public String getKEY_MINI_WITH_OUT_OHT_HORSE_POWER() {
        return KEY_MINI_WITH_OUT_OHT_HORSE_POWER;
    }

    public void setKEY_MINI_WITH_OUT_OHT_HORSE_POWER(String KEY_MINI_WITH_OUT_OHT_HORSE_POWER) {
        this.KEY_MINI_WITH_OUT_OHT_HORSE_POWER = KEY_MINI_WITH_OUT_OHT_HORSE_POWER;
    }

    public String getKEY_mini_with_out_oht_MOTOR_TYPE() {
        return KEY_mini_with_out_oht_MOTOR_TYPE;
    }

    public void setKEY_mini_with_out_oht_MOTOR_TYPE(String KEY_mini_with_out_oht_MOTOR_TYPE) {
        this.KEY_mini_with_out_oht_MOTOR_TYPE = KEY_mini_with_out_oht_MOTOR_TYPE;
    }

    public String getKEY_mini_with_out_oht_LAND_MARK() {
        return KEY_mini_with_out_oht_LAND_MARK;
    }

    public void setKEY_mini_with_out_oht_LAND_MARK(String KEY_mini_with_out_oht_LAND_MARK) {
        this.KEY_mini_with_out_oht_LAND_MARK = KEY_mini_with_out_oht_LAND_MARK;
    }

    public String getKEY_mini_with_out_oht_LONGTITUDE() {
        return KEY_mini_with_out_oht_LONGTITUDE;
    }

    public void setKEY_mini_with_out_oht_LONGTITUDE(String KEY_mini_with_out_oht_LONGTITUDE) {
        this.KEY_mini_with_out_oht_LONGTITUDE = KEY_mini_with_out_oht_LONGTITUDE;
    }

    public String getKEY_mini_with_out_oht_LATITUDE() {
        return KEY_mini_with_out_oht_LATITUDE;
    }

    public void setKEY_mini_with_out_oht_LATITUDE(String KEY_mini_with_out_oht_LATITUDE) {
        this.KEY_mini_with_out_oht_LATITUDE = KEY_mini_with_out_oht_LATITUDE;
    }

    public String getKEY_mini_with_out_oht_MOTOR_IMAGE() {
        return KEY_mini_with_out_oht_MOTOR_IMAGE;
    }

    public void setKEY_mini_with_out_oht_MOTOR_IMAGE(String KEY_mini_with_out_oht_MOTOR_IMAGE) {
        this.KEY_mini_with_out_oht_MOTOR_IMAGE = KEY_mini_with_out_oht_MOTOR_IMAGE;
    }

    ///////////////

    public String getTneb_hp_id() {
        return tneb_hp_id;
    }

    public void setTneb_hp_id(String tneb_hp_id) {
        this.tneb_hp_id = tneb_hp_id;
    }

    public String getTneb_tank_capacity__id() {
        return tneb_tank_capacity__id;
    }

    public void setTneb_tank_capacity__id(String tneb_tank_capacity__id) {
        this.tneb_tank_capacity__id = tneb_tank_capacity__id;
    }

    public String getTneb_tank_capacity_purpose_id() {
        return tneb_tank_capacity_purpose_id;
    }

    public void setTneb_tank_capacity_purpose_id(String tneb_tank_capacity_purpose_id) {
        this.tneb_tank_capacity_purpose_id = tneb_tank_capacity_purpose_id;
    }

    public String getTneb_horse_power() {
        return tneb_horse_power;
    }

    public void setTneb_horse_power(String tneb_horse_power) {
        this.tneb_horse_power = tneb_horse_power;
    }

    public String getTneb_tank_capacity() {
        return tneb_tank_capacity;
    }

    public void setTneb_tank_capacity(String tneb_tank_capacity) {
        this.tneb_tank_capacity = tneb_tank_capacity;
    }

    public String getMeter_available() {
        return meter_available;
    }

    public void setMeter_available(String meter_available) {
        this.meter_available = meter_available;
    }

    public String getBank_account_id() {
        return bank_account_id;
    }

    public void setBank_account_id(String bank_account_id) {
        this.bank_account_id = bank_account_id;
    }

    public String getBranch_name() {
        return branch_name;
    }

    public void setBranch_name(String branch_name) {
        this.branch_name = branch_name;
    }

    public String getBank_name() {
        return bank_name;
    }

    public void setBank_name(String bank_name) {
        this.bank_name = bank_name;
    }

    public String getBank_id() {
        return bank_id;
    }

    public void setBank_id(String bank_id) {
        this.bank_id = bank_id;
    }

    public String getBranch_id() {
        return branch_id;
    }

    public void setBranch_id(String branch_id) {
        this.branch_id = branch_id;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getIfsc_code() {
        return ifsc_code;
    }

    public void setIfsc_code(String ifsc_code) {
        this.ifsc_code = ifsc_code;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public String getAccount_name() {
        return account_name;
    }

    public void setAccount_name(String account_name) {
        this.account_name = account_name;
    }

    public String getAccount_id_display_order() {
        return account_id_display_order;
    }

    public void setAccount_id_display_order(String account_id_display_order) {
        this.account_id_display_order = account_id_display_order;
    }

    public String getAccount_saved_status() {
        return account_saved_status;
    }

    public void setAccount_saved_status(String account_saved_status) {
        this.account_saved_status = account_saved_status;
    }

    public String getMini_oht_tank_count() {
        return mini_oht_tank_count;
    }

    public void setMini_oht_tank_count(String mini_oht_tank_count) {
        this.mini_oht_tank_count = mini_oht_tank_count;
    }

    public String getOht_tank_count() {
        return oht_tank_count;
    }

    public void setOht_tank_count(String oht_tank_count) {
        this.oht_tank_count = oht_tank_count;
    }

    public String getKEY_MINI_LAND_MARK() {
        return KEY_MINI_LAND_MARK;
    }

    public void setKEY_MINI_LAND_MARK(String KEY_MINI_LAND_MARK) {
        this.KEY_MINI_LAND_MARK = KEY_MINI_LAND_MARK;
    }

    public String getKEY_MINI_MOTOR_TYPE() {
        return KEY_MINI_MOTOR_TYPE;
    }

    public void setKEY_MINI_MOTOR_TYPE(String KEY_MINI_MOTOR_TYPE) {
        this.KEY_MINI_MOTOR_TYPE = KEY_MINI_MOTOR_TYPE;
    }

    public String getKEY_MINI_MOTOR_HP() {
        return KEY_MINI_MOTOR_HP;
    }

    public void setKEY_MINI_MOTOR_HP(String KEY_MINI_MOTOR_HP) {
        this.KEY_MINI_MOTOR_HP = KEY_MINI_MOTOR_HP;
    }

    public String getKEY_MINI_MOTOR_TANK_CAPACITY() {
        return KEY_MINI_MOTOR_TANK_CAPACITY;
    }

    public void setKEY_MINI_MOTOR_TANK_CAPACITY(String KEY_MINI_MOTOR_TANK_CAPACITY) {
        this.KEY_MINI_MOTOR_TANK_CAPACITY = KEY_MINI_MOTOR_TANK_CAPACITY;
    }

    public String getKEY_MINI_OHT_MOTOR_IMAGE() {
        return KEY_MINI_OHT_MOTOR_IMAGE;
    }

    public void setKEY_MINI_OHT_MOTOR_IMAGE(String KEY_MINI_OHT_MOTOR_IMAGE) {
        this.KEY_MINI_OHT_MOTOR_IMAGE = KEY_MINI_OHT_MOTOR_IMAGE;
    }

    public String getKEY_MINI_OHT_TANK_IMAGE() {
        return KEY_MINI_OHT_TANK_IMAGE;
    }

    public void setKEY_MINI_OHT_TANK_IMAGE(String KEY_MINI_OHT_TANK_IMAGE) {
        this.KEY_MINI_OHT_TANK_IMAGE = KEY_MINI_OHT_TANK_IMAGE;
    }

    public String getKEY_OHT_TANK_IMAGE() {
        return KEY_OHT_TANK_IMAGE;
    }

    public void setKEY_OHT_TANK_IMAGE(String KEY_OHT_TANK_IMAGE) {
        this.KEY_OHT_TANK_IMAGE = KEY_OHT_TANK_IMAGE;
    }

    public String getKEY_OHT_MOTOR_IMAGE() {
        return KEY_OHT_MOTOR_IMAGE;
    }

    public void setKEY_OHT_MOTOR_IMAGE(String KEY_OHT_MOTOR_IMAGE) {
        this.KEY_OHT_MOTOR_IMAGE = KEY_OHT_MOTOR_IMAGE;
    }

    public String getKEY_MINI_OHT_LATITUDE() {
        return KEY_MINI_OHT_LATITUDE;
    }

    public void setKEY_MINI_OHT_LATITUDE(String KEY_MINI_OHT_LATITUDE) {
        this.KEY_MINI_OHT_LATITUDE = KEY_MINI_OHT_LATITUDE;
    }

    public String getKEY_OHT_LATITUDE() {
        return KEY_OHT_LATITUDE;
    }

    public void setKEY_OHT_LATITUDE(String KEY_OHT_LATITUDE) {
        this.KEY_OHT_LATITUDE = KEY_OHT_LATITUDE;
    }

    public String getKEY_MINI_OHT_LANGTITUDE() {
        return KEY_MINI_OHT_LANGTITUDE;
    }

    public void setKEY_MINI_OHT_LANGTITUDE(String KEY_MINI_OHT_LANGTITUDE) {
        this.KEY_MINI_OHT_LANGTITUDE = KEY_MINI_OHT_LANGTITUDE;
    }

    public String getKEY_OHT_LANGTITUDE() {
        return KEY_OHT_LANGTITUDE;
    }

    public void setKEY_OHT_LANGTITUDE(String KEY_OHT_LANGTITUDE) {
        this.KEY_OHT_LANGTITUDE = KEY_OHT_LANGTITUDE;
    }

    public String getKEY_OHT_TANK_LATITUDE() {
        return KEY_OHT_TANK_LATITUDE;
    }

    public void setKEY_OHT_TANK_LATITUDE(String KEY_OHT_TANK_LATITUDE) {
        this.KEY_OHT_TANK_LATITUDE = KEY_OHT_TANK_LATITUDE;
    }

    public String getKEY_OHT_TANK_LANGTITUDE() {
        return KEY_OHT_TANK_LANGTITUDE;
    }

    public void setKEY_OHT_TANK_LANGTITUDE(String KEY_OHT_TANK_LANGTITUDE) {
        this.KEY_OHT_TANK_LANGTITUDE = KEY_OHT_TANK_LANGTITUDE;
    }

    public String getKEY_MINI_OHT_TANK_LATITUDE() {
        return KEY_MINI_OHT_TANK_LATITUDE;
    }

    public void setKEY_MINI_OHT_TANK_LATITUDE(String KEY_MINI_OHT_TANK_LATITUDE) {
        this.KEY_MINI_OHT_TANK_LATITUDE = KEY_MINI_OHT_TANK_LATITUDE;
    }

    public String getKEY_MINI_OHT_TANK_LANGTITUDE() {
        return KEY_MINI_OHT_TANK_LANGTITUDE;
    }

    public void setKEY_MINI_OHT_TANK_LANGTITUDE(String KEY_MINI_OHT_TANK_LANGTITUDE) {
        this.KEY_MINI_OHT_TANK_LANGTITUDE = KEY_MINI_OHT_TANK_LANGTITUDE;
    }

    public String getKEY_LAND_MARK() {
        return KEY_LAND_MARK;
    }

    public void setKEY_LAND_MARK(String KEY_LAND_MARK) {
        this.KEY_LAND_MARK = KEY_LAND_MARK;
    }

    public String getTank_capacity() {
        return tank_capacity;
    }

    public TNEBSystem setTank_capacity(String tank_capacity) {
        this.tank_capacity = tank_capacity;
        return this;
    }

    public String getStreet_light_count() {
        return street_light_count;
    }

    public TNEBSystem setStreet_light_count(String street_light_count) {
        this.street_light_count = street_light_count;
        return this;
    }

    public String getMotor_type() {
        return motor_type;
    }

    public TNEBSystem setMotor_type(String motor_type) {
        this.motor_type = motor_type;
        return this;
    }

    public String getHorse_power() {
        return horse_power;
    }

    public TNEBSystem setHorse_power(String horse_power) {
        this.horse_power = horse_power;
        return this;
    }

    public String getKEY_HAB_CODE() {
        return KEY_HAB_CODE;
    }

    public void setKEY_HAB_CODE(String KEY_HAB_CODE) {
        this.KEY_HAB_CODE = KEY_HAB_CODE;
    }

    public String getKEY_CONNECTION_STATUS_ID() {
        return KEY_CONNECTION_STATUS_ID;
    }

    public void setKEY_CONNECTION_STATUS_ID(String KEY_CONNECTION_STATUS_ID) {
        this.KEY_CONNECTION_STATUS_ID = KEY_CONNECTION_STATUS_ID;
    }

    public String getKEY_METER_IMAGE() {
        return KEY_METER_IMAGE;
    }

    public void setKEY_METER_IMAGE(String KEY_METER_IMAGE) {
        this.KEY_METER_IMAGE = KEY_METER_IMAGE;
    }

    public String getKEY_LAST_BILL_IMAGE() {
        return KEY_LAST_BILL_IMAGE;
    }

    public void setKEY_LAST_BILL_IMAGE(String KEY_LAST_BILL_IMAGE) {
        this.KEY_LAST_BILL_IMAGE = KEY_LAST_BILL_IMAGE;
    }

    public String getKEY_EB_CARD_IMAGE() {
        return KEY_EB_CARD_IMAGE;
    }

    public void setKEY_EB_CARD_IMAGE(String KEY_EB_CARD_IMAGE) {
        this.KEY_EB_CARD_IMAGE = KEY_EB_CARD_IMAGE;
    }

    public String getKEY_METER_IMAGE_LAT() {
        return KEY_METER_IMAGE_LAT;
    }

    public void setKEY_METER_IMAGE_LAT(String KEY_METER_IMAGE_LAT) {
        this.KEY_METER_IMAGE_LAT = KEY_METER_IMAGE_LAT;
    }

    public String getKEY_METER_IMAGE_LONG() {
        return KEY_METER_IMAGE_LONG;
    }

    public void setKEY_METER_IMAGE_LONG(String KEY_METER_IMAGE_LONG) {
        this.KEY_METER_IMAGE_LONG = KEY_METER_IMAGE_LONG;
    }

    public String getKEY_PURPOSE_IDS() {
        return KEY_PURPOSE_IDS;
    }

    public void setKEY_PURPOSE_IDS(String KEY_PURPOSE_IDS) {
        this.KEY_PURPOSE_IDS = KEY_PURPOSE_IDS;
    }

    public String getConncetion_name() {
        return conncetion_name;
    }

    public TNEBSystem setConncetion_name(String conncetion_name) {
        this.conncetion_name = conncetion_name;
        return this;
    }

    public String getConnection_name_ta() {
        return connection_name_ta;
    }

    public void setConnection_name_ta(String connection_name_ta) {
        this.connection_name_ta = connection_name_ta;
    }

    public String getConnection_number() {
        return connection_number;
    }

    public TNEBSystem setConnection_number(String connection_number) {
        this.connection_number = connection_number;
        return this;
    }

    public String getId() {
        return id;
    }

    public TNEBSystem setId(String id) {
        this.id = id;
        return this;
    }

    public String getRegion_code() {
        return region_code;
    }

    public TNEBSystem setRegion_code(String region_code) {
        this.region_code = region_code;
        return this;
    }

    public String getCircle_code() {
        return circle_code;
    }

    public TNEBSystem setCircle_code(String circle_code) {
        this.circle_code = circle_code;
        return this;
    }

    public String getSection_code() {
        return section_code;
    }

    public TNEBSystem setSection_code(String section_code) {
        this.section_code = section_code;
        return this;
    }

    public String getDistribution_code() {
        return distribution_code;
    }

    public TNEBSystem setDistribution_code(String distribution_code) {
        this.distribution_code = distribution_code;
        return this;
    }

    public String getConsumer_code() {
        return consumer_code;
    }

    public TNEBSystem setConsumer_code(String consumer_code) {
        this.consumer_code = consumer_code;
        return this;
    }

    public String getCuscode() {
        return cuscode;
    }

    public TNEBSystem setCuscode(String cuscode) {
        this.cuscode = cuscode;
        return this;
    }

    public String getConsumer_name() {
        return consumer_name;
    }

    public TNEBSystem setConsumer_name(String consumer_name) {
        this.consumer_name = consumer_name;
        return this;
    }

    public String getLocation() {
        return location;
    }

    public TNEBSystem setLocation(String location) {
        this.location = location;
        return this;
    }

    public String getTariff() {
        return tariff;
    }

    public TNEBSystem setTariff(String tariff) {
        this.tariff = tariff;
        return this;
    }

    public String getTariff_desc() {
        return tariff_desc;
    }

    public TNEBSystem setTariff_desc(String tariff_desc) {
        this.tariff_desc = tariff_desc;
        return this;
    }

    public String getPurpose_as_per_tneb() {
        return purpose_as_per_tneb;
    }

    public TNEBSystem setPurpose_as_per_tneb(String purpose_as_per_tneb) {
        this.purpose_as_per_tneb = purpose_as_per_tneb;
        return this;
    }


    public String getConncetion_id() {
        return conncetion_id;
    }

    public TNEBSystem setConncetion_id(String conncetion_id) {
        this.conncetion_id = conncetion_id;
        return this;
    }

    public String getConnection_status() {
        return connection_status;
    }

    public TNEBSystem setConnection_status(String connection_status) {
        this.connection_status = connection_status;
        return this;
    }


    public String getHabitation_code() {
        return habitation_code;
    }

    public void setHabitation_code(String habitation_code) {
        this.habitation_code = habitation_code;
    }

    public String getHabitation_name() {
        return habitation_name;
    }

    public void setHabitation_name(String habitation_name) {
        this.habitation_name = habitation_name;
    }

    public String getHabitation_name_ta() {
        return habitation_name_ta;
    }

    public void setHabitation_name_ta(String habitation_name_ta) {
        this.habitation_name_ta = habitation_name_ta;
    }

    public String getImageRemark() {
        return imageRemark;
    }

    public void setImageRemark(String imageRemark) {
        this.imageRemark = imageRemark;
    }


    public String getPvName() {
        return PvName;
    }

    public void setPvName(String pvName) {
        PvName = pvName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }


    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDistictCode() {
        return distictCode;
    }

    public void setDistictCode(String distictCode) {
        this.distictCode = distictCode;
    }

    public String getBlockCode() {
        return blockCode;
    }

    public void setBlockCode(String blockCode) {
        this.blockCode = blockCode;
    }

    public String getBlockName() {
        return blockName;
    }

    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }

    public String getLatitude() {
        return Latitude;
    }

    public void setLatitude(String latitude) {
        Latitude = latitude;
    }

    public String getLongitude() {
        return Longitude;
    }

    public void setLongitude(String longitude) {
        Longitude = longitude;
    }

    public Bitmap getImage() {
        return Image;
    }

    public void setImage(Bitmap image) {
        Image = image;
    }

    public String getPvCode() {
        return PvCode;
    }

    public void setPvCode(String pvCode) {
        this.PvCode = pvCode;
    }

    public void setWorkStageName(String workStageName) {
        this.workStageName = workStageName;
    }

    public String getWorkStageName() {
        return workStageName;
    }

    public String getPHOTO_SAVED_STATUS() {
        return PHOTO_SAVED_STATUS;
    }

    public void setPHOTO_SAVED_STATUS(String PHOTO_SAVED_STATUS) {
        this.PHOTO_SAVED_STATUS = PHOTO_SAVED_STATUS;
    }

    private String water_supply_through;
    private String reason_for_supply;
    private String reason_type;

    public String getReason_for_supply() {
        return reason_for_supply;
    }

    public void setReason_for_supply(String reason_for_supply) {
        this.reason_for_supply = reason_for_supply;
    }

    public String getReason_type() {
        return reason_type;
    }

    public void setReason_type(String reason_type) {
        this.reason_type = reason_type;
    }

    public String getWater_supply_through() {
        return water_supply_through;
    }

    public void setWater_supply_through(String water_supply_through) {
        this.water_supply_through = water_supply_through;
    }

    private String status_type_id;
    private String water_supplied_reason_id;
    private String water_supplied_reason_name;
    private String water_supplied_through_id;
    private String water_supplied_through_name;
    private String total_no_of_trips;
    private String water_supply_litter;
    private String amount;

    public String getStatus_type_id() {
        return status_type_id;
    }

    public void setStatus_type_id(String status_type_id) {
        this.status_type_id = status_type_id;
    }

    public String getWater_supplied_reason_id() {
        return water_supplied_reason_id;
    }

    public void setWater_supplied_reason_id(String water_supplied_reason_id) {
        this.water_supplied_reason_id = water_supplied_reason_id;
    }

    public String getWater_supplied_reason_name() {
        return water_supplied_reason_name;
    }

    public void setWater_supplied_reason_name(String water_supplied_reason_name) {
        this.water_supplied_reason_name = water_supplied_reason_name;
    }

    public String getWater_supplied_through_id() {
        return water_supplied_through_id;
    }

    public void setWater_supplied_through_id(String water_supplied_through_id) {
        this.water_supplied_through_id = water_supplied_through_id;
    }

    public String getWater_supplied_through_name() {
        return water_supplied_through_name;
    }

    public void setWater_supplied_through_name(String water_supplied_through_name) {
        this.water_supplied_through_name = water_supplied_through_name;
    }

    public String getTotal_no_of_trips() {
        return total_no_of_trips;
    }

    public void setTotal_no_of_trips(String total_no_of_trips) {
        this.total_no_of_trips = total_no_of_trips;
    }

    public String getWater_supply_litter() {
        return water_supply_litter;
    }

    public void setWater_supply_litter(String water_supply_litter) {
        this.water_supply_litter = water_supply_litter;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    ////new Water Source
    private String water_source_type_id;
    private String water_source_type_name;
    private String water_source_details_id;
    private int water_source_details_primary_id;
    private int server_water_source_details_primary_id;
    private String image_1;
    private String image_1_lat;
    private String image_1_long;
    private String image_2;
    private String image_2_lat;
    private String image_2_long;
    private String no_of_photos;

    public int getServer_water_source_details_primary_id() {
        return server_water_source_details_primary_id;
    }

    public void setServer_water_source_details_primary_id(int server_water_source_details_primary_id) {
        this.server_water_source_details_primary_id = server_water_source_details_primary_id;
    }

    public int getWater_source_details_primary_id() {
        return water_source_details_primary_id;
    }

    public void setWater_source_details_primary_id(int water_source_details_primary_id) {
        this.water_source_details_primary_id = water_source_details_primary_id;
    }

    public String getNo_of_photos() {
        return no_of_photos;
    }

    public void setNo_of_photos(String no_of_photos) {
        this.no_of_photos = no_of_photos;
    }

    public String getImage_1() {
        return image_1;
    }

    public void setImage_1(String image_1) {
        this.image_1 = image_1;
    }

    public String getImage_1_lat() {
        return image_1_lat;
    }

    public void setImage_1_lat(String image_1_lat) {
        this.image_1_lat = image_1_lat;
    }

    public String getImage_1_long() {
        return image_1_long;
    }

    public void setImage_1_long(String image_1_long) {
        this.image_1_long = image_1_long;
    }

    public String getImage_2() {
        return image_2;
    }

    public void setImage_2(String image_2) {
        this.image_2 = image_2;
    }

    public String getImage_2_lat() {
        return image_2_lat;
    }

    public void setImage_2_lat(String image_2_lat) {
        this.image_2_lat = image_2_lat;
    }

    public String getImage_2_long() {
        return image_2_long;
    }

    public void setImage_2_long(String image_2_long) {
        this.image_2_long = image_2_long;
    }

    public String getWater_source_details_id() {
        return water_source_details_id;
    }

    public void setWater_source_details_id(String water_source_details_id) {
        this.water_source_details_id = water_source_details_id;
    }

    public String getWater_source_type_id() {
        return water_source_type_id;
    }

    public void setWater_source_type_id(String water_source_type_id) {
        this.water_source_type_id = water_source_type_id;
    }

    public String getWater_source_type_name() {
        return water_source_type_name;
    }

    public void setWater_source_type_name(String water_source_type_name) {
        this.water_source_type_name = water_source_type_name;
    }

    ////New Water Supply Status Details
    private String supply_timing_id;
    private String supply_timing;
    private String session_id;
    private String session_name;
    private String water_type_id;
    private String water_type;
    private String minimum_date;
    private String maximum_date;
    private String entry_date;
    private String water_supply_status_id;
    private int new_water_details_primary_id;

    public String getWater_supply_status_id() {
        return water_supply_status_id;
    }

    public void setWater_supply_status_id(String water_supply_status_id) {
        this.water_supply_status_id = water_supply_status_id;
    }

    public String getEntry_date() {
        return entry_date;
    }

    public void setEntry_date(String entry_date) {
        this.entry_date = entry_date;
    }

    public int getNew_water_details_primary_id() {
        return new_water_details_primary_id;
    }

    public void setNew_water_details_primary_id(int new_water_details_primary_id) {
        this.new_water_details_primary_id = new_water_details_primary_id;
    }

    public String getMinimum_date() {
        return minimum_date;
    }

    public void setMinimum_date(String minimum_date) {
        this.minimum_date = minimum_date;
    }

    public String getMaximum_date() {
        return maximum_date;
    }

    public void setMaximum_date(String maximum_date) {
        this.maximum_date = maximum_date;
    }

    public String getSupply_timing_id() {
        return supply_timing_id;
    }

    public void setSupply_timing_id(String supply_timing_id) {
        this.supply_timing_id = supply_timing_id;
    }

    public String getSupply_timing() {
        return supply_timing;
    }

    public void setSupply_timing(String supply_timing) {
        this.supply_timing = supply_timing;
    }

    public String getSession_id() {
        return session_id;
    }

    public void setSession_id(String session_id) {
        this.session_id = session_id;
    }

    public String getSession_name() {
        return session_name;
    }

    public void setSession_name(String session_name) {
        this.session_name = session_name;
    }

    public String getWater_type_id() {
        return water_type_id;
    }

    public void setWater_type_id(String water_type_id) {
        this.water_type_id = water_type_id;
    }

    public String getWater_type() {
        return water_type;
    }

    public void setWater_type(String water_type) {
        this.water_type = water_type;
    }

    private String morning_water_supply_timing_id;
    private String morning_water_supply_timing_name;
    private String evening_water_supply_timing_id;
    private String evening_water_supply_timing_name;

    public String getMorning_water_supply_timing_id() {
        return morning_water_supply_timing_id;
    }

    public void setMorning_water_supply_timing_id(String morning_water_supply_timing_id) {
        this.morning_water_supply_timing_id = morning_water_supply_timing_id;
    }

    public String getMorning_water_supply_timing_name() {
        return morning_water_supply_timing_name;
    }

    public void setMorning_water_supply_timing_name(String morning_water_supply_timing_name) {
        this.morning_water_supply_timing_name = morning_water_supply_timing_name;
    }

    public String getEvening_water_supply_timing_id() {
        return evening_water_supply_timing_id;
    }

    public void setEvening_water_supply_timing_id(String evening_water_supply_timing_id) {
        this.evening_water_supply_timing_id = evening_water_supply_timing_id;
    }

    public String getEvening_water_supply_timing_name() {
        return evening_water_supply_timing_name;
    }

    public void setEvening_water_supply_timing_name(String evening_water_supply_timing_name) {
        this.evening_water_supply_timing_name = evening_water_supply_timing_name;
    }

    private String session_fn_water_type_id;
    private String session_an_water_type_id;
    private String session_an_timing_id;
    private String session_fn_timing_id;
    private String session_fn_src_id;
    private String session_an_src_id;

    public String getSession_fn_water_type_id() {
        return session_fn_water_type_id;
    }

    public void setSession_fn_water_type_id(String session_fn_water_type_id) {
        this.session_fn_water_type_id = session_fn_water_type_id;
    }

    public String getSession_an_water_type_id() {
        return session_an_water_type_id;
    }

    public void setSession_an_water_type_id(String session_an_water_type_id) {
        this.session_an_water_type_id = session_an_water_type_id;
    }

    public String getSession_an_timing_id() {
        return session_an_timing_id;
    }

    public void setSession_an_timing_id(String session_an_timing_id) {
        this.session_an_timing_id = session_an_timing_id;
    }

    public String getSession_fn_timing_id() {
        return session_fn_timing_id;
    }

    public void setSession_fn_timing_id(String session_fn_timing_id) {
        this.session_fn_timing_id = session_fn_timing_id;
    }

    public String getSession_fn_src_id() {
        return session_fn_src_id;
    }

    public void setSession_fn_src_id(String session_fn_src_id) {
        this.session_fn_src_id = session_fn_src_id;
    }

    public String getSession_an_src_id() {
        return session_an_src_id;
    }

    public void setSession_an_src_id(String session_an_src_id) {
        this.session_an_src_id = session_an_src_id;
    }
}