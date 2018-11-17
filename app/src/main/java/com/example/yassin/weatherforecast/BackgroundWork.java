package com.example.yassin.weatherforecast;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yassin.weatherforecast.DBObj.AppDatabase;
import com.example.yassin.weatherforecast.DBObj.DBForecast;
import com.example.yassin.weatherforecast.DBObj.DBForecastData;
import com.example.yassin.weatherforecast.Model.Forecast;
import com.example.yassin.weatherforecast.Model.ForecastData;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundWork extends AsyncTask<Void, Void, Forecast> {

    private ForecastAdapter adapter = null;

    private WeakReference<TextView> mTextView;
    private WeakReference<RecyclerView> mRecyclerView;

    private double latitude;
    private double longitude;

    private HttpsURLConnection https = null;
    private BufferedReader reader = null;
    private String jsonText = null;

    JSONObject rootObj = null;
    JSONArray timeSeries = null;
    ArrayList<JSONObject> timeSeriesObjects = null;


    private String approvedTimeString = null;
    private String validTime = null;
    private double temperature;
    private int mean;

    private String devUrlString = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/";

    public BackgroundWork (TextView approvedTimeReference, RecyclerView recyclerViewReference, double latitude, double longitude){ //skicka in allt som recyclerView har + approved time texten

        mTextView = new WeakReference<>(approvedTimeReference);
        mRecyclerView = new WeakReference<>(recyclerViewReference);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Forecast doInBackground(Void... voids) {

        Forecast theForecast = null;
        ArrayList<ForecastData> theForecastData = new ArrayList<>();

        try {

            URL url = new URL(devUrlString);
            https = (HttpsURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(https.getInputStream()));
            StringBuffer response = new StringBuffer();
            String in = null;

            while ((in = reader.readLine()) != null){
                response.append(in);
            }

            jsonText = response.toString();

            rootObj = new JSONObject(jsonText);

            approvedTimeString = rootObj.getString("approvedTime");

            timeSeries = rootObj.getJSONArray("timeSeries");

            int indexTemp = 11, indexMean = 6;

            for (int i = 0; i < timeSeries.length(); i++){

                if (i > 5){
                    indexTemp = 1;
                    indexMean = 7;
                }

                validTime = timeSeries.getJSONObject(i).getString("validTime");
                temperature = timeSeries.getJSONObject(i).getJSONArray("parameters").getJSONObject(indexTemp).getJSONArray("values").getDouble(0);
                mean = timeSeries.getJSONObject(i).getJSONArray("parameters").getJSONObject(indexMean).getJSONArray("values").getInt(0);


                theForecastData.add(new ForecastData(validTime, temperature, mean));

            }


        }catch(Exception e){

            Log.e("NETWORK ERROR", e.getMessage());
            return null;
        }

        finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }

            if (https != null){
                https.disconnect();
            }
        }

        theForecast = new Forecast(approvedTimeString, latitude, longitude, theForecastData);

        try {
            AppDatabase.getInstance().forecastDAO().deleteForecasts();
            AppDatabase.getInstance().forecastDataDAO().deleteForecastData();
        } catch (Exception e) {
            Log.i("ERRORDELETION", e.getMessage());
            return null;
        }

        try {

            DBForecast dbForecast = new DBForecast(theForecast.getLatitude(), theForecast.getLongitude(), theForecast.getApprovedTime());

            AppDatabase.getInstance().forecastDAO().insertForecast(dbForecast);

            for (int i = 0; i < theForecast.getTheData().size(); i++){
                AppDatabase.getInstance().forecastDataDAO().insertForecastData(new DBForecastData(theForecastData.get(i).getTempTime(),
                                                                                                  theForecastData.get(i).getTemperature(),
                                                                                                  theForecastData.get(i).getMean(),
                                                                                                  dbForecast.getId()));
            }

        } catch (Exception e) {
            Log.i("ERRORINSERT", e.toString());
            return null;
        }

        /*try {
            List<DBForecast> forecasts = AppDatabase.getInstance().forecastDAO().getAllForecasts();
            theForecast = new Forecast(forecasts.get(0).getApprovedTime(),
                                       forecasts.get(0).getLatitude(),
                                       forecasts.get(0).getLongitude(),
                                new ArrayList<ForecastData>());
        } catch (Exception e) {
            Log.i("ERRORQUERY", e.toString());
            return null;
        }*/

        return theForecast;
    }

    protected void onPostExecute(Forecast forecast){


        if (forecast == null){
            System.out.println("Forecast is null");
        }

        else{
            mTextView.get().setText("Approved time: " + forecast.getApprovedTime());



            adapter = new ForecastAdapter(forecast.getTheData());
            mRecyclerView.get().setAdapter(adapter);
        }
        //gör toast om något av felen med doInBackground inträffar (forecast = null


    }
}
