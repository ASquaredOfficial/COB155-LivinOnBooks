package com.example.myapplayout;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.InputFilter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class UpdateJournalActivity extends AppCompatActivity {
    private static String preTag = "ASquared-";
    private static final String TAG = preTag + "UpdateJournalActivity";

    private String mUpdateBookID, mUpdateBookTitle, mUpdateBookPageCount,
            mUpdateBookPagesRead, mUpdateBookPublisher, mUpdateBookPublishDate, mUpdateDescription;
    private Button mExitButton, mRemoveButton, mSaveButton;
    private String statusButtonSelected = "";

    private Button mButtonReading, mButtonDropped, mButtonOnHold, mButtonComplete, mButtonPlanning;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_book_details);
        Log.d(TAG, "onCreate: started");

        //Get info from intents
        mUpdateBookTitle = (String) getIntent().getStringExtra("UpdateBookTitle");
        mUpdateBookID = (String) getIntent().getStringExtra("UpdateBookID");
        mUpdateBookPagesRead= (String) getIntent().getStringExtra("UpdateBookPagesRead");
        mUpdateBookPageCount = (String) getIntent().getStringExtra("UpdateBookPageCount");
        mUpdateBookPublisher= (String) getIntent().getStringExtra("UpdateBookPublisher");
        mUpdateBookPublishDate = (String) getIntent().getStringExtra("UpdateBookPublishDate");
        mUpdateDescription = (String) getIntent().getStringExtra("UpdateBookDescription");
        String currentProgress = mUpdateBookPagesRead;
        Log.d(TAG, "Current progress: " + currentProgress);
        if (mUpdateBookPagesRead == "" || mUpdateBookPagesRead == null){
            mUpdateBookPagesRead = "0";
        }if (mUpdateBookPageCount == "" || mUpdateBookPageCount == null){
            mUpdateBookPageCount = "0";
        }

        EditText updateProgress = (EditText) findViewById(R.id.updateBookProgress);
        updateProgress.setFilters(new InputFilter[]{ new UtilFilterMinMax(0 , Integer.parseInt(mUpdateBookPageCount))});
        updateProgress.setText(mUpdateBookPagesRead);

        TextView updateTitle, updateID, updatePageCount;
        updateTitle = (TextView) findViewById(R.id.updateBookTitle);
        updateTitle.setText(mUpdateBookTitle);
        updateID = (TextView) findViewById(R.id.updateBookID);
        updateID.setText("ID: " + mUpdateBookID);
        updatePageCount = (TextView) findViewById(R.id.upBookPageCount);
        updatePageCount.setText("/"+mUpdateBookPageCount);

        //Status Button Functions
        mButtonReading = (Button) findViewById(R.id.updateBookReading);
        mButtonDropped = (Button) findViewById(R.id.updateBookDropped);
        mButtonOnHold = (Button) findViewById(R.id.updateBookOnHold);
        mButtonComplete = (Button) findViewById(R.id.updateBookComplete);
        mButtonPlanning = (Button) findViewById(R.id.updateBookPlanning);
        mButtonReading.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatusButton(mButtonReading);
                statusButtonSelected = "reading";
            }
        });mButtonDropped.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatusButton(mButtonDropped);
                statusButtonSelected = "dropped";
            }
        });mButtonOnHold.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatusButton(mButtonOnHold);
                statusButtonSelected = "onhold";
            }
        });mButtonComplete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatusButton(mButtonComplete);
                statusButtonSelected = "completed";
                updateProgress.setText(mUpdateBookPageCount);
            }
        });mButtonPlanning.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectStatusButton(mButtonPlanning);
                statusButtonSelected = "planning";
                updateProgress.setText("0");
            }
        });

        //Get the status of the book to have previously highlighted button when screen opens
        BookDatabaseHelper databaseHelper = new BookDatabaseHelper(UpdateJournalActivity.this);
        statusButtonSelected = databaseHelper.getBookStatus(mUpdateBookID);
        String preChangeStatus = statusButtonSelected;
        highlightButton(statusButtonSelected);


        //Exit Update Book Page Functionality
        mExitButton = (Button) findViewById(R.id.upBookExit);
        mExitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        //Remove book from personal list
        mRemoveButton = (Button) findViewById(R.id.upBookPageRemove);
        mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDatabaseHelper databaseHelper = new BookDatabaseHelper(UpdateJournalActivity.this);
                List<MyBook> allBooks = databaseHelper.getMyBooks("all");
                Log.d(TAG, "allBooks = " + allBooks.toString());
            }
        });

        //Update PersonalList
        mSaveButton = (Button) findViewById(R.id.updateBookSave);
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BookDatabaseHelper databaseHelper = new BookDatabaseHelper(UpdateJournalActivity.this);
                int pageProgress = Integer.valueOf(updateProgress.getText().toString());

                //If book already in list
                if (databaseHelper.ifBookExists(mUpdateBookID)){
                    //If pages read has remained the same
                    if (currentProgress.equals(updateProgress.getText().toString()) && statusButtonSelected.equals(preChangeStatus)){
                        Toast.makeText(UpdateJournalActivity.this, "You haven't made any changes", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    //If pages read changes, then change page number read
                    else if (!currentProgress.equals(updateProgress.getText().toString())){
                        boolean success = databaseHelper.updateBookProgress(mUpdateBookID, pageProgress);
                        if (success){Toast.makeText(UpdateJournalActivity.this, "Book Updated", Toast.LENGTH_SHORT).show();} 
                        else {
                            Toast.makeText(UpdateJournalActivity.this, "An Error has occured", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //If book status changes then save the new book status
                    if (!statusButtonSelected.equals(preChangeStatus)){
                        databaseHelper.updateBookStatus(mUpdateBookID, statusButtonSelected);
                    }
                }
                //If book isn't in user's list
                else {
                    MyBook newBook;
                    try {
                        newBook = new MyBook(
                                "" + mUpdateBookID,
                                "" + mUpdateBookTitle,
                                "" + mUpdateBookPublisher,
                                "" + mUpdateBookPublishDate,
                                0+pageProgress,
                                0+Integer.parseInt(mUpdateBookPageCount),
                                ""+statusButtonSelected,
                                ""+mUpdateDescription);
                        Toast.makeText(UpdateJournalActivity.this, "Adding Book to MyList", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(UpdateJournalActivity.this, "Error Adding Book", Toast.LENGTH_LONG).show();
                        newBook = new MyBook(mUpdateBookID, "Title", "Error", "0000", 0, 0, "error", "error");
                    }
                    boolean success = databaseHelper.addBookToList(newBook);
                    Toast.makeText(UpdateJournalActivity.this, "Success = " + success, Toast.LENGTH_SHORT).show();
                }
                finish();
            }
        });
    }

    public void selectStatusButton(Button selectedButton){
        mButtonReading.setBackgroundColor(getResources().getColor(R.color.statusButtonBackground));
        mButtonDropped.setBackgroundColor(getResources().getColor(R.color.statusButtonBackground));
        mButtonOnHold.setBackgroundColor(getResources().getColor(R.color.statusButtonBackground));
        mButtonComplete.setBackgroundColor(getResources().getColor(R.color.statusButtonBackground));
        mButtonPlanning.setBackgroundColor(getResources().getColor(R.color.statusButtonBackground));
        selectedButton.setBackgroundColor(getResources().getColor(R.color.purple_500));
    }

    public void highlightButton(String status){
        if (status.equals("")){
            selectStatusButton(mButtonPlanning);
        } else if (status.equals("reading")){
            selectStatusButton(mButtonReading);
        } else if (status.equals("dropped")){
            selectStatusButton(mButtonDropped);
        } else if (status.equals("onhold")){
            selectStatusButton(mButtonOnHold);
        }  else if (status.equals("completed")){
            selectStatusButton(mButtonComplete);
        }  else if (status.equals("planning")){
            selectStatusButton(mButtonPlanning);
        }
    }

}