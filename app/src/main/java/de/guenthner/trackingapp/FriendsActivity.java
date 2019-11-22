package de.guenthner.trackingapp;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class FriendsActivity extends AppCompatActivity {

    TextView mySteps;
    Button newFriend;
    CardView cardview;
    Context context;
    LinearLayout linearLayout;
    LayoutParams layoutParams;


    @Override
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setTitle("Friends");
        setContentView(R.layout.activity_friends);

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavigationView);
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item){
                switch(item.getItemId()){
                    case R.id.action_friends:
                        startActivity(getIntent());
                        finish();
                        break;
                    case R.id.action_dashboard:
                        startActivity(new Intent(FriendsActivity.this, MainActivity.class));
                        break;
                    case R.id.action_settings:
                        startActivity(new Intent(FriendsActivity.this, SettingsActivity.class));
                        finish();
                        break;
                }
                return true;
            }
        });


        initialize();

    }

    private void initialize() {

        mySteps = (TextView) findViewById(R.id.stepsFriend);
        newFriend = (Button) findViewById(R.id.newFriendButton);
        cardview = (CardView) findViewById(R.id.friend2);

        context = getApplicationContext();

        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        MainActivity mainActivity = new MainActivity();
        mainActivity.steps = mySteps;

        newFriend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                createCardView();

            }
        });
    }


        public void createCardView() {

            cardview = new CardView(context);

            cardview.findViewById(R.id.friend2);

            layoutParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);

            cardview.setLayoutParams(layoutParams);
            linearLayout.addView(cardview);


        }

    }
