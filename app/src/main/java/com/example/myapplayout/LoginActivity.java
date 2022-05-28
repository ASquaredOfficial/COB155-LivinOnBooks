package com.example.myapplayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private static String preTag = "ASquared";
    private static final String TAG = preTag + "RegistrationActivity";

    SharedPreferences sharedPreferences;
    public static final String fileName = "login";
    public static final String sPrefUserName = "username";
    public static final String sPrefPassword = "password";

    private EditText inputUsername, inputPassword;
    TextView dontHaveAccount;
    Button btnLogin;
    ProgressDialog progDialogReg;

    FirebaseAuth mAuth;
    FirebaseUser mUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Log.d(TAG, "onCreate: started");

        sharedPreferences = getSharedPreferences(fileName, Context.MODE_PRIVATE);
        if (sharedPreferences.contains(sPrefUserName)){
            Intent i = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(i);
            finish();
        }

        inputUsername = (EditText) findViewById(R.id.inputUsernameLogin);
        inputPassword = (EditText) findViewById(R.id.inputPasswordLogin);

        progDialogReg = new ProgressDialog(this);

        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        dontHaveAccount = (TextView) findViewById(R.id.dontHaveAccount);
        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegistrationActivity.class);
                startActivity(intent);
            }
        });

        btnLogin = (Button) findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                performLogin();
            }
        });
    }

    private void performLogin(){
        String username = inputUsername.getText().toString();
        String password = inputPassword.getText().toString();

        username = username + "@lov.com";
        password = password + "@lov.com";
        progDialogReg.setMessage("Please wait for login process");
        progDialogReg.setTitle("login");
        progDialogReg.setCanceledOnTouchOutside(false);
        progDialogReg.show();

        String finalUsername = username;
        String finalPassword = password;
        mAuth.signInWithEmailAndPassword(username, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    progDialogReg.dismiss();
                    sendUserToNextActivity();
                    Toast.makeText(LoginActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();

                    // set shared preferences
                    SharedPreferences.Editor editor  = sharedPreferences.edit();
                    editor.putString(sPrefUserName, finalUsername);
                    editor.putString(sPrefPassword, finalPassword);
                    editor.commit();
                } else {
                    progDialogReg.dismiss();
                    inputUsername.requestFocus();
                    inputUsername.setText("");
                    Toast.makeText(LoginActivity.this, ""+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendUserToNextActivity(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}