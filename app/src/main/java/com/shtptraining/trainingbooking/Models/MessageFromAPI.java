package com.shtptraining.trainingbooking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class MessageFromAPI {
    @SerializedName("status")
    @Expose
    private String Status;

    @SerializedName("message")
    @Expose
    private String Message;

    public String getStatus() {
        return Status;
    }

    public String getMessage() {
        return Message;
    }
}
