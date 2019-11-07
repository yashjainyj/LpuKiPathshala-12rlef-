package com.example.lpukipathshala.videoblog;

public class mComments {

    public mComments() {
    }

    String uid;
    String cuserimg;
    String cusername;
    String cuserdate;
    String nuseranswer;
    String cqid;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getCuserimg() {
        return cuserimg;
    }

    public void setCuserimg(String cuserimg) {
        this.cuserimg = cuserimg;
    }

    public String getCusername() {
        return cusername;
    }

    public void setCusername(String cusername) {
        this.cusername = cusername;
    }

    public String getCuserdate() {
        return cuserdate;
    }

    public void setCuserdate(String cuserdate) {
        this.cuserdate = cuserdate;
    }

    public String getNuseranswer() {
        return nuseranswer;
    }

    public void setNuseranswer(String nuseranswer) {
        this.nuseranswer = nuseranswer;
    }

    public String getCqid() {
        return cqid;
    }

    public void setCqid(String cqid) {
        this.cqid = cqid;
    }

    public mComments(String uid, String cuserimg, String cusername, String cuserdate, String nuseranswer, String cqid) {
        this.uid = uid;
        this.cuserimg = cuserimg;
        this.cusername = cusername;
        this.cuserdate = cuserdate;
        this.nuseranswer = nuseranswer;
        this.cqid = cqid;
    }

    public mComments(String uid, String nuseranswer, String cuserdate,String cqid) {
        this.uid = uid;
        this.cuserdate = cuserdate;
        this.nuseranswer = nuseranswer;
        this.cqid = cqid;
    }


}
