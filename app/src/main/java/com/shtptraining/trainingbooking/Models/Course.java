package com.shtptraining.trainingbooking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Course {
    @SerializedName("NAME")
    @Expose
    private String Name;

    @SerializedName("DURATION_DATE")
    @Expose
    private String DurationDate;

    @SerializedName("DURATION_TIME")
    @Expose
    private String DurationTime;

    @SerializedName("DURATION")
    @Expose
    private String Duration;

    @SerializedName("TIME")
    @Expose
    private String Time;

    @SerializedName("DATE")
    @Expose
    private String Date;

    @SerializedName("START_DATE")
    @Expose
    private String StartDate;

    @SerializedName("TRAINER")
    @Expose
    private String Trainer;

    @SerializedName("FEE")
    @Expose
    private String Fee;

    @SerializedName("STATUS")
    @Expose
    private String Status;

    @SerializedName("NUMBEROF")
    @Expose
    private String NumberOf;

    @SerializedName("STATUS_NAME")
    @Expose
    private String StatusName;

    @SerializedName("STATUS_COLOR")
    @Expose
    private String StatusColor;

    public String getName() {
        return Name;
    }

    public String getDurationDate() {
        return DurationDate;
    }

    public String getDurationTime() {
        return DurationTime;
    }

    public String getDuration() {
        return Duration;
    }

    public String getTime() {
        return Time;
    }

    public String getDate() {
        return Date;
    }

    public void setDate(String date) {
        Date = date;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getTrainer() {
        return Trainer;
    }

    public String getFee() {
        return Fee;
    }

    public String getStatus() {
        return Status;
    }

    public String getNumberOf() {
        return NumberOf;
    }

    public String getStatusName() {
        return StatusName;
    }

    public String getStatusColor() {
        return StatusColor;
    }

}
