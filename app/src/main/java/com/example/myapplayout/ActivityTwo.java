package com.example.myapplayout;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class ActivityTwo extends AppCompatActivity {
    private static final int RESULT_LOAD_IMAGE = 1;

    private static String preTag = "ASquared-";
    private static final String TAG = preTag + "ActivityTwo";

    private static ImageButton mBtnSettings, mBtnImage;
    private ImageView imageViewToUpload;
    private Button mBtnEditBio;
    private Button mBtnUserGuide;
    private String newUsername;
    private String newDescription;

    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_two);
        Log.d(TAG, "onCreate: Started");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("My Notification", "My Notification", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        TextView title = (TextView) findViewById(R.id.profileHeader);
        title.setText("Profile");
        TextView usernameTitle = findViewById(R.id.profileUsernameField);
        TextView userDescription = findViewById(R.id.profileDescription);
        TextView userJoinDate = findViewById(R.id.profileJoinDate);

        //------Change Activity using Bottom Navbar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        //Change navbar highlight
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);
        
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_mylist:
                        Intent intent0 = new Intent(ActivityTwo.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.ic_search:
                        Intent intent1 = new Intent(ActivityTwo.this, ActivityOne.class);
                        startActivity(intent1);
                        break;
                    case R.id.ic_profile:
                        //Not needed, we are already on this page
                        break;
                }
                return false;
            }
        });

        //Get Profile Totals TextViews
        TextView totCompleted = findViewById(R.id.myListCompletedBuffer);
        TextView totReading = findViewById(R.id.myListReadingBuffer);
        TextView totPlanning = findViewById(R.id.myListPlanningToReadBuffer);
        TextView totOnHold = findViewById(R.id.myListOnHoldBuffer);
        TextView totDropped = findViewById(R.id.myListDroppedBuffer);
        TextView totBooks = findViewById(R.id.myListTotalBooksBuffer);

        //Set Totals TextvVews using local db
        BookDatabaseHelper databaseHelper = new BookDatabaseHelper(ActivityTwo.this);
        Log.d(TAG, "hi: "+ databaseHelper.getCount("dropped").getClass().getSimpleName());
        totCompleted.setText(databaseHelper.getCount("completed"));
        totReading.setText(databaseHelper.getCount("reading"));
        totPlanning.setText(databaseHelper.getCount("planning"));
        totOnHold.setText(databaseHelper.getCount("onhold"));
        totDropped.setText(databaseHelper.getCount("dropped"));
        totBooks.setText(databaseHelper.getCount("all"));

        //Check if Details in local database, then update page;
        UserDatabaseHelper uDatabaseHelper = new UserDatabaseHelper(ActivityTwo.this);
        String[] myDetailsList = uDatabaseHelper.getAll();
        LocalDate myDateObj = LocalDate.parse(myDetailsList[2]);
        System.out.println("Before Formatting: " + myDateObj);
        DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        String newDate = myDateObj.format(myFormatObj);
        usernameTitle.setText(myDetailsList[0]);
        userDescription.setText(myDetailsList[1]);
        userJoinDate.setText("Join Date : " + newDate);

        //------Change activity to Settings page
        mBtnSettings = (ImageButton) findViewById(R.id.settingsButton);
        mBtnSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentSettings = new Intent(ActivityTwo.this, SettingsActivity.class);
                startActivity(intentSettings);
            }
        });

        //Change Profile Image
        imageViewToUpload = (ImageView) findViewById(R.id.profile_image);
        mBtnImage = (ImageButton) findViewById(R.id.uploadNewPic);
        mBtnImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                chooseImage(view);
            }
        });

        //------Edit Bio Pop-up
        mBtnEditBio = (Button) findViewById(R.id.editBioButton);
        mBtnEditBio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder myDialog = new AlertDialog.Builder(ActivityTwo.this);
                myDialog.setTitle("Update Bio");

                TextView currentDescription = (TextView) findViewById(R.id.profileDescription);
                EditText descriptionInput = new EditText(ActivityTwo.this);
                descriptionInput.setInputType(InputType.TYPE_CLASS_TEXT);
                descriptionInput.setHint("Enter New Description");
                descriptionInput.setFilters(new InputFilter[] { new InputFilter.LengthFilter(150)});
                myDialog.setView(descriptionInput);

                myDialog.setPositiveButton("Save", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        newDescription = descriptionInput.getText().toString();
                        currentDescription.setText(newDescription);
                        uDatabaseHelper.setDescription(usernameTitle.getText().toString(), newDescription);

                        NotificationCompat.Builder builder = new NotificationCompat.Builder(ActivityTwo.this, "My Notification");
                        builder.setContentTitle("Bio edited");
                        builder.setContentText("Bio has been Edited Successfully");
                        builder.setSmallIcon(R.drawable.ic_user_guide);
                        builder.setAutoCancel(true);

                        NotificationManagerCompat managerCompat = NotificationManagerCompat.from(ActivityTwo.this);
                        managerCompat.notify(1, builder.build());
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

        //------open User Guide
        mBtnUserGuide = (Button) findViewById(R.id.userGuideBtn);
        mBtnUserGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ActivityTwo.this, UserGuideActivity.class);
                startActivity(intent);
            }
        });
    }

    //Request a new profile picture functionality
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        try {
            super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data.getData() != null){
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(), imageFilePath);
                imageViewToUpload.setImageBitmap(imageToStore);
                UserDatabaseHelper uDatabaseHelper = new UserDatabaseHelper(ActivityTwo.this);
                uDatabaseHelper.storeImage(new ModelClass(imageToStore));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void chooseImage(View objectView){
        Intent objIntent = new Intent();
        objIntent.setType("image/*");

        objIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objIntent, PICK_IMAGE_REQUEST);
    }

}
//Changed
