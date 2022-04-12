package com.moandal.rollingaverage;

import android.os.Bundle;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.text.InputType;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.text.DateFormat;
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
    EditText[] textEdRead = new EditText[arraySize];
    EditText[] textEdDate = new EditText[arraySize];
    RAData raData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        setupActionBar();

        raData = new RAData(rollingAverage, rollingNumber, decimalPlaces, numberToDisplay, readings, rollingAvs, readDates);
        raData.loadData(this);
        rollingNumber = raData.rollingNumber;
        decimalPlaces = raData.decimalPlaces;
        numberToDisplay = raData.numberToDisplay;
        readings = raData.readings;
        readDates = raData.readDates;
        displayData();
    }

    private void setupActionBar() {
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            // Show the Up button in the action bar.
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        Utils.saveData(this, readings, readDates);
    }

    @Override
    protected void onResume() {
        super.onResume();
        raData.loadData(this);
        rollingNumber = raData.rollingNumber;
        decimalPlaces = raData.decimalPlaces;
        numberToDisplay = raData.numberToDisplay;
        readings = raData.readings;
        readDates = raData.readDates;
        displayData();
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

    // Performed when the Update button is clicked
    public void updateReadings(View view) {

        LinearLayout linLayReading = (LinearLayout) findViewById(R.id.linLayReading);
        LinearLayout linLayDate = (LinearLayout) findViewById(R.id.linLayDate);
        String textValue;
        EditText editText;
        double inputValue;
        Date inputDate;
        Date defaultDate = Utils.convertStringToDate("01/01/1900");
        rollingAverage = 0;
        boolean duffDates = false;

        for (int i = 0; i < numberToDisplay; i++) {
            editText = (EditText) linLayReading.findViewById(i);
            textValue = editText.getText().toString();
            inputValue = Double.valueOf(textValue);
            readings[i] = inputValue;

            editText = (EditText) linLayDate.findViewById(i);
            textValue = editText.getText().toString();
            inputDate = Utils.convertStringToDate(textValue);
            if (inputDate.equals(defaultDate)) {
                duffDates = true;
            }
            else {
                readDates[i] = inputDate;
            }
        }

        if (duffDates)
            Utils.showErrorMessage("Invalid date(s) ignored", this);
        else
            Toast.makeText(this, "Data updated", Toast.LENGTH_LONG).show();

        RAData raData = new RAData(rollingAverage, rollingNumber, decimalPlaces, numberToDisplay, readings, rollingAvs, readDates);
        raData.calcAvs();
        rollingAverage = raData.rollingAverage;
        rollingAvs = raData.rollingAvs;
        Utils.saveData(this, readings, readDates);
    }

}
