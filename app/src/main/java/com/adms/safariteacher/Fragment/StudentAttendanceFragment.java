package com.adms.safariteacher.Fragment;

import android.content.Context;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.SessionViewStudentListAdapter;
import com.adms.safariteacher.Adapter.StudentAttendanceAdapter;
import com.adms.safariteacher.Model.PassSelectedValueModel;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.Session.sessionDataModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentStudentAttendanceBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

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
    String sessionIDStr, attendanceIDStr, ContactEnrollmentIDStr, noteStr, classTypeIDStr, totalstudetnStr, priceStr;//, SesionDetailIDStr, sessionDateStr, sessionTimeStr;
    SessionDetailModel dataResponse;
    List<sessionDataModel> studentList;

    public StudentAttendanceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentAttendanceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_attendance, container, false);

        rootView = studentAttendanceBinding.getRoot();
        mContext = getActivity();

        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {
//        MonthInt = Util.getTodaysDate();
//        Log.d("Date", MonthInt);
//        spiltmonth = MonthInt.split("\\/");
//        getMonthFun(Integer.parseInt(spiltmonth[1]));
//
//        TimeInt = Util.getCurrentTime();
//        Log.d("Time", TimeInt);
//        spilttime = TimeInt.split("\\:");
//
//        calendar = Calendar.getInstance();
//        Year = calendar.get(Calendar.YEAR);
//        Month = calendar.get(Calendar.MONTH);
//        Day = calendar.get(Calendar.DAY_OF_MONTH);
//
//        finaldateStr = spiltmonth[0] + ", " + monthDisplayStr + " " + spiltmonth[2];
//        studentAttendanceBinding.dateTxt.setText(Util.getTodaysDate());

        if (!Util.getPref(mContext, "coachTypeID").equalsIgnoreCase("1")) {
            studentAttendanceBinding.firstRowLinear.setVisibility(View.GONE);
        } else {
            studentAttendanceBinding.firstRowLinear.setVisibility(View.VISIBLE);
        }
        callSessionDetailApi();

    }

    public void setListners() {
//        studentAttendanceBinding.dateTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(StudentAttendanceFragment.this, Year, Month, Day);
//                datePickerDialog.setThemeDark(false);
//                datePickerDialog.setOkText("Done");
//                datePickerDialog.showYearPickerFirst(false);
//                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
//                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
//            }
//        });
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

                callGetSessionStudentAttendanceApi();
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
                            studentList = sessionStudentInfo.getData();
                            for (int i = 0; i < studentList.size(); i++) {
                                studentList.get(i).setCheckboxStatus("1");
                            }
                            totalstudetnStr = String.valueOf(sessionStudentInfo.getData().size());
                            Log.d("totalStudent", totalstudetnStr);
                            studentAttendanceBinding.totalStudentTxt.setText(totalstudetnStr);
                            studentAttendanceAdapter = new StudentAttendanceAdapter(mContext, studentList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
                            studentAttendanceBinding.studentListRcView.setLayoutManager(mLayoutManager);
                            studentAttendanceBinding.studentListRcView.setItemAnimator(new DefaultItemAnimator());
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
        map.put("SesionDetailID", Util.getPref(mContext, "sessionDetailID"));
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
        InsertAttendanceDetail();
        Map<String, String> map = new HashMap<>();
        map.put("AttendanceID", attendanceIDStr);
        map.put("ContactEnrollmentID", ContactEnrollmentIDStr);
        map.put("notes", noteStr);
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
                            callGetSessionStudentDetailApi();
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
            studentAttendanceBinding.standardTxt.setText(dataResponse.getData().get(i).getStandard());
            studentAttendanceBinding.subjectTxt.setText(dataResponse.getData().get(i).getLessionTypeName());
            studentAttendanceBinding.sessionNameTxt.setText(dataResponse.getData().get(i).getSessionName());
            studentAttendanceBinding.dateTxt.setText(AppConfiguration.DateStr);
            studentAttendanceBinding.timeTxt.setText(AppConfiguration.TimeStr);
            priceStr = dataResponse.getData().get(i).getSessionAmount();

        }
    }

    public void InsertAttendanceDetail() {
        final ArrayList<String> Attendacestatus = new ArrayList<>();
        final ArrayList<String> ContactEnrollmentid = new ArrayList<>();
        final ArrayList<String> Remarks = new ArrayList<>();

        for (int i = 0; i < dataResponse.getData().size(); i++) {
            ContactEnrollmentid.add(String.valueOf(dataResponse.getData().get(i).getContactEnrollmentID()));
            Attendacestatus.add(dataResponse.getData().get(i).getCheckboxStatus());
            Remarks.add(dataResponse.getData().get(i).getRemarks());

        }
        Log.d("Attendanceid", "" + ContactEnrollmentid);
        Log.d("Attendacestatus", "" + Attendacestatus);


        ContactEnrollmentIDStr = "";
        for (String s : ContactEnrollmentid) {
            ContactEnrollmentIDStr = ContactEnrollmentIDStr + "," + s;
        }

        ContactEnrollmentIDStr = ContactEnrollmentIDStr.substring(1, ContactEnrollmentIDStr.length());
        Log.d("ContactEnrollmentIDStr", ContactEnrollmentIDStr);

        attendanceIDStr = "";
        for (String s : Attendacestatus) {
            attendanceIDStr = attendanceIDStr + "," + s;
        }

        attendanceIDStr = attendanceIDStr.substring(1, attendanceIDStr.length());
        Log.d("attendanceIDStr", attendanceIDStr);

        noteStr = "";
        for (String s : Remarks) {
            noteStr = noteStr + "," + s;
        }

        noteStr = noteStr.substring(1, noteStr.length());
        Log.d("Remarks", noteStr);

        if (priceStr.equalsIgnoreCase("0.0000")) {
            classTypeIDStr = "3";
        } else {
            classTypeIDStr = "1";
        }
    }
}

