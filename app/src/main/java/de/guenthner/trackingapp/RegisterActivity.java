package de.guenthner.trackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.guenthner.trackingapp.FirebaseDB.Friends;
import de.guenthner.trackingapp.FirebaseDB.MovementInit;
import de.guenthner.trackingapp.FirebaseDB.UserData;

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

        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Bitte warten");
        progressDialog.setMessage("Account wird erstellt...");

        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            progressDialog.setMessage("Account wird eingeloggt...");

                            firebaseAuth.signInWithEmailAndPassword(email, password);

                            progressDialog.setMessage("Profil wird initialisiert...");

                            final FirebaseUser user = firebaseAuth.getCurrentUser();
                            final String username = usernameView.getText().toString();

                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                    .setDisplayName(username)
                                    .build();

                            try
                            {
                                user.updateProfile(profileUpdates)
                                        .addOnCompleteListener(new OnCompleteListener<Void>()
                                        {
                                            @Override
                                            public void onComplete(@NonNull Task<Void> task)
                                            {
                                                if (task.isSuccessful())
                                                {
                                                    progressDialog.setMessage("Datenbank wird initialisiert...");

                                                    firebaseDatabase = FirebaseDatabase.getInstance().getReference();

                                                    String userId = user.getUid();

                                                    Long tsLong = System.currentTimeMillis()/1000;
                                                    String timestamp = tsLong.toString();

                                                    writeNewUser(userId, username, email, timestamp);

                                                    progressDialog.dismiss();

                                                    startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                                    finish();
                                                }
                                            }
                                        });
                            }
                            catch (Exception e)
                            {
                                Toast.makeText(RegisterActivity.this, "Fehler bei der Erstellung.", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(RegisterActivity.this, "Fehler bei Email oder Passwort!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void writeNewUser(String userId, String username, String email, String timestamp)
    {
        UserData userData = new UserData(username, email, timestamp);
        MovementInit percent_init = new MovementInit(0, 0, 0, 0);
        Friends friendsInit = new Friends("null");

        firebaseDatabase.child("users").child(userId).setValue(userData);

        firebaseDatabase.child("users").child(userId).child("percentAll").setValue(percent_init);
        firebaseDatabase.child("users").child(userId).child("percentDay").setValue(percent_init);
        firebaseDatabase.child("users").child(userId).child("percentWeek").setValue(percent_init);
        firebaseDatabase.child("users").child(userId).child("percentMonth").setValue(percent_init);
        firebaseDatabase.child("users").child(userId).child("percentYear").setValue(percent_init);

        firebaseDatabase.child("users").child(userId).child("friends").setValue(friendsInit);
    }

}