package com.moandal.rollingaverage;

import android.app.Activity;

import androidx.appcompat.app.AlertDialog;

public class Utils {
    public static void showErrorMessage(String message, Activity activity) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Invalid Input");
        builder.setMessage(message);
        builder.setPositiveButton(android.R.string.ok, null);
        builder.show();
    }
}
