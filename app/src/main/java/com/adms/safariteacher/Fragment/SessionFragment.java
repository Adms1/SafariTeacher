package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
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
import android.widget.TextView;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Activities.DrawableCalendarEvent;
import com.adms.safariteacher.Adapter.SessionViewStudentListAdapter;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
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
    String sessionnameStr, sessionstrattimeStr = "", sessionendtimeStr = "", sessionDateStr = "", sessionIDStr;
    TextView start_time_txt, end_time_txt, session_title_txt, date_txt;
    RecyclerView studentnamelist_rcView;
    SessionViewStudentListAdapter sessionViewStudentListAdapter;
    ArrayList<String> arrayList;
    SessionDetailModel finalsessionfullDetailModel;
    List<CalendarEvent> eventList = new ArrayList<>();
    ArrayList<Integer> colorList = new ArrayList<>();

    public SessionFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        calendarBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_calendar, container, false);

        rootView = calendarBinding.getRoot();
        mContext = getActivity();

        ((DashBoardActivity) getActivity()).setActionBar(0, "true");
        colorList.add(getResources().getColor(R.color.green_dark));
        colorList.add(getResources().getColor(R.color.yellow_dark));
        colorList.add(getResources().getColor(R.color.blue_dark));
        callGetSessionDetailApi();


        setListner();
        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        callGetSessionDetailApi();
    }

    public void init() {
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

    @Override
    public void onDaySelected(DayItem dayItem) {
        Log.d("CurrentDay", "" + dayItem);
    }

    @Override
    public void onEventSelected(CalendarEvent event) {
        if (!event.getTitle().equals("No events")) {
            parseTodaysDate(String.valueOf(event.getStartTime().getTime()), String.valueOf(event.getEndTime().getTime()));
            sessionnameStr = event.getTitle();
            sessionIDStr = String.valueOf(event.getId());
            Log.d("sessionID", sessionIDStr);
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
        add_student_btn = (Button) sessionDialog.findViewById(R.id.add_student_btn);

        date_txt.setText(sessionDateStr);
        session_title_txt.setText(sessionnameStr);
        start_time_txt.setText(sessionstrattimeStr);
        end_time_txt.setText(sessionendtimeStr);
        arrayList = new ArrayList<>();
        arrayList.add("Amit Shah");
        arrayList.add("Nehal Patel");
        arrayList.add("Sujal Shah");
        arrayList.add("---------");
        arrayList.add("---------");

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
                args.putString("sessionIDStr", sessionIDStr);
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

        add_student_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OldFamilyListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "13");
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
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
                DrawableCalendarEvent event = new DrawableCalendarEvent(Integer.parseInt(finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionID()),
                        colorList.get(j), finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName(),
                        finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionName(),
                        finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getSessionTime() + System.getProperty("line.separator") + finalsessionfullDetailModel.getData().get(i).getSessionFullDetails().get(j).getRegionName(),
                        startDate, endDate, 0, "10:00 PM-11:00 PM", 1);
                eventList.add(event);
            }

        }


//        Log.d("eventList", "" + eventList);


//        try {
////            String dateString = "24/03/2018 2:00 PM";
////            String enddateString = "24/03/2018 3:00 PM";
//            Log.d("DataMOckSize",""+finalsessionfullDetailModel.getData().size());
//            String[] spiltTime = finalsessionfullDetailModel.getData().get(0).getSessionFullDetails().get(0).getSessionTime().split("\\-");
//            String dateString = finalsessionfullDetailModel.getData().get(0).getSessionFullDetails().get(0).getSessionDate() + " " + spiltTime[0];
//            String enddateString = finalsessionfullDetailModel.getData().get(0).getSessionFullDetails().get(0).getSessionDate() + " " + spiltTime[1];
////
//            SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
//            SimpleDateFormat sdf1 = new SimpleDateFormat("dd-MM-yyyy hh:mm a", Locale.getDefault());
//            Date date = sdf.parse(dateString);
//            Date date1 = sdf1.parse(enddateString);
//
//
//            startDate = date.getTime();
//            endDate = date1.getTime();
//            Log.d("FirstTime", "first event :" + startDate + endDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
////        long id, int color, String title, String description, String location, long dateStart, long dateEnd, int allDay, String duration, int drawableId
//        DrawableCalendarEvent event3 = new DrawableCalendarEvent(1, R.color.search_boder, "Yoga Class", "Yoga good for health", "PaldiRiverfront", startDate, endDate, 0, "1 hours", 1);
//        eventList.add(event3);

//        try {
//            String dateString = "24/03/2018 4:00 PM";
//            String enddateString = "24/03/2018 5:00 PM";
//            SimpleDateFormat sdf2 = new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.getDefault());
//            SimpleDateFormat sdf3 = new SimpleDateFormat("dd/MM/yyyy hh:mm a",Locale.getDefault());
//            Date date = sdf2.parse(dateString);
//            Date date1 = sdf3.parse(enddateString);
//
//
//            startDate = date.getTime();
//            endDate = date1.getTime();
//            Log.d("SecondTime", "Second event :" + startDate + endDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        DrawableCalendarEvent event4 = new DrawableCalendarEvent(2, R.color.search_boder, "Dance Class", "Dance good for health", "PaldiRiverfront", startDate, endDate, 2, "1 hours", 1);
//        eventList.add(event4);
//

    }

    public static SessionFragment newInstance() {
        if(fragment==null){
            fragment = new SessionFragment();
        }
        return fragment;
    }

}

