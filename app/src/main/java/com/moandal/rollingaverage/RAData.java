package com.moandal.rollingaverage;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.Date;

public class RAData //extends AppCompatActivity
{

    double rollingAverage;
    int rollingNumber; // number of readings to average over
    int decimalPlaces; // number of decimal places for rounding of rolling average
    int numberToDisplay; // number of readings in history to display
    double[] readings;
    double[] rollingAvs;
    Date[] readDates;
    private int arraySize = Utils.arraySize;

    RAData(double rollingAverage, int rollingNumber, int decimalPlaces, int numberToDisplay, double[] readings, double[] rollingAvs, Date[] readDates) {
        this.rollingAverage = rollingAverage;
        this.rollingNumber = rollingNumber;
        this.decimalPlaces = decimalPlaces;
        this.numberToDisplay = numberToDisplay;
        this.readings = readings;
        this.rollingAvs = rollingAvs;
        this.readDates = readDates;
    }

    void calcAvs() {

        double multiplier = Math.pow(10, decimalPlaces);
        int startIndex = arraySize - rollingNumber;

        for (int i = startIndex; i >= 0; i--) {

            rollingAverage = 0;

            for (int j = i; j < i + rollingNumber; j++) {
                rollingAverage = rollingAverage + readings[j];
            }

            rollingAverage = Math.round((rollingAverage / rollingNumber) * multiplier);
            rollingAverage = rollingAverage / multiplier;
            rollingAvs[i] = rollingAverage;

        }

    }

    public void loadData(Context context) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rollingNumber = Integer.parseInt(preferences.getString("rolling_number", "7"));
        decimalPlaces = Integer.parseInt(preferences.getString("decimal_places", "2"));
        numberToDisplay = Integer.parseInt(preferences.getString("number_to_display", "7"));

        SharedPreferences sp = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < arraySize; i++) {
            readings[i] = Double.valueOf(sp.getString("Weight" + i, "0"));
            readDates[i] = Utils.convertStringToDate(sp.getString("readDates" + i, "0"));
        }
    }

}
