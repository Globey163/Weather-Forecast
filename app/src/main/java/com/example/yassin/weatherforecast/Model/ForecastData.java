package com.example.yassin.weatherforecast.Model;

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
}
