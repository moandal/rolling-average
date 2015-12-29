package com.moandal.rollingaverage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
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

/*
todo Localise dates
*/

public class MainActivity extends AppCompatActivity {

    double rollingAverage;
    int rollingNumber; // number of readings to average over
    int decimalPlaces; // number of decimal places for rounding of rolling average
    int maxArrayIndex;
    double[] readings = new double[100];
    double[] rollingAvs = new double[100];
    Date[] readDates = new Date[100];
    SimpleDateFormat ddmmFormat = new SimpleDateFormat("dd/MM/yyyy");

    public Date convertStringToDate(String dateString)
    {
        Date formatteddate = new Date();
        try{
            formatteddate = ddmmFormat.parse(dateString);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return formatteddate;
    }

    private void displayData() {

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        String Hist1 = Double.toString(readings[0]);
        String Hav1 = Double.toString(rollingAvs[0]);
        String HDt1 = df.format(readDates[0]);

        TextView textHist1 = (TextView) findViewById(R.id.textHist1);
        TextView textHAv1 = (TextView) findViewById(R.id.textHAv1);
        TextView textHDt1 = (TextView) findViewById(R.id.textHDt1);
        TextView textAverage = (TextView) findViewById(R.id.textAverage);

        for (int i = 1; i < rollingNumber; i++) {
            Hist1 += "\n" + Double.toString(readings[i]);
            Hav1 += "\n" + Double.toString(rollingAvs[i]);
            HDt1 += "\n" + df.format(readDates[i]);
        }

        textHist1.setText(Hist1);
        textHAv1.setText(Hav1);
        textHDt1.setText(HDt1);
        textAverage.setText(Double.toString(rollingAverage));

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        displayData();
    }

    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_erase:
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

                Intent i = new Intent(this, SettingsActivity.class);
                startActivity(i);

                return true;

            case R.id.edit_data:

                Intent intent_Edit = new Intent(this, EditActivity.class);
                startActivity(intent_Edit);

                return true;

        }

        return super.onOptionsItemSelected(item);
    }

    public void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < maxArrayIndex; i++) {
            editor.putString("Weight" + i, Double.toString(readings[i]));
            editor.putString("rollingAvs" + i, Double.toString(rollingAvs[i]));
            editor.putString("readDates" + i, ddmmFormat.format(readDates[i]));
        }
        editor.putString("RollingAverage", Double.toString(rollingAverage));
        editor.commit();
    }

    public void loadData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        rollingNumber = Integer.parseInt(preferences.getString("rolling_number", "7"));
        decimalPlaces = Integer.parseInt(preferences.getString("decimal_places", "2"));

        //Calculate how many entries to keep track of so we can maintain the rolling average history
        if ( (rollingNumber * 2) - 1 > readings.length )
            maxArrayIndex = readings.length;
        else
            maxArrayIndex = (rollingNumber * 2) - 1;

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < maxArrayIndex; i++) {
            readings[i] = Double.valueOf(sp.getString("Weight" + i, "0"));
            rollingAvs[i] = Double.valueOf(sp.getString("rollingAvs" + i, "0"));
            readDates[i] = convertStringToDate(sp.getString("readDates" + i, "0"));
        }
        rollingAverage = Double.valueOf(sp.getString("RollingAverage","0"));
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
    }

    /** Called when the user clicks the Send button */
    public void showAverage(View view) {

        EditText editWeight = (EditText) findViewById(R.id.editWeight);
        String message = editWeight.getText().toString();
        double inputValue = Double.valueOf(message);
        rollingAverage = 0;
        Boolean init = true;
        for (int i = 0; i < maxArrayIndex; i++) {
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
                rollingAvs[i] = rollingAvs[i-1];
                readDates[i] = readDates[i-1];
                if (i < rollingNumber) {
                    rollingAverage = rollingAverage + readings[i];
                }
            }

            readings[0] = inputValue;
            double multiplier = Math.pow(10, decimalPlaces);
            rollingAverage = Math.round(((rollingAverage + readings[0]) / rollingNumber) * multiplier);
            rollingAverage = rollingAverage / multiplier;
            rollingAvs[0] = rollingAverage;
            readDates[0] = new Date();
        }

        displayData();
        saveData();
    }
}
