package com.example.mike.ftc2;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;


public class MainActivity extends ActionBarActivity {

    /** Called when the user clicks the Battery Level button */
    public void displayBattery(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayBatteryLevel.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Wifi info button */
    public void displayWifi(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayWifi.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Sensor info button */
    public void displaySensor(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplaySensors.class);
        startActivity(intent);
    }

    /** Called when the user clicks the Orientation button */
    public void displayOrientation(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayOrientation.class);
        startActivity(intent);
    }

    /** Called when the user clicks the wifi scan button */
    public void displayWifiScan(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayWifiScan.class);
        startActivity(intent);
    }

    /** Called when the user clicks the wifi channels button */
    public void displayWifiChannels(View view) {
        // Do something in response to button
        Intent intent = new Intent(this, DisplayWifiChannels.class);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
