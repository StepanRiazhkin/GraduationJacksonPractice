package com.crm.step_definitions;

import com.crm.pages.BasePage;
import com.crm.utilites.DB_Util;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import io.restassured.RestAssured;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.time.Duration;

import static com.crm.utilites.ConfigReader.*;
import static com.crm.utilites.Driver.*;

public class Hookis {

    private String url;

    @Before("@ui")
    public void setUp(){

        url = getProperty("libraryUrl");

        System.out.println("Attempting to navigate to: "+url);

        getDriver().get(url);
        getDriver().manage().window().maximize();
        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));

    }

    @After("@ui")
    public void tearDown(Scenario scenario){

        if(scenario.isFailed()){

            byte[] screenShot = ((TakesScreenshot)getDriver()).getScreenshotAs(OutputType.BYTES);
            scenario.attach(screenShot, "image/jpg", scenario.getName());

        }

        closeDriver();

    }



    @Before("@api")
    public void setBaseUrl(){

        url = getProperty("libraryBaseUrl");

        RestAssured.baseURI = url;
        System.out.println("Establishing connection with "+url);

    }

    @After("@api")
    public void closeBaseUrl(Scenario scenario){

        System.out.println("Test result for " + scenario.getName() + " "+scenario.getStatus());

    }

    @Before("@db")
    public void setUpDb(){

        System.out.println("ESTABLISHING DB CONNECTION");
        DB_Util.createConnection();

    }

    @After("@db")
    public void closeDb(){

        System.out.println("DESTROYING THE DB CONNECTION");
        DB_Util.destroy();

    }

    @After
    public void clearParamData(){

        BasePage.clearParamData();

    }

}
