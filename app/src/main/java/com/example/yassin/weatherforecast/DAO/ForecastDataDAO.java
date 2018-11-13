package com.example.yassin.weatherforecast.DAO;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.yassin.weatherforecast.DBObj.DBForecastData;

import java.util.List;

@Dao
public interface ForecastDataDAO {

    @Insert
    void insertForecastData(DBForecastData... forecastData);

    @Query("SELECT * FROM forecastData")
    List<DBForecastData> getForecastData();

    @Query("DELETE FROM forecastData")
    void deleteForecastData();
}
