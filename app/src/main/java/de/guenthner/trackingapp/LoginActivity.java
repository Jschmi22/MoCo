package de.guenthner.trackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity
{
    private EditText emailView;
    private EditText passwordView;
    private FirebaseAuth firebaseAuth;
    private GoogleSignInClient mSignInClient;

    private static final int RC_SIGN_IN = 9001;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        initialize();
    }

    private void initialize()
    {
        emailView = findViewById(R.id.emailLogin);
        passwordView = findViewById(R.id.passwordLogin);

        firebaseAuth = FirebaseAuth.getInstance();

        GoogleSignInOptions options = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mSignInClient = GoogleSignIn.getClient(this, options);

        //if(firebaseAuth.getCurrentUser() != null)
        //{
        //    startActivity(new Intent(LoginActivity.this, MainActivity.class));
        //    finish();
        //}
    }

    public void onLoginClick(View v)
    {
        String email = emailView.getText().toString();
        String password = passwordView.getText().toString();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this,
                        new OnCompleteListener<AuthResult>()
                        {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task)
                            {
                                if(task.isSuccessful())
                                {
                                    FirebaseUser user = firebaseAuth.getCurrentUser();

                                    //        if(user.isEmailVerified())
                                    //        {
                                    Log.d("LOGIN", "signInWithEmail succeded");

                                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                    finish();
                                    //        }
                                    //        else
                                    //        {
                                    //            Toast.makeText(LoginActivity.this, "Bitte bestätigen Sie Ihre Email-Adresse.", Toast.LENGTH_SHORT).show();
                                    //        }
                                }
                                else
                                {
                                    Log.d("LOGIN","signInWithEmail failed");
                                    Toast.makeText(LoginActivity.this, "Login fehlgeschlagen", Toast.LENGTH_SHORT).show();
                                }
                            }});

        //Intent signIntent = mSignInClient.getSignInIntent();
        //startActivityForResult(signIntent, RC_SIGN_IN);
    }

    public void onToRegisterScreenClick(View v)
    {
        startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        finish();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == RC_SIGN_IN)
        {
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            if(task.isSuccessful())
            {
                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                GoogleSignInAccount acct = task.getResult();
                finish();
            }
            else
            {
                Toast.makeText(this, "Login Fehlgeschlagen! EMail oder Passwort sind falsch.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onForgotPasswordClick(View v)
    {
        String email = emailView.getText().toString();

        if(email.equals(""))
        {
            Toast.makeText(this, "Bitte geben Sie eine Email ein!", Toast.LENGTH_SHORT).show();
        }
        else
        {
            firebaseAuth.sendPasswordResetEmail(email)
                    .addOnCompleteListener(new OnCompleteListener<Void>()
                    {
                        @Override
                        public void onComplete(@NonNull Task<Void> task)
                        {
                            if (task.isSuccessful())
                            {
                                Toast.makeText(LoginActivity.this, "Email wurde gesendet.", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                Toast.makeText(LoginActivity.this, "Fehler bei der Email. Email wurde nicht gesendet.", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }
}