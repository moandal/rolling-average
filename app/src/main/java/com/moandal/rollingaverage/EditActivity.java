package com.moandal.rollingaverage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class EditActivity extends AppCompatActivity {

    double rollingAverage;
    int rollingNumber;
    int decimalPlaces;
    int numberToDisplay; // number of readings in history to display
    int arraySize = 100;
    double[] readings = new double[arraySize];
    double[] rollingAvs = new double[arraySize];
    Date[] readDates = new Date[arraySize];
    SimpleDateFormat ddmmFormat = new SimpleDateFormat("dd/MM/yyyy");
    EditText[] textEdRead = new EditText[arraySize];
    EditText[] textEdDate = new EditText[arraySize];

    public Date convertStringToDate(String dateString)
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

    public Date convertddmmToDate(String dateString)
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
        numberToDisplay = Integer.parseInt(preferences.getString("number_to_display", "7"));

        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        for (int i = 0; i < arraySize; i++) {
            readings[i] = Double.valueOf(sp.getString("Weight" + i, "0"));
            //rollingAvs[i] = Double.valueOf(sp.getString("rollingAvs" + i, "0"));
            readDates[i] = convertddmmToDate(sp.getString("readDates" + i, "0"));
        }
        //rollingAverage = Double.valueOf(sp.getString("RollingAverage","0"));
    }

    public void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < arraySize; i++) {
            editor.putString("Weight" + i, Double.toString(readings[i]));
            //editor.putString("rollingAvs" + i, Double.toString(rollingAvs[i]));
            editor.putString("readDates" + i, ddmmFormat.format(readDates[i]));
        }
        //editor.putString("RollingAverage", Double.toString(rollingAverage));
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

        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.getDefault());

        LinearLayout linLayReading = (LinearLayout) findViewById(R.id.linLayReading);
        LinearLayout.LayoutParams linLayReadingparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        LinearLayout linLayDate = (LinearLayout) findViewById(R.id.linLayDate);
        LinearLayout.LayoutParams linLayDateparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < numberToDisplay; i++) {

            textEdRead[i] = new EditText(this);
            textEdRead[i].setLayoutParams(linLayReadingparams);
            textEdRead[i].setText(Double.toString(readings[i]));
            textEdRead[i].setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            textEdRead[i].setId(i);
            linLayReading.addView(textEdRead[i]);

            textEdDate[i] = new EditText(this);
            textEdDate[i].setLayoutParams(linLayDateparams);
            textEdDate[i].setText(df.format(readDates[i]));
            textEdDate[i].setInputType(InputType.TYPE_CLASS_DATETIME | InputType.TYPE_DATETIME_VARIATION_DATE);
            textEdDate[i].setId(i);
            linLayDate.addView(textEdDate[i]);
        }

    }

    // Calculate all the rolling averages for the entire history set
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

    // Performed when the Update button is clicked
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

        for (int i = 0; i < numberToDisplay; i++) {
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

        if (duffdates)
            Toast.makeText(this, "Invalid date(s) ignored", Toast.LENGTH_LONG).show();
        else
            Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show();

        calcAvs();
        saveData();
    }

}
