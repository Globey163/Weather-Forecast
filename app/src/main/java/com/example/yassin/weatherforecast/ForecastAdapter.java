package com.example.yassin.weatherforecast;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.yassin.weatherforecast.Model.ForecastData;

import java.util.List;

public class ForecastAdapter extends RecyclerView.Adapter<ForecastAdapter.ViewHolder> {

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView tempTime;
        public TextView temperature;
        public TextView mean;

        public ViewHolder(View itemView){

            super(itemView);

            tempTime = (TextView) itemView.findViewById(R.id.temp_time);
            temperature = (TextView) itemView.findViewById(R.id.temp_value);
            mean = (TextView) itemView.findViewById(R.id.mean);
        }
    }

    private List<ForecastData> mForecastData;

    public ForecastAdapter(List<ForecastData> forecastData){
        mForecastData = forecastData;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        View forecastView = inflater.inflate(R.layout.item_forecast_data, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(forecastView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {


        ForecastData theData = mForecastData.get(i);
        String tempTimeString = mForecastData.get(i).getTempTime();
        String temperatureString = "" + mForecastData.get(i).getTemperature();
        String meanString = null;

        switch (mForecastData.get(i).getMean()){

            case 0:
                meanString = "Clear " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 1:
                meanString = "Mostly clear " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 2:
                meanString = "A little cloudy " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 3:
                meanString = "Partly cloudy " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 4:
                meanString = "Moderately cloudy " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 5:
                meanString = "Cloudy " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 6:
                meanString = "Very cloudy " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 7:
                meanString = "Extremely cloudy " + "(" + mForecastData.get(i).getMean() + ")";
                break;

            case 8:
                meanString = "Blocked sky " + "(" + mForecastData.get(i).getMean() + ")";
                break;

                default:
                    meanString = "Error";
        }

        TextView tempTime = viewHolder.tempTime;
        TextView temperature = viewHolder.temperature;
        TextView mean = viewHolder.mean;

        tempTime.setText(tempTimeString.replaceAll("[a-zA-Z]", " "));
        temperature.setText(temperatureString + " \u2103");
        mean.setText(meanString);
    }

    @Override
    public int getItemCount() {
        return mForecastData.size();
    }
}
