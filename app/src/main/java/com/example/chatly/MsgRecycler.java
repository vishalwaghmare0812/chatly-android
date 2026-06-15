package com.example.chatly;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MsgRecycler extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    Context context;
    ArrayList<MsgModel> recyclerArrayList;

    int ITEM_SENDER = 1;
    int ITEM_RECEIVER = 2;

    public MsgRecycler(Context context, ArrayList<MsgModel> recyclerArrayList) {
        this.context = context;
        this.recyclerArrayList = recyclerArrayList;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == ITEM_SENDER) {

            View view = LayoutInflater.from(context)
                    .inflate(R.layout.sender_layout, parent, false);

            return new senderViewHolder(view);

        } else {

            View view = LayoutInflater.from(context)
                    .inflate(R.layout.receiver_layout, parent, false);

            return new receiverViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        MsgModel msgRecycler = recyclerArrayList.get(position);

        if (holder instanceof senderViewHolder) {

            senderViewHolder viewHolder = (senderViewHolder) holder;
            viewHolder.senderMsg.setText(msgRecycler.getMsgModelStr());

        } else {

            receiverViewHolder viewHolder = (receiverViewHolder) holder;
            viewHolder.receiverMsg.setText(msgRecycler.getMsgModelStr());
        }
    }

    @Override
    public int getItemCount() {
        return recyclerArrayList.size();
    }

    @Override
    public int getItemViewType(int position) {

        MsgModel msgRecycler = recyclerArrayList.get(position);

        if (FirebaseAuth.getInstance().getCurrentUser().getUid()
                .equals(msgRecycler.getSenderIdModel())) {

            return ITEM_SENDER;

        } else {

            return ITEM_RECEIVER;
        }
    }

    class senderViewHolder extends RecyclerView.ViewHolder {

        TextView senderMsg;

        public senderViewHolder(@NonNull View itemView) {
            super(itemView);

            senderMsg = itemView.findViewById(R.id.senderMsg);
        }
    }

    class receiverViewHolder extends RecyclerView.ViewHolder {

        TextView receiverMsg;

        public receiverViewHolder(@NonNull View itemView) {
            super(itemView);

            receiverMsg = itemView.findViewById(R.id.receiverMsg);
        }
    }
}