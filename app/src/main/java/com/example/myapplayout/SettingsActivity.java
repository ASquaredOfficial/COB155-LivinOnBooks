package com.example.myapplayout;

import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class SettingsActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    private static String preTag = "ASquared-";
    private static final String TAG = preTag + "SettingsActivity";

    private ImageView mBackButton;
    private Button mLogoutButton;
    private RelativeLayout mChangePassword, mDeleteAccount, mAboutUs;
    private ToggleButton mRemindersToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);
        Log.d(TAG, "onCreate: Started");

        TextView title = (TextView) findViewById(R.id.settingsHeader);
        title.setText("Settings");

        //---Back button Function
        mBackButton = (ImageView) findViewById(R.id.settingsButton);
        mBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent backToProfileIntent = new Intent(SettingsActivity.this, ActivityTwo.class);
                startActivity(backToProfileIntent);
            }
        });

        //---Change Password Function
        mChangePassword = (RelativeLayout) findViewById(R.id.settingsPassword);
        mChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(SettingsActivity.this);
                myDialog.setTitle("Change Password");

                EditText passwordInput = new EditText(SettingsActivity.this);
                passwordInput.setInputType(InputType.TYPE_CLASS_TEXT);
                passwordInput.setHint("Enter New Password");
                passwordInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150)});
                myDialog.setView(passwordInput);

                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //TODO - Change Password functionality
                    }
                });
                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        //---Delete Account Function
        mDeleteAccount = (RelativeLayout) findViewById(R.id.settingsDeleteAccount);
        mDeleteAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(SettingsActivity.this);
                myDialog.setTitle("Are you sure you would like to Delete your account?");

                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SettingsActivity.this, "You have Successfully Deleted you're account", Toast.LENGTH_SHORT).show();
                        //TODO - Make a Delete account functionality
                    }
                });
                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        //---Toggle Reminders Settings
        mRemindersToggle = (ToggleButton) findViewById(R.id.remindersToggle);
        mRemindersToggle.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) { // if The toggle is on
                    Toast.makeText(SettingsActivity.this, "Reminders have been turned on", Toast.LENGTH_SHORT).show();
                    // TODO - Turn on reminders settings
                    DialogFragment timePicker = new TimePickerFragment();
                    timePicker.show(getSupportFragmentManager(), "time picker");

                } else { // The toggle is disabled
                    Toast.makeText(SettingsActivity.this, "Reminders have been turned off", Toast.LENGTH_SHORT).show();
                    mRemindersToggle.setChecked(false);
                    cancelAlarm();
                }
            }
        });

        //---Show About Us Function
        mAboutUs = (RelativeLayout) findViewById(R.id.settingsAboutUs);
        mAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(SettingsActivity.this);
                myDialog.setTitle("About Us");

                TextView aboutUsText = new TextView(SettingsActivity.this);
                aboutUsText.setText("\nLovin' on books was made to allow everyday readers to have a log all all the books they've read in the pasta dn are currently reading. Our goal is to make journal your reading journey as easy as possible");
                myDialog.setView(aboutUsText);

                myDialog.setPositiveButton("Close", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });

        //---Logout Function
        mLogoutButton = (Button) findViewById(R.id.logoutButton);
        mLogoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(SettingsActivity.this);
                myDialog.setTitle("Are you sure you would like to logout?");

                myDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(SettingsActivity.this, "You have Successfully Logged Out", Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(SettingsActivity.this, WelcomeActivity.class);
                        startActivity(intent);
                        finish();
                    }
                });
                myDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
                myDialog.show();
            }
        });
    }


    //------Alarm Functions
    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        Calendar calen = Calendar.getInstance();
        calen.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calen.set(Calendar.MINUTE, minute);
        calen.set(Calendar.SECOND, 0);

        //Set string to new alarm time textview
        TextView alarmText = (TextView) findViewById(R.id.AlarmTime) ;
        String newMinute = "";
        if (minute < 10) {
            newMinute = "0" + String.valueOf(minute);
        } else {
            newMinute = String.valueOf(minute);
        }
        alarmText.setText(hourOfDay+":"+newMinute);
        startAlarm(calen);

    }
    private void startAlarm(Calendar c){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SettingsActivity.this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        if (c.before(Calendar.getInstance())){
            //If alarm was set before current time, add 1 day so it will go off the next day
            c.add(Calendar.DATE,1);
        }
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, c.getTimeInMillis(), pendingIntent);
    }
    private void cancelAlarm(){
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(SettingsActivity.this, AlertReciever.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);

        TextView alarmText = (TextView) findViewById(R.id.AlarmTime) ;
        alarmText.setText("N/A");

        alarmManager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Cancelled", Toast.LENGTH_SHORT).show();
    }


}
