package com.example.mike.ftc2;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.internal.widget.AdapterViewCompat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


public class DisplayWifiChannels extends ActionBarActivity implements View.OnClickListener{
    WifiManager wifi;
    ListView lv;
    TextView textStatus;
    Button buttonScan;
    int size = 0;
    List<ScanResult> results;
    int[][] channels = new int[15][2];  // store invalid channels in row 0
    // store count in column[0] and strength in colum[1]

    // create the grid item mapping
    String[] from = new String[] {"channel", "count", "strength"};
    int[] to = new int[] { R.id.channel, R.id.count, R.id.strength };
    //String[] from = new String[] {"SSID", "frequency", "channel", "strength"};
    //int[] to = new int[] { R.id.SSID, R.id.frequency, R.id.channel, R.id.strength };

    ArrayList<HashMap<String, String>> arraylist = new ArrayList<HashMap<String, String>>();
    SimpleAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_wifi_channels);
        textStatus = (TextView) findViewById(R.id.textStatus);
        buttonScan = (Button) findViewById(R.id.buttonScan);
        buttonScan.setOnClickListener(this);
        lv = (ListView)findViewById(R.id.list);

        wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        if (wifi.isWifiEnabled() == false)
        {
            Toast.makeText(getApplicationContext(), "wifi is disabled..making it enabled", Toast.LENGTH_LONG).show();
            wifi.setWifiEnabled(true);
        }
        //this.adapter = new SimpleAdapter(DisplayWifiScan.this, arraylist, R.layout.row, new String[] { ITEM_KEY }, new int[] { R.id.list_value });
        this.adapter = new SimpleAdapter(DisplayWifiChannels.this, arraylist, R.layout.channel_row, from, to);
        lv.setAdapter(this.adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Toast.makeText(getApplicationContext(), "onItemClick: position: " + position + ", id: " + id,
                        Toast.LENGTH_SHORT).show();
            }
        });

        registerReceiver(new BroadcastReceiver() {
            @Override
            public void onReceive(Context c, Intent intent) {
                results = wifi.getScanResults();
//                Collections.sort(results, new Comparator<ScanResult>() {
//                    @Override
//                    public int compare(final ScanResult object1, final ScanResult object2) {
//                        return (object1.level - object2.level); // sort descending order
//                    }
//                });
                size = results.size();
                c.unregisterReceiver(this);
            }
        }, new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
    }

    private static int convertFrequencyToChannel(int freq) {
        // only handles channels 1-11, 12-14 are generally not usable in North America
        // don't bother with 5000+ freq
        if (freq >= 2412 && freq <= 2472) {
            return (freq - 2412) / 5 + 1;
        } else if (freq == 2484) {  // had to fix for 14
            return 14;
       // } else if (freq >= 5170 && freq <= 5825) {
       //     return (freq - 5170) / 5 + 34;
        } else {
            return -1;
        }
    }

    private void getScanList() {
        arraylist.clear();
        wifi.startScan();

        for (int[] item : channels) {
            item[0] = 0;
            item[1] = 0;
        }
        if(size > 0) {
            for(ScanResult item : results){
                int c = convertFrequencyToChannel(item.frequency);
                int s = WifiManager.calculateSignalLevel(item.level, 5);
                if (c==-1) {
                    c = 0;
                }
                channels[c][0]++; // increment count
                channels[c][1] = channels[c][1] + s; // sum strength settings
            }
        }

        Toast.makeText(this, "Scanning...." + size, Toast.LENGTH_SHORT).show();
        try
        {
            // add column headers
            {
                HashMap<String, String> item = new HashMap<String, String>();
                item.put("channel", "Channel");
                item.put("count", "Using Count");
                item.put("strength", "Strength");
                arraylist.add(item);
                adapter.notifyDataSetChanged();
            }

            if (size > 0) {
                for(int i = 1; i<15; i++){  // do normal channels
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("channel", Integer.toString(i));
                    item.put("count", Integer.toString(channels[i][0]));
                    item.put("strength", Integer.toString(channels[i][1]));
                    arraylist.add(item);
                    adapter.notifyDataSetChanged();
                }
                // add 'others' channel if any
                if(channels[0][0]>0) {
                    HashMap<String, String> item = new HashMap<String, String>();
                    item.put("channel", "Others...");
                    item.put("count", Integer.toString(channels[0][0]));
                    item.put("strength", Integer.toString(channels[0][1]));
                    arraylist.add(item);
                    adapter.notifyDataSetChanged();
                }
            }
        }
        catch (Exception e)
        { }
    }

    public void onClick(View view)
    {
        switch(view.getId()) {
            case R.id.buttonScan:
                // it was the button
                getScanList();
                break;
            case R.id.list_item:
                // it was the list
                Toast.makeText(this, "List was clicked....", Toast.LENGTH_SHORT).show();
                break;
        }

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_display_wifi_channels, menu);
//        return true;
//    }

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
