package com.example.yassin.weatherforecast.Model;

import java.util.ArrayList;
import java.util.List;

public class Forecast {

    private String approvedTime;
    private double latitude;
    private double longitude;
    private ArrayList<ForecastData> theData;

    public Forecast(String approvedTime, double latitude, double longiitude, ArrayList<ForecastData> theData){

        this.approvedTime = approvedTime;
        this.latitude = latitude;
        this.longitude = longiitude;
        this.theData = theData;

    }


    public String getApprovedTime() {
        return approvedTime;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public ArrayList<ForecastData> getTheData() {
        return theData;
    }

}
