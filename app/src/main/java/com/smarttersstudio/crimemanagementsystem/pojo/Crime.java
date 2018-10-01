package com.smarttersstudio.crimemanagementsystem.pojo;

public class Crime {
    String date,desc,pin,status,title,uid;

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public Crime() {

    }

    public Crime(String date, String desc, String pin, String status, String title, String uid) {

        this.date = date;
        this.desc = desc;
        this.pin = pin;
        this.status = status;
        this.title = title;
        this.uid = uid;
    }
}
