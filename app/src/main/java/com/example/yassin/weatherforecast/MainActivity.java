package com.example.yassin.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yassin.weatherforecast.DBObj.AppDatabase;
import com.example.yassin.weatherforecast.Model.ForecastData;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Database init
        db = AppDatabase.buildInstance(this);


        //test code
        ArrayList<ForecastData> testData;
        testData = ForecastData.createDummyData(30);
        RecyclerView rvForecast = (RecyclerView) findViewById(R.id.rvForecastData);
        ForecastAdapter adapter = new ForecastAdapter(testData);
        rvForecast.setAdapter(adapter);
        rvForecast.setLayoutManager(new LinearLayoutManager(this));
        //test code
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

        if (checkUserInput(latitudeText, longitudeText)){

            double latitude = Double.parseDouble(latitudeText.getText().toString());
            double longitude = Double.parseDouble(longitudeText.getText().toString());
            new BackgroundWork(latitude, longitude).execute();
        }

        else{
            Toast toast = Toast.makeText(this, "Invalid coordinate inputs!", Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    private boolean checkUserInput(EditText latitudeText, EditText longitudeText){

        if (((!latitudeText.getText().toString().equals("")) == (!latitudeText.getText().toString().equals(".")) ) && ((!longitudeText.getText().toString().equals("")) == (!longitudeText.getText().toString().equals("")))){

            return true;
        }

        return false;
    }
}
