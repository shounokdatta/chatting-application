package com.nit.nitchattingapp;

import android.content.SharedPreferences;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import yuku.ambilwarna.AmbilWarnaDialog;

public class Customization extends AppCompatActivity {

    Button btn_color_bubble, btn_chat_color, btn_color_sender;
    TextView showSenderColor, showReceiverColor, showChatColor, senderMessagePreview, receiverMessagePreview;
    LinearLayout linearLayout1, linearLayout2;

    private int receiverColor;
    private int senderColor;
    private int chatColor;
    private int chatTextColor; // new variable for chat text color

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customization);

        btn_color_bubble = findViewById(R.id.btn_color_bubble_receiver);
        btn_chat_color = findViewById(R.id.btn_chat_color);
        btn_color_sender = findViewById(R.id.btn_color_bubble_sender);
        showSenderColor = findViewById(R.id.showSenderColor);
        showReceiverColor = findViewById(R.id.showReceiverColor);
        showChatColor = findViewById(R.id.showChatColor);
        linearLayout1 = findViewById(R.id.receiver_color);
        linearLayout2 = findViewById(R.id.sender_color);
        senderMessagePreview = findViewById(R.id.sender_message_preview);
        receiverMessagePreview = findViewById(R.id.receiver_message_preview);

        SharedPreferences sharedPreferences = getSharedPreferences("color_prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        // Load and apply saved colors
        receiverColor = sharedPreferences.getInt("receiver_color", Color.parseColor("#C7EFA9"));
        linearLayout1.setBackgroundTintList(ColorStateList.valueOf(receiverColor));
        showReceiverColor.setText(String.valueOf(receiverColor));

        senderColor = sharedPreferences.getInt("sender_color", Color.parseColor("#A9DCEF"));
        linearLayout2.setBackgroundTintList(ColorStateList.valueOf(senderColor));
        showSenderColor.setText(String.valueOf(senderColor));

        chatTextColor = sharedPreferences.getInt("chat_text_color", Color.BLACK);
        senderMessagePreview.setTextColor(chatTextColor);
        receiverMessagePreview.setTextColor(chatTextColor);
        showChatColor.setText(String.valueOf(chatTextColor));


        btn_color_bubble.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(Customization.this, receiverColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        receiverColor = color;
                        showReceiverColor.setText(String.valueOf(color));
                        linearLayout1.setBackgroundTintList(ColorStateList.valueOf(color));
                        editor.putInt("receiver_color", color);
                        editor.apply();
                    }
                });
                dialog.show();
            }
        });

        btn_color_sender.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(Customization.this, senderColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        senderColor = color;
                        showSenderColor.setText(String.valueOf(color));
                        linearLayout2.setBackgroundTintList(ColorStateList.valueOf(color));
                        editor.putInt("sender_color", color);
                        editor.apply();
                    }
                });
                dialog.show();
            }
        });

        btn_chat_color.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AmbilWarnaDialog dialog = new AmbilWarnaDialog(Customization.this, chatTextColor, new AmbilWarnaDialog.OnAmbilWarnaListener() {
                    @Override
                    public void onCancel(AmbilWarnaDialog dialog) {
                    }

                    @Override
                    public void onOk(AmbilWarnaDialog dialog, int color) {
                        chatTextColor = color;
                        showChatColor.setText(String.valueOf(color));
                        senderMessagePreview.setTextColor(color);
                        receiverMessagePreview.setTextColor(color);
                        editor.putInt("chat_text_color", color);
                        editor.apply();
                    }
                });
                dialog.show();
            }
        });
    }
}
