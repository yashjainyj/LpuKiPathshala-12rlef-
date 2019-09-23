package com.example.lpukipathshala.DataModels;

import android.graphics.Bitmap;

public class Add_Book_Model  {
    private
    String userId;
    private String bookId;
    private String bookName;
    private String authorName;
    private String edition;
    private String branch;
    private String description;
    private String price;
    private String picUrl;

    public Add_Book_Model() {}

    public Add_Book_Model(String bookName, String price, String picUrl,String bookId) {
        this.bookName = bookName;
        this.price = price;
        this.picUrl = picUrl;
        this.bookId = bookId;
    }

    public Add_Book_Model(String userId, String bookId, String bookName, String authorName, String edition, String branch, String description, String price,String picUrl) {
        this.userId = userId;
        this.bookId = bookId;
        this.bookName = bookName;
        this.authorName = authorName;
        this.edition = edition;
        this.branch = branch;
        this.description = description;
        this.price = price;
        this.picUrl = picUrl;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getBookId() {
        return bookId;
    }

    public void setBookId(String bookId) {
        this.bookId = bookId;
    }

    public String getBookName() {
        return bookName;
    }

    public void setBookName(String bookName) {
        this.bookName = bookName;
    }

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public String getEdition() {
        return edition;
    }

    public void setEdition(String edition) {
        this.edition = edition;
    }

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() { return price; }

    public void setPrice(String price) { this.price = price; }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
