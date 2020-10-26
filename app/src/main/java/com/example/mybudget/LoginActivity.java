package com.example.mybudget;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Email login";
    private FirebaseAuth mAuth;

    private TextView emailInput;
    private TextView passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();

        emailInput = (TextView) findViewById(R.id.inputEmail);
        passwordInput = (TextView) findViewById(R.id.inputPassword);

        ImageView backBtn = (ImageView) findViewById(R.id.btnBackLogin);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Button loginBtn = (Button) findViewById(R.id.buttonLogin);
        loginBtn.setOnClickListener( v -> {
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if(email.equals("") || password.equals("")) {
                if(email.equals(""))
                    emailInput.setError(getString(R.string.email_error));
                if(password.equals(""))
                    passwordInput.setError(getString(R.string.password_error));
                Toast.makeText(LoginActivity.this, getString(R.string.fields_error), Toast.LENGTH_LONG).show();
                return;
            }



            Log.d(TAG, "email:" + email);
            Log.d(TAG, "password:" + password);

            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "signInWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();
                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "signInWithEmail:failure", task.getException());
                                // com.google.firebase.auth.FirebaseAuthInvalidCredentialsException

                                Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(LoginActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }

                        }
                    });
        });


        Button signUpBtn = (Button) findViewById(R.id.buttonSignUp);
        signUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), SignUpActivity.class));
                //finish();
            }
        });

    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void resetTextFields() {
        emailInput.setText("");
        passwordInput.setText("");
    }
}