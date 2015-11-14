package com.moandal.rollingaverage;

import android.app.ActionBar;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;

/*
todo Add settings
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        loadData();
        TextView textHist1 = (TextView) findViewById(R.id.textHist1);
        TextView textHist2 = (TextView) findViewById(R.id.textHist2);
        TextView textHist3 = (TextView) findViewById(R.id.textHist3);
        TextView textHist4 = (TextView) findViewById(R.id.textHist4);
        TextView textHist5 = (TextView) findViewById(R.id.textHist5);
        TextView textHist6 = (TextView) findViewById(R.id.textHist6);
        TextView textHist7 = (TextView) findViewById(R.id.textHist7);
        TextView textHAv1 = (TextView) findViewById(R.id.textHAv1);
        TextView textHAv2 = (TextView) findViewById(R.id.textHAv2);
        TextView textHAv3 = (TextView) findViewById(R.id.textHAv3);
        TextView textHAv4 = (TextView) findViewById(R.id.textHAv4);
        TextView textHAv5 = (TextView) findViewById(R.id.textHAv5);
        TextView textHAv6 = (TextView) findViewById(R.id.textHAv6);
        TextView textHAv7 = (TextView) findViewById(R.id.textHAv7);
        TextView textHDt1 = (TextView) findViewById(R.id.textHDt1);
        TextView textHDt2 = (TextView) findViewById(R.id.textHDt2);
        TextView textHDt3 = (TextView) findViewById(R.id.textHDt3);
        TextView textHDt4 = (TextView) findViewById(R.id.textHDt4);
        TextView textHDt5 = (TextView) findViewById(R.id.textHDt5);
        TextView textHDt6 = (TextView) findViewById(R.id.textHDt6);
        TextView textHDt7 = (TextView) findViewById(R.id.textHDt7);
        TextView textAverage = (TextView) findViewById(R.id.textAverage);
        textHist1.setText(Double.toString(readings[0]));
        textHist2.setText(Double.toString(readings[1]));
        textHist3.setText(Double.toString(readings[2]));
        textHist4.setText(Double.toString(readings[3]));
        textHist5.setText(Double.toString(readings[4]));
        textHist6.setText(Double.toString(readings[5]));
        textHist7.setText(Double.toString(readings[6]));
        textHAv1.setText(Double.toString(rollingAvs[0]));
        textHAv2.setText(Double.toString(rollingAvs[1]));
        textHAv3.setText(Double.toString(rollingAvs[2]));
        textHAv4.setText(Double.toString(rollingAvs[3]));
        textHAv5.setText(Double.toString(rollingAvs[4]));
        textHAv6.setText(Double.toString(rollingAvs[5]));
        textHAv7.setText(Double.toString(rollingAvs[6]));
        textHDt1.setText(formatter.format(readDates[0]));
        textHDt2.setText(formatter.format(readDates[1]));
        textHDt3.setText(formatter.format(readDates[2]));
        textHDt4.setText(formatter.format(readDates[3]));
        textHDt5.setText(formatter.format(readDates[4]));
        textHDt6.setText(formatter.format(readDates[5]));
        textHDt7.setText(formatter.format(readDates[6]));
        textAverage.setText(Double.toString(rollingAverage));
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate our menu from the resources by using the menu inflater.
        getMenuInflater().inflate(R.menu.main, menu);

        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_erase:
                TextView textAverage = (TextView) findViewById(R.id.textAverage);
                EditText editWeight = (EditText) findViewById(R.id.editWeight);
                TextView textHist1 = (TextView) findViewById(R.id.textHist1);
                TextView textHist2 = (TextView) findViewById(R.id.textHist2);
                TextView textHist3 = (TextView) findViewById(R.id.textHist3);
                TextView textHist4 = (TextView) findViewById(R.id.textHist4);
                TextView textHist5 = (TextView) findViewById(R.id.textHist5);
                TextView textHist6 = (TextView) findViewById(R.id.textHist6);
                TextView textHist7 = (TextView) findViewById(R.id.textHist7);
                TextView textHAv1 = (TextView) findViewById(R.id.textHAv1);
                TextView textHAv2 = (TextView) findViewById(R.id.textHAv2);
                TextView textHAv3 = (TextView) findViewById(R.id.textHAv3);
                TextView textHAv4 = (TextView) findViewById(R.id.textHAv4);
                TextView textHAv5 = (TextView) findViewById(R.id.textHAv5);
                TextView textHAv6 = (TextView) findViewById(R.id.textHAv6);
                TextView textHAv7 = (TextView) findViewById(R.id.textHAv7);
                TextView textHDt1 = (TextView) findViewById(R.id.textHDt1);
                TextView textHDt2 = (TextView) findViewById(R.id.textHDt2);
                TextView textHDt3 = (TextView) findViewById(R.id.textHDt3);
                TextView textHDt4 = (TextView) findViewById(R.id.textHDt4);
                TextView textHDt5 = (TextView) findViewById(R.id.textHDt5);
                TextView textHDt6 = (TextView) findViewById(R.id.textHDt6);
                TextView textHDt7 = (TextView) findViewById(R.id.textHDt7);
                rollingAverage = 0;

                Arrays.fill(readings, 0);
                Arrays.fill(rollingAvs, 0);
                Arrays.fill(readDates, new Date());

                textAverage.setText(Double.toString(rollingAverage));
                textHist1.setText(Double.toString(readings[0]));
                textHist2.setText(Double.toString(readings[1]));
                textHist3.setText(Double.toString(readings[2]));
                textHist4.setText(Double.toString(readings[3]));
                textHist5.setText(Double.toString(readings[4]));
                textHist6.setText(Double.toString(readings[5]));
                textHist7.setText(Double.toString(readings[6]));
                textHAv1.setText(Double.toString(rollingAvs[0]));
                textHAv2.setText(Double.toString(rollingAvs[1]));
                textHAv3.setText(Double.toString(rollingAvs[2]));
                textHAv4.setText(Double.toString(rollingAvs[3]));
                textHAv5.setText(Double.toString(rollingAvs[4]));
                textHAv6.setText(Double.toString(rollingAvs[5]));
                textHAv7.setText(Double.toString(rollingAvs[6]));
                textHDt1.setText(formatter.format(readDates[0]));
                textHDt2.setText(formatter.format(readDates[1]));
                textHDt3.setText(formatter.format(readDates[2]));
                textHDt4.setText(formatter.format(readDates[3]));
                textHDt5.setText(formatter.format(readDates[4]));
                textHDt6.setText(formatter.format(readDates[5]));
                textHDt7.setText(formatter.format(readDates[6]));

                return true;
        }

        return super.onOptionsItemSelected(item);
    }

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
        TextView textAverage = (TextView) findViewById(R.id.textAverage);
        EditText editWeight = (EditText) findViewById(R.id.editWeight);
        TextView textHist1 = (TextView) findViewById(R.id.textHist1);
        TextView textHist2 = (TextView) findViewById(R.id.textHist2);
        TextView textHist3 = (TextView) findViewById(R.id.textHist3);
        TextView textHist4 = (TextView) findViewById(R.id.textHist4);
        TextView textHist5 = (TextView) findViewById(R.id.textHist5);
        TextView textHist6 = (TextView) findViewById(R.id.textHist6);
        TextView textHist7 = (TextView) findViewById(R.id.textHist7);
        TextView textHAv1 = (TextView) findViewById(R.id.textHAv1);
        TextView textHAv2 = (TextView) findViewById(R.id.textHAv2);
        TextView textHAv3 = (TextView) findViewById(R.id.textHAv3);
        TextView textHAv4 = (TextView) findViewById(R.id.textHAv4);
        TextView textHAv5 = (TextView) findViewById(R.id.textHAv5);
        TextView textHAv6 = (TextView) findViewById(R.id.textHAv6);
        TextView textHAv7 = (TextView) findViewById(R.id.textHAv7);
        TextView textHDt1 = (TextView) findViewById(R.id.textHDt1);
        TextView textHDt2 = (TextView) findViewById(R.id.textHDt2);
        TextView textHDt3 = (TextView) findViewById(R.id.textHDt3);
        TextView textHDt4 = (TextView) findViewById(R.id.textHDt4);
        TextView textHDt5 = (TextView) findViewById(R.id.textHDt5);
        TextView textHDt6 = (TextView) findViewById(R.id.textHDt6);
        TextView textHDt7 = (TextView) findViewById(R.id.textHDt7);
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

        textAverage.setText(Double.toString(rollingAverage));
        textHist1.setText(Double.toString(readings[0]));
        textHist2.setText(Double.toString(readings[1]));
        textHist3.setText(Double.toString(readings[2]));
        textHist4.setText(Double.toString(readings[3]));
        textHist5.setText(Double.toString(readings[4]));
        textHist6.setText(Double.toString(readings[5]));
        textHist7.setText(Double.toString(readings[6]));
        textHAv1.setText(Double.toString(rollingAvs[0]));
        textHAv2.setText(Double.toString(rollingAvs[1]));
        textHAv3.setText(Double.toString(rollingAvs[2]));
        textHAv4.setText(Double.toString(rollingAvs[3]));
        textHAv5.setText(Double.toString(rollingAvs[4]));
        textHAv6.setText(Double.toString(rollingAvs[5]));
        textHAv7.setText(Double.toString(rollingAvs[6]));
        textHDt1.setText(formatter.format(readDates[0]));
        textHDt2.setText(formatter.format(readDates[1]));
        textHDt3.setText(formatter.format(readDates[2]));
        textHDt4.setText(formatter.format(readDates[3]));
        textHDt5.setText(formatter.format(readDates[4]));
        textHDt6.setText(formatter.format(readDates[5]));
        textHDt7.setText(formatter.format(readDates[6]));
        saveData();
    }
}
