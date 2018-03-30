package com.adms.safariteacher.Model;

public class PassSelectedValueModel {

    private String sessionID;
    private String studentAvailable;
    private String sessionDetailID;
    private String sessionDate;
    private String sessionTime;
    private String sessionCapacity;
    private String sessionName;
    private String price;

    public PassSelectedValueModel() {
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSessionName() {
        return sessionName;
    }

    public void setSessionName(String sessionName) {
        this.sessionName = sessionName;
    }

    public String getSessionCapacity() {
        return sessionCapacity;
    }

    public void setSessionCapacity(String sessionCapacity) {
        this.sessionCapacity = sessionCapacity;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getStudentAvailable() {
        return studentAvailable;
    }

    public void setStudentAvailable(String studentAvailable) {
        this.studentAvailable = studentAvailable;
    }

    public String getSessionDetailID() {
        return sessionDetailID;
    }

    public void setSessionDetailID(String sessionDetailID) {
        this.sessionDetailID = sessionDetailID;
    }

    public String getSessionDate() {
        return sessionDate;
    }

    public void setSessionDate(String sessionDate) {
        this.sessionDate = sessionDate;
    }

    public String getSessionTime() {
        return sessionTime;
    }

    public void setSessionTime(String sessionTime) {
        this.sessionTime = sessionTime;
    }
}
