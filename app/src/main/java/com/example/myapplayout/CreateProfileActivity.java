package com.example.myapplayout;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class CreateProfileActivity extends AppCompatActivity {
    private static final String preTag = "ASquared-";
    private static final String TAG = preTag + "CreateProfile";

    private Button createProfileBtn;
    private TextView usernameWelcome;
    private EditText userBio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        Log.d(TAG, "onCreate: started");

        String username = (String) getIntent().getStringExtra("myUsername");
        usernameWelcome = (TextView) findViewById(R.id.profileUsername);
        usernameWelcome.setText("Welcome " + username);
        userBio = (EditText) findViewById(R.id.createUserBio);

        createProfileBtn = (Button) findViewById(R.id.createProfile);
        createProfileBtn.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                Log.d(TAG, "BIO: "+ userBio.getText().toString());
                UserDatabaseHelper db = new UserDatabaseHelper(CreateProfileActivity.this);
                db.insertUserDetails(username, userBio.getText().toString());
                sendUserToNextActivity();
            }
        });

    }

    private void sendUserToNextActivity(){
        Intent intent = new Intent(CreateProfileActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}