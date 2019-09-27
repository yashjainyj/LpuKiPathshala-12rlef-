package com.example.lpukipathshala.DataModels;

public class OurProductDetails  {
    String name;
    String price;
    String picUrl;

    public OurProductDetails(String name, String price, String picUrl) {
        this.name = name;
        this.price = price;
        this.picUrl = picUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }
}
