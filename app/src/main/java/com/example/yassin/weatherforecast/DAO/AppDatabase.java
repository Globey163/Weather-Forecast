package com.example.yassin.weatherforecast.DAO;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import com.example.yassin.weatherforecast.DBObj.DBForecast;
import com.example.yassin.weatherforecast.DBObj.DBForecastData;

@Database(entities = {DBForecast.class, DBForecastData.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ForecastDAO forecastDAO();
    public abstract ForecastDataDAO forecastDataDAO();
}
