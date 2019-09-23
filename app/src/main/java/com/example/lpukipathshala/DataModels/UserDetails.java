package com.example.lpukipathshala.DataModels;

public class UserDetails {
    private String fname;
    private String lname;
    private String phone;
    private String email;
    private String location;
    private String about;
    private String pic_url;
    public UserDetails(String fname, String lname, String phone, String email, String location, String about, String pic_url) {
        this.fname = fname;
        this.lname = lname;
        this.phone = phone;
        this.email = email;
        this.location = location;
        this.about = about;
        this.pic_url = pic_url;
    }

    public  UserDetails() {}

    public String getFname() {
        return fname;
    }

    public void setFname(String fname) {
        this.fname = fname;
    }

    public String getLname() {
        return lname;
    }

    public void setLname(String lname) {
        this.lname = lname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getPic_url() {
        return pic_url;
    }

    public void setPic_url(String pic_url) {
        this.pic_url = pic_url;
    }
}