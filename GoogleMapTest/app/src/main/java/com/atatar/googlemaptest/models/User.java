package com.atatar.googlemaptest.models;

import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class User implements Serializable {

    private String username;
    private String password;
    private String birthDate;

    public User(String username, String password, String birthDate) {
        this.username = username;
        this.password = password;
        this.birthDate = birthDate;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean checkAgeOfMajority(){

        Date birthDate=null;

        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        try {
            birthDate = dateFormat.parse(this.birthDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        Date currentDate = new Date();
        int d1 = birthDate.getYear();
        int d2 = currentDate.getYear();
        int age =  d2- d1;
        if(age < 18)
            return false;

        return true;
    }
}
