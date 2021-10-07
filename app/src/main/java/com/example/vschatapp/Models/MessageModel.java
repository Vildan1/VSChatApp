package com.example.vschatapp.Models;

public class MessageModel {

    String userId, text, time,type;
    Boolean seen;
    String from;

    public  MessageModel()
    {


    }

    public MessageModel(String from, Boolean seen, String text, String time,String type) {

        this.text = text;
        this.time = time;
        this.type = type;
        this.seen = seen;
        this.from=from;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getSeen() {
        return seen;
    }

    public void setSeen(Boolean seen) {
        this.seen = seen;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }
}
