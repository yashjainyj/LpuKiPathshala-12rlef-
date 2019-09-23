package com.example.lpukipathshala.DataModels;

public class Chat_Data  {
    private  String sender_id;
    private  String receiver_id;
    private  String show_message;

    public Chat_Data() {
    }

    public Chat_Data(String sender_id) {
        this.sender_id = sender_id;

    }

    public Chat_Data(String sender_id, String receiver_id, String show_message) {
        this.sender_id = sender_id;
        this.receiver_id = receiver_id;
        this.show_message = show_message;

    }
    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(String receiver_id) {
        this.receiver_id = receiver_id;
    }

    public String getShow_message() {
        return show_message;
    }

    public void setShow_message(String show_message) {
        this.show_message = show_message;
    }


}
