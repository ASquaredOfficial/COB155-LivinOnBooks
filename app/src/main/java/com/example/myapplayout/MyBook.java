package com.example.myapplayout;

public class MyBook {
    private String bookId;
    private String bookTitle;
    private String bookPublisher;
    private String publishDate;
    private int pageCount;
    private String bookStatus;
    private int pagesRead;
    private String description;

    public MyBook(String bookId, String bookTitle, String bookPublisher, String publishDate, int pagesRead,int pageCount, String bookStatus, String description) {
        this.bookId = bookId;
        this.bookTitle = bookTitle;
        this.bookPublisher = bookPublisher;
        this.publishDate = publishDate;
        this.pagesRead = pagesRead;
        this.pageCount = pageCount;
        this.bookStatus = bookStatus;
        this.description = description;
    }

    public String getBookId(){
        return bookId;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getBookPublisher() {
        return bookPublisher;
    }

    public String getBookPublishDate() {
        return publishDate;
    }

    public int getPagesRead(){
        return pagesRead;
    }

    public int getPageCount(){
        return pageCount;
    }

    public String getBookPageCount() {
        return pagesRead+"/"+pageCount;
    }

    public String getBookStatus() {
        return bookStatus;
    }

    public String getDescription(){
        return description;
    }



    public void setBookPublisher(String bookPublisher) {
        this.bookPublisher = bookPublisher;
    }

    public void setPublishDate(String publishDate) {
        this.publishDate = publishDate;
    }

    public void setDescription(String description){
        this.description = description;
    }


    @Override
    public String toString(){
        return "("+this.bookId + ", " + this.bookTitle + ", " + this.bookPublisher + ", " + this.publishDate + ", " + this.pageCount+ ", "+ this.pageCount + ", " + this.bookStatus +")";
    }

    public void setPagesRead(int pageNo){
        this.pagesRead = pageNo;
    }
}
