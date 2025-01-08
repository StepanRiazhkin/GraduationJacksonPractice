package com.crm.pages;

import com.crm.utilites.Driver;
import org.openqa.selenium.support.PageFactory;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class BasePage {

    protected static Map<String, Object> parameters = new LinkedHashMap<>(); //stores data

    public BasePage(){

        PageFactory.initElements(Driver.getDriver(), this);

    }

    public static void setParamData(String key, String value) {
        parameters.put(key, value);
    }

    public static String getParamData(String key) {
        return (String) parameters.get(key);
    }

    public static void setParamData(Map<String, Object> map){

        parameters.putAll(map);

    }

    public static void clearParamData() { //clears the map from data (if no need)

        parameters.clear();

    }



}
