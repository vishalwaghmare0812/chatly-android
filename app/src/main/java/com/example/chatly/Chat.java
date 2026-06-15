package com.example.chatly;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class Chat extends AppCompatActivity {

    String receiveNameChat, receiveImgChat, receiveUidChat;

    CircleImageView profileChat;
    TextView usernameChat;
    ImageView backChat, sendChat;
    EditText msgFiledChat;

    RecyclerView msgRecyclerChat;

    ArrayList<MsgModel> arrayListMsgModel;
    MsgRecycler msgRecyclerAdapter;

    FirebaseAuth ChatAuth;
    FirebaseDatabase ChatDatabase;

    String senderUidChat;
    String senderRoom, receiverRoom;

    String senderPicChat, receiverPicChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Firebase Init
        ChatAuth = FirebaseAuth.getInstance();
        ChatDatabase = FirebaseDatabase.getInstance();

        // Views Init
        profileChat = findViewById(R.id.profileChat);
        usernameChat = findViewById(R.id.usernameChat);
        backChat = findViewById(R.id.backChat);
        sendChat = findViewById(R.id.sendChat);
        msgFiledChat = findViewById(R.id.msgfiledChat);
        msgRecyclerChat = findViewById(R.id.msgRecyclerChat);

        // Intent Data
        receiveNameChat = getIntent().getStringExtra("intentName");
        receiveImgChat = getIntent().getStringExtra("intentImg");
        receiveUidChat = getIntent().getStringExtra("intentUid");

        // Set Data
        usernameChat.setText(receiveNameChat);

        if (receiveImgChat != null && !receiveImgChat.isEmpty()) {
            Picasso.get().load(receiveImgChat).into(profileChat);
        }

        // Sender UID
        senderUidChat = ChatAuth.getUid();

        // Rooms
        senderRoom = senderUidChat + receiveUidChat;
        receiverRoom = receiveUidChat + senderUidChat;

        // RecyclerView
        arrayListMsgModel = new ArrayList<>();

        LinearLayoutManager linearLayoutManager =
                new LinearLayoutManager(this);

        linearLayoutManager.setStackFromEnd(true);

        msgRecyclerChat.setLayoutManager(linearLayoutManager);

        msgRecyclerChat.setHasFixedSize(false);

        msgRecyclerAdapter =
                new MsgRecycler(Chat.this, arrayListMsgModel);

        msgRecyclerChat.setAdapter(msgRecyclerAdapter);

        // Get Messages
        DatabaseReference chatsRef = ChatDatabase.getReference()
                .child("chats")
                .child(senderRoom)
                .child("messages");

        chatsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                arrayListMsgModel.clear();

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {

                    MsgModel msgModel =
                            dataSnapshot.getValue(MsgModel.class);

                    arrayListMsgModel.add(msgModel);
                }

                msgRecyclerAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        // Send Message
        sendChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String messageChat =
                        msgFiledChat.getText().toString().trim();

                if (messageChat.isEmpty()) {
                    Toast.makeText(Chat.this,
                            "Enter Message",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                msgFiledChat.setText("");

                Date dateClass = new Date();

                MsgModel msgModelclass = new MsgModel(
                        messageChat,
                        senderUidChat,
                        dateClass.getTime()
                );

                // Sender Room
                ChatDatabase.getReference()
                        .child("chats")
                        .child(senderRoom)
                        .child("messages")
                        .push()
                        .setValue(msgModelclass)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {

                                // Receiver Room
                                ChatDatabase.getReference()
                                        .child("chats")
                                        .child(receiverRoom)
                                        .child("messages")
                                        .push()
                                        .setValue(msgModelclass);
                            }
                        });
            }
        });

        // Back Button
        backChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent =
                        new Intent(Chat.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        });
    }
}