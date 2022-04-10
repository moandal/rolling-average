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

public class RAData extends AppCompatActivity{

    public double rollingAverage;
    public int rollingNumber; // number of readings to average over
    public int decimalPlaces; // number of decimal places for rounding of rolling average
    public int numberToDisplay; // number of readings in history to display
    public int arraySize = 100;
    public double[] readings = new double[arraySize];
    public double[] rollingAvs = new double[arraySize];
    public Date[] readDates = new Date[arraySize];
    SimpleDateFormat ddmmFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void loadData(Activity activity) {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(activity);
        rollingNumber = Integer.parseInt(preferences.getString("rolling_number", "7"));
        decimalPlaces = Integer.parseInt(preferences.getString("decimal_places", "2"));
        numberToDisplay = Integer.parseInt(preferences.getString("number_to_display", "7"));

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < arraySize; i++) {
            readings[i] = Double.valueOf(sp.getString("Weight" + i, "0"));
            readDates[i] = Utils.convertStringToDate(sp.getString("readDates" + i, "0"));
        }
    }

    public void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < arraySize; i++) {
            editor.putString("Weight" + i, Double.toString(readings[i]));
            editor.putString("readDates" + i, ddmmFormat.format(readDates[i]));
        }
        editor.commit();
    }

    public void calcAvs() {

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

}
