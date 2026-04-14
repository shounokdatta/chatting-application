package com.nit.nitchattingapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<MessageModel> messageModelList;
    Context context;
    String currentUserId;

    int Left = 0;
    int Right = 1;
    private final int receiverColor;
    private final int senderColor;
    private final int chatTextColor;


    public RecyclerViewAdapter(List<MessageModel> messageModelList, Context context, String currentUserId) {
        this.messageModelList = messageModelList;
        this.context = context;
        this.currentUserId = currentUserId;
        SharedPreferences sharedPreferences = context.getSharedPreferences("color_prefs", Context.MODE_PRIVATE);
        receiverColor = sharedPreferences.getInt("receiver_color", Color.parseColor("#C7EFA9"));
        senderColor = sharedPreferences.getInt("sender_color", Color.parseColor("#A9DCEF"));
        chatTextColor = sharedPreferences.getInt("chat_text_color", Color.BLACK);
    }

    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModelList.get(position);
        if(messageModel.getId()==null){
           return Left;
        }

        if(messageModel.getId().equals(currentUserId)){
            return Right;
        }else{
            return Left;
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType==Right){
            View view = LayoutInflater.from(context).inflate(R.layout.right_chat_bubble, parent, false);
            return new RightViewHolder(view);
        }else{
            View view = LayoutInflater.from(context).inflate(R.layout.left_chat_bubble, parent, false);
            return  new LeftViewHolder(view);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
          MessageModel messageModel = messageModelList.get(position);
          if(holder instanceof RightViewHolder ){
              ((RightViewHolder) holder).message.setText(messageModel.getMessage());
              ((RightViewHolder) holder).username.setText(messageModel.getName());
              ((RightViewHolder) holder).time.setText(messageModel.getTime());
              ((RightViewHolder) holder).bubble.setBackgroundTintList(ColorStateList.valueOf(senderColor));
              ((RightViewHolder) holder).message.setTextColor(chatTextColor);


          }else{
              ((LeftViewHolder) holder).message.setText(messageModel.getMessage());
              ((LeftViewHolder) holder).username.setText(messageModel.getName());
              ((LeftViewHolder) holder).time.setText(messageModel.getTime());
              ((LeftViewHolder) holder).bubble.setBackgroundTintList(ColorStateList.valueOf(receiverColor));
              ((LeftViewHolder) holder).message.setTextColor(chatTextColor);


          }

    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }


    public static class LeftViewHolder extends RecyclerView.ViewHolder{
        TextView message, username, time;
        LinearLayout bubble;

        public LeftViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            username = itemView.findViewById(R.id.user_name);
            time = itemView.findViewById(R.id.time);
            bubble = itemView.findViewById(R.id.bubble);


        }
    }

    public static  class RightViewHolder extends RecyclerView.ViewHolder{
        TextView message, username, time;
        LinearLayout bubble;

        public RightViewHolder(@NonNull View itemView) {
            super(itemView);
            message = itemView.findViewById(R.id.message);
            username = itemView.findViewById(R.id.user_name);
            time = itemView.findViewById(R.id.time);
            bubble = itemView.findViewById(R.id.bubble);

        }
    }

}
