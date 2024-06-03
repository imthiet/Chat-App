package com.example.chatapp.Class;

public class User {
    String name,password,fn,dob,interes;
    String UrlImg;


    public User(String name, String password, String fn, String dob, String interes,String urlImg ) {
        this.name = name;
        this.password = password;
        this.fn = fn;
        this.dob = dob;
        this.interes = interes;
        this.UrlImg = urlImg;

    }

    public String getUrlImg() {
        return UrlImg;
    }

    public void setUrlImg(String urlImg) {

        this.UrlImg = UrlImg;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {

        this.name = name;
    }

    public String getPassword() {
        return "Pwd: " +password;
    }

    public void setPassword(String password) {
        this.password =  password;
    }

    public String getFn() {
        return "Full Name: " +fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public String getDob() {
        return"Dob: " +dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getInteres() {
        return "Hobby: " +interes;
    }

    public void setInteres(String interes) {
        this.interes = interes;
    }
}
