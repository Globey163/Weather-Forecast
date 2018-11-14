package com.example.yassin.weatherforecast;

import android.os.AsyncTask;
import android.util.Log;

import com.example.yassin.weatherforecast.DBObj.AppDatabase;
import com.example.yassin.weatherforecast.DBObj.DBForecast;
import com.example.yassin.weatherforecast.Model.Forecast;
import com.example.yassin.weatherforecast.Model.ForecastData;

import java.util.ArrayList;
import java.util.List;

public class BackgroundWork extends AsyncTask<Void, Void, Forecast> {

    private double latitude;
    private double longitude;

    public BackgroundWork (double latitude, double longitude){

        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Forecast doInBackground(Void... voids) {

        try {
            AppDatabase.getInstance().forecastDAO().deleteForecasts();
        } catch (Exception e) {
            Log.i("ERRORDELETION", e.toString());
        }

        Forecast theForecast = null;

        try {
            AppDatabase.getInstance().forecastDAO().insertForecast(new DBForecast(latitude, longitude, "2018-11-14"));
        } catch (Exception e) {
            Log.i("ERRORINSERT", e.toString());
        }

        try {
            List<DBForecast> forecasts = AppDatabase.getInstance().forecastDAO().getAllForecasts();
            theForecast = new Forecast(forecasts.get(0).getApprovedTime(),
                                       forecasts.get(0).getLatitude(),
                                       forecasts.get(0).getLongitude(),
                                new ArrayList<ForecastData>());
        } catch (Exception e) {
            Log.i("ERRORQUERY", e.toString());
        }

        return theForecast;
    }

    protected void onPostExecute(Forecast forecast){
        System.out.println("Approved time: " + forecast.getApprovedTime() + "\n" +
                           "Latitude: " +      forecast.getLatitude()     + "\n" +
                           "Longitude: " +     forecast.getLongitude()    + "\n" +
                           "The data: " +      forecast.getTheData().toString());
    }
}
