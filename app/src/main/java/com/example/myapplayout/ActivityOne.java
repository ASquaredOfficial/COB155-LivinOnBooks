package com.example.myapplayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;



import java.util.ArrayList;

public class ActivityOne extends AppCompatActivity {

    private static String preTag = "ASquared-";
    private static final String TAG = preTag + "ActivityOne";

    private RequestQueue mRequestQueue;
    private ArrayList<MyBook> bookInfoArrayList;

    private ImageButton mSearchButton;
    private EditText mBookSearched;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_one);
        Log.d(TAG, "onCreate: Started");

        //------Change Activity using Bottom Navbar
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);

        //Change navbar highlight
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.ic_mylist:
                        Intent intent0 = new Intent(ActivityOne.this, MainActivity.class);
                        startActivity(intent0);
                        break;
                    case R.id.ic_search:
                        //Not needed, we are already on this page
                        break;
                    case R.id.ic_profile:
                        Intent intent2 = new Intent(ActivityOne.this, ActivityTwo.class);
                        startActivity(intent2);
                        break;
                }
                return false;
            }
        });

        //Search for books function
        mBookSearched = (EditText) findViewById(R.id.searchPageSearchBar);
        mSearchButton = (ImageButton) findViewById(R.id.searchBooksButton);
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking if our edittext field is empty or not.
                if (mBookSearched.getText().toString().isEmpty()) {
                    mBookSearched.setError("Please enter search query");
                    Toast.makeText(ActivityOne.this, "Please enter a query", Toast.LENGTH_SHORT).show();
                    return;
                } else {
                    searchBook(mBookSearched.getText().toString());
                }
            }
        });
    }

    private void searchBook(String query){
        Toast.makeText(ActivityOne.this, "Query searched", Toast.LENGTH_SHORT).show();
        //TODO - Search function
        // creating a new array list.
        bookInfoArrayList = new ArrayList<>();

        // used to initialize the variable for our request queue.
        mRequestQueue = Volley.newRequestQueue(ActivityOne.this);

        // used to clear cache this will be use when our data is being updated.
        mRequestQueue.getCache().clear();

        // below is the url for getting data from API in json format.
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + query;

        // below line we are creating a new request queue.
        RequestQueue queue = Volley.newRequestQueue(ActivityOne.this);


        // below line is use to make json object request inside that we
        // are passing url, get method and getting json object. .
        JsonObjectRequest booksObjrequest = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                // inside on response method we are extracting all our json data.
                try {
                    JSONArray itemsArray = response.getJSONArray("items");
                    for (int i = 0; i < itemsArray.length(); i++) {
                        JSONObject itemsObj = itemsArray.getJSONObject(i);
                        String bookID = itemsObj.optString("id");
                        Log.d(TAG, "Book ID:"+i+" - " + bookID);
                        JSONObject volumeObj = itemsObj.getJSONObject("volumeInfo");
                        String title = volumeObj.optString("title");
                        String publisher = volumeObj.optString("publisher");
                        String publishedDate = volumeObj.optString("publishedDate");
                        String description = volumeObj.optString("description");
                        int pageCount = volumeObj.optInt("pageCount");
                        // after extracting all the data we are
                        // saving this data in our modal class.
                        MyBook bookInfo = new MyBook(bookID, title, publisher, publishedDate, 0, pageCount, "planning", description);
                        // below line is use to pass our modal
                        // class in our array list.
                        bookInfoArrayList.add(bookInfo);

                        // below line is use to pass our
                        // array list in adapter class.
                        BookAdapter adapter = new BookAdapter(bookInfoArrayList, ActivityOne.this);

                        // below line is use to add linear layout
                        // manager for our recycler view.
                        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ActivityOne.this, RecyclerView.VERTICAL, false);
                        RecyclerView mRecyclerView = (RecyclerView) findViewById(R.id.searchedBooks);

                        // in below line we are setting layout manager and
                        // adapter to our recycler view.
                        mRecyclerView.setLayoutManager(linearLayoutManager);
                        mRecyclerView.setAdapter(adapter);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    // displaying a toast message when we get any error from API
                    Toast.makeText(ActivityOne.this, "No Data Found" + e, Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // also displaying error message in toast.
                Toast.makeText(ActivityOne.this, "Error found is " + error, Toast.LENGTH_SHORT).show();
            }
        });
        // at last we are adding our json object
        // request in our request queue.
        queue.add(booksObjrequest);
    }
}
//Changed
