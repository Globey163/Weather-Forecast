package com.example.yassin.weatherforecast.DBObj;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;
import android.util.Log;

import com.example.yassin.weatherforecast.DAO.ForecastDAO;
import com.example.yassin.weatherforecast.DAO.ForecastDataDAO;

@Database(entities = {DBForecast.class, DBForecastData.class}, version = 1, exportSchema = false)
public abstract class AppDatabase extends RoomDatabase {

    public abstract ForecastDAO forecastDAO();
    public abstract ForecastDataDAO forecastDataDAO();

    private static volatile AppDatabase db;

    public static AppDatabase buildInstance (final Context context){

        if (db == null){
            synchronized (AppDatabase.class){
                if (db == null){
                    db = Room.databaseBuilder(context.getApplicationContext(), AppDatabase.class, "forecast_db").build();
                }
            }
        }

        return db;
    }

    public static AppDatabase getInstance() throws Exception {

        return db;
    }
}
