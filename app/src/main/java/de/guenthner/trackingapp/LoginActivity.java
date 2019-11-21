package de.guenthner.trackingapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
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
                                            Log.d("AUTH", "signInWithEmail succeded");
                                            FirebaseUser user = firebaseAuth.getCurrentUser();
                                            startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                        }
                                        else
                                        {
                                            Log.d("AUTH","signInWithEmail failed");
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

}
