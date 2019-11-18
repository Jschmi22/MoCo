package de.guenthner.trackingapp;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.SensorEventListener;
import android.os.Bundle;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorManager;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    SensorManager sensorManager;

    TextView steps;

    boolean running = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        steps = (TextView)findViewById(R.id.steps);
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
    }

      /*public void addPerson ( View view) {

         TextView input = findViewById(R.id.input);

         String inputText = input.getText().toString();


         TextView output = findViewById(R.id.output);
         output.setText(inputText);

     }*/

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
