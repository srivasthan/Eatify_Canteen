package com.eatify.sale.eatifysale.globalfiles;

public class GlobalUrl {

    private static String BaseUrl="https://mathrix19.herokuapp.com";
    public static String QRActvity=BaseUrl+"/api/v1/workshops/check_status.json?m_id=";
    public static String Pay=BaseUrl+"/api/v1/workshops/onspot_pay.json?m_id=";
    public static String Check_pay=BaseUrl+"/api/v1/workshops/onspot_pay.json?m_id=";
    public static String Workshop=BaseUrl+"/api/v1/workshops/onspot_register.json?m_id=";

}
