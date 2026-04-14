package com.nit.nitchattingapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class HomePage extends AppCompatActivity {
    ImageView btnSend;
    EditText user_message;

    RecyclerView recyclerView;

    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btnSend = findViewById(R.id.btnSend);

        user_message = findViewById(R.id.user_message);
        recyclerView = findViewById(R.id.recyclerView);
       toolbar = findViewById(R.id.appbar);


        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        Toast.makeText(this, sharedPreferences.getString("id", null), Toast.LENGTH_SHORT).show();

        ArrayList<MessageModel>messagesList = new ArrayList<>();
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(messagesList,getApplicationContext(), sharedPreferences.getString( "id", null) );


        setSupportActionBar(toolbar);

        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("message");


        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (user_message.getText().toString().isEmpty()){
                        Toast.makeText(HomePage.this, "Please Enter a Message", Toast.LENGTH_SHORT).show();
                    }else{
                        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a", Locale.getDefault());
                        String time = simpleDateFormat.format(new Date());

                        MessageModel messageModel = new MessageModel(sharedPreferences.getString("id", null), sharedPreferences.getString("name", null), user_message.getText().toString(),  time);
                        myRef.push().setValue(messageModel);
                        user_message.setText("");
                    }

            }
        });

        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                messagesList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessageModel messageModel = snapshot.getValue(MessageModel.class);
                    messagesList.add(messageModel);

                }
                adapter.notifyDataSetChanged();
                recyclerView.scrollToPosition(messagesList.size()-1);




            }

            @Override
            public void onCancelled(DatabaseError error) {


            }
        });


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_items, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.settings){
            Intent intent = new Intent(HomePage.this, SeetingsPage.class);
            startActivity(intent);
        }else if(item.getItemId() == R.id.log_out){
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent = new Intent(HomePage.this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
