package com.example.myapplayout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class MyListBookAdapter extends ArrayAdapter<MyBook> {
    private ArrayList<MyBook> bookList;

    public MyListBookAdapter(@NonNull Context context, int resource, ArrayList<MyBook> bookList) {
        super(context, resource, bookList);
        this.bookList = bookList;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        int bookIndex = position;
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }
        ImageView bookImage = convertView.findViewById(R.id.book_imageView);
        TextView titleTextView = convertView.findViewById(R.id.title_textview);
        TextView publisherTextView = convertView.findViewById(R.id.idListViewPublisher);
        TextView pageCountTextView = convertView.findViewById(R.id.idListViewPageNo);

        String url = "https://books.google.com/books/content?id=" + bookList.get(position).getBookId() + "&printsec=frontcover&img=1&zoom=1&source=gbs_api";
        Picasso.get().load(url).into(bookImage);
        //bookImage.setImageResource(bookList.get(position).getBookImageId());
        titleTextView.setText(bookList.get(position).getBookTitle());
        publisherTextView.setText(bookList.get(position).getBookPublisher() + ", " + bookList.get(position).getBookPublishDate());
        pageCountTextView.setText(bookList.get(position).getBookPageCount());
        return convertView;
    }
}
