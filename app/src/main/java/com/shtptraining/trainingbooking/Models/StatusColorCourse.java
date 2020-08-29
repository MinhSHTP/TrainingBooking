package com.shtptraining.trainingbooking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class StatusColorCourse {
    @SerializedName("CODE")
    @Expose
    private String Code;
    @SerializedName("NAME")
    @Expose
    private String Name;

    @SerializedName("COLORSTRING")
    @Expose
    private String ColorString;

    public String getCode() {
        return Code;
    }

    public String getName() {
        return Name;
    }

    public String getColorString() {
        return ColorString;
    }

    public void setCode(String code) {
        Code = code;
    }

    public void setName(String name) {
        Name = name;
    }

    public void setColorString(String colorString) {
        ColorString = colorString;
    }
}
