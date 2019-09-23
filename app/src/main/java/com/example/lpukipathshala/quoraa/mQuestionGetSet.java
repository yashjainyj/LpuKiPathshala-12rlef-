package com.example.lpukipathshala.quoraa;

public class mQuestionGetSet {

    private String uid,qtitle,qtype,qimgurl,qdate,q_id;

    mQuestionGetSet(){

    }
    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getQtitle() {
        return qtitle;
    }

    public void setQtitle(String qtitle) {
        this.qtitle = qtitle;
    }

    public String getQdate() {
        return qdate;
    }

    public void setQdate(String qdate) {
        this.qdate = qdate;
    }

    public mQuestionGetSet(String uid, String qtitle, String qtype, String qimgurl, String qdate) {
        this.uid = uid;
        this.qtitle = qtitle;
        this.qtype = qtype;
        this.qimgurl = qimgurl;
        this.qdate = qdate;
    }
    public mQuestionGetSet(String uid, String qtitle, String qtype, String qimgurl, String qdate,String q_id) {
        this.uid = uid;
        this.qtitle = qtitle;
        this.qtype = qtype;
        this.qimgurl = qimgurl;
        this.qdate = qdate;
        this.q_id=q_id;
    }

    public String getQtype() {
        return qtype;
    }

    public void setQtype(String qtype) {
        this.qtype = qtype;
    }

    public String getQimgurl() {
        return qimgurl;
    }

    public void setQimgurl(String qimgurl) {
        this.qimgurl = qimgurl;
    }

    public String getQ_id() {
        return q_id;
    }

    public void setQ_id(String q_id) {
        this.q_id = q_id;
    }
}
