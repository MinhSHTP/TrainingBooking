package com.shtptraining.trainingbooking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Account {
    @SerializedName("NAME")
    @Expose
    private String Name;

    @SerializedName("GENDER")
    @Expose
    private String Gender;

    @SerializedName("BIRTH_YEAR")
    @Expose
    private String BirthYear;

    @SerializedName("EMAIL")
    @Expose
    private String Email;

    @SerializedName("PASSWORD")
    @Expose
    private String Password;

    @SerializedName("ADDRESS")
    @Expose
    private String Address;

    @SerializedName("PHONE")
    @Expose
    private String Phone;

    @SerializedName("ROLE")
    @Expose
    private String Role;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }

    public String getBirthYear() {
        return BirthYear;
    }

    public void setBirthYear(String birthYear) {
        BirthYear = birthYear;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getPhone() {
        return Phone;
    }

    public void setPhone(String phone) {
        Phone = phone;
    }

    public String getRole() {
        return Role;
    }

    public void setRole(String role) {
        Role = role;
    }
}
