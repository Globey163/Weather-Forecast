package com.example.yassin.weatherforecast;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.TextView;

import com.example.yassin.weatherforecast.DBObj.AppDatabase;
import com.example.yassin.weatherforecast.DBObj.DBForecast;
import com.example.yassin.weatherforecast.Model.Forecast;
import com.example.yassin.weatherforecast.Model.ForecastData;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundWork extends AsyncTask<Void, Void, Forecast> {

    private WeakReference<TextView> mTextView;

    private double latitude;
    private double longitude;

    private HttpsURLConnection https = null;
    private BufferedReader reader = null;
    private StringBuffer response = null;
    String jsonText = null;

    private String devUrlString = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/";

    public BackgroundWork (TextView approvedTimeReference, double latitude, double longitude){ //skicka in allt som recyclerView har + approved time texten

        mTextView = new WeakReference<>(approvedTimeReference);

        this.latitude = latitude;
        this.longitude = longitude;
    }

    @Override
    protected Forecast doInBackground(Void... voids) {

        try {

            URL url = new URL(devUrlString);
            https = (HttpsURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(https.getInputStream()));
            StringBuffer response = new StringBuffer();
            String test = null;

            while ((test = reader.readLine()) != null){
                response.append(test);
            }

            jsonText = response.toString();


        }catch(Exception e){

            Log.e("NETWORK ERROR","Failed to fetch data from server.");
            return null;
        }

        finally {
            if (reader != null){
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (https != null){
                https.disconnect();
            }
        }

        try {
            AppDatabase.getInstance().forecastDAO().deleteForecasts();
            AppDatabase.getInstance().forecastDataDAO().deleteForecastData();
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

        //gör toast om något av felen med doInBackground inträffar (forecast = null

        /*System.out.println("Approved time: " + forecast.getApprovedTime() + "\n" +
                           "Latitude: " +      forecast.getLatitude()     + "\n" +
                           "Longitude: " +     forecast.getLongitude()    + "\n" +
                           "The data: " +      forecast.getTheData().toString());
        */

        mTextView.get().setText("postExecuteTest");
        System.out.println(jsonText);
    }
}
