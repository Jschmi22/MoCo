package de.guenthner.trackingapp;
import java.util.ArrayList;
import java.lang.reflect.Type;

import android.app.Instrumentation;
import android.content.Context;

import com.google.android.gms.location.DetectedActivity;
import com.google.gson.Gson;
import android.content.Intent;
import android.app.IntentService;
import android.preference.PreferenceManager;
import android.content.res.Resources;
import com.google.gson.reflect.TypeToken;
import com.google.android.gms.location.ActivityRecognitionResult;





import androidx.annotation.Nullable;

public class DetectedActivitiesIntentService extends IntentService {

    protected static final String TAG = DetectedActivitiesIntentService.class.getSimpleName();
    //Call the super IntentService constructor with the name for the worker thread//

    public DetectedActivitiesIntentService(){
        //Use the TAG to name the worker thread
        super(TAG);
    }

    @Override
    public void onCreate(){
        super.onCreate();
    }

    @SuppressWarnings("unchecked")
    //Define an onHandleIntent() method, which will be called whenever an activity detection update is available//

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(ActivityRecognitionResult.hasResult(intent)){

            //if data is available then extract the ActivityRecognitionResult from the Intent
            ActivityRecognitionResult result = ActivityRecognitionResult.extractResult(intent);

            //get an Array of DetectedActivity Objects
            ArrayList<DetectedActivity> detectedActivities = (ArrayList) result.getProbableActivities();

            PreferenceManager.getDefaultSharedPreferences(this).edit().putString(MainActivity.DETECTED_ACTIVITY, detectedActivitiesToJson(detectedActivities)).apply();
        }


    }

    static String getActivityString (Context context, int detectedActivityType) {
        Resources resources = context.getResources();
        switch (detectedActivityType){
            case DetectedActivity.ON_BICYCLE:
                return resources.getString(R.string.bicycle);
            case DetectedActivity.ON_FOOT:
                return  resources.getString(R.string.foot);
            case DetectedActivity.RUNNING:
                return resources.getString(R.string.running);
            case DetectedActivity.STILL:
                return resources.getString(R.string.still);
            case DetectedActivity.TILTING:
                return resources.getString(R.string.tilting);
            case DetectedActivity.WALKING:
                return resources.getString(R.string.walking);
            case DetectedActivity.IN_VEHICLE:
                return resources.getString(R.string.vehicle);
            default:
                return resources.getString(R.string.unknown_activity);
        }
    }

    static final int[] POSSIBLE_ACTIVITIES = {

            DetectedActivity.STILL,
            DetectedActivity.ON_FOOT,
            DetectedActivity.WALKING,
            DetectedActivity.RUNNING,
            DetectedActivity.IN_VEHICLE,
            DetectedActivity.ON_BICYCLE,
            DetectedActivity.TILTING,
            DetectedActivity.UNKNOWN
    };

    static String detectedActivitiesToJson(ArrayList<DetectedActivity> detectedActivitiesList){
        Type type = new TypeToken<ArrayList<DetectedActivity>>() {}.getType();
        return new Gson().toJson(detectedActivitiesList, type);
    }

    static ArrayList<DetectedActivity> detectedActivityFromJson(String jsonArray){
        Type listType = new TypeToken<ArrayList<DetectedActivity>>(){}.getType();
        ArrayList<DetectedActivity> detectedActivities = new Gson().fromJson(jsonArray, listType);
        if(detectedActivities == null){
            detectedActivities = new ArrayList<>();
        }
        return detectedActivities;
    }


}
