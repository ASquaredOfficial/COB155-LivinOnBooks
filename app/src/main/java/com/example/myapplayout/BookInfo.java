package com.example.myapplayout;

import java.util.ArrayList;

public class BookInfo {

    private String ISBN_13;
    //Part of Details
    private String BookTitle;
    private String publisher;
    private String publishedDate;
    private int pageNo;
    private String thumbnail;
    //Rest of Details
    private ArrayList<String> authors;
    private String description;
    private String selfLink;
    private int readSoFar = 0;

    public BookInfo(String ISBN_13, String bookTitle, String publisher, String publishedDate, int pageNo, String thumbnail,
                     String description, String selfLink) {
        this.ISBN_13 = ISBN_13;
        BookTitle = bookTitle;
        this.publisher = publisher;
        this.publishedDate = publishedDate;
        this.pageNo = pageNo;
        this.thumbnail = thumbnail;
        this.description = description;
        this.selfLink = selfLink;
    }

    //Getters
    public String getISBN_13() {
        return ISBN_13;
    } public String getBookTitle() {
        return BookTitle;
    }public String getPublisher() {
        return publisher;
    }public String getPublishedDate() {
        return publishedDate;
    }public int getPageNo() {
        return pageNo;
    }public String getThumbnail() {
        return thumbnail;
    }public ArrayList<String> getAuthors() {
        return authors;
    }public String getDescription() {
        return description;
    }public String getselfLink() {
        return selfLink;
    }

    //Setters
    public void setPagesRead(int pagesRead) {
        this.readSoFar = pagesRead;
    }



}
