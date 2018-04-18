package com.adms.safariteacher.Fragment;

import android.content.Context;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.StudentAttendanceAdapter;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.Session.sessionDataModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentStudentAttendanceBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class StudentAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentStudentAttendanceBinding studentAttendanceBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
    String monthDisplayStr, MonthInt, TimeInt, finaldateStr;
    String[] spiltmonth;
    String[] spilttime;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    StudentAttendanceAdapter studentAttendanceAdapter;
    ArrayList<String> arrayList;
    String sessionIDStr, attendanceIDStr, ContactEnrollmentIDStr = "", noteStr, classTypeIDStr = "", totalstudetnStr, priceStr;//, SesionDetailIDStr, sessionDateStr, sessionTimeStr;
    SessionDetailModel dataResponse;
    TeacherInfoModel classListInfo;
    List<sessionDataModel> studentList;
    HashMap<Integer, String> spinnerClassMap;
    String classType = "";

    public StudentAttendanceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentAttendanceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_attendance, container, false);

        rootView = studentAttendanceBinding.getRoot();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(3, "true");
        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {
        if (!Util.getPref(mContext, "coachTypeID").equalsIgnoreCase("1")) {
            studentAttendanceBinding.firstRowLinear.setVisibility(View.GONE);
        } else {
            studentAttendanceBinding.firstRowLinear.setVisibility(View.VISIBLE);
        }
        callSessionDetailApi();
        callClassAttendanceDetailApi();

    }

    public void setListners() {
        studentAttendanceBinding.sessionCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();


            }
        });
        studentAttendanceBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                InsertAttendanceDetail();
                if (!ContactEnrollmentIDStr.equalsIgnoreCase("") && !classTypeIDStr.equalsIgnoreCase("")) {
                    callGetSessionStudentAttendanceApi();
                } else {
                    Util.ping(mContext, "Select Attendnance.");
                }

            }
        });
        studentAttendanceBinding.classSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String name = studentAttendanceBinding.classSpinner.getSelectedItem().toString();
                String getid = spinnerClassMap.get(studentAttendanceBinding.classSpinner.getSelectedItemPosition());

                Log.d("value", name + " " + getid);
                classTypeIDStr = getid.toString();
                Log.d("classTypeIDStr", classTypeIDStr);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public static boolean isPackageInstalled(String packagename, Context context) {
        PackageManager pm = context.getPackageManager();
        try {
            pm.getPackageInfo(packagename, PackageManager.GET_ACTIVITIES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }

    public void getMonthFun(int month) {
        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
        try {
            monthDisplayStr = monthDisplay.format(monthParse.parse(String.valueOf(month)));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        Log.d("month", "" + monthDisplayStr);
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mDay = dayOfMonth;
        mMonth = monthOfYear + 1;
        mYear = year;
        String d, m, y;
        d = Integer.toString(mDay);
        m = Integer.toString(mMonth);
        y = Integer.toString(mYear);

        if (mDay < 10) {
            d = "0" + d;
        }
        if (mMonth < 10) {
            m = "0" + m;
        }

        MonthInt = d + "/" + m + "/" + y;

        studentAttendanceBinding.dateTxt.setText(MonthInt);
    }

    //Use for Get SessionStudent Detail
    public void callGetSessionStudentDetailApi() {
        if (Util.isNetworkConnected(mContext)) {
//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Session_StudentDetail(getsessionStudentDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionStudentInfo, Response response) {
                    Util.dismissDialog();
                    if (sessionStudentInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionStudentInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionStudentInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        if (sessionStudentInfo.getData() != null) {
                            Util.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (sessionStudentInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        dataResponse = sessionStudentInfo;

                        if (sessionStudentInfo.getData() != null) {
                            studentAttendanceBinding.listLinear.setVisibility(View.VISIBLE);
                            studentAttendanceBinding.headerLinear.setVisibility(View.VISIBLE);
                            studentAttendanceBinding.submitBtn.setVisibility(View.VISIBLE);
                            studentAttendanceBinding.noRecordTxt.setVisibility(View.GONE);


                            studentList = dataResponse.getData();

                            for (int i = 0; i < studentList.size(); i++) {
                                studentList.get(i).setAttendanceID("0");
                                studentList.get(i).setStatus("0");
                                studentList.get(i).setReason("");
                            }
                            totalstudetnStr = String.valueOf(sessionStudentInfo.getData().size());
                            Log.d("totalStudent", totalstudetnStr);
                            studentAttendanceBinding.totalStudentTxt.setText(totalstudetnStr);
                            studentAttendanceAdapter = new StudentAttendanceAdapter(mContext, studentList);
                            studentAttendanceBinding.studentListRcView.setAdapter(studentAttendanceAdapter);
                        } else {
                            studentAttendanceBinding.listLinear.setVisibility(View.GONE);
                            studentAttendanceBinding.headerLinear.setVisibility(View.GONE);
                            studentAttendanceBinding.submitBtn.setVisibility(View.GONE);
                            studentAttendanceBinding.noRecordTxt.setVisibility(View.VISIBLE);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getsessionStudentDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionDetailID", Util.getPref(mContext, "sessionDetailID"));
        return map;
    }


    //Use for Get SessionStudentAttendace Detail
    public void callGetSessionStudentAttendanceApi() {
        if (Util.isNetworkConnected(mContext)) {
            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Add_ClassAttendance(getsessionStudentAttendanceDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel attendanceInfo, Response response) {
                    Util.dismissDialog();
                    if (attendanceInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (attendanceInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (attendanceInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        if (attendanceInfo.getData() != null) {
                            Util.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (attendanceInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (classType.equalsIgnoreCase("")) {
                            Util.ping(mContext, "Attendance Added Successfully.");
                        } else {
                            Util.ping(mContext, "Attendance Updated Successfully.");
                        }
                        studentAttendanceBinding.submitBtn.setText("Update");
                        //callGetSessionStudentAttendanceApi();
                        Fragment fragment = new SessionFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getsessionStudentAttendanceDetail() {
//        InsertAttendanceDetail();
        Map<String, String> map = new HashMap<>();
        map.put("ContactEnrollmentID", ContactEnrollmentIDStr);
        map.put("ClassTypeID", classTypeIDStr);
        return map;
    }

    //Use for SessionDetail
    public void callSessionDetailApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailBySessionID(getSessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionInfo, Response response) {
                    Util.dismissDialog();
                    if (sessionInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (sessionInfo.getData().size() > 0) {
                            dataResponse = sessionInfo;
                            fillSessionData();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getSessionDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Util.getPref(mContext, "coachID"));//coachIdStr
        map.put("SessionID", Util.getPref(mContext, "SessionID"));
        return map;
    }

    public void fillSessionData() {
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            studentAttendanceBinding.boardTxt.setText(dataResponse.getData().get(i).getBoard());
            studentAttendanceBinding.standardTxt.setText(dataResponse.getData().get(i).getStandard() + "-" + dataResponse.getData().get(i).getStream());
            studentAttendanceBinding.subjectTxt.setText(dataResponse.getData().get(i).getLessionTypeName());
            studentAttendanceBinding.sessionNameTxt.setText(dataResponse.getData().get(i).getSessionName());
            studentAttendanceBinding.dateTxt.setText(AppConfiguration.DateStr);
            studentAttendanceBinding.timeTxt.setText(AppConfiguration.TimeStr);

        }
    }

    public void InsertAttendanceDetail() {
//        final ArrayList<String> Attendacestatus = new ArrayList<>();
//        final ArrayList<String> ContactEnrollmentid = new ArrayList<>();
//        final ArrayList<String> Remarks = new ArrayList<>();
//
//        for (int i = 0; i < dataResponse.getData().size(); i++) {
//            ContactEnrollmentid.add(String.valueOf(dataResponse.getData().get(i).getContactEnrollmentID()));
//            Attendacestatus.add(dataResponse.getData().get(i).getCheckboxStatus());
//            Remarks.add(dataResponse.getData().get(i).getRemarks());
//
//        }
//        Log.d("Attendanceid", "" + ContactEnrollmentid);
//        Log.d("Attendacestatus", "" + Attendacestatus);
//
//
//        ContactEnrollmentIDStr = "";
//        for (String s : ContactEnrollmentid) {
//            ContactEnrollmentIDStr = ContactEnrollmentIDStr + "," + s;
//        }
//
//        ContactEnrollmentIDStr = ContactEnrollmentIDStr.substring(1, ContactEnrollmentIDStr.length());
//        Log.d("ContactEnrollmentIDStr", ContactEnrollmentIDStr);
//
//        attendanceIDStr = "";
//        for (String s : Attendacestatus) {
//            attendanceIDStr = attendanceIDStr + "," + s;
//        }
//
//        attendanceIDStr = attendanceIDStr.substring(1, attendanceIDStr.length());
//        Log.d("attendanceIDStr", attendanceIDStr);
//
//        noteStr = "";
//        for (String s : Remarks) {
//            noteStr = noteStr + "," + s;
//        }
//
//        noteStr = noteStr.substring(1, noteStr.length());
//        Log.d("Remarks", noteStr);
        String responseString = "";

        ArrayList<String> newArray = new ArrayList<>();
        for (int i = 0; i < studentAttendanceAdapter.getCount(); i++) {
            sessionDataModel sessionInfoObj = studentAttendanceAdapter.getItem(i);
            int stuId = Integer.parseInt(sessionInfoObj.getContactEnrollmentID());
            boolean isEnable = false;
            String studentString = "";
            String status = sessionInfoObj.getStatus();
            if (status.equalsIgnoreCase("1")) {
                if (sessionInfoObj.getReason().equalsIgnoreCase("null")) {
                    sessionInfoObj.setReason("");
                }
                if (!isEnable) {
                    studentString = String.valueOf(stuId) + "@" + sessionInfoObj.getAttendanceID() + "@" + sessionInfoObj.getReason();
                    isEnable = true;
                } else {
                    studentString = studentString + "|" + String.valueOf(stuId) + "," + sessionInfoObj.getAttendanceID() + "@" + sessionInfoObj.getReason();
                }
            }
            newArray.add(studentString);
        }

        for (String s : newArray) {
            if (!s.equals("")) {
                responseString = responseString + "|" + s;
            }
        }
        if (!responseString.equalsIgnoreCase("")) {
            responseString = responseString.substring(1, responseString.length());
            Log.d("responseString ", responseString);

            ContactEnrollmentIDStr = responseString;
            Log.d("ContactEnrollmentIDStr ", ContactEnrollmentIDStr);
        }
    }

    //Use for ClassTypeDetail
    public void callClassDetailApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_ClassTypeList(getClassDeatil(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel classInfo, Response response) {
                    Util.dismissDialog();
                    if (classInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (classInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (classInfo.getData().size() > 0) {
                            classListInfo = classInfo;
                            fillClassTypeSpinner();
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getClassDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillClassTypeSpinner() {
        ArrayList<Integer> classTypeId = new ArrayList<Integer>();
        for (int i = 0; i < classListInfo.getData().size(); i++) {
            classTypeId.add(Integer.valueOf(classListInfo.getData().get(i).getClassTypeID()));
        }
        ArrayList<String> className = new ArrayList<String>();
        for (int j = 0; j < classListInfo.getData().size(); j++) {
            className.add(classListInfo.getData().get(j).getClassTypeName());
        }

        String[] spinnerclassIdArray = new String[classTypeId.size()];

        spinnerClassMap = new HashMap<Integer, String>();
        for (int i = 0; i < classTypeId.size(); i++) {
            spinnerClassMap.put(i, String.valueOf(classTypeId.get(i)));
            spinnerclassIdArray[i] = className.get(i).trim();
        }
        try {
            Field popup = Spinner.class.getDeclaredField("mPopup");
            popup.setAccessible(true);

            // Get private mPopup member variable and try cast to ListPopupWindow
            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(studentAttendanceBinding.classSpinner);

            popupWindow.setHeight(spinnerclassIdArray.length > 4 ? 500 : spinnerclassIdArray.length * 100);
        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
            // silently fail...
        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnerclassIdArray);
        studentAttendanceBinding.classSpinner.setAdapter(adapterTerm);
        if (!classType.equalsIgnoreCase("")) {
            for (int m = 0; m < spinnerclassIdArray.length; m++) {
                if (classType.equalsIgnoreCase((spinnerclassIdArray[m]))) {
                    Log.d("spinnerValue", spinnerclassIdArray[m]);
                    int index = m;
                    Log.d("indexOf", String.valueOf(index));
                    studentAttendanceBinding.classSpinner.setSelection(m);
                }
            }
        } else {
            String classValue;
            if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
                classValue = "Free Class";
            }else{
                classValue="Regular Class";
            }
            for (int m = 0; m < spinnerclassIdArray.length; m++) {
                    if (classValue.equalsIgnoreCase((spinnerclassIdArray[m]))) {
                        Log.d("spinnerValue", spinnerclassIdArray[m]);
                        int index = m;
                        Log.d("indexOf", String.valueOf(index));
                        studentAttendanceBinding.classSpinner.setSelection(m);
                    }

            }
        }
    }

    //Use for ClassAttendanceDetail
    public void callClassAttendanceDetailApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_ClassAttendance(getClassAttendanceDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel classattendanceInfo, Response response) {
                    Util.dismissDialog();
                    if (classattendanceInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classattendanceInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (classattendanceInfo.getSuccess().equalsIgnoreCase("False")) {
                        Util.dismissDialog();
                        if (classattendanceInfo.getData().size() == 0) {
                            studentAttendanceBinding.submitBtn.setText("SUBMIT");
                            callGetSessionStudentDetailApi();
                            Log.d("false", "hello");
                        }

                        return;
                    }
                    if (classattendanceInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (classattendanceInfo.getData().size() > 0) {

//                            studentAttendanceBinding.submitBtn.setText("UPDATE");
                            dataResponse = classattendanceInfo;
                            classType = dataResponse.getData().get(0).getClassType();
                            if (classType.equalsIgnoreCase("")) {
                                studentAttendanceBinding.submitBtn.setText("SUBMIT");
                            } else {
                                studentAttendanceBinding.submitBtn.setText("UPDATE");
                            }
                            if (classattendanceInfo.getData().get(0).getAttendanceData() != null) {
                                studentAttendanceBinding.listLinear.setVisibility(View.VISIBLE);
                                studentAttendanceBinding.headerLinear.setVisibility(View.VISIBLE);
                                studentAttendanceBinding.submitBtn.setVisibility(View.VISIBLE);
                                studentAttendanceBinding.noRecordTxt.setVisibility(View.GONE);

                                for (int i = 0; i < classattendanceInfo.getData().size(); i++) {
                                    studentList = classattendanceInfo.getData().get(i).getAttendanceData();

                                }
                                for (int i = 0; i < studentList.size(); i++) {
                                    studentList.get(i).setStatus("1");
                                }
                                totalstudetnStr = String.valueOf(classattendanceInfo.getData().get(0).getAttendanceData().size());
                                Log.d("totalStudent", totalstudetnStr);
                                studentAttendanceBinding.totalStudentTxt.setText(totalstudetnStr);
                                studentAttendanceAdapter = new StudentAttendanceAdapter(mContext, studentList);
                                studentAttendanceBinding.studentListRcView.setAdapter(studentAttendanceAdapter);
                            } else {
                                studentAttendanceBinding.listLinear.setVisibility(View.GONE);
                                studentAttendanceBinding.headerLinear.setVisibility(View.GONE);
                                studentAttendanceBinding.submitBtn.setVisibility(View.GONE);
                                studentAttendanceBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            }
                        }
                        callClassDetailApi();
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getClassAttendanceDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionDetailID", Util.getPref(mContext, "sessionDetailID"));
        return map;
    }

}