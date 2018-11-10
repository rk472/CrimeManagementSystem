package com.smarttersstudio.crimemanagementsystem.pojo;

public class Missing {
    String name,age,date,gender,image,pin,status,uid,phone;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
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

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Missing(String name, String age, String date, String gender, String image, String pin, String status, String uid, String phone) {

        this.name = name;
        this.age = age;
        this.date = date;
        this.gender = gender;
        this.image = image;
        this.pin = pin;
        this.status = status;
        this.uid = uid;
        this.phone = phone;
    }

    public Missing() {

    }
}
