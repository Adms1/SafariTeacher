package com.adms.safariteacher.Utility;


import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;

import java.util.Map;

import retrofit.Callback;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by admsandroid on 3/20/2018.
 */

public interface WebServices {
    @FormUrlEncoded
    @POST("/Create_Teacher")
    public void getCreateTeacher(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Teacher_Login")
    public void getTeacherLogin(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Check_EmailAddress")
    public void getCheckEmailAddress(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Create_Session")
    public void getCreate_Session(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_Board")
    public void get_Board(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_Lesson")
    public void get_Lesson(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_Region")
    public void get_Region(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_Standard")
    public void get_Standard(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_Stream")
    public void get_Stream(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_SessionDetailByCoachID")
    public void get_SessionDetailByCoachID(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_SessionDetailByCoachIDAndSessionID")
    public void get_SessionDetailBySessionID(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Update_Session")
    public void get_Update_Session(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Create_Family")
    public void get_Create_Family(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Add_FamilyContact")
    public void get_AddFamilyContact(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Get_FamiliyByFamilyID")
    public void get_FamiliyByFamilyID(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Session_ContactEnrollment")
    public void get_Session_ContactEnrollment(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Session_StudentDetail")
    public void get_Session_StudentDetail(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Add_ClassAttendance")
    public void get_Add_ClassAttendance(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Get_ClassTypeList")
    public void get_ClassTypeList(@FieldMap Map<String, String> map, Callback<TeacherInfoModel> callback);

    @FormUrlEncoded
    @POST("/Get_ClassAttendance")
    public void get_ClassAttendance(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);

    @FormUrlEncoded
    @POST("/Get_ContactEnrollmentByCoachID")
    public void get_ContactEnrollmentByCoachID(@FieldMap Map<String, String> map, Callback<SessionDetailModel> callback);
}
