package de.guenthner.trackingapp;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.location.ActivityRecognitionClient;
import com.google.android.gms.location.DetectedActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.model.SliceValue;
import lecho.lib.hellocharts.view.PieChartView;

public class MainActivity extends AppCompatActivity implements SharedPreferences.OnSharedPreferenceChangeListener {
    private Context context;
    public static final String DETECTED_ACTIVITY = ".DETECTED_ACTIVITY";

    private ActivityRecognitionClient mActivityRecognitionClient;
    private ActivitiesAdapter mAdapter;

    private ListView detectedActivitiesListView;

    FirebaseUser user;
    FirebaseAuth firebaseAuth;
    FirebaseDatabase firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;

        initialize();
    }

    private void initialize()
    {
        //Retrieve the ListView where we’ll display our activity data//
        detectedActivitiesListView = (ListView) findViewById(R.id.activities_listview);

        ArrayList<DetectedActivity> detectedActivities = DetectedActivitiesIntentService.detectedActivityFromJson(
                PreferenceManager.getDefaultSharedPreferences(this).getString(
                        DETECTED_ACTIVITY, ""));

        //Bind the adapter to the ListView//
        mAdapter = new ActivitiesAdapter(this, detectedActivities);
        detectedActivitiesListView.setAdapter(mAdapter);
        mActivityRecognitionClient = new ActivityRecognitionClient(this);



        firebaseAuth = FirebaseAuth.getInstance();
        user = firebaseAuth.getCurrentUser();
        setTitle("Dashboard von " + user.getDisplayName());

    }


    @Override
    protected void onResume(){
        super.onResume();
        PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
        updateDetectedActivitiesList();
        /*running = true;
        Sensor countSensor = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
        if(countSensor != null){
            sensorManager.registerListener(this, countSensor, SensorManager.SENSOR_DELAY_UI);
        } else {
            Toast.makeText(this, "sensor not found", Toast.LENGTH_SHORT).show();
        }*/
    }


    @Override
    protected void onPause(){
        PreferenceManager.getDefaultSharedPreferences(this).unregisterOnSharedPreferenceChangeListener(this);

        super.onPause();

        // if you unregister the hardware will stop detecting steps
        // sensorManager.unregisterListener(this);
    }


    public void requestUpdatesHandler(View view) {
//Set the activity detection interval
        Task<Void> task = mActivityRecognitionClient.requestActivityUpdates(3000, getActivityDetectionPendingIntent());
        task.addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void result) {
                updateDetectedActivitiesList();
            }
        });
    }


    protected void updateDetectedActivitiesList() {
        ArrayList<DetectedActivity> detectedActivities = DetectedActivitiesIntentService.detectedActivityFromJson(
                PreferenceManager.getDefaultSharedPreferences(context)
                        .getString(DETECTED_ACTIVITY, ""));

        mAdapter.updateActivities(detectedActivities);
    }


        private PendingIntent getActivityDetectionPendingIntent() {
//Send the activity data to our DetectedActivitiesIntentService class//
            Intent intent = new Intent(this, DetectedActivitiesIntentService.class);
            return PendingIntent.getService(this, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        }


    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if (s.equals(DETECTED_ACTIVITY)) {
            updateDetectedActivitiesList();

        }
    }

    public void printPercentages(View view)
    {
        Log.d("PIE","walking: " + getPercentages().get("walking").toString());
        Log.d("PIE", "running: " + getPercentages().get("running").toString());
        Log.d("PIE","vehicle: " + getPercentages().get("in_vehicle").toString());
        Log.d("PIE", "bicycle: " + getPercentages().get("on_bicycle").toString());

        PieChartView pieChartView = findViewById(R.id.chart);

        List<SliceValue> pieData = new ArrayList<>();

        pieData.add(new SliceValue(Integer.valueOf(getPercentages().get("walking").toString()), Color.GREEN).setLabel("Gegangen"));
        pieData.add(new SliceValue(Integer.valueOf(getPercentages().get("running").toString()), Color.BLUE).setLabel("Gerannt"));
        pieData.add(new SliceValue(Integer.valueOf(getPercentages().get("in_vehicle").toString()), Color.MAGENTA).setLabel("Auto"));
        pieData.add(new SliceValue(Integer.valueOf(getPercentages().get("on_bicycle").toString()), Color.CYAN).setLabel("Fahrrad"));

        PieChartData pieChartData = new PieChartData(pieData);
        pieChartData.setHasLabels(true);

        pieChartView.setPieChartData(pieChartData);
    }

    public HashMap getPercentages()
    {
        HashMap<String, Integer> map = new HashMap<>();

        int walking = getPercentage(2);
        int running = getPercentage(3);
        int in_vehicle = getPercentage(4);
        int on_bicycle = getPercentage(5);

        map.put("walking", walking);
        map.put("running", running);
        map.put("in_vehicle", in_vehicle);
        map.put("on_bicycle", on_bicycle);

        return  map;
    }

    public int getPercentage(int i)
    {
        DetectedActivity detectedActivity = (DetectedActivity) detectedActivitiesListView.getItemAtPosition(i);

        String detectedActivityText = detectedActivity.toString();

        String[] detectedActivityTextArray = detectedActivityText.split("=");

        detectedActivityText = detectedActivityTextArray[2];

        detectedActivityText = detectedActivityText.substring(0, (detectedActivityText.length() - 1));

        return Integer.valueOf(detectedActivityText);
    }


    /*@Override
    public void onSensorChanged(SensorEvent event) {
        if( running) {
            steps.setText(String.valueOf(event.values[0]));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }*/


    /*ListView lst;
    String[] activityName={"Still", "On Foot", "Walking", "Running", "Tilting", "In Vehicle", "On Bicycle", "Unknown Activity"};
    String[] percentages;
    int[] imgid={ R.drawable.ic_still,*/
}
