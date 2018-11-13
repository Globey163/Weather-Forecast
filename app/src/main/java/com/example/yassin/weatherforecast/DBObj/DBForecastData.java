package com.example.yassin.weatherforecast.DBObj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "forecastData")
public class DBForecastData {

    @PrimaryKey
    private int id;

    private String tempTime;
    private float temperature;
    private int mean;

    private int forecastId;

    public DBForecastData(String tempTime, float temperature, int mean, int forecastId){

        this.id = hashCode();
        this.tempTime = tempTime;
        this.temperature = temperature;
        this.mean = mean;
        this.forecastId = forecastId;

    }

    @Override
    public int hashCode(){

       return Objects.hash(id);
    }

    public String getTempTime() { return tempTime; }
    public float getTemperature() { return temperature; }
    public int getMean() { return mean; }
    public int getForecastId() { return forecastId; }
    public int getId(){ return id; }

    public void setId(int id){ this.id = id;}
}
