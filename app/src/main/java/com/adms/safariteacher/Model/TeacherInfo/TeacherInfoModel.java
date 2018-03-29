package com.adms.safariteacher.Model.TeacherInfo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

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
    @SerializedName("ContactID")
    @Expose
    private String contactID;
    @SerializedName("Data")
    @Expose
    private List<FamilyDetailModel> data = new ArrayList<FamilyDetailModel>();
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

    public String getContactID() {
        return contactID;
    }

    public void setContactID(String contactID) {
        this.contactID = contactID;
    }

    public List<FamilyDetailModel> getData() {
        return data;
    }

    public void setData(List<FamilyDetailModel> data) {
        this.data = data;
    }
}
