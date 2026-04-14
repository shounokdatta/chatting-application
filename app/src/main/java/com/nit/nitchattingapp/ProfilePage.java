package com.nit.nitchattingapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.SetOptions;

import java.util.HashMap;
import java.util.Map;

public class ProfilePage extends AppCompatActivity {

    private static final String TAG = "ProfilePage";

    EditText editUserName, editPassword;
    FirebaseFirestore db;
    DocumentReference userRef;

    private boolean isUpdatingFromFirebase = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_page);

        editUserName = findViewById(R.id.edit_user_name);
        editPassword = findViewById(R.id.edit_password);

        db = FirebaseFirestore.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String userId = sharedPreferences.getString("id", null);

        if (userId == null) {
            Log.d(TAG, "User ID not found in SharedPreferences.");
            Toast.makeText(this, "User not logged in.", Toast.LENGTH_SHORT).show();
            return;
        }

        userRef = db.collection("users").document(userId);
        addTextWatchers();
        addRealtimeUpdateListener();
    }

    private void addTextWatchers() {
        editUserName.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (!isUpdatingFromFirebase) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("name", s.toString());
                    userRef.set(data, SetOptions.merge());
                }
            }
        });

        editPassword.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {}
            @Override public void afterTextChanged(Editable s) {
                if (!isUpdatingFromFirebase) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("password", s.toString());
                    userRef.set(data, SetOptions.merge());
                }
            }
        });
    }

    private void addRealtimeUpdateListener() {
        userRef.addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    return;
                }

                if (snapshot != null && snapshot.exists()) {
                    String name = snapshot.getString("name");
                    String password = snapshot.getString("password");

                    isUpdatingFromFirebase = true;

                    if (name != null && !editUserName.getText().toString().equals(name)) {
                        editUserName.setText(name);
                    }
                    if (password != null && !editPassword.getText().toString().equals(password)) {
                        editPassword.setText(password);
                    }

                    isUpdatingFromFirebase = false;
                } else {
                    Log.d(TAG, "User document does not exist yet. It will be created when the user types.");
                }
            }
        });
    }
}
