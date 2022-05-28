package com.example.myapplayout;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {
    private static final String preTag = "ASquared-";
    private static final String TAG = preTag + "BookAdapter";

    // creating variables for arraylist and context.
    private final ArrayList<MyBook> bookInfoArrayList;
    private final Context mcontext;

    public BookAdapter(ArrayList<MyBook> bookInfoArrayList, Context mcontext) {
        this.bookInfoArrayList = bookInfoArrayList;
        this.mcontext = mcontext;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflating our layout for item of recycler view item.
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_search, parent, false);
        return new BookViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {

        //-----First fill out all the textviews with values
        // inside on bind view holder method we are
        // setting ou data to each UI component.

        MyBook bookInfo = bookInfoArrayList.get(position);
        holder.titleTextView.setText(bookInfo.getBookTitle());
        holder.publisherTextView.setText(bookInfo.getBookPublisher());
        holder.pageCountTextView.setText("No of Pages : " + bookInfo.getPageCount());
        holder.publishDateTextView.setText(bookInfo.getBookPublishDate());
        // below line is use to set image from URL in our image view.
        String url = "https://books.google.com/books/content?id=" + bookInfo.getBookId() + "&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        Picasso.get().load(url).into(holder.bookImageView);
        Log.d("Unknown", "thumbnail: " + url);

        //------Give them an onclick
        // below line is use to add on click listener for our item of recycler view.
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // inside on click listener method we are calling a new activity
                // and passing all the data of that item in next intent.
                Intent i = new Intent(mcontext, BookDetailsActivity.class);
                i.putExtra("ListViewBookID", bookInfo.getBookId());
                i.putExtra("ListViewBookTitle", bookInfo.getBookTitle());
                i.putExtra("ListViewBookPublisher", bookInfo.getBookPublisher());
                i.putExtra("ListViewBookPublishDate", bookInfo.getBookPublishDate());
                i.putExtra("ListViewBookPageCount", String.valueOf(bookInfo.getPageCount()));
                i.putExtra("ListViewBookPagesRead", String.valueOf(bookInfo.getPagesRead()));
                i.putExtra("ListViewBookDescription", bookInfo.getDescription());

                // after passing that data we are
                // starting our new intent.
                mcontext.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        // inside get item count method we
        // are returning the size of our array list.
        return bookInfoArrayList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        // below line is use to initialize
        // our text view and image views.
        TextView titleTextView, publisherTextView, pageCountTextView, publishDateTextView;
        ImageView bookImageView;

        public BookViewHolder(View itemView) {
            super(itemView);
            pageCountTextView = itemView.findViewById(R.id.searchBookPageCount);
            publishDateTextView = itemView.findViewById(R.id.searchBookPublishDate);
            publisherTextView = itemView.findViewById(R.id.searchBookPublisher);
            titleTextView = itemView.findViewById(R.id.searchBookTitle);
            bookImageView = itemView.findViewById(R.id.searchBookImage);
        }
    }
}
