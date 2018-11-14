package com.example.yassin.weatherforecast;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

//import com.example.yassin.weatherforecast.DBObj.AppDatabase;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //AppDatabase db = AppDatabase.buildInstance(this);

        //new BackgroundWork(60.3, 78.7).execute();
    }
}
