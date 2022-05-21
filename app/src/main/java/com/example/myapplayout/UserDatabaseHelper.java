package com.example.myapplayout;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.Nullable;

public class UserDatabaseHelper  extends SQLiteOpenHelper {

    private static String preTag = "ASquared";
    private static final String TAG = preTag + "UserDatabaseHelper";

    private static final String TABLE_NAME = "userInfo";
    private static final String COL1 = "USERNAME";
    private static final String COL2 = "DESCRIPTION";
    private static final String COL3 = "JOINDATE";
    private static final String COL4 = "IMAGE";

    private ByteArrayOutputStream objByteArrayOutputStream;
    private byte[] imgInBytes;

    public UserDatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME + ".db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableStatement = "CREATE TABLE " + TABLE_NAME + "(" + COL1 + " TEXT PRIMARY KEY, " +
                COL2 + " TEXT," + COL3 + " TEXT," + COL4 + " BLOB" + ")";
        db.execSQL(createTableStatement);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public boolean insertUserDetails(String username, String bio){
        //Add data to personal Database of my Books
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //Log.d(TAG, "Book Progress: " + pageNo);

        LocalDate myDate = LocalDate.now();
        contentValues.put(COL1, username);
        contentValues.put(COL2, bio);
        contentValues.put(COL3, myDate.toString());

        long result = db.insert(TABLE_NAME, null, contentValues);
        return result != -1;
    }

    public void storeImage(ModelClass objectModelClass){
        SQLiteDatabase db = this.getWritableDatabase();
        Bitmap imgStoreBitmap = objectModelClass.getImage();

        objByteArrayOutputStream = new ByteArrayOutputStream();
        imgStoreBitmap.compress(Bitmap.CompressFormat.JPEG, 100, objByteArrayOutputStream);

        imgInBytes = objByteArrayOutputStream.toByteArray();
        ContentValues objCContentValues = new ContentValues();
        objCContentValues.put(COL4, imgInBytes);

        long result = db.insert(TABLE_NAME, null, objCContentValues);
        if (result != -1){
            Log.d(TAG, "Image stored to table");
        } else {
            Log.d(TAG, "Failed to save image to table");
        }

    }

    public String[] getAll(){
        String username = "Username";
        String desc = "description text............";
        String date = "day, moth year";
        String[] myDets = null;
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataSet = db.rawQuery(queryString, null);
        if (dataSet.moveToFirst()){
            username = dataSet.getString(0);
            desc = dataSet.getString(1);
            date = dataSet.getString(2);
            myDets = new String[] {username, desc, date};
        }// else failure. do not do anything

        dataSet.close();
        db.close();
        return myDets;

    }
    public boolean setDescription(String profileUsername, String profileBio){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, profileBio);

        long result = db.update(TABLE_NAME, contentValues, COL1+"=?", new String[]{profileUsername} );
        return result != -1;

    }

    public boolean deleteNull(){
        String queryString = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getWritableDatabase();
        onUpgrade(db, 0, 1);
        Cursor cursor = db.rawQuery(queryString, null);
        if (cursor.getCount() > 0){
            long result = db.delete(TABLE_NAME, COL1+"=?", new String[] {null});
            Log.d(TAG, "transaction"+result);
            if (result == -1){
                return false;
            }else {
                return true;
            }
        } else {
            return true;
        }

    }

    public void deleteAll(){
        SQLiteDatabase db = this.getWritableDatabase();
        db.execSQL("delete from "+ TABLE_NAME);
    }

    public ModelClass getImage(){
        String queryString = "SELECT " + COL4 + " FROM " + TABLE_NAME;

        ModelClass myImg = null;
        SQLiteDatabase userDB = this.getReadableDatabase();
        Cursor objectCursor = userDB.rawQuery(queryString, null);
        Log.d(TAG, "Got IMG");
        try{
            if(objectCursor.moveToFirst()){
                byte[] imgBytes = objectCursor.getBlob(3);
                Bitmap imgBitmap = BitmapFactory.decodeByteArray(imgBytes,0, imgBytes.length);
                myImg = new ModelClass(imgBitmap);

            } else {
                Log.d(TAG, "There are no pictures un the database");
            }
        } catch (Exception e){
            Log.d(TAG, "Error: "+e);
        }
        objectCursor.close();
        userDB.close();
        return myImg;
    }

}