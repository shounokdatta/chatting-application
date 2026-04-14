package com.nit.nitchattingapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    Button button;
    EditText email, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button = findViewById(R.id.button);
        email = findViewById(R.id.editTextTextEmailAddress);
        password = findViewById(R.id.editTextTextPassword);

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        if(sharedPreferences.getString("email", null)!=null ){
          Intent intent = new Intent(MainActivity.this, HomePage.class);
          startActivity(intent);
        }

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                final int[] isUserExist = {0};
                final String[] name = {""};
                final String[] id = {""};

                db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if( document.get("email")!=null && email.getText().toString().equals(document.get("email").toString()) && password.getText().toString().equals(document.get("password"))){
                                            isUserExist[0] = 1;
                                            name[0] = document.get("name").toString();
                                            id[0] = task.getResult().getDocuments().get(0).getId();
                                            break;
                                        }
                                    }

                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if( document.get("email")!=null && email.getText().toString().equals(document.get("email").toString()) && !password.getText().toString().equals(document.get("password"))){
                                            isUserExist[0] = -1;
                                            break;
                                        }
                                    }

                                  if(isUserExist[0] == 1){
                                      SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
                                      SharedPreferences.Editor editor = sharedPreferences.edit();
                                      editor.putString("email", email.toString());
                                      editor.putString("name", name[0]);
                                      editor.putString("id",id[0]);
                                      editor.apply();



                                      Intent intent = new Intent(MainActivity.this, HomePage.class);
                                        startActivity(intent);
                                    } else if (isUserExist[0] ==-1){
                                      Toast.makeText(MainActivity.this, "Incorrect Password", Toast.LENGTH_SHORT).show();

                                  } else {
                                        Toast.makeText(MainActivity.this, "Welcome", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity.this, UserName.class);
                                        intent.putExtra("email", email.getText().toString());
                                        intent.putExtra("password", password.getText().toString());
                                        startActivity(intent);
                                    }
                                } else {


                                }
                            }
                        });



            }
        });



    }
}