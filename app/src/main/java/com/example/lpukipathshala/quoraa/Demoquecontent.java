package com.example.lpukipathshala.quoraa;

public class Demoquecontent {

    String quetype,queque;
    int postimg,userimg;
    String queusername,quedate,queanswer;


    public Demoquecontent(String quetype, String queque, int postimg,int userimg, String queusername, String quedate, String queanswer) {
        this.quetype = quetype;
        this.queque = queque;
        this.postimg = postimg;
        this.queusername = queusername;
        this.quedate = quedate;
        this.queanswer = queanswer;
        this.userimg = userimg;
    }

    public String getQuetype() {
        return quetype;
    }

    public void setQuetype(String quetype) {
        this.quetype = quetype;
    }

    public String getQueque() {
        return queque;
    }

    public void setQueque(String queque) {
        this.queque = queque;
    }

    public int getPostimg() {
        return postimg;
    }

    public void setPostimg(int postimg) {
        this.postimg = postimg;
    }

    public String getQueusername() {
        return queusername;
    }

    public void setQueusername(String queusername) {
        this.queusername = queusername;
    }

    public String getQuedate() {
        return quedate;
    }

    public void setQuedate(String quedate) {
        this.quedate = quedate;
    }

    public String getQueanswer() {
        return queanswer;
    }

    public void setQueanswer(String queanswer) {
        this.queanswer = queanswer;
    }

    public int getUserimg() {
        return userimg;
    }

    public void setUserimg(int userimg) {
        this.userimg = userimg;
    }
}
