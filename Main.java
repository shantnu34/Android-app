package com.example.manoj.graphapp;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.GraphViewSeries;
import com.jjoe64.graphview.LineGraphView;

public class Main extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private boolean color = false;
    private View view;
    private long lastUpdate;
    public final String TAG = "MAIN";
    private Double[] dataPoints;
    private GraphView graphView;
    private GraphViewSeries series;
    private double graph2LastXValue = 5d;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        view = findViewById(R.id.textView);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        lastUpdate = System.currentTimeMillis();

        series = new GraphViewSeries(new GraphView.GraphViewData[] {
        });
        graphView = new LineGraphView(
                this
                , "Acceleration"
        );
        graphView.addSeries(series);
        graphView.setViewPort(1, 10);
        graphView.setManualYAxisBounds(20,1);
		viewport.setMinY(-6);
        viewport.setMaxY(6);
        graphView.setScalable(true);

        LinearLayout layout = (LinearLayout) findViewById(R.id.linearLayout);
        layout.addView(graphView);

        dataPoints = new Double[500];
    }
    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            getAccelerometer(event);
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {
    }

    private void getAccelerometer(SensorEvent event) {
        float[] values = event.values;
        float x = values[0];
        float y = values[1];
        float z = values[2];

        float accelationSquareRoot = (x * x + y * y + z * z)
                / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
        double acceleration = Math.sqrt(accelationSquareRoot);
        long actualTime = System.currentTimeMillis();
        graph2LastXValue += 1d;
        series.appendData(new GraphView.GraphViewData(graph2LastXValue, acceleration), true, 10);
        addDataPoint(acceleration);

    }

    private void addDataPoint(double acceleration) {
        dataPoints[499] = acceleration;
    }


    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this,
                sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
                SensorManager.SENSOR_DELAY_NORMAL);
    }
    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }
}
