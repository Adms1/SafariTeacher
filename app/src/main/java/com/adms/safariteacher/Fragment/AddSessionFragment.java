package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;


import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.AddSessionTimeAdapter;
import com.adms.safariteacher.Adapter.AlertListAdapter;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.Session.sessionDataModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.AddSessionDialogBinding;
import com.adms.safariteacher.databinding.FragmentAddSessionBinding;
import com.adms.safariteacher.Interface.onViewClick;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddSessionFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private FragmentAddSessionBinding addSessionBinding;
    private View rootView;
    private Context mContext;

    //Use for Alert Dialog
    ArrayList<String> timegapArray;
    AlertListAdapter alertListAdapter;

    //Use for AddSession Time Dialog
    TextView start_date_txt;
    TextView end_date_txt;

    static TextView sun_start_time_txt, sun_end_time_txt, mon_start_time_txt, mon_end_time_txt, tue_start_time_txt,
            tue_end_time_txt, wed_start_time_txt, wed_end_time_txt, thu_start_time_txt, thu_end_time_txt, fri_end_time_txt,
            fri_start_time_txt, sat_end_time_txt, sat_start_time_txt;

    static String Tag;

    static Button sun_start_add_session_btn, sun_end_add_session_btn, mon_start_add_session_btn, mon_end_add_session_btn,
            tue_start_add_session_btn, tue_end_add_session_btn, wed_start_add_session_btn, wed_end_add_session_btn,
            thu_start_add_session_btn, thu_end_add_session_btn, fri_start_add_session_btn, fri_end_add_session_btn,
            sat_start_add_session_btn, sat_end_add_session_btn;
    RecyclerView day_name_rcView;

    static LinearLayout sun_start_linear, mon_start_linear, tue_start_linear, wed_start_linear, thu_start_linear, fri_start_linear, sat_start_linear,
            sun_end_linear, mon_end_linear, tue_end_linear, wed_end_linear, thu_end_linear, fri_end_linear, sat_end_linear;

    Button done_btn;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    private static String dateFinal;
    private static boolean isFromDate = false;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
    public Dialog popularDialog;
    String flag;

    //Use for selectedSessionTimeValue
    String coachIdStr, lessionTypeIdStr, sessionNameStr, boardStr, standardStr, streamStr, startDateStr, endDateStr,
            address1Str, address2Str, regionStr, cityStr, stateStr, zipcodeStr, descriptionStr, sessionamtStr,
            sessioncapacityStr, alerttimeStr, scheduleStr = "";

    String sunstartTimeStr, sunendTimeStr, finalsunTimeStr, monstartTimeStr, monendTimeStr, finalmonTimeStr,
            tuestartTimeStr, tueendTimeStr, finaltueTimeStr, wedstartTimeStr, wedendTimeStr, finalwedTimeStr,
            thustartTimeStr, thuendTimeStr, finalthuTimeStr, fristartTimeStr, friendTimeStr, finalfriTimeStr,
            satstartTimeStr, satendTimeStr, finalsatTimeStr;

    ArrayList<String> scheduleArray;
    ArrayList<String> newEnteryArray;
    SessionDetailModel dataResponse;

    //USe for Autocomplete Textview
    HashMap<Integer, String> spinnerBoardMap;
    HashMap<Integer, String> spinnerStandardMap;
    HashMap<Integer, String> spinnerStreamMap;
    HashMap<Integer, String> spinnerLessionMap;
    HashMap<Integer, String> spinnerRegionMap;

    public AddSessionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addSessionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_session, container, false);

        rootView = addSessionBinding.getRoot();
        mContext = getActivity();
        flag = getArguments().getString("flag");
        if (flag.equalsIgnoreCase("edit")) {
            ((DashBoardActivity) getActivity()).setActionBar(1, "edit");
        } else {
            ((DashBoardActivity) getActivity()).setActionBar(1, "add");
        }
        initViews();
        callBoardApi();
        callstandardApi();
        callStreamApi();
        callRegionApi();
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
//        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
//        CalendarMinute = calendar.get(Calendar.MINUTE);
//        addSessionBinding.displayStarttimeTxt.setText(Util.getCurrentTime());
//        addSessionBinding.displayEndtimeTxt.setText(Util.getCurrentTime());


//        if (Integer.parseInt(spilttime[0]) > 12) {
//            addSessionBinding.displayStarttimeTxt.setText(Util.getCurrentTime() + "PM");
//            addSessionBinding.displayEndtimeTxt.setText(Util.getCurrentTime() + "PM");
//        } else {
//            addSessionBinding.displayStarttimeTxt.setText(Util.getCurrentTime() + "AM");
//            addSessionBinding.displayEndtimeTxt.setText(Util.getCurrentTime() + "AM");
//        }
//        finaldateStr = spiltmonth[0] + ", " + monthDisplayStr + " " + spiltmonth[2];
//        addSessionBinding.displayDateTxt.setText(finaldateStr);

        timegapArray = new ArrayList<String>();
        timegapArray.add("none");
        timegapArray.add("At time of event");
        for (int i = 1; i < 7; i++) {
            timegapArray.add(String.valueOf(i * 5) + " minutes before");
        }
        timegapArray.add("1 hour before");
        timegapArray.add("1 day before");
        Log.d("timegapArray", "" + timegapArray);

    }

    public void setListners() {

        addSessionBinding.fessStatusRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addSessionBinding.fessStatusRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.free_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.GONE);
                        sessionamtStr = "0";
                        break;
                    case R.id.paid_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.VISIBLE);
                        sessionamtStr = addSessionBinding.sessionPriceEdt.getText().toString();
                        break;
                }
            }
        });
        addSessionBinding.alertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TestDialog();

            }
        });

        addSessionBinding.sessionCal.setOnClickListener(new View.OnClickListener() {
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
        addSessionBinding.sessionTimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionTimeDialog();
            }
        });
        addSessionBinding.addSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionTimeDialog();
            }
        });
        addSessionBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (!coachIdStr.equalsIgnoreCase(""))
                callCreateSessionApi();
            }
        });
    }

    public void TestDialog() {
        final BottomSheetDialog mBottomSheetDialog = new BottomSheetDialog(getActivity());
        View sheetView = getActivity().getLayoutInflater().inflate(R.layout.alert_time_dialog, null);
        mBottomSheetDialog.setContentView(sheetView);
        mBottomSheetDialog.show();

        RecyclerView rcView = (RecyclerView) sheetView.findViewById(R.id.alert_list_rcView);
        TextView canceltxt = (TextView) sheetView.findViewById(R.id.cancel_txt);
        canceltxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mBottomSheetDialog.dismiss();
            }
        });
        alertListAdapter = new AlertListAdapter(mContext, timegapArray, new onViewClick() {
            @Override
            public void getViewClick() {
                getSelectedTimeAlert();
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //Do something after 100ms
                        mBottomSheetDialog.dismiss();
                    }
                }, 600);

            }
        });
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        rcView.setLayoutManager(mLayoutManager);
        rcView.setItemAnimator(new DefaultItemAnimator());
        rcView.setAdapter(alertListAdapter);
    }

    public void getSelectedTimeAlert() {
        String rowValueStr = "";
        for (int k = 0; k < alertListAdapter.getTime().size(); k++) {
            rowValueStr = alertListAdapter.getTime().get(k);
            Log.d("rowValueStr", rowValueStr);
        }
        addSessionBinding.alertBtn.setText(rowValueStr);
    }

    public void SessionTimeDialog() {
        popularDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = popularDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        wlp.gravity = Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.grid_shape);

        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(false);
        popularDialog.setContentView(R.layout.add_session_dialog);
        popularDialog.show();

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        start_date_txt = (TextView) popularDialog.findViewById(R.id.start_date_txt);
        end_date_txt = (TextView) popularDialog.findViewById(R.id.end_date_txt);

        sun_start_time_txt = (TextView) popularDialog.findViewById(R.id.sun_start_time_txt);
        sun_end_time_txt = (TextView) popularDialog.findViewById(R.id.sun_end_time_txt);
        mon_start_time_txt = (TextView) popularDialog.findViewById(R.id.mon_start_time_txt);
        mon_end_time_txt = (TextView) popularDialog.findViewById(R.id.mon_end_time_txt);
        tue_start_time_txt = (TextView) popularDialog.findViewById(R.id.tue_start_time_txt);
        tue_end_time_txt = (TextView) popularDialog.findViewById(R.id.tue_end_time_txt);
        wed_start_time_txt = (TextView) popularDialog.findViewById(R.id.wed_start_time_txt);
        wed_end_time_txt = (TextView) popularDialog.findViewById(R.id.wed_end_time_txt);
        thu_start_time_txt = (TextView) popularDialog.findViewById(R.id.thu_start_time_txt);
        thu_end_time_txt = (TextView) popularDialog.findViewById(R.id.thu_end_time_txt);
        fri_end_time_txt = (TextView) popularDialog.findViewById(R.id.fri_start_time_txt);
        fri_start_time_txt = (TextView) popularDialog.findViewById(R.id.fri_end_time_txt);
        sat_end_time_txt = (TextView) popularDialog.findViewById(R.id.sat_end_time_txt);
        sat_start_time_txt = (TextView) popularDialog.findViewById(R.id.sat_start_time_txt);

        sun_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        mon_start_linear = (LinearLayout) popularDialog.findViewById(R.id.mon_start_linear);
        tue_start_linear = (LinearLayout) popularDialog.findViewById(R.id.tue_start_linear);
        wed_start_linear = (LinearLayout) popularDialog.findViewById(R.id.wed_start_linear);
        thu_start_linear = (LinearLayout) popularDialog.findViewById(R.id.thu_start_linear);
        fri_start_linear = (LinearLayout) popularDialog.findViewById(R.id.fri_start_linear);
        sat_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sat_start_linear);

        sun_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_end_linear);
        mon_end_linear = (LinearLayout) popularDialog.findViewById(R.id.mon_end_linear);
        tue_end_linear = (LinearLayout) popularDialog.findViewById(R.id.tue_end_linear);
        wed_end_linear = (LinearLayout) popularDialog.findViewById(R.id.wed_end_linear);
        thu_end_linear = (LinearLayout) popularDialog.findViewById(R.id.thu_end_linear);
        fri_end_linear = (LinearLayout) popularDialog.findViewById(R.id.fri_end_linear);
        sat_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sat_end_linear);

        sun_start_add_session_btn = (Button) popularDialog.findViewById(R.id.sun_start_add_session_btn);
        sun_end_add_session_btn = (Button) popularDialog.findViewById(R.id.sun_end_add_session_btn);
        mon_start_add_session_btn = (Button) popularDialog.findViewById(R.id.mon_start_add_session_btn);
        mon_end_add_session_btn = (Button) popularDialog.findViewById(R.id.mon_end_add_session_btn);
        tue_start_add_session_btn = (Button) popularDialog.findViewById(R.id.tue_start_add_session_btn);
        tue_end_add_session_btn = (Button) popularDialog.findViewById(R.id.tue_end_add_session_btn);
        wed_start_add_session_btn = (Button) popularDialog.findViewById(R.id.wed_start_add_session_btn);
        wed_end_add_session_btn = (Button) popularDialog.findViewById(R.id.wed_end_add_session_btn);
        thu_start_add_session_btn = (Button) popularDialog.findViewById(R.id.thu_start_add_session_btn);
        thu_end_add_session_btn = (Button) popularDialog.findViewById(R.id.tue_end_add_session_btn);
        fri_start_add_session_btn = (Button) popularDialog.findViewById(R.id.fri_start_add_session_btn);
        fri_end_add_session_btn = (Button) popularDialog.findViewById(R.id.fri_end_add_session_btn);
        sat_start_add_session_btn = (Button) popularDialog.findViewById(R.id.sat_start_add_session_btn);
        sat_end_add_session_btn = (Button) popularDialog.findViewById(R.id.sat_end_add_session_btn);


        day_name_rcView = (RecyclerView) popularDialog.findViewById(R.id.day_name_rcView);

        done_btn = (Button) popularDialog.findViewById(R.id.done_btn);
        start_date_txt.setText(Util.getTodaysDate());
        end_date_txt.setText(Util.getTodaysDate());
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scheduleArray = new ArrayList<>();
                newEnteryArray = new ArrayList<>();
                scheduleStr = "";

                if (!sun_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !sun_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sunstartTimeStr = sun_start_time_txt.getText().toString();
                    sunendTimeStr = sun_end_time_txt.getText().toString();
                    finalsunTimeStr = "sun" + "," + sunstartTimeStr + "-" + sunendTimeStr;
                    Log.d("SundayTime", finalsunTimeStr);
                    scheduleArray.add(finalsunTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");

                }
                if (!mon_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !mon_end_time_txt.getText().toString().equalsIgnoreCase("")) {
                    monstartTimeStr = mon_start_time_txt.getText().toString();
                    monendTimeStr = mon_end_time_txt.getText().toString();
                    finalmonTimeStr = "mon" + "," + monstartTimeStr + "-" + monendTimeStr;
                    scheduleArray.add(finalmonTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");

                }
                if (!tue_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !tue_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    tuestartTimeStr = tue_start_time_txt.getText().toString();
                    tueendTimeStr = tue_end_time_txt.getText().toString();
                    finaltueTimeStr = "tue" + "," + tuestartTimeStr + "-" + tueendTimeStr;
                    scheduleArray.add(finaltueTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");

                }
                if (!wed_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !wed_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    wedstartTimeStr = wed_start_time_txt.getText().toString();
                    wedendTimeStr = wed_end_time_txt.getText().toString();
                    finalwedTimeStr = "wed" + "," + wedstartTimeStr + "-" + wedendTimeStr;
                    scheduleArray.add(finalwedTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");

                }
                if (!thu_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !thu_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    thustartTimeStr = thu_start_time_txt.getText().toString();
                    thuendTimeStr = thu_end_time_txt.getText().toString();
                    finalthuTimeStr = "thu" + "," + thustartTimeStr + "-" + thuendTimeStr;
                    scheduleArray.add(finalthuTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");

                }
                if (!fri_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !fri_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    fristartTimeStr = fri_start_time_txt.getText().toString();
                    friendTimeStr = fri_end_time_txt.getText().toString();
                    finalfriTimeStr = "fri" + "," + fristartTimeStr + "-" + friendTimeStr;
                    scheduleArray.add(finalfriTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");

                }
                if (!sat_start_time_txt.getText().toString().equalsIgnoreCase("Add") && !sat_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    satstartTimeStr = sat_start_time_txt.getText().toString();
                    satendTimeStr = sat_end_time_txt.getText().toString();
                    finalsatTimeStr = "sat" + "," + satstartTimeStr + "-" + satendTimeStr;
                    scheduleArray.add(finalsatTimeStr);
                } else {
//                    Util.ping(mContext, "Please Select Time.");
                }
                Log.d("scheduleArray", "" + scheduleArray.size());

                for (int i = 0; i < scheduleArray.size(); i++) {
                    newEnteryArray.add(scheduleArray.get(i));
                }
                Log.d("newEnteryArray", "" + newEnteryArray.size());

                for (String s : newEnteryArray) {
                    if (!s.equals("")) {
                        scheduleStr = scheduleStr + "|" + s;
                    }

                }
                Log.d("scheduleStr", scheduleStr);
                if (!scheduleStr.equalsIgnoreCase("")) {
                    scheduleStr = scheduleStr.substring(1, scheduleStr.length());
                    Log.d("responseString ", scheduleStr);
                }
                if (!scheduleStr.equalsIgnoreCase("")) {
                    popularDialog.dismiss();
                } else {
                    Util.ping(mContext, "Please Select Time.");
                }

            }
        });
        start_date_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");
            }
        });
        end_date_txt.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "DatePickerDialog");

            }
        });
        sun_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sun_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        mon_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        mon_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        tue_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        tue_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        wed_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        wed_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        thu_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        thu_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        fri_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        fri_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        sat_start_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sat_end_linear.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sun_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sun_start_add_session_btn.setText("+");
                sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sun_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sun_start_time_txt.setText("Add");
                }

            }
        });
        sun_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sun_end_add_session_btn.setText("+");
                sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sun_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sun_end_time_txt.setText("Add");
                }
            }
        });
        mon_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                mon_start_add_session_btn.setText("+");
                mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!mon_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    mon_start_time_txt.setText("Add");
                }
            }
        });
        mon_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                mon_end_add_session_btn.setText("+");
                mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!mon_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    mon_end_time_txt.setText("Add");
                }
            }
        });
        tue_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                tue_start_add_session_btn.setText("+");
                tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!tue_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    tue_start_time_txt.setText("Add");
                }
            }
        });
        tue_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                tue_end_add_session_btn.setText("+");
                tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!tue_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    tue_end_time_txt.setText("Add");
                }
            }
        });
        wed_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                wed_start_add_session_btn.setText("+");
                wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!wed_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    wed_start_time_txt.setText("Add");
                }
            }
        });
        wed_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                wed_end_add_session_btn.setText("+");
                wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!wed_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    wed_end_time_txt.setText("Add");
                }
            }
        });
        thu_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                thu_start_add_session_btn.setText("+");
                thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!thu_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    thu_start_time_txt.setText("Add");
                }
            }
        });
        thu_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                thu_end_add_session_btn.setText("+");
                thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!thu_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    thu_end_time_txt.setText("Add");
                }
            }
        });
        fri_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                fri_start_add_session_btn.setText("+");
                fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!fri_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    fri_start_time_txt.setText("Add");
                }
            }
        });
        fri_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                fri_end_add_session_btn.setText("+");
                fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!fri_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    fri_end_time_txt.setText("Add");
                }
            }
        });
        sat_start_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sat_start_add_session_btn.setText("+");
                sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sat_start_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sat_start_time_txt.setText("Add");
                }
            }
        });
        sat_end_add_session_btn.setOnClickListener(new View.OnClickListener()

        {
            @Override
            public void onClick(View view) {
                Tag = view.getTag().toString();
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
                sun_end_add_session_btn.setText("+");
                sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.blue));
                sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_btn));
                if (!sat_end_time_txt.getText().toString().equalsIgnoreCase("Add")) {
                    sat_end_time_txt.setText("Add");
                }

            }
        });
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view,
                          int year, int monthOfYear, int dayOfMonth) {
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
        dateFinal = d + "/" + m + "/" + y;
        if (isFromDate) {
            start_date_txt.setText(dateFinal);
        } else {
            end_date_txt.setText(dateFinal);
        }
    }


    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog tpd4 = new TimePickerDialog(getActivity(),
                    android.app.AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, android.text.format.DateFormat.is24HourFormat(getActivity()));

            return tpd4;
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
            String status = "AM";
            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then the current AM PM status is PM
                status = "PM";
            }

            // Initialize a new variable to hold 12 hour format hour value
            int hour_of_12_hour_format;

            if (hourOfDay > 11) {

                // If the hour is greater than or equal to 12
                // Then we subtract 12 from the hour to make it 12 hour format time
                hour_of_12_hour_format = hourOfDay - 12;
            } else {
                hour_of_12_hour_format = hourOfDay;
            }

            switch (Tag) {
                case "0":
                    sun_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    sun_start_add_session_btn.setText("x");
                    sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "1":
                    sun_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    sun_end_add_session_btn.setText("x");
                    sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "2":
                    mon_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    mon_start_add_session_btn.setText("x");
                    mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "3":
                    mon_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    mon_end_add_session_btn.setText("x");
                    mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "4":
                    tue_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    tue_start_add_session_btn.setText("x");
                    tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "5":
                    tue_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    tue_end_add_session_btn.setText("x");
                    tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "6":
                    wed_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    wed_start_add_session_btn.setText("x");
                    wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "7":
                    wed_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    wed_end_add_session_btn.setText("x");
                    wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "8":
                    thu_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    thu_start_add_session_btn.setText("x");
                    thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "9":
                    thu_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    thu_end_add_session_btn.setText("x");
                    thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "10":
                    fri_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    fri_start_add_session_btn.setText("x");
                    fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "11":
                    fri_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    fri_end_add_session_btn.setText("x");
                    fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "12":
                    sat_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    sat_start_add_session_btn.setText("x");
                    sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "13":
                    sat_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + "" + status);
                    sat_end_add_session_btn.setText("x");
                    sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                default:
            }
        }
    }

    public void getSelectedSessionTimeValue() {
        
        coachIdStr = AppConfiguration.coachId;
        lessionTypeIdStr = "7";
        sessionNameStr = addSessionBinding.sessionNameEdt.getText().toString();
        boardStr = addSessionBinding.boardNameEdt.getText().toString();
        standardStr = addSessionBinding.standardEdt.getText().toString();
        streamStr = addSessionBinding.subjectEdt.getText().toString();
        address1Str = addSessionBinding.addressEdt.getText().toString();
        address2Str = addSessionBinding.addressEdt.getText().toString();
        regionStr = addSessionBinding.areaEdt.getText().toString();
        cityStr = addSessionBinding.cityEdt.getText().toString();
        stateStr = addSessionBinding.stateEdt.getText().toString();
        zipcodeStr = addSessionBinding.zipcodeEdt.getText().toString();
        descriptionStr = addSessionBinding.descriptionEdt.getText().toString();
        sessioncapacityStr = addSessionBinding.sportsEdt.getText().toString();
        alerttimeStr = addSessionBinding.alertBtn.getText().toString();
        startDateStr = start_date_txt.getText().toString();
        endDateStr = end_date_txt.getText().toString();


    }

    //Use for Create Session
    public void callCreateSessionApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().getCreate_Session(getNewSessionDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel sessionDetailModel, Response response) {
                    Util.dismissDialog();
                    if (sessionDetailModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionDetailModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionDetailModel.getSuccess().equalsIgnoreCase("True")) {

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

    private Map<String, String> getNewSessionDetail() {
        getSelectedSessionTimeValue();
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", coachIdStr);
        map.put("LessionTypeID", lessionTypeIdStr);
        map.put("SessionName", sessionNameStr);
        map.put("Board", boardStr);
        map.put("Standard", standardStr);
        map.put("Stream", streamStr);
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);
        map.put("Address1", address1Str);
        map.put("Address2", address2Str);
        map.put("Region", regionStr);
        map.put("City", cityStr);
        map.put("State", stateStr);
        map.put("Zipcode", zipcodeStr);
        map.put("Description", descriptionStr);
        map.put("SessionAmount", sessionamtStr);
        map.put("SessionCapacity", sessioncapacityStr);
        map.put("AlertTime", alerttimeStr);
        map.put("Schedule", scheduleStr);
        return map;
    }

    //Use for Board
    public void callBoardApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Board(getBoardDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Util.dismissDialog();
                    if (boardInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillBoard();
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

    private Map<String, String> getBoardDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillBoard() {
        ArrayList<Integer> BoardId = new ArrayList<Integer>();
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            BoardId.add(Integer.valueOf(dataResponse.getData().get(i).getBoardID()));
        }
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            BoardName.add(dataResponse.getData().get(j).getBoardName());
        }

        String[] spinnertermIdArray = new String[BoardId.size()];

        spinnerBoardMap = new HashMap<Integer, String>();
        for (int i = 0; i < BoardId.size(); i++) {
            spinnerBoardMap.put(i, String.valueOf(BoardId.get(i)));
            spinnertermIdArray[i] = BoardName.get(i).trim();
        }
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            // Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(addSessionBinding.boardNameEdt);
//
//            popupWindow.setHeight(spinnertermIdArray.length > 4 ? 500 : spinnertermIdArray.length * 100);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//            // silently fail...
//        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnertermIdArray);
        addSessionBinding.boardNameEdt.setThreshold(1);
        addSessionBinding.boardNameEdt.setAdapter(adapterTerm);

    }

    //Use for standard
    public void callstandardApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Standard(getstandardDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Util.dismissDialog();
                    if (boardInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();

                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillStandard();
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

    private Map<String, String> getstandardDetail() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillStandard() {
        ArrayList<Integer> StandardId = new ArrayList<Integer>();
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            StandardId.add(Integer.valueOf(dataResponse.getData().get(i).getStandardID()));
        }
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StandardName.add(dataResponse.getData().get(j).getStandardName());
        }

        String[] spinnertermIdArray = new String[StandardId.size()];

        spinnerStandardMap = new HashMap<Integer, String>();
        for (int i = 0; i < StandardId.size(); i++) {
            spinnerStandardMap.put(i, String.valueOf(StandardId.get(i)));
            spinnertermIdArray[i] = StandardName.get(i).trim();
        }
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            // Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(addSessionBinding.standardEdt);
//
//            popupWindow.setHeight(spinnertermIdArray.length > 4 ? 500 : spinnertermIdArray.length * 100);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//            // silently fail...
//        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnertermIdArray);
        addSessionBinding.standardEdt.setThreshold(1);
        addSessionBinding.standardEdt.setAdapter(adapterTerm);
    }

    //Use for Stream
    public void callStreamApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Stream(getStreamDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Util.dismissDialog();
                    if (boardInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillStream();
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

    private Map<String, String> getStreamDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillStream() {
        ArrayList<Integer> StreamId = new ArrayList<Integer>();
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            StreamId.add(Integer.valueOf(dataResponse.getData().get(i).getStreamID()));
        }
        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StreamName.add(dataResponse.getData().get(j).getStreamName());
        }

        String[] spinnertermIdArray = new String[StreamId.size()];

        spinnerStreamMap = new HashMap<Integer, String>();
        for (int i = 0; i < StreamId.size(); i++) {
            spinnerStreamMap.put(i, String.valueOf(StreamId.get(i)));
            spinnertermIdArray[i] = StreamName.get(i).trim();
        }
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            // Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(addSessionBinding.subjectEdt);
//
//            popupWindow.setHeight(spinnertermIdArray.length > 4 ? 500 : spinnertermIdArray.length * 100);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//            // silently fail...
//        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnertermIdArray);
        addSessionBinding.boardNameEdt.setThreshold(1);
        addSessionBinding.subjectEdt.setAdapter(adapterTerm);
    }

    //Use for Lession
    public void callLessionApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Lesson(getLessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Util.dismissDialog();
                    if (boardInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillLession();
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

    private Map<String, String> getLessionDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillLession() {
        ArrayList<Integer> StreamId = new ArrayList<Integer>();
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            StreamId.add(Integer.valueOf(dataResponse.getData().get(i).getStreamID()));
        }
        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StreamName.add(dataResponse.getData().get(j).getStreamName());
        }

        String[] spinnertermIdArray = new String[StreamId.size()];

        spinnerStreamMap = new HashMap<Integer, String>();
        for (int i = 0; i < StreamId.size(); i++) {
            spinnerStreamMap.put(i, String.valueOf(StreamId.get(i)));
            spinnertermIdArray[i] = StreamName.get(i).trim();
        }
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            // Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(addSessionBinding.subjectEdt);
//
//            popupWindow.setHeight(spinnertermIdArray.length > 4 ? 500 : spinnertermIdArray.length * 100);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//            // silently fail...
//        }

//        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnertermIdArray);
//        addSessionBinding.boardNameEdt.setThreshold(1);
//        addSessionBinding.subjectEdt.setAdapter(adapterTerm);
    }

    //Use for Region
    public void callRegionApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Region(getRegionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel boardInfo, Response response) {
                    Util.dismissDialog();
                    if (boardInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (boardInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (boardInfo.getData().size() > 0) {
                            dataResponse = boardInfo;
                            fillArea();
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

    private Map<String, String> getRegionDeatil() {
        Map<String, String> map = new HashMap<>();
        return map;
    }

    public void fillArea() {
        ArrayList<Integer> AreaId = new ArrayList<Integer>();
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            AreaId.add(Integer.valueOf(dataResponse.getData().get(i).getRegionID()));
        }
        ArrayList<String> AreaName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            AreaName.add(dataResponse.getData().get(j).getRegionName());
        }

        String[] spinnertermIdArray = new String[AreaId.size()];

        spinnerRegionMap = new HashMap<Integer, String>();
        for (int i = 0; i < AreaId.size(); i++) {
            spinnerRegionMap.put(i, String.valueOf(AreaId.get(i)));
            spinnertermIdArray[i] = AreaName.get(i).trim();
        }
//        try {
//            Field popup = Spinner.class.getDeclaredField("mPopup");
//            popup.setAccessible(true);
//
//            // Get private mPopup member variable and try cast to ListPopupWindow
//            android.widget.ListPopupWindow popupWindow = (android.widget.ListPopupWindow) popup.get(addSessionBinding.subjectEdt);
//
//            popupWindow.setHeight(spinnertermIdArray.length > 4 ? 500 : spinnertermIdArray.length * 100);
//        } catch (NoClassDefFoundError | ClassCastException | NoSuchFieldException | IllegalAccessException e) {
//            // silently fail...
//        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, spinnertermIdArray);
        addSessionBinding.areaEdt.setThreshold(1);
        addSessionBinding.areaEdt.setAdapter(adapterTerm);
    }
}

