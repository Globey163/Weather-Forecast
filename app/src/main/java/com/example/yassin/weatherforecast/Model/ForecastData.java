package com.example.yassin.weatherforecast.Model;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ForecastData {

    private String tempTime;
    private double temperature;
    private int mean;

    public ForecastData(String tempTime, double temperature, int mean){
        this.tempTime = tempTime;
        this.temperature = temperature;
        this.mean = mean;
    }

    public String getTempTime() {
        return tempTime;
    }

    public double getTemperature() {
        return temperature;
    }

    public int getMean() {
        return mean;
    }

    //test code

    public static ArrayList<ForecastData> createDummyData(int size){

        ArrayList<ForecastData> dummyData = new ArrayList<>();

        for (int i = 0; i < size; i++){
            dummyData.add(new ForecastData("2018-06-17 13:34", 17.2, 5));
        }

        return dummyData;
    }
}
