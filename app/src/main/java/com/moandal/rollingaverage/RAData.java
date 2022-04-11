package com.moandal.rollingaverage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import androidx.appcompat.app.AppCompatActivity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class RAData //extends AppCompatActivity
{

    double rollingAverage;
    int rollingNumber; // number of readings to average over
    int decimalPlaces; // number of decimal places for rounding of rolling average
    int numberToDisplay; // number of readings in history to display
    double[] readings;
    double[] rollingAvs;
    SharedPreferences settings;
    SharedPreferences rollavData;
    private int arraySize = 100;
 //   Date[] readDates = new Date[R.integer.array_size];
 //   SimpleDateFormat ddmmFormat = new SimpleDateFormat("dd/MM/yyyy");

    RAData(double rollingAverage, int rollingNumber, int decimalPlaces, int numberToDisplay, double[] readings, double[] rollingAvs) {
        this.rollingAverage = rollingAverage;
        this.rollingNumber = rollingNumber;
        this.decimalPlaces = decimalPlaces;
        this.numberToDisplay = numberToDisplay;
        this.readings = readings;
        this.rollingAvs = rollingAvs;
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
/*
    public void loadData(Activity activity) {

        Context context = activity;
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        rollingNumber = Integer.parseInt(preferences.getString("rolling_number", "7"));
        decimalPlaces = Integer.parseInt(preferences.getString("decimal_places", "2"));
        numberToDisplay = Integer.parseInt(preferences.getString("number_to_display", "7"));

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < R.integer.array_size; i++) {
            readings[i] = Double.valueOf(sp.getString("Weight" + i, "0"));
            readDates[i] = Utils.convertStringToDate(sp.getString("readDates" + i, "0"));
        }
    }

    public void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < R.integer.array_size; i++) {
            editor.putString("Weight" + i, Double.toString(readings[i]));
            editor.putString("readDates" + i, ddmmFormat.format(readDates[i]));
        }
        editor.commit();
    }
*/
}
