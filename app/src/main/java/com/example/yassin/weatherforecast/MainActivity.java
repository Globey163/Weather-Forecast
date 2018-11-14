package com.example.yassin.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.example.yassin.weatherforecast.DBObj.AppDatabase;

public class MainActivity extends AppCompatActivity {

    AppDatabase db = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Database init
        db = AppDatabase.buildInstance(this);
    }

    public void getForecastData(View view){

        new BackgroundWork(60.3, 78.7).execute();
    }
}
