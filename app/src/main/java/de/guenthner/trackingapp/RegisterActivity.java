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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity
{
    private EditText emailView;
    private EditText passwordView;
    private EditText passwordRepeatView;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        initialize();
    }

    private void initialize()
    {
        emailView = (EditText)findViewById(R.id.emailRegister);
        passwordView = (EditText) findViewById(R.id.passwordRegister);
        passwordRepeatView = (EditText) findViewById(R.id.passwordRepeat);

        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void onToLoginScreenClick(View v)
    {
        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
        finish();
    }

    public void onRegisterClick(View v)
    {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();
        String passwordRepeat = passwordRepeatView.getText().toString();

        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Bitte geben Sie eine Email-Adresse ein!", Toast.LENGTH_SHORT).show();
        }
        
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Bitte geben Sie ein Passwort ein!", Toast.LENGTH_SHORT).show();
        }
        
        if(password.length()<4)
        {
            Toast.makeText(this, "Das Passwort muss aus mindestens 4 Zeichen bestehen!", Toast.LENGTH_SHORT).show();
        }
        
        if(!password.equals(passwordRepeat))
        {
            Toast.makeText(this, "Die Passwörter stimmen nicht überein!", Toast.LENGTH_SHORT).show();
        }
        
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>()
                {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task)
                    {
                        if(task.isSuccessful())
                        {
                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                            finish();
                        }
                        else 
                        {
                            Toast.makeText(RegisterActivity.this, "Fehler bei Email oder Passwort!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
