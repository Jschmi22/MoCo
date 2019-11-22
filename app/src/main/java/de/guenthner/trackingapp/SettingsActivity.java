package de.guenthner.trackingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class SettingsActivity extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setTitle("Settings");
        setContentView(R.layout.activity_settings);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId()){
                    case R.id.action_friends:
                        startActivity(new Intent(SettingsActivity.this, FriendsActivity.class));
                        finish();
                        break;
                    case R.id.action_dashboard:
                        startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                        break;
                    case R.id.action_settings:
                        startActivity(getIntent());
                        break;
                }
                return true;
            }
        });


    }
}
