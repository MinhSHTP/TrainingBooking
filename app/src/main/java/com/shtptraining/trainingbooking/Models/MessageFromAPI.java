package com.shtptraining.trainingbooking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class MessageFromAPI implements Serializable {
    @SerializedName("status")
    @Expose
    private String Status;

    @SerializedName("message")
    @Expose
    private String Message;

    @SerializedName("data")
    @Expose
    private String Data;

    public String getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }

    public String getData() {
        return Data;
    }
}
