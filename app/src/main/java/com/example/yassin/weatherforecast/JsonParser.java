package com.example.yassin.weatherforecast;

import com.example.yassin.weatherforecast.Model.ForecastData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class JsonParser {

    private JSONObject rootObj;

    public JsonParser(String jsonText) throws JSONException {

        rootObj = new JSONObject(jsonText);

    }

    public String setAndReturnApprovedTime() throws JSONException {


        return rootObj.getString("approvedTime").replaceAll("[a-zA-Z]", " ");
    }

    public ArrayList<ForecastData> parseForecastData() throws JSONException {

        ArrayList<ForecastData> theData = new ArrayList<>();
        String validTime;
        double temperature;
        int mean;

        JSONArray timeSeries = rootObj.getJSONArray("timeSeries");

        int indexTemp = 11, indexMean = 6;

        for (int i = 0; i < timeSeries.length(); i++){

            if (i > 5){
                indexTemp = 1;
                indexMean = 7;
            }

            validTime = timeSeries.getJSONObject(i).getString("validTime");
            temperature = timeSeries.getJSONObject(i).getJSONArray("parameters").getJSONObject(indexTemp).getJSONArray("values").getDouble(0);
            mean = timeSeries.getJSONObject(i).getJSONArray("parameters").getJSONObject(indexMean).getJSONArray("values").getInt(0);

            theData.add(new ForecastData(validTime, temperature, mean));
        }
        return theData;
    }
}
