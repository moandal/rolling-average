package com.moandal.rollingaverage;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.view.Menu;
import android.view.MenuItem;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.Locale;

//Todo Gather common code in a single class
//Todo Option to export data to a file
//Todo Improve Edit activity so that invalid dates are reset to what they were after clicking the Update button

public class MainActivity extends AppCompatActivity {

    public double rollingAverage;
    public int rollingNumber; // number of readings to average over
    public int decimalPlaces; // number of decimal places for rounding of rolling average
    public int numberToDisplay; // number of readings in history to display
    public int arraySize = 100;
    public double[] readings = new double[arraySize];
    public double[] rollingAvs = new double[arraySize];
    public Date[] readDates = new Date[arraySize];
    SimpleDateFormat ddmmFormat = new SimpleDateFormat("dd/MM/yyyy");

    public void loadData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
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
/*
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
*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        displayData();
    }

    private void displayData() {

        RAData raData = new RAData(rollingAverage, rollingNumber, decimalPlaces, numberToDisplay, readings, rollingAvs);
        raData.calcAvs();
        rollingAverage = raData.rollingAverage;
        readings = raData.readings;
        rollingAvs = raData.rollingAvs;

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        String Hist1 = Double.toString(readings[0]);
        String Hav1 = Double.toString(rollingAvs[0]);
        String HDt1 = df.format(readDates[0]);

        TextView textHist1 = (TextView) findViewById(R.id.textHist1);
        TextView textHAv1 = (TextView) findViewById(R.id.textHAv1);
        TextView textHDt1 = (TextView) findViewById(R.id.textHDt1);
        TextView textAverage = (TextView) findViewById(R.id.textAverage);

        for (int i = 1; i < numberToDisplay; i++) {
            Hist1 += "\n" + Double.toString(readings[i]);
            Hav1 += "\n" + Double.toString(rollingAvs[i]);
            HDt1 += "\n" + df.format(readDates[i]);
            if (i == rollingNumber - 1) {
                Hist1 += "\n---";
                Hav1 += "\n---";
                HDt1 += "\n---";
            }
        }

        textHist1.setText(Hist1);
        textHAv1.setText(Hav1);
        textHDt1.setText(HDt1);
        textAverage.setText(Double.toString(rollingAverage));

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //Take appropriate action depending on which Menu item is chosen
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_erase: //Erase all data - check first
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                rollingAverage = 0;

                                Arrays.fill(readings, 0);
                                Arrays.fill(rollingAvs, 0);
                                Arrays.fill(readDates, new Date());

                                displayData();

                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                // "No" button clicked so do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;

            case R.id.settings:

                Intent intent_Settings = new Intent(this, SettingsActivity.class);
                startActivity(intent_Settings);

                return true;

            case R.id.edit_data:

                Intent intent_Edit = new Intent(this, EditActivity.class);
                startActivity(intent_Edit);

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadData();
        displayData();
    }

    // Called when the user clicks the Enter button
    public void showAverage(View view) {

        EditText editWeight = (EditText) findViewById(R.id.editWeight);
        String message = editWeight.getText().toString();
        double inputValue = Double.valueOf(message);
        rollingAverage = 0;
        Boolean init = true;
        for (int i = 0; i < arraySize; i++) {
            if (readings[i] != 0) {
                init = false;
            }
        }

        if (init) {
            Arrays.fill(readings, inputValue);
            Arrays.fill(rollingAvs, inputValue);
            Arrays.fill(readDates, new Date());
            rollingAverage = inputValue;
        }
        else {
            for (int i = readings.length - 1; i > 0; i--) {
                readings[i] = readings[i-1];
                readDates[i] = readDates[i-1];

            }

            readings[0] = inputValue;
            readDates[0] = new Date();

        }

        displayData();
        saveData();
    }
}
