package com.example.yassin.weatherforecast.DBObj;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

import java.util.Objects;

@Entity
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

    public String getApprovedTime() {
        return approvedTime;
    }

    public void setApprovedTime(String approvedTime) {
        this.approvedTime = approvedTime;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }
}
