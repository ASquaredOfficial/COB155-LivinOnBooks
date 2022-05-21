package com.example.myapplayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.time.LocalDate;

public class RegistrationActivity extends AppCompatActivity {

    private static String preTag = "ASquared";
    private static final String TAG = preTag + "RegistrationActivity";

    EditText inputUsername, inputPassword, inputConUsername, inputConPassword;
    TextView alreadyHaveAccount;
    Button btnRegister;
    ProgressDialog progDialogReg;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        Log.d(TAG, "onCreate: started");

        inputUsername = (EditText) findViewById(R.id.regInputUsername);
        inputPassword = (EditText) findViewById(R.id.regInputPassword);
        inputConUsername = (EditText) findViewById(R.id.regInputUsernameConfirm);
        inputConPassword = (EditText) findViewById(R.id.regInputPasswordConfirm);
        progDialogReg = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        alreadyHaveAccount = (TextView) findViewById(R.id.haveAccount);
        alreadyHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RegistrationActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        btnRegister = (Button) findViewById(R.id.registerBtn);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                performAuth();
            }
        });
    }

    private void performAuth(){
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();
        String usernameCon = inputConUsername.getText().toString();
        String passwordCon = inputConPassword.getText().toString();

        if (username.length() < 3){
            Toast.makeText(RegistrationActivity.this, "Enter valid username", Toast.LENGTH_SHORT).show();
        } if (!username.equals(usernameCon)){
            Toast.makeText(RegistrationActivity.this, "Usernames must be matching", Toast.LENGTH_SHORT).show();
        } else if (password.isEmpty() || password.length() < 6){
            Toast.makeText(RegistrationActivity.this, "Enter valid password", Toast.LENGTH_SHORT).show();
        } else if (!password.equals(passwordCon)){
            Toast.makeText(RegistrationActivity.this, "Make sure passwords match", Toast.LENGTH_SHORT).show();
        } else {
            String usernameConverted = username + "@lov.com";
            String passwordConverted = password + "@lov.com";
            progDialogReg.setMessage("Please wait for registration process");
            progDialogReg.setTitle("Registration");
            progDialogReg.setCanceledOnTouchOutside(false);
            progDialogReg.show();

            mAuth.createUserWithEmailAndPassword(usernameConverted, passwordConverted).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        progDialogReg.dismiss();
                        Toast.makeText(RegistrationActivity.this, "Registration Successful", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(RegistrationActivity.this, CreateProfileActivity.class);
                        intent.putExtra("myUsername", username);
                        startActivity(intent);
                    } else {
                        progDialogReg.dismiss();
                        Toast.makeText(RegistrationActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
}