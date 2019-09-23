package com.example.lpukipathshala.Cart;

import android.graphics.Bitmap;

public class Cart_Details {
    private String product_name,product_price;private Bitmap image;

    public Cart_Details(String product_name, String product_price) {
        this.product_name = product_name;
        this.product_price = product_price;

    }

    public Cart_Details() {
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_price() {
        return product_price;
    }

    public void setProduct_price(String product_price) {
        this.product_price = product_price;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
