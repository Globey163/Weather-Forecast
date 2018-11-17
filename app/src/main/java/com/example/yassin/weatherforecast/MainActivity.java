package com.example.yassin.weatherforecast;

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
    RecyclerView rvForecast = null;

    private static final String APPROVED_TIME_TEXT = "approvedTimeText";

    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        approvedTimeTextView = findViewById(R.id.approvedTime);
        rvForecast = (RecyclerView) findViewById(R.id.rvForecastData);
        rvForecast.setLayoutManager(new LinearLayoutManager(this));

        //Database init
        db = AppDatabase.buildInstance(this);

    }

    @Override
    protected void onStart() {
        super.onStart();
        //if approved time has expired --> fetch data based on previous coordinates
        //get from database if offline and give a marker that it may be out of date
        //if more than 1 hr passed: fetch data
    }

    public void getForecastData(View view){

        EditText latitudeText = (EditText) findViewById(R.id.editTextLatitude);
        EditText longitudeText = (EditText) findViewById(R.id.editTextLongitude);

        try{
            double latitude = Double.parseDouble(latitudeText.getText().toString());
            double longitude = Double.parseDouble(longitudeText.getText().toString());
            new BackgroundWork(approvedTimeTextView, rvForecast, latitude, longitude).execute();

        }catch(Exception e){
            Toast toast = Toast.makeText(this, "Invalid coordinate inputs!", Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}
