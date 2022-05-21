package com.example.myapplayout;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class BookDetailsActivity extends AppCompatActivity {
    private static final String preTag = "ASquared-";
    private static final String TAG = preTag + "Book-Details";

    TextView titleTV, publisherTV, bookPagesNoTV, bookIdTV, publishDateTV, descriptionIdTV;
    String mBookDetailsID, mBookDetailsTitle, mBookDetailsPublisher, mBookDetailsPublishDate,
            mBookDetailsPageCount, mBookDetailsPagesRead, mBookDetailsDescription;
    ImageView imageTV;
    Button lookUpBtn;
    Button buyBookBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.book_details_activity);
        Log.d(TAG, "onCreate: started");

        //Get Book Details from intent
        mBookDetailsID = (String) getIntent().getStringExtra("ListViewBookID");
        mBookDetailsTitle = (String) getIntent().getStringExtra("ListViewBookTitle");
        mBookDetailsPublisher= (String) getIntent().getStringExtra("ListViewBookPublisher");
        mBookDetailsPublishDate = (String) getIntent().getStringExtra("ListViewBookPublishDate");
        mBookDetailsPagesRead = (String) getIntent().getStringExtra("ListViewBookPagesRead");
        mBookDetailsPageCount = (String) getIntent().getStringExtra("ListViewBookPageCount");
        mBookDetailsDescription = (String) getIntent().getStringExtra("ListViewBookDescription");

        //Find the view of the textviews
        titleTV =(TextView) findViewById(R.id.idTVTitle);
        publisherTV =(TextView) findViewById(R.id.idTVPublisher);
        bookPagesNoTV =(TextView) findViewById(R.id.idTVNoOfPage);
        publishDateTV=(TextView) findViewById(R.id.idTVPublishDate);
        descriptionIdTV= (TextView) findViewById(R.id.idTVDescription);
        bookIdTV=(TextView) findViewById(R.id.idTVBookID);

        //give all the views values
        titleTV.setText(mBookDetailsTitle);
        publisherTV.setText(mBookDetailsPublisher);
        bookPagesNoTV.setText(mBookDetailsPageCount + " Pages");
        publishDateTV.setText(mBookDetailsPublishDate);
        descriptionIdTV.setText(mBookDetailsDescription);
        bookIdTV.setText("ID:" +  mBookDetailsID);
        imageTV = (ImageView) findViewById(R.id.bookDetailsPicture);
        String url = "https://books.google.com/books/content?id=" + mBookDetailsID + "&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        Picasso.get().load(url).into(imageTV);

        //Open Webview showing the book details in more detail
        lookUpBtn = (Button) findViewById(R.id.lookUpBookDetailsBtn);
        lookUpBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailsActivity.this, WebsiteActivity.class);
                intent.putExtra("BookID", mBookDetailsID);
                startActivity(intent);
            }
        });

        //View Book in Google Play Store
        buyBookBtn = (Button) findViewById(R.id.buyBookBtn);
        buyBookBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/books/details?id=" + mBookDetailsID + "&source=gbs_api")));
            }
        });

        ImageButton editMyRecordBtn = (ImageButton) findViewById(R.id.editRecordButton);
        editMyRecordBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(BookDetailsActivity.this, UpdateJournalActivity.class);
                intent.putExtra("UpdateBookID", mBookDetailsID);
                intent.putExtra("UpdateBookTitle", mBookDetailsTitle);
                intent.putExtra("UpdateBookPagesRead", mBookDetailsPagesRead);
                intent.putExtra("UpdateBookPageCount", mBookDetailsPageCount);
                intent.putExtra("UpdateBookPublisher", mBookDetailsPublisher);
                intent.putExtra("UpdateBookPublishDate", mBookDetailsPublishDate);
                startActivity(intent);
            }
        });
    }
}
