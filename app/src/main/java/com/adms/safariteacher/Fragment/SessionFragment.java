package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Activities.DrawableCalendarEvent;
import com.adms.safariteacher.Adapter.SessionViewStudentListAdapter;
import com.adms.safariteacher.Interface.onViewClick;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.Session.sessionDataModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentCalendarBinding;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class SessionFragment extends Fragment implements CalendarPickerController {

    FragmentCalendarBinding calendarBinding;
    private View rootView;
    public static SessionFragment fragment;
    private Context mContext;
    public Dialog sessionDialog;
    Button cancel_btn, add_attendance_btn, edit_session_btn, add_student_btn;
    String sessionnameStr, sessionstrattimeStr = "", sessionendtimeStr = "", sessionDateStr = "", sessionIDStr, sessionDetailIDStr, priceStr;
    TextView start_time_txt, end_time_txt, session_title_txt, date_txt, total_spot_txt, spot_left_txt, no_record_txt;
    RecyclerView studentnamelist_rcView;
    LinearLayout list_linear;
    SessionViewStudentListAdapter sessionViewStudentListAdapter;
    List<sessionDataModel> arrayList;
    SessionDetailModel finalsessionfullDetailModel;
    List<CalendarEvent> eventList = new ArrayList<>();
    ArrayList<Integer> colorList = new ArrayList<>();
    int sessionCapacity, arraySize, studentAvailability;
    ArrayList<sessionDataModel> StudentList;
    String Address;
    int SessionHour = 0;
    Integer SessionMinit = 0;
    String flag;
    Calendar calendar;
    String dateStr;
    int k;

    //Use for Confirmation Dialog
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_student_txt_view, session_name_txt, location_txt, duration_txt, time_txt, session_fee_txt;
    String contatIDstr, orderIDStr,familyNameStr;
    ArrayList<String> selectedId;

    public SessionFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (rootView == null) {
            calendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);

            rootView = calendarBinding.getRoot();
            getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setRetainInstance(true);
            mContext = getActivity();
            ((DashBoardActivity) getActivity()).setActionBar(0, "true");
            colorList.add(getResources().getColor(R.color.yellow_dark));
            colorList.add(getResources().getColor(R.color.green_dark));
            colorList.add(getResources().getColor(R.color.blue_dark));

            callGetSessionDetailApi();

        } else {

        }

        setListner();
        return rootView;
    }

    public void init() {
        if (Util.checkAndRequestPermissions(mContext)) {
        }
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
        maxDate.add(Calendar.YEAR, 1);

        mockList(eventList);
        calendarBinding.agendaCalendarView.init(eventList, minDate, maxDate, Locale.US, this);
    }

    public void setListner() {
        calendarBinding.addEventFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                Fragment fragment = new AddSessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("flag", "Add");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        calendarBinding.forwadTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();

                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
                String d = calendarBinding.monthYearTxt.getText().toString();
                Date date = null;
                try {
                    date = new Date(sdf.parse(d).getTime());
                    date.setMonth(date.getMonth() + 1);
                    dateStr = String.valueOf(date);
                    Log.d("dateStr", dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
                String outputPattern = "MMMM yyyy";

                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = null;
                String str = null;
                try {
                    date1 = inputFormat.parse(dateStr);
                    str = outputFormat.format(date1);

                    Log.i("mini", "Month:" + str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendarBinding.monthYearTxt.setText(str);
            }
        });

        calendarBinding.backTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SimpleDateFormat sdf = new SimpleDateFormat("MMMM yyyy");
                String d = calendarBinding.monthYearTxt.getText().toString();
                Date date = null;
                try {
                    date = new Date(sdf.parse(d).getTime());
                    date.setMonth(date.getMonth() - 1);
                    dateStr = String.valueOf(date);
                    Log.d("dateStr", dateStr);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
                String outputPattern = "MMMM yyyy";

                SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                Date date1 = null;
                String str = null;
                try {
                    date1 = inputFormat.parse(dateStr);
                    str = outputFormat.format(date1);

                    Log.i("mini", "Month:" + str);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calendarBinding.monthYearTxt.setText(str);
            }
        });
    }

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("CurrentDay", "" + dayItem);
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        if (!event.getTitle().equals("No events")) {
            parseTodaysDate(String.valueOf(event.getStartTime().getTime()), String.valueOf(event.getEndTime().getTime()));

            sessionnameStr = event.getTitle();
            sessionDetailIDStr = String.valueOf(event.getId());
            Log.d("sessionDetailIDStr", sessionDetailIDStr);
            Util.setPref(mContext, "sessionDetailID", sessionDetailIDStr);

            for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {
                for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {
                    if (sessionDetailIDStr.equalsIgnoreCase(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDetailID())) {
                        sessionIDStr = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionID();
                        sessionCapacity = Integer.parseInt(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionCapacity());
                        priceStr = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionPrice();
                        AppConfiguration.SessionLocation = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressLine1()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressCity()
                                + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressState()
                                + "- " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressZipCode();
                        String[] spiltTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                        calculateHours(spiltTime[0], spiltTime[1]);
                        AppConfiguration.SessionName = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName();
                        AppConfiguration.SessionDuration = "( " + SessionHour + "hr" + ", " + SessionMinit + "min )";
                        AppConfiguration.SessionTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime();
                        AppConfiguration.SessionPrice = String.valueOf(Math.round(Float.parseFloat(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionPrice())));
                        Util.setPref(mContext, "SessionID", sessionIDStr);
                    }

                }
            }


            SessionDialog();
        } else {
//            Util.ping(mContext, "No Event Available...");
        }

    }

    @Override
    public void onScrollToDate(Calendar calendar) {
        Log.d("month", String.valueOf(calendar.getTime()));
        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
        String outputPattern = "MMMM yyyy";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;
        try {
            date = inputFormat.parse(String.valueOf(calendar.getTime()));
            str = outputFormat.format(date);

            Log.i("mini", "Month:" + str);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        calendarBinding.monthYearTxt.setText(str);
    }

    public String parseTodaysDate(String Starttime, String Endtime) {
        String inputPattern = "EEE MMM d HH:mm:ss zzz yyyy";
        String outputPattern = "dd-MM-yyyy";
        String outputTimePattern = "hh:mm a";

        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);
        SimpleDateFormat outputFormat1 = new SimpleDateFormat(outputTimePattern);

        Date date = null, startdateTime = null, enddateTime = null;
        String str = null, StartTimeStr = null, EndTimeStr = null;

        try {
            date = inputFormat.parse(Starttime);
            str = outputFormat.format(date);

            startdateTime = inputFormat.parse(Starttime);
            StartTimeStr = outputFormat1.format(startdateTime);

            enddateTime = inputFormat.parse(Endtime);
            EndTimeStr = outputFormat1.format(enddateTime);

            sessionDateStr = str;
            sessionstrattimeStr = StartTimeStr;
            sessionendtimeStr = EndTimeStr;
            Log.i("mini", "Converted Date Today:" + StartTimeStr + "=" + EndTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }

    public void SessionDialog() {
        sessionDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = sessionDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        sessionDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        sessionDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        sessionDialog.setCancelable(false);
        sessionDialog.setContentView(R.layout.dialog_view_session);
        sessionDialog.show();

        cancel_btn = (Button) sessionDialog.findViewById(R.id.cancel_btn);
        add_attendance_btn = (Button) sessionDialog.findViewById(R.id.add_attendance_btn);
        edit_session_btn = (Button) sessionDialog.findViewById(R.id.edit_session_btn);
        session_title_txt = (TextView) sessionDialog.findViewById(R.id.session_title_txt);
        start_time_txt = (TextView) sessionDialog.findViewById(R.id.start_time_txt);
        end_time_txt = (TextView) sessionDialog.findViewById(R.id.end_time_txt);
        date_txt = (TextView) sessionDialog.findViewById(R.id.date_txt);
        studentnamelist_rcView = (RecyclerView) sessionDialog.findViewById(R.id.student_name_list_rcView);
        list_linear = (LinearLayout) sessionDialog.findViewById(R.id.list_linear);
        add_student_btn = (Button) sessionDialog.findViewById(R.id.add_student_btn);
        total_spot_txt = (TextView) sessionDialog.findViewById(R.id.total_spot_txt);
        spot_left_txt = (TextView) sessionDialog.findViewById(R.id.spot_left_txt);
        no_record_txt = (TextView) sessionDialog.findViewById(R.id.no_record_txt);

        callGetSessionStudentDetailApi();

        date_txt.setText(sessionDateStr);
        session_title_txt.setText(sessionnameStr);
        start_time_txt.setText(sessionstrattimeStr);
        end_time_txt.setText(sessionendtimeStr);


        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionDialog.dismiss();
            }
        });

        edit_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                Fragment fragment = new AddSessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("flag", flag);
                args.putString("sessionID", sessionIDStr);
                args.putString("studentAvailable", String.valueOf(arraySize));
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });

        add_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                AppConfiguration.DateStr = date_txt.getText().toString();
                AppConfiguration.TimeStr = start_time_txt.getText().toString() + "-" + end_time_txt.getText().toString();
                Fragment fragment = new StudentAttendanceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "2");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });

        add_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DashBoardActivity.navItemIndex = 1;
                Fragment fragment = new OldFamilyListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "13");
                args.putString("sessionID", sessionIDStr);
                fragmentTransaction.replace(R.id.frame, fragment);
//                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });
    }


    //Use for Get AllSession Detail
    public void callGetSessionDetailApi() {
        if (Util.isNetworkConnected(mContext)) {
            Util.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailByCoachID(getsessionDetail(), new retrofit.Callback<SessionDetailModel>() {
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
                        if (sessionInfo.getData() != null) {
                            Util.ping(mContext, getString(R.string.false_msg));
                        }
                        return;
                    }
                    if (sessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (sessionInfo.getData() != null) {
                            finalsessionfullDetailModel = sessionInfo;

                            Log.d("DataModel", "" + finalsessionfullDetailModel.getData().size());
                            init();
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

    private Map<String, String> getsessionDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Util.getPref(mContext, "coachID"));//Util.getPref(mContext, "coachID"));
        return map;
    }

    public void mockList(List<CalendarEvent> eventList) {
        long startDate = 0, endDate = 0;

        for (int i = 0; i < finalsessionfullDetailModel.getData().size(); i++) {


            for (int j = 0; j < finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().size(); j++) {

                String[] spiltTime = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime().split("\\-");
                try {
                    String dateString = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate() + " " + spiltTime[0];
                    String enddateString = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDate() + " " + spiltTime[1];
                    Log.d("StartDate and EndDate :", dateString + " " + enddateString);
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                    SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy hh:mm a", Locale.getDefault());
                    Date date = sdf.parse(dateString);
                    Date date1 = sdf1.parse(enddateString);

                    startDate = date.getTime();
                    endDate = date1.getTime();
                    Log.d("FirstTime", "first event :" + startDate + endDate);


                } catch (ParseException e) {
                    e.printStackTrace();
                }
                calculateHours(spiltTime[0], spiltTime[1]);
                if (k == 2) {
                    k = 0;
                } else {
                    k++;
                }

                Address = finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressLine1()
                        + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName()
                        + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressCity()
                        + ", " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressState()
                        + "- " + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getAddressZipCode();
                DrawableCalendarEvent event = new DrawableCalendarEvent(Integer.parseInt(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionDetailID()),
                        colorList.get(k), finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName(),
                        finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName(),
                        finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime()
                                + " " + "( " + SessionHour + " hr" + ", " + SessionMinit + " min )" + System.getProperty("line.separator")
                                + Address, startDate, endDate, 0, String.valueOf(SessionHour), R.drawable.email);
                eventList.add(event);

            }
        }
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
                        arrayList = sessionStudentInfo.getData();
                        StudentList = new ArrayList<sessionDataModel>();
                        arraySize = arrayList.size();
                        studentAvailability = sessionCapacity - arraySize;
                        Log.d("capacity", "" + sessionCapacity + "arraySize :" + arraySize + "studentAvailability :" + studentAvailability);
                        if (sessionStudentInfo.getData().size() > 0) {
                            list_linear.setVisibility(View.VISIBLE);
                            no_record_txt.setVisibility(View.GONE);
                            for (int i = 0; i < arrayList.size(); i++) {
                                StudentList.add(arrayList.get(i));
                            }
                            Log.d("StudentList", "" + StudentList);
                            sessionViewStudentListAdapter = new SessionViewStudentListAdapter(mContext, StudentList, new onViewClick() {
                                @Override
                                public void getViewClick() {
                                    getFamilyID();
                                    ConformationDialog();

                                }
                            });
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            studentnamelist_rcView.setLayoutManager(mLayoutManager);
                            studentnamelist_rcView.setItemAnimator(new DefaultItemAnimator());
                            studentnamelist_rcView.setAdapter(sessionViewStudentListAdapter);

                        } else {
                            list_linear.setVisibility(View.GONE);
                            no_record_txt.setVisibility(View.VISIBLE);
                        }
                        total_spot_txt.setText(String.valueOf(sessionCapacity));
                        spot_left_txt.setText(String.valueOf(studentAvailability));
                        if (arraySize > 0) {
                            add_attendance_btn.setEnabled(true);
                            add_attendance_btn.setAlpha(1);
                        } else {
                            add_attendance_btn.setEnabled(false);
                            add_attendance_btn.setAlpha(0.5f);
                        }
                        if (studentAvailability > 0) {
                            add_student_btn.setEnabled(true);
                            add_student_btn.setAlpha(1);
                        } else {
                            add_student_btn.setEnabled(false);
                            add_student_btn.setAlpha(0.5f);
                        }
                        if (arraySize == 0) {
                            edit_session_btn.setText("Edit Session");
                            flag = "edit";
                        } else {
                            edit_session_btn.setText("View Session");
                            flag = "view";
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
        map.put("SessionDetailID", sessionDetailIDStr);
        return map;
    }

    public void calculateHours(String time1, String time2) {
        Date date1, date2;
        int days, hours, min;
        String hourstr, minstr;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("hh:mm a");
        try {
            date1 = simpleDateFormat.parse(time1);
            date2 = simpleDateFormat.parse(time2);

            long difference = date2.getTime() - date1.getTime();
            days = (int) (difference / (1000 * 60 * 60 * 24));
            hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
            min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
            hours = (hours < 0 ? -hours : hours);
            SessionHour = hours;

            SessionMinit = min;

            Log.i("======= Hours", " :: " + hours + ":" + min);

        } catch (ParseException e) {
            e.printStackTrace();
        }


    }

    public void ConformationDialog() {
        confimDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.10f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(R.layout.confirm_session_dialog);

        session_student_txt_view = (TextView) confimDialog.findViewById(R.id.session_student_txt_view);
        session_student_txt = (TextView) confimDialog.findViewById(R.id.session_student_txt);
        session_name_txt = (TextView) confimDialog.findViewById(R.id.session_name_txt);
        location_txt = (TextView) confimDialog.findViewById(R.id.location_txt);
        duration_txt = (TextView) confimDialog.findViewById(R.id.duration_txt);
        time_txt = (TextView) confimDialog.findViewById(R.id.time_txt);
        session_fee_txt = (TextView) confimDialog.findViewById(R.id.session_fee_txt);
        confirm_txt = (TextView) confimDialog.findViewById(R.id.confirm_txt);
        cancel_txt = (TextView) confimDialog.findViewById(R.id.cancel_txt);

//        getsessionID();

        if (AppConfiguration.SessionPrice.equalsIgnoreCase("0")) {
            session_fee_txt.setText("Free");
        } else {
            session_fee_txt.setText("₹ " + AppConfiguration.SessionPrice);
        }

        session_student_txt.setText(familyNameStr);
        session_name_txt.setText(AppConfiguration.SessionName);
        location_txt.setText(AppConfiguration.SessionLocation);
        duration_txt.setText(AppConfiguration.SessionDuration);
        time_txt.setText(AppConfiguration.SessionTime);
        AppConfiguration.UserName = session_student_txt.getText().toString();

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("")) {
                    callpaymentRequestApi();
                }
                confimDialog.dismiss();
                sessionDialog.dismiss();

            }
        });


        confimDialog.show();

    }

    //Use for paymentRequest
    public void callpaymentRequestApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_GeneratePaymentRequest(getpaymentRequestdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentRequestModel, Response response) {
                    Util.dismissDialog();
                    if (paymentRequestModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("True")) {

                        orderIDStr = paymentRequestModel.getOrderID();
                        Fragment fragment = new PaymentFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("orderID", orderIDStr);
                        args.putString("amount", AppConfiguration.SessionPrice);
                        args.putString("mode", "TEST");
                        args.putString("username", session_student_txt.getText().toString());
                        args.putString("sessionID", sessionIDStr);
                        args.putString("contactID", contatIDstr);
                        args.putString("type", Util.getPref(mContext, "Type"));
                        fragment.setArguments(args);
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

    private Map<String, String> getpaymentRequestdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);
        map.put("SessionID", sessionIDStr);
        map.put("Amount", AppConfiguration.SessionPrice);
        return map;
    }

    public void getFamilyID() {
        selectedId = new ArrayList<String>();

        selectedId = sessionViewStudentListAdapter.getContactID();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            String[] spiltValue = selectedId.get(i).split("\\|");
            contatIDstr = spiltValue[0];
            familyNameStr = spiltValue[1] + " " + spiltValue[2];
            Log.d("selectedIdStr", contatIDstr);
        }
    }

}

