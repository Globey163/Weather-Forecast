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

public class MainActivity extends AppCompatActivity {

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

        if (approvedTimeTextView.getText() != null){

        }
        //if approved time has expired --> fetch data based on previous coordinates
        //get from database if offline and give a marker that it may be out of date
        //if more than 1 hr passed: fetch data
    }

    public void getForecastData(View view){

        boolean connectionExists = isThereAConnection();
        double latitude = 0;
        double longitude = 0;

        EditText latitudeText = (EditText) findViewById(R.id.editTextLatitude);
        EditText longitudeText = (EditText) findViewById(R.id.editTextLongitude);

        if (connectionExists){

            try{
                latitude = Double.parseDouble(latitudeText.getText().toString());
                longitude = Double.parseDouble(longitudeText.getText().toString());
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
}
