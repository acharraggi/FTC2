package com.example.mike.ftc2;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.util.Arrays;


public class DisplayOrientation extends ActionBarActivity implements SensorEventListener {

    private SensorManager mSensorManager;
    Sensor accelerometer;
    Sensor magnetometer;
    Sensor orientation;

    private float azimut = 0.0f;
    private float[] mGravity;
    private float[] mGeomagnetic;
    private float[] mOrientation;

    private boolean bGravity = false;
    private boolean bGeomagnetic = false;

    private TextView tvAzimut;
    private TextView tvGravity;
    private TextView tvGeomagnetic;
    private TextView tvOrientation;

    private void getOrientation() {
        tvAzimut.setText("Calculated azimut: " + azimut);
        tvGravity.setText("Gravity values: " + Arrays.toString(mGravity));
        tvGeomagnetic.setText("Geomagnetic values:"+Arrays.toString(mGeomagnetic));
        tvOrientation.setText("Orientation values:"+Arrays.toString(mOrientation));
//        tvGravity.setText("Gravity sensor event: " + bGravity);
//        tvGeomagnetic.setText("Geomagnetic sensor event:"+bGeomagnetic);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("DisplayOrientation", "onCreate called");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_orientation);
        tvAzimut = (TextView) this.findViewById(R.id.tvAzimut);
        tvGravity = (TextView) this.findViewById(R.id.tvGravity);
        tvGeomagnetic = (TextView) this.findViewById(R.id.tvGeomagnetic);
        tvOrientation = (TextView) this.findViewById(R.id.tvOrientation);

        // Register the sensor listeners
        mSensorManager = (SensorManager)getSystemService(SENSOR_SERVICE);
        accelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.i("DisplayOrientation", "accelerometer name: "+accelerometer.getName());
        magnetometer = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        Log.i("DisplayOrientation", "magnetometer name: "+magnetometer.getName());
        orientation = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        Log.i("DisplayOrientation", "orientation sensor name: "+orientation.getName());

        Thread t = new Thread() {

            @Override
            public void run() {
                try {
                    while (!isInterrupted()) {
                        Thread.sleep(1000);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                // update TextView here!
                                getOrientation();
                            }
                        });
                    }
                } catch (InterruptedException e) {
                }
            }
        };

        t.start();
    }

    protected void onResume() {
        Log.i("DisplayOrientation", "onResume called");
        super.onResume();
        mSensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, magnetometer, SensorManager.SENSOR_DELAY_UI);
        mSensorManager.registerListener(this, orientation, SensorManager.SENSOR_DELAY_UI);
    }

    protected void onPause() {
        Log.i("DisplayOrientation", "onPause called");
        super.onPause();
        mSensorManager.unregisterListener(this);
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        Log.i("DisplayOrientation", "onAccuracyChanged called");
    }


    public void onSensorChanged(SensorEvent event) {
        Log.i("DisplayOrientation", "onSensorChanged called");
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            mGravity = event.values;
            bGravity = true;
        }
        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            mGeomagnetic = event.values;
            bGeomagnetic = true;
        }
        if (event.sensor.getType() == Sensor.TYPE_ORIENTATION) {
            mOrientation = event.values;
        }
        if (mGravity != null && mGeomagnetic != null) {
            float R[] = new float[9];
            float I[] = new float[9];
            boolean success = SensorManager.getRotationMatrix(R, I, mGravity, mGeomagnetic);
            if (success) {
                float orientation[] = new float[3];
                SensorManager.getOrientation(R, orientation);
                azimut = orientation[0]; // orientation contains: azimut, pitch and roll
            }
        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_display_orientation, menu);
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
