package com.moandal.rollingaverage;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/*
todo Allow change of number to average over
todo Store last 100 readings
todo Show last 100 readings
todo Allow editing of previous readings
todo Localise dates
*/

public class MainActivity extends AppCompatActivity {

    double rollingAverage;
    int rollingNumber = 7;
    double[] readings = new double[7];
    double[] rollingAvs = new double[7];
    Date[] readDates = new Date[7];
    SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");

    public Date convertStringToDate(String dateString)
    {
        SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
        Date formatteddate = new Date();
        try{
            formatteddate = formatter.parse(dateString);
        }
        catch(ParseException e){
            e.printStackTrace();
        }
        return formatteddate;
    }

    private void displayData() {
        String Hist1 = Double.toString(readings[0]);
        String Hav1 = Double.toString(rollingAvs[0]);
        String HDt1 = formatter.format(readDates[0]);

        TextView textHist1 = (TextView) findViewById(R.id.textHist1);
        TextView textHAv1 = (TextView) findViewById(R.id.textHAv1);
        TextView textHDt1 = (TextView) findViewById(R.id.textHDt1);
        TextView textAverage = (TextView) findViewById(R.id.textAverage);

        for (int i = 1; i < readings.length; i++) {
            Hist1 += "\n" + Double.toString(readings[i]);
            Hav1 += "\n" + Double.toString(rollingAvs[i]);
            HDt1 += "\n" + formatter.format(readDates[i]);
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
        // Inflate our menu from the resources by using the menu inflater.
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
                                // No button clicked
                                // do nothing
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("Are you sure?")
                        .setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //TODO Put save and load data in a loop

    private void saveData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString("Weight0", Double.toString(readings[0]));
        editor.putString("Weight1", Double.toString(readings[1]));
        editor.putString("Weight2", Double.toString(readings[2]));
        editor.putString("Weight3", Double.toString(readings[3]));
        editor.putString("Weight4", Double.toString(readings[4]));
        editor.putString("Weight5", Double.toString(readings[5]));
        editor.putString("Weight6", Double.toString(readings[6]));
        editor.putString("rollingAvs0", Double.toString(rollingAvs[0]));
        editor.putString("rollingAvs1", Double.toString(rollingAvs[1]));
        editor.putString("rollingAvs2", Double.toString(rollingAvs[2]));
        editor.putString("rollingAvs3", Double.toString(rollingAvs[3]));
        editor.putString("rollingAvs4", Double.toString(rollingAvs[4]));
        editor.putString("rollingAvs5", Double.toString(rollingAvs[5]));
        editor.putString("rollingAvs6", Double.toString(rollingAvs[6]));
        editor.putString("readDates0", formatter.format(readDates[0]));
        editor.putString("readDates1", formatter.format(readDates[1]));
        editor.putString("readDates2", formatter.format(readDates[2]));
        editor.putString("readDates3", formatter.format(readDates[3]));
        editor.putString("readDates4", formatter.format(readDates[4]));
        editor.putString("readDates5", formatter.format(readDates[5]));
        editor.putString("readDates6", formatter.format(readDates[6]));
        editor.putString("RollingAverage", Double.toString(rollingAverage));
        editor.commit();
    }

    private void loadData() {
        SharedPreferences sp = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE);
        readings[0] = Double.valueOf(sp.getString("Weight0","0"));
        readings[1] = Double.valueOf(sp.getString("Weight1","0"));
        readings[2] = Double.valueOf(sp.getString("Weight2","0"));
        readings[3] = Double.valueOf(sp.getString("Weight3","0"));
        readings[4] = Double.valueOf(sp.getString("Weight4","0"));
        readings[5] = Double.valueOf(sp.getString("Weight5","0"));
        readings[6] = Double.valueOf(sp.getString("Weight6","0"));
        rollingAvs[0] = Double.valueOf(sp.getString("rollingAvs0","0"));
        rollingAvs[1] = Double.valueOf(sp.getString("rollingAvs1","0"));
        rollingAvs[2] = Double.valueOf(sp.getString("rollingAvs2","0"));
        rollingAvs[3] = Double.valueOf(sp.getString("rollingAvs3","0"));
        rollingAvs[4] = Double.valueOf(sp.getString("rollingAvs4","0"));
        rollingAvs[5] = Double.valueOf(sp.getString("rollingAvs5","0"));
        rollingAvs[6] = Double.valueOf(sp.getString("rollingAvs6","0"));
        readDates[0] = convertStringToDate(sp.getString("readDates0","0"));
        readDates[1] = convertStringToDate(sp.getString("readDates1","0"));
        readDates[2] = convertStringToDate(sp.getString("readDates2","0"));
        readDates[3] = convertStringToDate(sp.getString("readDates3","0"));
        readDates[4] = convertStringToDate(sp.getString("readDates4","0"));
        readDates[5] = convertStringToDate(sp.getString("readDates5","0"));
        readDates[6] = convertStringToDate(sp.getString("readDates6", "0"));
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
        /*todo Fix initialisation bug below */
        if (readings[6] == 0) {
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
                rollingAverage = rollingAverage + readings[i];
            }
            readings[0] = inputValue;
            rollingAverage = Math.round(((rollingAverage + readings[0]) / rollingNumber) * 100);
            rollingAverage = rollingAverage / 100;
            rollingAvs[0] = rollingAverage;
            readDates[0] = new Date();
        }

        displayData();
        saveData();
    }
}
