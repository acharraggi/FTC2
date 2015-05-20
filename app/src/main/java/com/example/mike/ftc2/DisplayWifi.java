package com.example.mike.ftc2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class DisplayWifi extends ActionBarActivity {
    private TextView wifiHeader;
    private TextView wifiAvailable;
    private TextView wifiConnected;
    private TextView wifiState;
    private TextView wifiString;

    private Context myContext;

    private void getWifiInfo() {

        wifiHeader.setText("--- WIFI information ---");
        ConnectivityManager connManager =
                (ConnectivityManager)myContext.
                        getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connManager != null) {
            NetworkInfo networkInfo = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (networkInfo != null) {
                wifiAvailable.setText("Wifi available: "+networkInfo.isAvailable());
                wifiConnected.setText("Wifi connected: "+networkInfo.isConnected());
                wifiState.setText("Wifi state: "+networkInfo.getState());
                wifiState.setText("Wifi network info string: "+networkInfo.toString());
            }
            else {
                wifiConnected.setText("No WIFI info, WIFI network info returned null");
            }
        }
        else {
            wifiHeader.setText("No WIFI info, connection manager returned null");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        myContext = this.getApplication().getApplicationContext();
        setContentView(R.layout.activity_display_wifi);

        wifiHeader = (TextView) this.findViewById(R.id.wifiHeader);
        wifiConnected = (TextView) this.findViewById(R.id.wifiConnected);
        wifiAvailable = (TextView) this.findViewById(R.id.wifiAvailable);
        wifiState = (TextView) this.findViewById(R.id.wifiState);
        wifiString = (TextView) this.findViewById(R.id.wifiString);

        getWifiInfo();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_display_wifi, menu);
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
