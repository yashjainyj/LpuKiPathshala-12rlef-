package com.example.lpukipathshala.quoraa;

public class answercontent {
    String imgurl;
    String textofans;

    public answercontent(String imgurl, String textofans) {
        this.imgurl = imgurl;
        this.textofans = textofans;
    }

    public String getImgurl() {
        return imgurl;
    }

    public void setImgurl(String imgurl) {
        this.imgurl = imgurl;
    }

    public String getTextofans() {
        return textofans;
    }

    public void setTextofans(String textofans) {
        this.textofans = textofans;
    }
}
