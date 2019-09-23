package com.example.lpukipathshala.product;

class ProductEntry {
    public String title;
    public String price;


    public ProductEntry(String title, String price) {
        this.title = title;
        this.price = price;

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }
}
