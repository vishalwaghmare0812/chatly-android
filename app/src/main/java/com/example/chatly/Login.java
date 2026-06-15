package com.example.chatly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {

    Button login;
    TextView goSign;
    EditText lgEmail, lgPassword;

    String myTag = "LOGIN_DEBUG";

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        getSupportActionBar().hide();

        Log.d(myTag, "Login Activity Started");

        auth = FirebaseAuth.getInstance();

        lgEmail = findViewById(R.id.lgEmail);
        lgPassword = findViewById(R.id.lgPassword);
        login = findViewById(R.id.loginbtn);
        goSign = findViewById(R.id.goSign);

        // Login Button
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(myTag, "Login button clicked");

                String email = lgEmail.getText().toString().trim();
                String pass = lgPassword.getText().toString().trim();

                Log.d(myTag, "Email Entered: " + email);

                // Validation
                if (TextUtils.isEmpty(email) && TextUtils.isEmpty(pass)) {

                    Log.d(myTag, "Both email and password are empty");

                    Toast.makeText(Login.this,
                            "Please enter your email and password.",
                            Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(email)) {

                    Log.d(myTag, "Email is empty");

                    Toast.makeText(Login.this,
                            "Please enter your email.",
                            Toast.LENGTH_SHORT).show();

                } else if (TextUtils.isEmpty(pass)) {

                    Log.d(myTag, "Password is empty");

                    Toast.makeText(Login.this,
                            "Please enter your password.",
                            Toast.LENGTH_SHORT).show();

                } else {

                    Log.d(myTag, "Attempting Firebase Login");

                    auth.signInWithEmailAndPassword(email, pass)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        Log.d(myTag, "Login Successful");

                                        Toast.makeText(Login.this,
                                                "Login Successful",
                                                Toast.LENGTH_SHORT).show();

                                        Intent intent =
                                                new Intent(Login.this, MainActivity.class);

                                        startActivity(intent);
                                        finish();

                                    } else {

                                        Log.e(myTag,
                                                "Login Failed: "
                                                        + task.getException().getMessage());

                                        Toast.makeText(Login.this,
                                                "Login Failed",
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });

        // Navigate to SignUp
        goSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(myTag, "Navigating to SignUp Activity");

                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
                finish();
            }
        });


    }
}