package com.example.yassin.weatherforecast;

import android.content.Context;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class BackgroundWork extends AsyncTask<Void, Void, Forecast> {

    private ForecastAdapter adapter = null;

    private WeakReference<TextView> mTextView;
    private WeakReference<RecyclerView> mRecyclerView;
    private double latitude;
    private double longitude;
    private boolean connected;
    private Context context;

    private HttpsURLConnection https = null;
    private BufferedReader reader = null;

    private String jsonText = null;
    private JsonParser parser = null;

    private String approvedTimeString = null;

    private String devUrlString = "https://maceo.sth.kth.se/api/category/pmp3g/version/2/geotype/point/lon/14.333/lat/60.383/";
    private String realUrlString = null;

    public BackgroundWork (TextView approvedTimeReference, RecyclerView recyclerViewReference, double latitude, double longitude, boolean connected, Context context){ //skicka in allt som recyclerView har + approved time texten

        mTextView = new WeakReference<>(approvedTimeReference);
        mRecyclerView = new WeakReference<>(recyclerViewReference);

        this.latitude = latitude;
        this.longitude = longitude;
        this.connected = connected;
        this.context = context;

        realUrlString = "https://opendata-download-metfcst.smhi.se/api/category/pmp3g/version/2/geotype/point/lon/" + this.longitude + "/lat/" + this.latitude + "/data.json";
    }

    @Override
    protected Forecast doInBackground(Void... voids) {

        Forecast theForecast;
        ArrayList<ForecastData> theForecastData;

        if (connected){
            try {

                URL url = new URL(realUrlString);
                //URL url = new URL(devUrlString);
                https = (HttpsURLConnection) url.openConnection();
                reader = new BufferedReader(new InputStreamReader(https.getInputStream()));
                StringBuffer response = new StringBuffer();
                String in = null;

                while ((in = reader.readLine()) != null){
                    response.append(in);
                }

                jsonText = response.toString();

                parser = new JsonParser(jsonText);
                approvedTimeString = parser.setAndReturnApprovedTime();
                theForecastData = parser.parseForecastData();

            }catch(Exception e){

                Log.e("NETWORK ERROR", e.getMessage());
                e.printStackTrace();
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
        }

        else{

            theForecastData = new ArrayList<>();

            try {
                List<DBForecast> forecastsFromDB = AppDatabase.getInstance().forecastDAO().getAllForecasts();
                List<DBForecastData> forecastDataFromDB = AppDatabase.getInstance().forecastDataDAO().getForecastData();

                for (int i = 0; i < forecastDataFromDB.size(); i++){
                    theForecastData.add(new ForecastData(forecastDataFromDB.get(i).getTempTime(),
                                                         forecastDataFromDB.get(i).getTemperature(),
                                                         forecastDataFromDB.get(i).getMean()));
                }

                theForecast = new Forecast(forecastsFromDB.get(0).getApprovedTime(),
                        forecastsFromDB.get(0).getLatitude(),
                        forecastsFromDB.get(0).getLongitude(),
                        theForecastData);
            } catch (Exception e) {
                Log.i("ERRORQUERY", e.toString());
                return null;
            }
        }

        return theForecast;
    }

    protected void onPostExecute(Forecast forecast){


        if (forecast == null){

            Toast toast = Toast.makeText(context, "Something went wrong with fetching data: either online or offline", Toast.LENGTH_LONG);
            toast.show();
            System.out.println("Forecast is null");
        }

        else{
            mTextView.get().setText("Approved time: " + forecast.getApprovedTime());

            adapter = new ForecastAdapter(forecast.getTheData());
            mRecyclerView.get().setAdapter(adapter);
        }
    }
}
