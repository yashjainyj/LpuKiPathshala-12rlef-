package com.example.lpukipathshala.quoraa;

public class mAnswerGetSet {

    mAnswerGetSet(){

    }
    String uid;
    String quid;
    String aAnswer;
    String aimgurl;

    public String getAnsprofileimage() {
        return ansprofileimage;
    }

    public void setAnsprofileimage(String ansprofileimage) {
        this.ansprofileimage = ansprofileimage;
    }

    public String getAnsusername() {
        return ansusername;
    }

    public void setAnsusername(String ansusername) {
        this.ansusername = ansusername;
    }

    public String getAnsdate() {
        return ansdate;
    }

    public void setAnsdate(String ansdate) {
        this.ansdate = ansdate;
    }

    String ansprofileimage;
    String ansusername;
    String ansdate;

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQuid() {
        return quid;
    }

    public void setQuid(String quid) {
        this.quid = quid;
    }

    public String getaAnswer() {
        return aAnswer;
    }

    public void setaAnswer(String aAnswer) {
        this.aAnswer = aAnswer;
    }

    public String getAimgurl() {
        return aimgurl;
    }

    public void setAimgurl(String aimgurl) {
        this.aimgurl = aimgurl;
    }

    public mAnswerGetSet(String uid, String quid, String aAnswer, String aimgurl, String ansprofileimage, String ansusername, String ansdate) {
        this.uid = uid;
        this.quid = quid;
        this.aAnswer = aAnswer;
        this.aimgurl = aimgurl;
        this.ansprofileimage = ansprofileimage;
        this.ansusername = ansusername;
        this.ansdate = ansdate;
    }

    public mAnswerGetSet(String uid, String quid, String aAnswer, String aimgurl,String ansdate) {
        this.uid = uid;
        this.quid = quid;
        this.aAnswer = aAnswer;
        this.aimgurl = aimgurl;
        this.ansdate = ansdate;
    }




}
