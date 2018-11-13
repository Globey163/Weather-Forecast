package com.example.yassin.weatherforecast.DBObj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity(tableName = "forecasts")
public class DBForecast {

    @PrimaryKey
    public int id;

    private float latitude;
    private float longitude;

    private String approvedTime;


    public DBForecast(float latitude, float longitude, String approvedTime){

        this.latitude = latitude;
        this.longitude = longitude;
        this.approvedTime = approvedTime;
        id = hashCode();
    }


    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public float getLatitude() { return latitude; }
    public float getLongitude() { return longitude; }
    public String getApprovedTime() { return approvedTime; }
}
