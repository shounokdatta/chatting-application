package com.nit.nitchattingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserName extends AppCompatActivity {
    EditText name;
    Button btn_login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_name);
        name = findViewById(R.id.editTextText);
        btn_login = findViewById(R.id.btn_confirm);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        String password = intent.getStringExtra("password");

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = name.getText().toString();
                if (userName.isEmpty()) {
                    Toast.makeText(UserName.this, "Please enter a name", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 1. Create a reference for a new document to get its unique ID.
                DocumentReference newUserRef = db.collection("users").document();
                String userId = newUserRef.getId();

                // 2. Create the user model with the unique ID.
                UserModel user = new UserModel(userName, password, email, userId);

                // 3. Set the user data in Firestore using the generated ID.
                newUserRef.set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // 4. On success, save the SAME ID to SharedPreferences.
                            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putString("email", email);
                            editor.putString("name", userName);
                            editor.putString("id", userId); // <-- This is the crucial fix.
                            editor.apply();

                            Intent homeIntent = new Intent(UserName.this, HomePage.class);
                            startActivity(homeIntent);
                            finish(); // Finish this activity to prevent user from going back.
                        } else {
                            Toast.makeText(UserName.this, "Failed to create account.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
