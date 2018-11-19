package com.example.yassin.weatherforecast;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yassin.weatherforecast.DBObj.AppDatabase;
import com.example.yassin.weatherforecast.DBObj.DBForecast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    double latitude = 0;
    double longitude = 0;

    private static final class SavedCoordinates{

        public static double savedLatitude = 0;
        public static double savedLongitude = 0;
    }

    TextView approvedTimeTextView = null;
    TextView offlineTextView = null;
    RecyclerView rvForecast = null;

    ConnectivityManager networkManager = null;
    NetworkInfo activeNetworkInfo = null;

    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //View init
        approvedTimeTextView = findViewById(R.id.approvedTime);
        offlineTextView = findViewById(R.id.offline);
        offlineTextView.setVisibility(View.INVISIBLE);
        rvForecast = (RecyclerView) findViewById(R.id.rvForecastData);
        rvForecast.setLayoutManager(new LinearLayoutManager(this));

        //Database init
        db = AppDatabase.buildInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();

        boolean connectionExists = isThereAConnection();

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();


        if (connectionExists){

            Thread getSavedCoordinates = new Thread(){

                @Override
                public void run() {
                    try {
                        List<DBForecast> savedData = AppDatabase.getInstance().forecastDAO().getAllForecasts();

                        if (savedData.size() != 0){

                            SavedCoordinates.savedLatitude = savedData.get(0).getLatitude();
                            SavedCoordinates.savedLongitude = savedData.get(0).getLongitude();
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            };

            getSavedCoordinates.start();

            if (approvedTimeTextView.getText() != null && approvedTimeTextView.getText() != ""){

                try {
                    String dateFromDataString = approvedTimeTextView.getText().toString().substring(15);
                    Date dateFromData = sdf.parse(dateFromDataString);

                    if ((currentTime.getTime() - dateFromData.getTime()) >= 1000*60*60*2){

                        Toast toast = Toast.makeText(this, "Refreshing data", Toast.LENGTH_LONG);
                        toast.show();
                        new BackgroundWork(approvedTimeTextView, rvForecast, SavedCoordinates.savedLatitude, SavedCoordinates.savedLongitude, true, this).execute();
                        offlineTextView.setVisibility(View.INVISIBLE);
                    }

                } catch (ParseException e) {
                    Toast toast = Toast.makeText(this, "Error refreshing data", Toast.LENGTH_LONG);
                    toast.show();
                    e.printStackTrace();
                }
            }
        }
    }

    public void getForecastData(View view){

        boolean connectionExists = isThereAConnection();

        EditText latitudeText = (EditText) findViewById(R.id.editTextLatitude);
        EditText longitudeText = (EditText) findViewById(R.id.editTextLongitude);

        if (connectionExists){

            try{
                latitude = Double.parseDouble(latitudeText.getText().toString());
                longitude = Double.parseDouble(longitudeText.getText().toString());
                latitude = Math.round(latitude * 1000000.0) / 1000000.0;
                longitude = Math.round(longitude * 1000000.0) / 1000000.0;
                checkCoordinates(latitude, longitude);
                SavedCoordinates.savedLatitude = latitude;
                SavedCoordinates.savedLongitude = longitude;

                System.out.println("Latitude: " + latitude + "\n" +
                                   "Longitude: " + longitude);
                new BackgroundWork(approvedTimeTextView, rvForecast, latitude, longitude, true, this).execute();
                offlineTextView.setVisibility(View.INVISIBLE);

            }catch(Exception e){
                Toast toast = Toast.makeText(this, "Invalid coordinate inputs!", Toast.LENGTH_SHORT);
                toast.show();
            }
        }

        else{

            new BackgroundWork(approvedTimeTextView, rvForecast, latitude, longitude, false, this).execute();
            offlineTextView.setVisibility(View.VISIBLE);
            Toast toast = Toast.makeText(this, "No connection found, fetching offline data", Toast.LENGTH_LONG);
            toast.show();
        }
    }

    private boolean isThereAConnection(){
        networkManager = (ConnectivityManager) this.getSystemService(Context.CONNECTIVITY_SERVICE);
        activeNetworkInfo = networkManager.getActiveNetworkInfo();

        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    private void checkCoordinates (double latitude, double longitude) throws CoordinatesOutOfRangeException{

        if ((latitude < 53.0 || latitude > 71) || (longitude < 0 || longitude > 30)){
            throw new CoordinatesOutOfRangeException();
        }
    }
}

