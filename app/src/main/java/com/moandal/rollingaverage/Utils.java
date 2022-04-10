package com.moandal.rollingaverage;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

import java.text.DateFormat;
import java.text.ParseException;
import java.util.Date;
import java.util.Locale;

public class Utils {

    public static void showErrorMessage(String message, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Invalid Input");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }

    public static Date convertStringToDate(String dateString)
    {
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        df.setLenient(false);
        Date formatteddate = new Date();

        try{
            formatteddate = df.parse(dateString);
        }
        catch(ParseException e){
            try{
                formatteddate = df.parse("01/01/1900");
            }
            catch(ParseException f){
                f.printStackTrace();
            }
        }
        return formatteddate;
    }

}
