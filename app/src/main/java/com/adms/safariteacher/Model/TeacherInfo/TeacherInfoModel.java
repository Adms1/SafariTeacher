package com.adms.safariteacher.Model.TeacherInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by admsandroid on 3/22/2018.
 */

public class TeacherInfoModel {
    @SerializedName("Success")
    @Expose
    private String success;
    @SerializedName("ErrorMessage")
    @Expose
    private String errorMessage;
    @SerializedName("CoachID")
    @Expose
    private String coachID;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getCoachID() {
        return coachID;
    }

    public void setCoachID(String coachID) {
        this.coachID = coachID;
    }
}
