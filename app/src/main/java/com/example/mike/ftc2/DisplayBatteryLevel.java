package com.example.mike.ftc2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
//import android.content.IntentFilter;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayBatteryLevel extends ActionBarActivity {
// code from http://www.compiletimeerror.com/2013/05/android-battery-percentage-example.html
    private TextView batteryPercent;
    private TextView batteryHealth;
    private TextView batteryStatus;
    private TextView batteryVoltage;
    private TextView batteryTemperature;
    private TextView batteryTechnology;
    private TextView batteryPlugged;

    private void getBatteryPercentage() {
        BroadcastReceiver batteryLevelReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                context.unregisterReceiver(this);

                // determine battery level
                int currentLevel = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
                int scale = intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
                int level = -1;
                if (currentLevel >= 0 && scale > 0) {
                    level = (currentLevel * 100) / scale;
                }
                batteryPercent.setText("Battery Level Remaining: " + level + "%");

                // report battery health
                int health = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, -1);
                String msg;
                switch (health) {
                    case BatteryManager.BATTERY_HEALTH_COLD: msg = "COLD"; break;
                    case BatteryManager.BATTERY_HEALTH_DEAD: msg = "DEAD"; break;
                    case BatteryManager.BATTERY_HEALTH_GOOD: msg = "GOOD"; break;
                    case BatteryManager.BATTERY_HEALTH_OVERHEAT: msg = "OVERHEAT"; break;
                    case BatteryManager.BATTERY_HEALTH_UNKNOWN: msg = "UNKNOWN"; break;
                    case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE: msg = "UNSPECIFIED FAILURE"; break;
                    default: msg = "unknown health value: "+health;
                }
                batteryHealth.setText("Battery health: " + msg);

                // report battery status
                int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
                switch (status) {
                    case BatteryManager.BATTERY_STATUS_CHARGING: msg = "CHARGING"; break;
                    case BatteryManager.BATTERY_STATUS_DISCHARGING: msg = "DISCHARGING"; break;
                    case BatteryManager.BATTERY_STATUS_FULL: msg = "FULL"; break;
                    case BatteryManager.BATTERY_STATUS_NOT_CHARGING: msg = "NOT CHARGING"; break;
                    case BatteryManager.BATTERY_STATUS_UNKNOWN: msg = "UNKNOWN"; break;
                    default: msg = "unknown status value: "+status;
                }
                batteryStatus.setText("Battery status: " + msg);

                // report battery voltage
                int voltage = intent.getIntExtra(BatteryManager.EXTRA_VOLTAGE, -1);
                batteryVoltage.setText("Battery voltage: " + voltage);

                // report battery temperature
                int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE, -1);
                batteryTemperature.setText("Battery temperature: " + temperature);

                // report battery technology
                String technology= intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
                batteryTechnology.setText("Battery technology: " + technology);

                // report battery plugin status
                int  plugged= intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,0);
                switch (plugged) {
                    case 0: msg = "unplugged, on battery"; break;
                    case BatteryManager.BATTERY_PLUGGED_AC: msg = "on AC power"; break;
                    case BatteryManager.BATTERY_PLUGGED_USB: msg = "on USB power"; break;
                    case BatteryManager.BATTERY_PLUGGED_WIRELESS: msg = "on wireless power"; break;
                    default: msg = "unknown plugin value: "+plugged;
                }
                batteryPlugged.setText("Plug in status: " + msg);

            }
        };
        IntentFilter batteryLevelFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        registerReceiver(batteryLevelReceiver, batteryLevelFilter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_display_battery_level);

        setContentView(R.layout.activity_display_battery_level);
        batteryPercent = (TextView) this.findViewById(R.id.batteryLevel);
        batteryHealth = (TextView) this.findViewById(R.id.batteryHealth);
        batteryStatus = (TextView) this.findViewById(R.id.batteryStatus);
        batteryVoltage = (TextView) this.findViewById(R.id.batteryVoltage);
        batteryTemperature = (TextView) this.findViewById(R.id.batteryTemperature);
        batteryTechnology = (TextView) this.findViewById(R.id.batteryTechnology);
        batteryPlugged = (TextView) this.findViewById(R.id.batteryPlugged);

        getBatteryPercentage();
    }

    //@Override
    //public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
    //    getMenuInflater().inflate(R.menu.menu_display_battery_level, menu);
    //    return true;
    //}

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
