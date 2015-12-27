package com.moandal.rollingaverage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Activity;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

public class EditActivity extends AppCompatActivity {

    double rollingAverage;
    int rollingNumber;
    int decimalPlaces;
    double[] readings = new double[100];
    double[] rollingAvs = new double[100];
    Date[] readDates = new Date[100];
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    EditText[] textEdRead = new EditText[100];
    EditText[] textEdDate = new EditText[100];

    public Date convertStringToDate(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        formatter.setLenient(false);
        Date formatteddate = new Date();

        try{
            formatteddate = formatter.parse(dateString);
        }
        catch(ParseException e){
            try{
                formatteddate = formatter.parse("01/01/1900");
            }
            catch(ParseException f){
                f.printStackTrace();
            }
        }
        return formatteddate;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setupActionBar();
        loadData();
        displayData();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    public void loadData() {

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        rollingNumber = Integer.parseInt(preferences.getString("rolling_number", "7"));
        decimalPlaces = Integer.parseInt(preferences.getString("decimal_places", "2"));

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < rollingNumber; i++) {
            readings[i] = Double.valueOf(sp.getString("Weight" + i, "0"));
            rollingAvs[i] = Double.valueOf(sp.getString("rollingAvs" + i, "0"));
            readDates[i] = convertStringToDate(sp.getString("readDates" + i, "0"));
        }
        rollingAverage = Double.valueOf(sp.getString("RollingAverage","0"));
    }

    public void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < rollingNumber; i++) {
            editor.putString("Weight" + i, Double.toString(readings[i]));
            editor.putString("rollingAvs" + i, Double.toString(rollingAvs[i]));
            editor.putString("readDates" + i, formatter.format(readDates[i]));
        }
        editor.putString("RollingAverage", Double.toString(rollingAverage));
        editor.commit();
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

    private void displayData() {

        LinearLayout linLayReading = (LinearLayout) findViewById(R.id.linLayReading);
        LinearLayout.LayoutParams linLayReadingparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linLayDate = (LinearLayout) findViewById(R.id.linLayDate);
        LinearLayout.LayoutParams linLayDateparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < rollingNumber; i++) {

            textEdRead[i] = new EditText(this);
            textEdRead[i].setLayoutParams(linLayReadingparams);
            textEdRead[i].setText(Double.toString(readings[i]));
            textEdRead[i].setInputType(InputType.TYPE_CLASS_NUMBER);
            textEdRead[i].setId(i);
            linLayReading.addView(textEdRead[i]);

            textEdDate[i] = new EditText(this);
            textEdDate[i].setLayoutParams(linLayDateparams);
            textEdDate[i].setText(formatter.format(readDates[i]));
            textEdDate[i].setInputType(InputType.TYPE_CLASS_DATETIME);
            textEdDate[i].setId(i);
            linLayDate.addView(textEdDate[i]);

        }
        
    }

    public void recalcAvs() {

        double multiplier = Math.pow(10, decimalPlaces);

        for (int i = rollingNumber - 1; i >= 0; i--) {

            rollingAverage = 0;

            for (int j = i; j < i + rollingNumber; j++) {
                rollingAverage = rollingAverage + readings[j];
            }

            rollingAverage = Math.round((rollingAverage / rollingNumber) * multiplier);
            rollingAverage = rollingAverage / multiplier;
            rollingAvs[i] = rollingAverage;

        }

    }

    public void updateReadings(View view) {

        LinearLayout linLayReading = (LinearLayout) findViewById(R.id.linLayReading);
        LinearLayout linLayDate = (LinearLayout) findViewById(R.id.linLayDate);
        String textValue;
        EditText editText;
        double inputValue;
        Date inputDate;
        Date defaultDate = convertStringToDate("01/01/1900");
        rollingAverage = 0;
        boolean duffdates = false;

        for (int i = 0; i < rollingNumber; i++) {
            editText = (EditText) linLayReading.findViewById(i);
            textValue = editText.getText().toString();
            inputValue = Double.valueOf(textValue);
            readings[i] = inputValue;

            editText = (EditText) linLayDate.findViewById(i);
            textValue = editText.getText().toString();
            inputDate = convertStringToDate(textValue);
            if (inputDate.equals(defaultDate)) {
                duffdates = true;
            }
            else {
                readDates[i] = inputDate;
            }

        }

        if (duffdates) {
            Toast.makeText(this, "Invalid date(s) ignored", Toast.LENGTH_LONG).show();
        }

        recalcAvs();
        saveData();
    }


}
