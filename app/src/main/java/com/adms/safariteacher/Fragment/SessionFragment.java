package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.TextView;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Activities.DrawableCalendarEvent;
import com.adms.safariteacher.Adapter.SessionViewStudentListAdapter;
import com.adms.safariteacher.Adapter.StudentAttendanceAdapter;
import com.adms.safariteacher.R;
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
import java.util.List;
import java.util.Locale;


public class SessionFragment extends Fragment implements CalendarPickerController {

    FragmentCalendarBinding calendarBinding;
    private View rootView;
    private Context mContext;
    public Dialog sessionDialog;
    Button cancel_btn, add_attendance_btn, edit_session_btn;
    String sessionnameStr, sessionstrattimeStr = "", sessionendtimeStr = "", sessionDateStr = "";
    TextView start_time_txt, end_time_txt, session_title_txt, date_txt;
    RecyclerView studentnamelist_rcView;
    SessionViewStudentListAdapter sessionViewStudentListAdapter;
    ArrayList<String> arrayList;

    public SessionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);

        rootView = calendarBinding.getRoot();
        mContext = getActivity().getApplicationContext();

        ((DashBoardActivity) getActivity()).setActionBar(0, "true");

        init();
        setListner();
        return rootView;
    }

    public void init() {
        Calendar minDate = Calendar.getInstance();
        Calendar maxDate = Calendar.getInstance();

        minDate.add(Calendar.MONTH, -2);
//        minDate.set(Calendar.DAY_OF_MONTH,0);
        maxDate.add(Calendar.YEAR, 1);

        List<CalendarEvent> eventList = new ArrayList<>();
        mockList(eventList);


        calendarBinding.agendaCalendarView.init(eventList, minDate, maxDate, Locale.US, this);
    }

    public void setListner() {
        calendarBinding.addEventFabBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddSessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("flag", "Add");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }

    private void mockList(List<CalendarEvent> eventList) {

//        Calendar startTime1 = Calendar.getInstance();
//        Calendar endTime1 = Calendar.getInstance();
//        endTime1.add(Calendar.MONTH, 1);
//        BaseCalendarEvent event1 = new BaseCalendarEvent("Thibault travels in Iceland", "A wonderful journey!", "Iceland",
//                ContextCompat.getColor(this, android.R.color.holo_red_light), startTime1, endTime1, true);
//        eventList.add(event1);
//
//        Calendar startTime2 = Calendar.getInstance();
//        startTime2.add(Calendar.DAY_OF_YEAR, 1);
//        Calendar endTime2 = Calendar.getInstance();
//        endTime2.add(Calendar.DAY_OF_YEAR, 3);
//        BaseCalendarEvent event2 = new BaseCalendarEvent("Visit to Dalvík", "A beautiful small town", "Dalvík",
//                ContextCompat.getColor(this, R.color.blue_selected), startTime2, endTime2, true);
//        eventList.add(event2);
        Calendar startTime3 = Calendar.getInstance();
        Calendar endTime3 = Calendar.getInstance();

        startTime3.set(Calendar.HOUR_OF_DAY, 14);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 15);
        endTime3.set(Calendar.MINUTE, 0);
        Log.d("ADMSTime", "StartTime" + startTime3 + "EndTime" + endTime3);

        DrawableCalendarEvent event3 = new DrawableCalendarEvent("Yoga Class", "", "Satelite",
                ContextCompat.getColor(mContext, R.color.absent), startTime3, endTime3, true, R.drawable.img_location);

        eventList.add(event3);
        startTime3.set(Calendar.HOUR_OF_DAY, 16);
        startTime3.set(Calendar.MINUTE, 0);
        endTime3.set(Calendar.HOUR_OF_DAY, 17);
        endTime3.set(Calendar.MINUTE, 0);
        DrawableCalendarEvent event4 = new DrawableCalendarEvent("Music Class", "", "Satelite2",
                ContextCompat.getColor(mContext, R.color.leave), startTime3, endTime3, true, R.drawable.img_location);

        eventList.add(event4);

        Calendar startTime4 = Calendar.getInstance();
        Calendar endTime4 = Calendar.getInstance();
        startTime4.add(Calendar.DAY_OF_YEAR, 1);
        endTime4.add(Calendar.DAY_OF_YEAR, 1);
        startTime4.set(Calendar.HOUR_OF_DAY, 9);
        startTime4.set(Calendar.MINUTE, 0);
        endTime4.set(Calendar.HOUR_OF_DAY, 15);
        endTime4.set(Calendar.MINUTE, 0);

        DrawableCalendarEvent event5 = new DrawableCalendarEvent("Dance Class", "", "Paldi",
                ContextCompat.getColor(mContext, R.color.present), startTime4, endTime4, true, R.drawable.img_location);

        eventList.add(event5);
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
            SessionDialog();
        } else {
            Util.ping(mContext, "No Event Available...");
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

        sessionDialog.getWindow().setBackgroundDrawableResource(R.drawable.grid_shape);

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

        date_txt.setText(sessionDateStr);
        session_title_txt.setText(sessionnameStr);
        start_time_txt.setText(sessionstrattimeStr);
        end_time_txt.setText(sessionendtimeStr);
        arrayList = new ArrayList<>();
//        ArrayList<String> name = new ArrayList<>();
        arrayList.add("Amit Shah");
        arrayList.add("Nehal Patel");
        arrayList.add("Sujal Shah");
        arrayList.add("---------");
        arrayList.add("---------");

//        for (int i = 0; i < name; i++) {
//            arrayList.add(String.valueOf(i));
//            if (i > 3) {
//                arrayList.add("--------");
//            }
//        }
//        Log.d("arrayList", "" + arrayList.size());
        sessionViewStudentListAdapter = new SessionViewStudentListAdapter(mContext, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        studentnamelist_rcView.setLayoutManager(mLayoutManager);
        studentnamelist_rcView.setItemAnimator(new DefaultItemAnimator());
        studentnamelist_rcView.setAdapter(sessionViewStudentListAdapter);

        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sessionDialog.dismiss();
            }
        });

        edit_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddSessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("flag", "edit");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });

        add_attendance_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new StudentAttendanceFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
                sessionDialog.dismiss();
            }
        });
    }
}

