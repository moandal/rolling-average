package com.moandal.rollingaverage;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;

import androidx.appcompat.app.AlertDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Utils {

    static int arraySize = 100;

    public static void showMessage(String title, String message, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    public static Date convertStringToDate(String dateString)
    {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        sdf.setLenient(false);
        Date formattedDate = new Date();

        try {
            formattedDate = sdf.parse(dateString);
        }
        catch(ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static Date validateStringToDate(String dateString)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        df.setLenient(false);
        Date formattedDate = new Date();

        try{
            formattedDate = df.parse(dateString);
        }
        catch(ParseException e){
            try{
                formattedDate = df.parse("01/01/1900");
            }
            catch(ParseException f){
                f.printStackTrace();
            }
        }
        return formattedDate;
    }

    public static void saveData(Context context, double[] readings, Date[] readDates) {
        SimpleDateFormat ddmmFormat = new SimpleDateFormat("dd/MM/yyyy");
        SharedPreferences sp = context.getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < arraySize; i++) {
            editor.putString("Weight" + i, Double.toString(readings[i]));
            editor.putString("readDates" + i, ddmmFormat.format(readDates[i]));
        }
        editor.commit();

    }

}
