package com.example.chatly;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.viewholder> {

    MainActivity mainActivity;
    ArrayList<UserAdapter> userArrayList;
    public RecyclerAdapter(MainActivity mainActivity, ArrayList<UserAdapter> userArrayList) {
        this.mainActivity=mainActivity;
        this.userArrayList=userArrayList;
    }

    @NonNull
    @Override
    public RecyclerAdapter.viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mainActivity).inflate(R.layout.user_item, parent, false);
        return new viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerAdapter.viewholder holder, int position) {

        UserAdapter userAdapter =userArrayList.get(position);
        holder.userName_item.setText(userAdapter.userNameAdpt);
        holder.userStatus_item.setText(userAdapter.statusAdpt);
        Picasso.get().load(userAdapter.profilepicAdpt).into(holder.userPic_item);

        //sent data for ChatActivity
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mainActivity, Chat.class);
                intent.putExtra("intentName", userAdapter.getUserNameAdpt());
                intent.putExtra("intentImg", userAdapter.getProfilepicAdpt());
                intent.putExtra("intentUid", userAdapter.getUserIdAdpt());
                mainActivity.startActivity(intent);
            }


        });
    }

    @Override
    public int getItemCount() {
        return userArrayList.size();
    }

    public class viewholder extends RecyclerView.ViewHolder {
        CircleImageView userPic_item;
        TextView userName_item, userStatus_item;
        public viewholder(@NonNull View itemView) {
            super(itemView);
            userPic_item= itemView.findViewById(R.id.userPic_item);
            userName_item= itemView.findViewById(R.id.userName_item);
            userStatus_item= itemView.findViewById(R.id.userStatus_item);

        }
    }
}
