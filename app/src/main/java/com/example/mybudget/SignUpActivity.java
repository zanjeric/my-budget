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

import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Map;

public class SignUpActivity extends AppCompatActivity {
    private static final String TAG = "Email Sign in";
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private TextView fullNameInput;
    private TextView emailInput;
    private TextView passwordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        fullNameInput = (TextView) findViewById(R.id.inputFullname);
        emailInput = (TextView) findViewById(R.id.inputEmail);
        passwordInput = (TextView) findViewById(R.id.inputPassword);

        ImageView backBtn = (ImageView) findViewById(R.id.btnBack);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        View signUpBtn = (View) findViewById(R.id.btnSignUp);
        signUpBtn.setOnClickListener( v -> {
            String name = fullNameInput.getText().toString();
            String email = emailInput.getText().toString();
            String password = passwordInput.getText().toString();

            if(name.equals("") || email.equals("") || password.equals("")) {
                if(name.equals(""))
                    fullNameInput.setError(getString(R.string.username_error));
                if(email.equals(""))
                    emailInput.setError(getString(R.string.email_error));
                if(password.equals(""))
                    passwordInput.setError(getString(R.string.password_error));

                Toast.makeText(SignUpActivity.this, getString(R.string.fields_error), Toast.LENGTH_LONG).show();
                return;
            }

            mAuth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in success, update UI with the signed-in user's information
                                Log.d(TAG, "createUserWithEmail:success");
                                FirebaseUser user = mAuth.getCurrentUser();

                                Map<String, Object> userObject = new HashMap<>();
                                userObject.put("email", user.getEmail());
                                userObject.put("username", name);

                                db.collection("users")
                                        .document(user.getUid())
                                        .set(userObject)
                                        .addOnSuccessListener(e -> Log.w(TAG, "User written successfully"))
                                        .addOnFailureListener(e -> Log.w(TAG, "Error adding document", e));

                                Toast.makeText(SignUpActivity.this, "Account created successfully.", Toast.LENGTH_SHORT).show();

                                updateUI(user);
                            } else {
                                // If sign in fails, display a message to the user.
                                Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                // FirebaseAuthUserCollisionException
                                // FirebaseAuthWeakPasswordException .. use .getReason()

                                Toast.makeText(SignUpActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
                                Toast.makeText(SignUpActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                updateUI(null);
                            }
                        }

                    });
        });
    }

    private void updateUI(FirebaseUser user) {
        if(user != null) {
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
        }
    }

    private void resetFields() {
        fullNameInput.setText("");
        emailInput.setText("");
        passwordInput.setText("");
    }
}