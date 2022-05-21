package com.example.myapplayout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import java.util.ArrayList;

public class Tab3Fragment extends Fragment {
    private static String preTag = "ASquared-";
    private static final String TAG = preTag + "ReadingFragment";

    private static final String loadError = "Not Applicable";
    private ArrayList<MyBook> mBookList;

    private ArrayList<MyBook> AllBookLists;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragement2_layout, container, false);
        Log.d(TAG, "onCreate: started");
        mBookList = new ArrayList<>();
        BookDatabaseHelper db = new BookDatabaseHelper(getActivity());
        AllBookLists = (ArrayList<MyBook>) db.getMyBooks("reading");;
        populateBookList(AllBookLists, view);

        //Setting adapter and listview
        MyListBookAdapter adapter = new MyListBookAdapter(getActivity(), R.layout.list_item, mBookList);
        ListView listview = (ListView) view.findViewById(R.id.book_list);
        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                String tempBookID = mBookList.get(position).getBookId();
                String tempBookTitle = mBookList.get(position).getBookTitle();
                String tempBookPublisher = mBookList.get(position).getBookPublisher();
                String tempBookPublishDate = mBookList.get(position).getBookPublishDate();
                String tempBookPagesRead = String.valueOf(mBookList.get(position).getPagesRead());
                String tempBookPageCount = String.valueOf(mBookList.get(position).getPageCount());
                String tempBookDescription = String.valueOf(mBookList.get(position).getDescription());

                Intent intent = new Intent(getActivity(), BookDetailsActivity.class);
                intent.putExtra("ListViewBookID", tempBookID);
                intent.putExtra("ListViewBookTitle", tempBookTitle);
                intent.putExtra("ListViewBookPublisher", tempBookPublisher);
                intent.putExtra("ListViewBookPublishDate", tempBookPublishDate);
                intent.putExtra("ListViewBookPagesRead", tempBookPagesRead);
                intent.putExtra("ListViewBookPageCount", tempBookPageCount);
                intent.putExtra("ListViewBookDescription", tempBookDescription);
                startActivity(intent);
            }
        });
        return view;
    }

    public void populateBookList(ArrayList<MyBook> books, View view){
        for (int i = 0; i < books.size(); i++){
            if (books.get(i).getBookPublisher().equals("") || books.get(i).getBookPublisher().equals(null) || books.get(i).getBookPublisher().equals("null")) {
                books.get(i).setBookPublisher(loadError);
            } if (books.get(i).getBookPublishDate().equals("") || books.get(i).getBookPublishDate().equals(null) || books.get(i).getBookPublishDate().equals("null")) {
                books.get(i).setPublishDate(loadError);
            }if (books.get(i).getDescription().equals("") || books.get(i).getDescription().equals(null) || books.get(i).getDescription().equals("null")) {
                books.get(i).setDescription(loadError);
            }
            MyBook book = new MyBook(books.get(i).getBookId(), books.get(i).getBookTitle(), books.get(i).getBookPublisher(),
                    books.get(i).getBookPublishDate(), books.get(i).getPagesRead(), books.get(i).getPageCount(),
                    books.get(i).getBookStatus(), books.get(i).getDescription());
            mBookList.add(book);
        }if (books.size() == 0){
            RelativeLayout myLayout = view.findViewById(R.id.emptyCompletedList);
            myLayout.setVisibility(View.VISIBLE);
        }
    }
}

