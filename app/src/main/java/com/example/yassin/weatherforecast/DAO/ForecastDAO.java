package com.example.yassin.weatherforecast.DAO;


import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.yassin.weatherforecast.DBObj.DBForecast;

import java.util.List;

@Dao
public interface ForecastDAO {

    @Insert
    void insertForecast(DBForecast... forecasts);

    @Query("SELECT * FROM forecasts")
    List<DBForecast> getAllForecasts();

    @Query("DELETE FROM forecasts")
    void deleteForecasts();
}
