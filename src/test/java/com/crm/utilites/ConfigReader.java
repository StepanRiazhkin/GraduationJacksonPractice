package com.crm.utilites;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static{

        try {

            FileInputStream file = new FileInputStream("configuration.properties");
            properties.load(file);
            file.close();

        } catch (IOException e) {

            throw new RuntimeException(e);

        }

    }

    public static String getProperty(String key){

        return properties.getProperty(key);

    }

}
