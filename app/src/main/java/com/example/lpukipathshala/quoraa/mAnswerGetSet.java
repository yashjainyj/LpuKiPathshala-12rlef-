package com.example.lpukipathshala.quoraa;

public class mAnswerGetSet {

    mAnswerGetSet(){

    }
    String uid;
    String quid;
    String aAnswer;
    String aimgurl;

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

    public mAnswerGetSet(String uid, String quid, String aAnswer, String aimgurl) {
        this.uid = uid;
        this.quid = quid;
        this.aAnswer = aAnswer;
        this.aimgurl = aimgurl;
    }




}
