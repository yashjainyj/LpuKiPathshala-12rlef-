package com.example.lpukipathshala.videoblog;

public class mAnswerVideoGetSet {

    public mAnswerVideoGetSet() {
    }

    String uid;
    String quid;
    String aAnswer;
    String avideourl;
    String ansprofileimage;

    public String getAuid() {
        return auid;
    }

    public void setAuid(String auid) {
        this.auid = auid;
    }

    String auid;

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

    public String getAvideourl() {
        return avideourl;
    }

    public void setAvideourl(String avideourl) {
        this.avideourl = avideourl;
    }

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

    String ansusername;
    String ansdate;

    public mAnswerVideoGetSet(String uid, String quid, String aAnswer, String avideourl, String ansprofileimage, String ansusername, String ansdate ,String auid) {
        this.uid = uid;
        this.quid = quid;
        this.aAnswer = aAnswer;
        this.avideourl = avideourl;
        this.ansprofileimage = ansprofileimage;
        this.ansusername = ansusername;
        this.ansdate = ansdate;


    }

    public mAnswerVideoGetSet(String uid, String quid, String aAnswer, String avideourl, String ansdate) {
        this.uid = uid;
        this.quid = quid;
        this.aAnswer = aAnswer;
        this.avideourl = avideourl;
        this.ansdate = ansdate;

    }

    public mAnswerVideoGetSet(String uid, String quid, String aAnswer, String avideourl, String ansdate, String auid) {
        this.uid = uid;
        this.quid = quid;
        this.aAnswer = aAnswer;
        this.avideourl = avideourl;
        this.ansdate = ansdate;
        this.auid = auid;
    }
}
