package com.nit.nitchattingapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SeetingsPage extends AppCompatActivity {
     ConstraintLayout btnProfile,btn_customization;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seetings_page);
        btnProfile = findViewById(R.id.btn_profile);
        btn_customization = findViewById(R.id.btn_customization);

        btnProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeetingsPage.this, ProfilePage.class);
                startActivity(intent);
            }
        });

        btn_customization.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SeetingsPage.this, Customization.class);
                startActivity(intent);
            }
        });

    }
}