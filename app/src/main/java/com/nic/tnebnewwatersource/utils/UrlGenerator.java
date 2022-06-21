package com.nic.tnebnewwatersource.utils;


import com.nic.tnebnewwatersource.R;
import com.nic.tnebnewwatersource.application.NICApplication;

/**
 * Created by M.Kavitha on 22/10/2021.
 */
public class UrlGenerator {



    public static String getLoginUrl() {
        return NICApplication.getAppString(R.string.LOGIN_URL);
    }

    public static String getServicesListUrl() {
        return NICApplication.getAppString(R.string.BASE_SERVICES_URL);
    }

    public static String getMainServiceUrl() {
        return NICApplication.getAppString(R.string.APP_MAIN_SERVICES_URL);
//        return "http://10.163.19.140/rdweb/project/webservices_forms/work_monitoring/work_monitoring_services_test.php";
    }
    public static String getBankServiceUrl() {
        return NICApplication.getAppString(R.string.BANK_DETAILS_SERVICES_URL);
//        return "http://10.163.19.140/rdweb/project/webservices_forms/work_monitoring/work_monitoring_services_test.php";
    }


    public static String getTnrdHostName() {
        return NICApplication.getAppString(R.string.TNRD_HOST_NAME);
    }



}
