package de.guenthner.trackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;
import android.os.Bundle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    TextView steps;

    boolean running = false;

    private SeekBar seekbarBike;
    private TextView bikeView;
    private TextView carView;
    private SeekBar seekbarCar;
    private  SeekBar seekbarOpnv;
    private TextView opnvView;
    private Button saveBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        steps = (TextView)findViewById(R.id.steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        seekbarBike = (SeekBar) findViewById(R.id.seekBarBike);
        bikeView = (TextView) findViewById(R.id.bikeValue);

        seekbarCar = (SeekBar) findViewById(R.id.seekBarCar);
        carView = (TextView) findViewById(R.id.valueCar);

        seekbarOpnv = (SeekBar) findViewById(R.id.seekBarOPNV);
        opnvView = (TextView) findViewById(R.id.opnvValue);

        saveBtn = (Button) findViewById(R.id.saveButton);


        bikeView.setText("0 km");
        carView.setText("0 km");
        opnvView.setText("0 km");

        seekbarBike.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                bikeView.setText(pval *  10 + " km");
            }

        });


        seekbarCar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //write custom code to on start progress
            }
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                carView.setText(pval *  2 + " km");
            }

        });


        seekbarOpnv.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            int pval = 0;

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                pval = progress;

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                opnvView.setText(pval * 10 + "km");


            }
        });


        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                seekbarBike.setVisibility(View.INVISIBLE);
                bikeView.setText("wert gespeichert");
                seekbarCar.setVisibility(View.INVISIBLE);
                carView.setText("wert gespeichert");
                seekbarOpnv.setVisibility(View.INVISIBLE);
                opnvView.setText("wert gespeichert");
            }

        });






    }



    @Override
    protected void onResume(){
        super.onResume();
        running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "sensor not found", Toast.LENGTH_SHORT).show();
        }
    }


    @Override
    protected void onPause(){
        super.onPause();
        running = false;
        // if you unregister the hardware will stop detecting steps
        // sensorManager.unregisterListener(this);
    }


    @Override
    public void onSensorChanged(SensorEvent event) {
        if( running) {
            steps.setText(String.valueOf(event.values[0]));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
