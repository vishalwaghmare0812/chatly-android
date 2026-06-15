package com.example.chatly;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import de.hdodenhof.circleimageview.CircleImageView;

public class SignUp extends AppCompatActivity {

    // TAG For Logcat
    private static final String TAG = "SignUpDebug";

    // UI Components
    CircleImageView profilePic;

    EditText signEmail, signPass, signName;

    Button signbtn;

    TextView goLogin;

    // Firebase
    FirebaseAuth auth;
    FirebaseDatabase database;
    FirebaseStorage storage;

    // Image URI
    Uri imageURI;

    String imgUriStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

//        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        getSupportActionBar().hide();

        // Firebase Initialize
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();
        storage = FirebaseStorage.getInstance();

        Log.d(TAG, "Firebase Initialized");

        // Bind Views
        profilePic = findViewById(R.id.profile_image);
        signEmail = findViewById(R.id.signEmail);
        signPass = findViewById(R.id.signPass);
        signName = findViewById(R.id.signName);
        signbtn = findViewById(R.id.signBtn);
        goLogin = findViewById(R.id.rg_login);

        Log.d(TAG, "Views Initialized");

        // =========================================
        // SIGNUP BUTTON
        // =========================================

        signbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG, "Signup Button Clicked");

                String userName =
                        signName.getText().toString().trim();

                String email =
                        signEmail.getText().toString().trim();

                String password =
                        signPass.getText().toString().trim();

                Log.d(TAG, "Username : " + userName);
                Log.d(TAG, "Email : " + email);

                // Validation
                if (TextUtils.isEmpty(userName)) {

                    Log.e(TAG, "Username Empty");

                    signName.setError("Enter Username");

                } else if (TextUtils.isEmpty(email)) {

                    Log.e(TAG, "Email Empty");

                    signEmail.setError("Enter Email");

                } else if (TextUtils.isEmpty(password)) {

                    Log.e(TAG, "Password Empty");

                    signPass.setError("Enter Password");

                } else {

                    Log.d(TAG, "Validation Success");

                    // Firebase Auth Signup
                    auth.createUserWithEmailAndPassword(
                                    email,
                                    password
                            )
                            .addOnCompleteListener(
                                    new OnCompleteListener<AuthResult>() {

                                        @Override
                                        public void onComplete(
                                                @NonNull Task<AuthResult> task) {

                                            // AUTH SUCCESS
                                            if (task.isSuccessful()) {

                                                Log.d(TAG,
                                                        "Firebase Auth Success");

                                                // Current User Id
                                                String id =
                                                        task.getResult()
                                                                .getUser()
                                                                .getUid();

                                                Log.d(TAG,
                                                        "User UID : " + id);

                                                // Database Reference
                                                DatabaseReference databaseRef =
                                                        database.getReference()
                                                                .child("user")
                                                                .child(id);

                                                Log.d(TAG,
                                                        "Database Reference Created");

                                                // Storage Reference
                                                StorageReference storageRef =
                                                        storage.getReference()
                                                                .child("upload")
                                                                .child(id);

                                                Log.d(TAG,
                                                        "Storage Reference Created");

                                                // Default Status
                                                String statusAdpt =
                                                        "Active";

                                                // =================================
                                                // IMAGE SELECTED
                                                // =================================

                                                if (imageURI != null) {

                                                    Log.d(TAG,
                                                            "Image Selected");

                                                    Log.d(TAG,
                                                            "Uploading Image...");

                                                    storageRef.putFile(imageURI)
                                                            .addOnCompleteListener(
                                                                    new OnCompleteListener<UploadTask.TaskSnapshot>() {

                                                                        @Override
                                                                        public void onComplete(
                                                                                @NonNull Task<UploadTask.TaskSnapshot> task) {

                                                                            if (task.isSuccessful()) {

                                                                                Log.d(TAG,
                                                                                        "Image Upload Success");

                                                                                // Get Download Url
                                                                                storageRef.getDownloadUrl()
                                                                                        .addOnSuccessListener(
                                                                                                new OnSuccessListener<Uri>() {

                                                                                                    @Override
                                                                                                    public void onSuccess(Uri uri) {

                                                                                                        imgUriStr =
                                                                                                                uri.toString();

                                                                                                        Log.d(TAG,
                                                                                                                "Download Url : "
                                                                                                                        + imgUriStr);

                                                                                                        // Create User Object
                                                                                                        UserAdapter user =
                                                                                                                new UserAdapter(
                                                                                                                        id,
                                                                                                                        userName,
                                                                                                                        email,
                                                                                                                        password,
                                                                                                                        imgUriStr,
                                                                                                                        statusAdpt
                                                                                                                );

                                                                                                        Log.d(TAG,
                                                                                                                "User Object Created");

                                                                                                        // Save To Database
                                                                                                        databaseRef.setValue(user)
                                                                                                                .addOnCompleteListener(
                                                                                                                        new OnCompleteListener<Void>() {

                                                                                                                            @Override
                                                                                                                            public void onComplete(
                                                                                                                                    @NonNull Task<Void> task) {

                                                                                                                                if (task.isSuccessful()) {

                                                                                                                                    Log.d(TAG,
                                                                                                                                            "User Data Saved Successfully");

                                                                                                                                    Toast.makeText(
                                                                                                                                            SignUp.this,
                                                                                                                                            "Signup Successful",
                                                                                                                                            Toast.LENGTH_SHORT
                                                                                                                                    ).show();

                                                                                                                                    startActivity(
                                                                                                                                            new Intent(
                                                                                                                                                    SignUp.this,
                                                                                                                                                    MainActivity.class
                                                                                                                                            ));

                                                                                                                                    finish();

                                                                                                                                } else {

                                                                                                                                    Log.e(TAG,
                                                                                                                                            "Database Save Failed");

                                                                                                                                    Log.e(TAG,
                                                                                                                                            task.getException().toString());

                                                                                                                                    Toast.makeText(
                                                                                                                                            SignUp.this,
                                                                                                                                            "Database Error",
                                                                                                                                            Toast.LENGTH_SHORT
                                                                                                                                    ).show();
                                                                                                                                }
                                                                                                                            }
                                                                                                                        });
                                                                                                    }
                                                                                                });

                                                                            } else {

                                                                                Log.e(TAG,
                                                                                        "Image Upload Failed");

                                                                                Log.e(TAG,
                                                                                        task.getException().toString());

                                                                                Toast.makeText(
                                                                                        SignUp.this,
                                                                                        "Image Upload Failed",
                                                                                        Toast.LENGTH_SHORT
                                                                                ).show();
                                                                            }
                                                                        }
                                                                    });

                                                }

                                                // =================================
                                                // NO IMAGE SELECTED
                                                // =================================

                                                else {

                                                    Log.d(TAG,
                                                            "No Image Selected");

                                                    imgUriStr = "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcRqHxa6h_89nTZH3fH0FtVekkIqhoSUcdo36Q&s";

                                                    Log.d(TAG,
                                                            "Default Image Set");

                                                    // Create User Object
                                                    UserAdapter user =
                                                            new UserAdapter(
                                                                    id,
                                                                    userName,
                                                                    email,
                                                                    password,
                                                                    imgUriStr,
                                                                    statusAdpt
                                                            );

                                                    Log.d(TAG,
                                                            "User Object Created");

                                                    // Save Data
                                                    databaseRef.setValue(user)
                                                            .addOnCompleteListener(
                                                                    new OnCompleteListener<Void>() {

                                                                        @Override
                                                                        public void onComplete(
                                                                                @NonNull Task<Void> task) {

                                                                            if (task.isSuccessful()) {

                                                                                Log.d(TAG,
                                                                                        "User Saved Successfully");

                                                                                Toast.makeText(
                                                                                        SignUp.this,
                                                                                        "Signup Successful",
                                                                                        Toast.LENGTH_SHORT
                                                                                ).show();

                                                                                startActivity(
                                                                                        new Intent(
                                                                                                SignUp.this,
                                                                                                MainActivity.class
                                                                                        ));

                                                                                finish();

                                                                            } else {

                                                                                Log.e(TAG,
                                                                                        "Database Save Failed");

                                                                                Log.e(TAG,
                                                                                        task.getException().toString());

                                                                                Toast.makeText(
                                                                                        SignUp.this,
                                                                                        "Database Error",
                                                                                        Toast.LENGTH_SHORT
                                                                                ).show();
                                                                            }
                                                                        }
                                                                    });
                                                }

                                            }

                                            // AUTH FAILED
                                            else {

                                                Log.e(TAG,
                                                        "Firebase Auth Failed");

                                                Log.e(TAG,
                                                        task.getException().toString());

                                                Toast.makeText(
                                                        SignUp.this,
                                                        task.getException().getMessage(),
                                                        Toast.LENGTH_LONG
                                                ).show();
                                            }
                                        }
                                    });
                }
            }
        });

        // =========================================
        // GO LOGIN PAGE
        // =========================================

        goLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,
                        "Navigate To Login");

                startActivity(
                        new Intent(SignUp.this,
                                Login.class));

                finish();
            }
        });

        // =========================================
        // SELECT PROFILE IMAGE
        // =========================================

        profilePic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Log.d(TAG,
                        "Opening Gallery");

                Intent intent = new Intent();

                intent.setType("image/*");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(
                        Intent.createChooser(
                                intent,
                                "Select Picture"
                        ),
                        10
                );
            }
        });

    }

    // =========================================
    // IMAGE PICKER RESULT
    // =========================================

    @Override
    protected void onActivityResult(
            int requestCode,
            int resultCode,
            @Nullable Intent data) {

        super.onActivityResult(
                requestCode,
                resultCode,
                data
        );

        Log.d(TAG,
                "onActivityResult Called");

        if (requestCode == 10
                && resultCode == RESULT_OK
                && data != null
                && data.getData() != null) {

            imageURI = data.getData();

            Log.d(TAG,
                    "Image Uri : " + imageURI);

            profilePic.setImageURI(imageURI);

            Log.d(TAG,
                    "Profile Image Set");
        }
    }
}