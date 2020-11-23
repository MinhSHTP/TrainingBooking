package com.shtptraining.trainingbooking.Models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class EvaluationCourse implements Serializable {

    @SerializedName("ID")
    @Expose
    private Integer Id;

    @SerializedName("CONTENT")
    @Expose
    private String Content;


    @SerializedName("TYPE")
    @Expose
    private Integer Type;

    @SerializedName("ID_COURSE")
    @Expose
    private Integer CourseID;

    @SerializedName("ID_EVALUATION_COURSE")
    @Expose
    private Integer EvaluationCourseID;

    @SerializedName("POINT")
    @Expose
    private Integer Point;

    @SerializedName("ISPOINT")
    @Expose
    private Boolean IsPoint;

    @SerializedName("NOTE")
    @Expose
    private String Note;

    @SerializedName("EMAIL")
    @Expose
    private String Email;
}
