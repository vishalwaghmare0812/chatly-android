package com.example.chatly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    RecyclerView mainRecyclerView;
    RecyclerAdapter recyclerAdapter;
    ArrayList<UserAdapter> userArrayList;
    ImageButton logoutMain;

    FirebaseAuth auth;
    FirebaseDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Firebase
        auth = FirebaseAuth.getInstance();
        database = FirebaseDatabase.getInstance();

        // Login check
        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(MainActivity.this, Login.class));
            finish();
            return;
        }

        // RecyclerView
        mainRecyclerView = findViewById(R.id.mainRecyclerView);
        mainRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        userArrayList = new ArrayList<>();
        recyclerAdapter = new RecyclerAdapter(this, userArrayList);
        mainRecyclerView.setAdapter(recyclerAdapter);

        // Fetch users
        DatabaseReference databaseRef = database.getReference().child("user");

        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                userArrayList.clear();

                FirebaseUser currentUser = auth.getCurrentUser();
                String currentUserId = currentUser.getUid();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    UserAdapter user =
                            dataSnapshot.getValue(UserAdapter.class);

                    if (user != null
                            && user.getUserIdAdpt() != null
                            && !user.getUserIdAdpt().equals(currentUserId)) {

                        userArrayList.add(user);
                    }
                }

                recyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Logout button
        logoutMain = findViewById(R.id.logoutMain);

        logoutMain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();

                Intent intent = new Intent(MainActivity.this, Login.class);
                startActivity(intent);
                finish();
            }
        });
    }
}