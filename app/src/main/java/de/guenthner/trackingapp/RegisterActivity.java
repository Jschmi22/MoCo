package de.guenthner.trackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity
{
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordRepeatView;
    private EditText usernameView;
    private FirebaseAuth firebaseAuth;
    private DatabaseReference firebaseDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        setTitle("Account erstellen");

        initialize();
    }

    private void initialize()
    {
        emailView = (EditText)findViewById(R.id.emailRegister);
        passwordView = (EditText) findViewById(R.id.passwordRegister);
        passwordRepeatView = (EditText) findViewById(R.id.passwordRepeat);
        usernameView = findViewById(R.id.username);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void onToLoginScreenClick(View v)
    {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void onRegisterClick(View v)
    {
        final String email = emailView.getText().toString();
        final String password = passwordView.getText().toString();
        String passwordRepeat = passwordRepeatView.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Bitte geben Sie eine Email-Adresse ein!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Bitte geben Sie ein Passwort ein!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if(password.length()<6)
        {
            Toast.makeText(this, "Das Passwort muss aus mindestens 6 Zeichen bestehen!", Toast.LENGTH_SHORT).show();
            return;
        }
        
        if(!password.equals(passwordRepeat))
        {
            Toast.makeText(this, "Die Passwörter stimmen nicht überein!", Toast.LENGTH_SHORT).show();
            return;
        }
        if(usernameView.getText().toString().equals(""))
        {
            Toast.makeText(this, "Bitte geben Sie einen Nutzernamen ein.", Toast.LENGTH_SHORT).show();
            return;
        }

        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            firebaseAuth.signInWithEmailAndPassword(email, password);

                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            final String username = usernameView.getText().toString();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            user.updateProfile(profileUpdates)
                                    .addOnCompleteListener(new OnCompleteListener<Void>()
                                    {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task)
                                        {
                                            if (task.isSuccessful())
                                            {
                                                firebaseDatabase = FirebaseDatabase.getInstance().getReference();

                                                String userId = user.getUid();

                                                Long tsLong = System.currentTimeMillis()/1000;
                                                String timestamp = tsLong.toString();

                                                writeNewUser(userId, username, email, timestamp, 0, 0, 0, 0);

                                                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                finish();
                                            }
                                        }
                                    });
                        }
                        else 
                        {
                            Toast.makeText(RegisterActivity.this, "Fehler bei Email oder Passwort!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String userId, String username, String email, String timestamp, int kmFeet,
                              int kmBike, int kmCar, int kmOepnv)
    {
        UserData userData = new UserData(username, email, timestamp, kmFeet, kmBike, kmCar, kmOepnv);

        firebaseDatabase.child("users").child(userId).setValue(userData);

    }

    public class UserData
    {
        public String username;
        public String email;
        public String timestamp;
        public int kmFeet;
        public int kmBike;
        public int kmCar;
        public int kmOepnv;

        public UserData()
        {

        }

        public UserData(String username, String email, String timestamp, int kmFeet, int kmBike, int kmCar, int kmOepnv)
        {
            this.username = username;
            this.email = email;
            this.timestamp = timestamp;
            this.kmFeet = kmFeet;
            this.kmBike = kmBike;
            this.kmCar = kmCar;
            this.kmOepnv = kmOepnv;
        }
    }
}
