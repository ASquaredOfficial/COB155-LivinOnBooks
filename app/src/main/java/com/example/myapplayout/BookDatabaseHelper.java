package com.example.myapplayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class BookDatabaseHelper extends SQLiteOpenHelper {

    private static final String preTag = "ASquared";
    private static final String TAG = preTag + "BookDbHelper";

    private static final String TABLE_NAME = "myUserDetails";
    private static final String COL1 = "ID";

    private static final String COL2 = "TITLE";
    private static final String COL3 = "PUBLISHER";
    private static final String COL4 = "PUBLISHDATE";
    private static final String COL5 = "PAGECOUNT";
    private static final String COL6 = "PAGESREAD";
    private static final String COL7 = "STATUS";
    private static final String COL8 = "DESCRIPTION";

    public BookDatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db){
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " TEXT PRIMARY KEY, " +
                COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " TEXT," + COL5 + " INTEGER," + COL6 + " INTEGER," + COL7 + " INTEGER, " + COL8 + " TEXT "+ ")";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1){
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public boolean addBookToList(MyBook item) {
        //Add data to personal Database of my Books
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, item.getBookId());
        contentValues.put(COL2, item.getBookTitle());
        contentValues.put(COL3, item.getBookPublisher());
        contentValues.put(COL4, item.getBookPublishDate());
        contentValues.put(COL5, item.getPageCount());
        contentValues.put(COL6, item.getPagesRead());
        contentValues.put(COL7, item.getBookStatus());
        contentValues.put(COL8, item.getDescription());
        Log.d(TAG, "Book Added: " + item);

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public boolean updateBookProgress(String bookID, int pageNo){
        //Add data to personal Database of my Books
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL6, pageNo);

        long result = db.update(TABLE_NAME, contentValues, COL1+"=?", new String[]{bookID} );
        return result != -1;
    }

    public void updateBookStatus(String bookID, String bookStatus){
        //Update the status of specific book in mylist
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Log.d(TAG, "New Book Progress: " + bookStatus);
        contentValues.put(COL7, bookStatus);

        long result = db.update(TABLE_NAME, contentValues, COL1+"=?", new String[]{bookID});
    }

    public List<MyBook> getMyBooks(String statusType){
        List<MyBook> returnList = new ArrayList<>();

        String queryString = "SELECT * FROM " + TABLE_NAME;
        if (!statusType.equals("all")){
            //if a specific type
            queryString = queryString + " WHERE " + COL7 + " = '" + statusType + "'";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataSet = db.rawQuery(queryString, null);
        if (dataSet.moveToFirst()){
            // If there are any results
            do {
                String bookID = dataSet.getString(0);
                String bookName = dataSet.getString(1);
                String bookPublisher = dataSet.getString(2);
                String bookPublishDate = dataSet.getString(3);
                int bookPageCount  = dataSet.getInt(4);
                int bookPagesRead  = dataSet.getInt(5);
                String bookStatus = dataSet.getString(6);
                String bookDescription = dataSet.getString(7);
                MyBook newBook = new MyBook(bookID, bookName, bookPublisher, bookPublishDate,
                        bookPagesRead, bookPageCount, bookStatus, bookDescription);
                Log.d(TAG, "book:"+ newBook);
                returnList.add(newBook);
            } while (dataSet.moveToNext());
        }
        // else failure. do not do anything

        dataSet.close();
        db.close();
        return returnList;
    }

    public String getBookStatus(String bookID){

        String queryString = "SELECT "+ COL7 +" FROM " + TABLE_NAME + " WHERE " + COL1 + " = '" + bookID + "'";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataRow = db.rawQuery(queryString, null);
        String bookStatus;
        if (dataRow.moveToFirst()){
            bookStatus = dataRow.getString(0).trim();
            Log.d(TAG, "Status: " + bookStatus);
        } else {
            Log.d(TAG, "Book Does not exist");
            bookStatus = "planning";
        }
        db.close();
        dataRow.close();
        return bookStatus;
    }

    public String getCount(String statusType){
        String queryString = "SELECT COUNT(" + COL1 + ") FROM " + TABLE_NAME;
        if (!statusType.equals("all")){
            //if a specific type, then narrow by status type
            queryString = queryString + " WHERE " + COL7 + " = '"+statusType+"'";
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String total = "";
        Cursor dataRow = db.rawQuery(queryString, null);

        if (dataRow.moveToFirst()){
            total = dataRow.getString(0).trim();
        } else {
            total = "0";
        }
        dataRow.close();
        db.close();
        return total;
    }

    public boolean ifBookExists(String bookId){
        String queryString = "SELECT COUNT("+COL1+") FROM " + TABLE_NAME + " WHERE " + COL1 + " = '"+bookId+"'";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataRow = db.rawQuery(queryString, null);

        boolean exists = false;
        if (dataRow.moveToFirst()){
            if (!dataRow.getString(0).trim().equals("0")){
                exists = true;
            }  //Do nothing since variable is already false

        }
        dataRow.close();
        db.close();
        return exists;
    }


}