package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.TimePicker;


import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.AlertListAdapter;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.AddSessionDialogBinding;
import com.adms.safariteacher.databinding.FragmentAddSessionBinding;
import com.adms.safariteacher.Interface.onViewClick;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class AddSessionFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    private FragmentAddSessionBinding addSessionBinding;
    private View rootView;
    private Context mContext;
    public static AddSessionFragment fragment;
    //Use for Alert Dialog
    ArrayList<String> timegapArray;
    AlertListAdapter alertListAdapter;

    //Use for AddSession Time Dialog
    TextView start_date_txt, end_date_txt;
    AddSessionDialogBinding sessionDialogBinding;
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

    Button done_btn, cancel_btn;
    int Year, Month, Day;
    Calendar calendar;
    int mYear, mMonth, mDay;
    private static String dateFinal;
    private static String minuteFinal, hourFinal;
    private static boolean isFromDate = false;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
    public Dialog popularDialog;
    String flag, SeslectedsessionID, CoachTypeStr, studentAvailable;

    //Use for selectedSessionTimeValue
    String coachIdStr, lessionTypeNameStr = "", sessionNameStr = "", boardStr = "", standardStr = "", streamStr = "", startDateStr = "", endDateStr = "",
            address1Str = "", address2Str = "", regionStr = "", cityStr = "", stateStr = "", zipcodeStr = "", descriptionStr = "", sessionamtStr = "0",
            sessioncapacityStr = "", alerttimeStr = "", scheduleStr = "", sessiontypeStr = "1";

    String sunstartTimeStr, sunendTimeStr, finalsunTimeStr, monstartTimeStr, monendTimeStr, finalmonTimeStr,
            tuestartTimeStr, tueendTimeStr, finaltueTimeStr, wedstartTimeStr, wedendTimeStr, finalwedTimeStr,
            thustartTimeStr, thuendTimeStr, finalthuTimeStr, fristartTimeStr, friendTimeStr, finalfriTimeStr,
            satstartTimeStr, satendTimeStr, finalsatTimeStr;

    String EditStartDateStr, EditEndDateStr, EditScheduleStr = "";

    ArrayList<String> scheduleArray;
    ArrayList<String> newEnteryArray;
    SessionDetailModel dataResponse;

    public AddSessionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addSessionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_session, container, false);

        rootView = addSessionBinding.getRoot();
        mContext = getActivity();
        flag = getArguments().getString("flag");


        SeslectedsessionID = getArguments().getString("sessionID");
        if (flag.equalsIgnoreCase("edit")) {
            ((DashBoardActivity) getActivity()).setActionBar(1, "edit");
            studentAvailable = getArguments().getString("studentAvailable");
            Log.d("studentAvailable", studentAvailable);
        } else if (flag.equalsIgnoreCase("view")) {
            ((DashBoardActivity) getActivity()).setActionBar(1, "view");
            studentAvailable = getArguments().getString("studentAvailable");
            Log.d("studentAvailable", studentAvailable);
        } else {
            ((DashBoardActivity) getActivity()).setActionBar(1, "add");
        }


        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        coachIdStr = Util.getPref(mContext, "coachID");
        CoachTypeStr = Util.getPref(mContext, "coachTypeID");

        if (!CoachTypeStr.equalsIgnoreCase("1")) {
            addSessionBinding.sessionBoardLinear.setVisibility(View.GONE);
            addSessionBinding.sessionStandardLinear.setVisibility(View.GONE);
            addSessionBinding.sessionStreamLinear.setVisibility(View.GONE);
        } else {
            addSessionBinding.sessionBoardLinear.setVisibility(View.VISIBLE);
            addSessionBinding.sessionStandardLinear.setVisibility(View.VISIBLE);
            addSessionBinding.sessionStreamLinear.setVisibility(View.VISIBLE);
        }


        if (flag.equalsIgnoreCase("edit")) {
            if (!studentAvailable.equalsIgnoreCase("0")) {
                addSessionBinding.sessionTimeLinear.setVisibility(View.GONE);
            } else {
                addSessionBinding.sessionTimeLinear.setVisibility(View.VISIBLE);
            }
            addSessionBinding.submitBtn.setText("Update");
            callEditSessionApi();
        } else if (flag.equalsIgnoreCase("view")) {
            disableControl();
            callEditSessionApi();
        } else {
            addSessionBinding.submitBtn.setText("Submit");
            callBoardApi();
            callstandardApi();
            callStreamApi();
            callLessionApi();
            callRegionApi();
        }
        initViews();
        setListners();

    }

    public void initViews() {
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
                        addSessionBinding.sessionPriceEdt.setText("0");
//                        sessionamtStr = "0";
                        break;
                    case R.id.paid_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.VISIBLE);
                        sessionamtStr = addSessionBinding.sessionPriceEdt.getText().toString();
                        break;
                }
            }
        });
        addSessionBinding.sessionTypeRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addSessionBinding.sessionTypeRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.recurring_rb:
                        sessiontypeStr = "1";
                        break;
                    case R.id.single_rb:
                        sessiontypeStr = "2";
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
        addSessionBinding.addSessionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SessionTimeDialog();
            }
        });
        addSessionBinding.submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                fillEditSessionFiled();
                editSessionValidation();
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

        popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

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
        fri_end_time_txt = (TextView) popularDialog.findViewById(R.id.fri_end_time_txt);
        fri_start_time_txt = (TextView) popularDialog.findViewById(R.id.fri_start_time_txt);
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
        thu_end_add_session_btn = (Button) popularDialog.findViewById(R.id.thu_end_add_session_btn);
        fri_start_add_session_btn = (Button) popularDialog.findViewById(R.id.fri_start_add_session_btn);
        fri_end_add_session_btn = (Button) popularDialog.findViewById(R.id.fri_end_add_session_btn);
        sat_start_add_session_btn = (Button) popularDialog.findViewById(R.id.sat_start_add_session_btn);
        sat_end_add_session_btn = (Button) popularDialog.findViewById(R.id.sat_end_add_session_btn);


        day_name_rcView = (RecyclerView) popularDialog.findViewById(R.id.day_name_rcView);

        done_btn = (Button) popularDialog.findViewById(R.id.done_btn);
        cancel_btn = (Button) popularDialog.findViewById(R.id.cancel_btn);
        if (flag.equalsIgnoreCase("edit")) {
            start_date_txt.setText(EditStartDateStr);
            end_date_txt.setText(EditEndDateStr);
            String[] spiltPipes = EditScheduleStr.split("\\|");
            String[] spiltComma;
            String[] spiltDash;
            Log.d("spilt", "" + spiltPipes.toString());
            for (int i = 0; i < spiltPipes.length; i++) {
                spiltComma = spiltPipes[i].split("\\,");
                spiltDash = spiltComma[1].split("\\-");
                switch (spiltComma[0]) {
                    case "sun":
                        sun_start_time_txt.setText(spiltDash[0]);
                        sun_end_time_txt.setText(spiltDash[1]);
                        break;
                    case "mon":
                        mon_start_time_txt.setText(spiltDash[0]);
                        mon_end_time_txt.setText(spiltDash[1]);
                        break;
                    case "tue":
                        tue_start_time_txt.setText(spiltDash[0]);
                        tue_end_time_txt.setText(spiltDash[1]);
                        break;
                    case "wed":
                        wed_start_time_txt.setText(spiltDash[0]);
                        wed_end_time_txt.setText(spiltDash[1]);
                        break;
                    case "thu":
                        thu_start_time_txt.setText(spiltDash[0]);
                        thu_end_time_txt.setText(spiltDash[1]);
                        break;
                    case "fri":
                        fri_start_time_txt.setText(spiltDash[0]);
                        fri_end_time_txt.setText(spiltDash[1]);
                        break;
                    case "sat":
                        sat_start_time_txt.setText(spiltDash[0]);
                        sat_end_time_txt.setText(spiltDash[1]);
                        break;
                    default:

                }
            }
        } else {
            start_date_txt.setText(Util.getTodaysDate());
            end_date_txt.setText(Util.getTodaysDate());
        }


        List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
        System.out.println(days);
        cancel_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularDialog.dismiss();
            }
        });
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
                datePickerDialog.setMinDate(Calendar.getInstance());
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
                datePickerDialog.setMinDate(Calendar.getInstance());
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
        fri_start_add_session_btn.setOnClickListener(new View.OnClickListener() {
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
        fri_end_add_session_btn.setOnClickListener(new View.OnClickListener() {
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
        sat_start_add_session_btn.setOnClickListener(new View.OnClickListener() {
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
        sat_end_add_session_btn.setOnClickListener(new View.OnClickListener() {
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

        startDateStr = start_date_txt.getText().toString();
        endDateStr = end_date_txt.getText().toString();
    }

    private static List<String> getDates(String dateString1, String dateString2) {
        ArrayList<String> days = new ArrayList<String>();
        DateFormat df1 = new SimpleDateFormat("dd/MM/yyyy");

        Date date1 = null;
        Date date2 = null;

        try {
            date1 = df1.parse(dateString1);
            date2 = df1.parse(dateString2);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(date1);


        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(date2);

        while (!cal1.after(cal2)) {
            days.add(new SimpleDateFormat("EE").format(cal1.getTime()));
            cal1.add(Calendar.DATE, 1);

        }
        Log.d("days", "" + days);
        sun_start_linear.setEnabled(false);
        sun_end_linear.setEnabled(false);
        sun_start_linear.setAlpha(0.2f);
        sun_end_linear.setAlpha(0.2f);
        sun_start_add_session_btn.setEnabled(false);
        sun_end_add_session_btn.setEnabled(false);

        mon_start_linear.setEnabled(false);
        mon_end_linear.setEnabled(false);
        mon_start_linear.setAlpha(0.2f);
        mon_end_linear.setAlpha(0.2f);
        mon_start_add_session_btn.setEnabled(false);
        mon_end_add_session_btn.setEnabled(false);

        tue_start_linear.setEnabled(false);
        tue_end_linear.setEnabled(false);
        tue_start_linear.setAlpha(0.2f);
        tue_end_linear.setAlpha(0.2f);
        tue_start_add_session_btn.setEnabled(false);
        tue_end_add_session_btn.setEnabled(false);

        wed_start_linear.setEnabled(false);
        wed_end_linear.setEnabled(false);
        wed_start_linear.setAlpha(0.2f);
        wed_end_linear.setAlpha(0.2f);
        wed_start_add_session_btn.setEnabled(false);
        wed_end_add_session_btn.setEnabled(false);

        thu_start_linear.setEnabled(false);
        thu_end_linear.setEnabled(false);
        thu_start_linear.setAlpha(0.2f);
        thu_end_linear.setAlpha(0.2f);
        thu_start_add_session_btn.setEnabled(false);
        thu_end_add_session_btn.setEnabled(false);

        fri_start_linear.setEnabled(false);
        fri_end_linear.setEnabled(false);
        fri_start_linear.setAlpha(0.2f);
        fri_end_linear.setAlpha(0.2f);
        fri_start_add_session_btn.setEnabled(false);
        fri_end_add_session_btn.setEnabled(false);

        sat_start_linear.setEnabled(false);
        sat_end_linear.setEnabled(false);
        sat_start_linear.setAlpha(0.2f);
        sat_end_linear.setAlpha(0.2f);
        sat_start_add_session_btn.setEnabled(false);
        sat_end_add_session_btn.setEnabled(false);
        for (int i = 0; i < days.size(); i++) {
            switch (days.get(i)) {
                case "Sun":
                    sun_start_linear.setEnabled(true);
                    sun_end_linear.setEnabled(true);
                    sun_start_linear.setAlpha(1);
                    sun_end_linear.setAlpha(1);
                    sun_start_add_session_btn.setEnabled(true);
                    sun_end_add_session_btn.setEnabled(true);
                    break;
                case "Mon":
                    mon_start_linear.setEnabled(true);
                    mon_end_linear.setEnabled(true);
                    mon_start_linear.setAlpha(1);
                    mon_end_linear.setAlpha(1);
                    mon_start_add_session_btn.setEnabled(true);
                    mon_end_add_session_btn.setEnabled(true);
                    break;
                case "Tue":
                    tue_start_linear.setEnabled(true);
                    tue_end_linear.setEnabled(true);
                    tue_start_linear.setAlpha(1);
                    tue_end_linear.setAlpha(1);
                    tue_start_add_session_btn.setEnabled(true);
                    tue_end_add_session_btn.setEnabled(true);
                    break;
                case "Wed":
                    wed_start_linear.setEnabled(true);
                    wed_end_linear.setEnabled(true);
                    wed_start_linear.setAlpha(1);
                    wed_end_linear.setAlpha(1);
                    wed_start_add_session_btn.setEnabled(true);
                    wed_end_add_session_btn.setEnabled(true);
                    break;
                case "Thu":
                    thu_start_linear.setEnabled(true);
                    thu_end_linear.setEnabled(true);
                    thu_start_linear.setAlpha(1);
                    thu_end_linear.setAlpha(1);
                    thu_start_add_session_btn.setEnabled(true);
                    thu_end_add_session_btn.setEnabled(true);
                    break;
                case "Fri":
                    fri_start_linear.setEnabled(true);
                    fri_end_linear.setEnabled(true);
                    fri_start_linear.setAlpha(1);
                    fri_end_linear.setAlpha(1);
                    fri_start_add_session_btn.setEnabled(true);
                    fri_end_add_session_btn.setEnabled(true);
                    break;
                case "Sat":
                    sat_start_linear.setEnabled(true);
                    sat_end_linear.setEnabled(true);
                    sat_start_linear.setAlpha(1);
                    sat_end_linear.setAlpha(1);
                    sat_start_add_session_btn.setEnabled(true);
                    sat_end_add_session_btn.setEnabled(true);
                    break;
                default:
            }
        }
        return days;
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
        startDateStr = start_date_txt.getText().toString();
        endDateStr = end_date_txt.getText().toString();
        List<String> days = getDates(start_date_txt.getText().toString(), end_date_txt.getText().toString());
        System.out.println(days);

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
            String hour = "";
            if (hourOfDay > 11) {
                // If the hour is greater than or equal to 12
                // Then we subtract 12 from the hour to make it 12 hour format time

                hour_of_12_hour_format = hourOfDay - 12;

            } else {
                hour_of_12_hour_format = hourOfDay;
            }

            if (hour_of_12_hour_format < 10) {
                hour = "0" + hour_of_12_hour_format;
                hourFinal = hour;
            } else {
                hourFinal = String.valueOf(hour_of_12_hour_format);
            }

            String m = "";
            if (minute < 10) {
                m = "0" + minute;
                minuteFinal = m;
            } else {
                minuteFinal = String.valueOf(minute);
            }

            switch (Tag) {
                case "0":
                    sun_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    sun_start_add_session_btn.setText("x");
                    sun_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sun_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "1":
                    sun_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    sun_end_add_session_btn.setText("x");
                    sun_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sun_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "2":
                    mon_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    mon_start_add_session_btn.setText("x");
                    mon_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    mon_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "3":
                    mon_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    mon_end_add_session_btn.setText("x");
                    mon_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    mon_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "4":
                    tue_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    tue_start_add_session_btn.setText("x");
                    tue_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    tue_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "5":
                    tue_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    tue_end_add_session_btn.setText("x");
                    tue_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    tue_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "6":
                    wed_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    wed_start_add_session_btn.setText("x");
                    wed_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    wed_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "7":
                    wed_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    wed_end_add_session_btn.setText("x");
                    wed_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    wed_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "8":
                    thu_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    thu_start_add_session_btn.setText("x");
                    thu_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    thu_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "9":
                    thu_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    thu_end_add_session_btn.setText("x");
                    thu_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    thu_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "10":
                    fri_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    fri_start_add_session_btn.setText("x");
                    fri_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    fri_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "11":
                    fri_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    fri_end_add_session_btn.setText("x");
                    fri_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    fri_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "12":
                    sat_start_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    sat_start_add_session_btn.setText("x");
                    sat_start_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sat_start_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                case "13":
                    sat_end_time_txt.setText(hourFinal + ":" + minuteFinal + " " + status);
                    sat_end_add_session_btn.setText("x");
                    sat_end_add_session_btn.setTextColor(getResources().getColor(R.color.search_boder));
                    sat_end_add_session_btn.setBackground(getResources().getDrawable(R.drawable.round_red_btn));
                    break;
                default:
            }
        }

    }

    public void getSelectedSessionTimeValue() {
        sessionNameStr = addSessionBinding.sessionNameEdt.getText().toString();
        lessionTypeNameStr = addSessionBinding.subjectEdt.getText().toString();
        boardStr = addSessionBinding.boardNameEdt.getText().toString();
        standardStr = addSessionBinding.standardEdt.getText().toString();
        streamStr = addSessionBinding.streamEdt.getText().toString();
        address1Str = addSessionBinding.addressEdt.getText().toString();
        address2Str = addSessionBinding.addressEdt.getText().toString();
        regionStr = addSessionBinding.areaEdt.getText().toString();
        cityStr = addSessionBinding.cityEdt.getText().toString();
        stateStr = addSessionBinding.stateEdt.getText().toString();
        zipcodeStr = addSessionBinding.zipcodeEdt.getText().toString();
        descriptionStr = addSessionBinding.descriptionEdt.getText().toString();
        sessioncapacityStr = addSessionBinding.sportsEdt.getText().toString();
        alerttimeStr = addSessionBinding.alertBtn.getText().toString();
        sessionamtStr = addSessionBinding.sessionPriceEdt.getText().toString();
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
                        Util.dismissDialog();
                        Util.ping(mContext, "Session created Successfully.");
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

    private Map<String, String> getNewSessionDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("CoachID", coachIdStr);
        map.put("SessionTypeID", sessiontypeStr);
        map.put("SessionName", sessionNameStr);
        map.put("Board", boardStr);
        map.put("Standard", standardStr);
        map.put("Stream", streamStr);
        map.put("LessionTypeName", lessionTypeNameStr);
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
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);
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
        ArrayList<String> BoardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            BoardName.add(dataResponse.getData().get(j).getBoardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, BoardName);
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
        ArrayList<String> StandardName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StandardName.add(dataResponse.getData().get(j).getStandardName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StandardName);
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

        ArrayList<String> StreamName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            StreamName.add(dataResponse.getData().get(j).getStreamName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, StreamName);
        addSessionBinding.streamEdt.setThreshold(1);
        addSessionBinding.streamEdt.setAdapter(adapterTerm);
    }

    //Use for Lession
    public void callLessionApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
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
                        Util.dismissDialog();
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

        ArrayList<String> LessionName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            LessionName.add(dataResponse.getData().get(j).getLessonTypeName());
        }

        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, LessionName);
        addSessionBinding.subjectEdt.setThreshold(1);
        addSessionBinding.subjectEdt.setAdapter(adapterTerm);
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
        ArrayList<String> AreaName = new ArrayList<String>();
        for (int j = 0; j < dataResponse.getData().size(); j++) {
            AreaName.add(dataResponse.getData().get(j).getRegionName());
        }
        ArrayAdapter<String> adapterTerm = new ArrayAdapter<String>(mContext, R.layout.autocomplete_layout, AreaName);
        addSessionBinding.areaEdt.setThreshold(1);
        addSessionBinding.areaEdt.setAdapter(adapterTerm);
    }

    //Use for EditSession
    public void callEditSessionApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_SessionDetailBySessionID(getEditSessionDeatil(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel editsessionInfo, Response response) {
                    Util.dismissDialog();
                    if (editsessionInfo == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (editsessionInfo.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (editsessionInfo.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (editsessionInfo.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        if (editsessionInfo.getData().size() > 0) {
                            dataResponse = editsessionInfo;
                            fillEditSessionFiled();
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

    private Map<String, String> getEditSessionDeatil() {
        Map<String, String> map = new HashMap<>();
        map.put("CoachID", coachIdStr);//coachIdStr
        map.put("SessionID", SeslectedsessionID);
        return map;
    }

    public void fillEditSessionFiled() {

        ArrayList<String> editTime = new ArrayList<>();
        boolean isEnable = false;
        String studentString = "";
        for (int i = 0; i < dataResponse.getData().size(); i++) {
            switch (dataResponse.getData().get(i).getSessionType()) {
                case "1":
                    addSessionBinding.recurringRb.setChecked(true);
                    break;
                case "2":
                    addSessionBinding.singleRb.setChecked(true);
                    break;
                default:
            }
            addSessionBinding.sessionNameEdt.setText(dataResponse.getData().get(i).getSessionName());
            addSessionBinding.boardNameEdt.setText(dataResponse.getData().get(i).getBoard());
            addSessionBinding.standardEdt.setText(dataResponse.getData().get(i).getStandard());
            addSessionBinding.streamEdt.setText(dataResponse.getData().get(i).getStream());
            addSessionBinding.subjectEdt.setText(dataResponse.getData().get(i).getLessionTypeName());
            EditStartDateStr = dataResponse.getData().get(i).getStartDate();
            EditEndDateStr = dataResponse.getData().get(i).getEndDate();
            EditScheduleStr = dataResponse.getData().get(i).getSchedule();
            startDateStr = EditStartDateStr;
            endDateStr = EditEndDateStr;
            String[] spiltPipes = EditScheduleStr.split("\\|");
            String[] spiltComma;
            String[] spiltDash;
            Log.d("spilt", "" + spiltPipes.length);
            for (int j = 0; j < spiltPipes.length; j++) {
                spiltComma = spiltPipes[i].split("\\,");
                if (!isEnable) {
                    studentString = spiltComma[0] + "," + spiltComma[1];
                    isEnable = true;
                } else {
                    studentString = studentString + "|" + spiltComma[0] + "," + spiltComma[1];
                }
                editTime.add(studentString);
            }
            for (String s : editTime) {
                if (!s.equals("")) {
                    scheduleStr = scheduleStr + "|" + s;
                }
            }
            scheduleStr = scheduleStr.substring(1, scheduleStr.length());
            Log.d("scheduleStr ", scheduleStr);


//            if (!studentAvailable.equalsIgnoreCase("0")) {
////                addSessionBinding.addSessionBtn.performClick();
//            } else {
//                addSessionBinding.addSessionBtn.performClick();
//            }
            addSessionBinding.addressEdt.setText(dataResponse.getData().get(i).getAddressLine1());
            addSessionBinding.areaEdt.setText(dataResponse.getData().get(i).getRegionName());
            addSessionBinding.cityEdt.setText(dataResponse.getData().get(i).getAddressCity());
            addSessionBinding.stateEdt.setText(dataResponse.getData().get(i).getAddressState());
            addSessionBinding.zipcodeEdt.setText(dataResponse.getData().get(i).getAddressZipCode());
            addSessionBinding.descriptionEdt.setText(dataResponse.getData().get(i).getDescription());
            if (dataResponse.getData().get(i).getSessionAmount().equalsIgnoreCase("0.00")) {
                addSessionBinding.freeRb.setChecked(true);
            } else {
                addSessionBinding.paidRb.setChecked(true);
                addSessionBinding.sessionPriceEdt.setText(dataResponse.getData().get(i).getSessionAmount());
            }
            addSessionBinding.sportsEdt.setText(dataResponse.getData().get(i).getSessionCapacity());
            addSessionBinding.alertBtn.setText(dataResponse.getData().get(i).getAlertTime());
        }
    }

    //Use for Update Session
    public void callUpdateSessionApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Update_Session(getUpdateSessionDetail(), new retrofit.Callback<SessionDetailModel>() {
                @Override
                public void success(SessionDetailModel updatesessionDetailModel, Response response) {
                    Util.dismissDialog();
                    if (updatesessionDetailModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (updatesessionDetailModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (updatesessionDetailModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (updatesessionDetailModel.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        Util.ping(mContext, "Session Update Successfully.");
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

    private Map<String, String> getUpdateSessionDetail() {
//        getSelectedSessionTimeValue();
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", SeslectedsessionID);
        map.put("CoachID", coachIdStr);//coachIdStr
        map.put("SessionTypeID", sessiontypeStr);
        map.put("SessionName", sessionNameStr);
        map.put("Board", boardStr);
        map.put("Standard", standardStr);
        map.put("Stream", streamStr);
        map.put("LessionTypeName", lessionTypeNameStr);
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
        map.put("StartDate", startDateStr);
        map.put("EndDate", endDateStr);
        map.put("Schedule", scheduleStr);
        return map;
    }


//    public static AddSessionFragment newInstance() {
//
//        if (fragment == null) {
//            fragment = new AddSessionFragment();
//        }
//        return fragment;
//    }

    public void editSessionValidation() {
//        sessionamtStr = addSessionBinding.sessionPriceEdt.getText().toString();
        getSelectedSessionTimeValue();
        if (!CoachTypeStr.equalsIgnoreCase("1")) {
            if (flag.equalsIgnoreCase("edit")) {
                if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                    if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                        if (!address1Str.equalsIgnoreCase("")) {
                            if (!regionStr.equalsIgnoreCase("")) {
                                if (!cityStr.equalsIgnoreCase("")) {
                                    if (!stateStr.equalsIgnoreCase("")) {
                                        if (!zipcodeStr.equalsIgnoreCase("")) {
                                            if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                if (!sessionamtStr.equalsIgnoreCase("")) {
                                                    callUpdateSessionApi();
                                                } else {
                                                    addSessionBinding.sessionPriceEdt.setError("Please enter session amount.");
                                                }
                                            } else {
                                                addSessionBinding.sportsEdt.setError("Please enter session capacity.");
                                            }
                                        } else {
                                            addSessionBinding.zipcodeEdt.setError("Please enter zipcode.");
                                        }
                                    } else {
                                        addSessionBinding.stateEdt.setError("Please enter session state.");
                                    }
                                } else {
                                    addSessionBinding.cityEdt.setError("Please enter session city.");
                                }
                            } else {
                                addSessionBinding.areaEdt.setError("Please enter session area.");
                            }
                        } else {
                            addSessionBinding.addressEdt.setError("Please enter session address.");
                        }
                    } else {
                        addSessionBinding.subjectEdt.setError("Please enter lession name.");
                    }
                } else {
                    addSessionBinding.sessionNameEdt.setError("Please enter session name.");
                }
            } else {
                if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                    if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                        if (!startDateStr.equalsIgnoreCase("")) {
                            if (!endDateStr.equalsIgnoreCase("")) {
                                if (!scheduleStr.equalsIgnoreCase("")) {
                                    if (!address1Str.equalsIgnoreCase("")) {
                                        if (!regionStr.equalsIgnoreCase("")) {
                                            if (!cityStr.equalsIgnoreCase("")) {
                                                if (!stateStr.equalsIgnoreCase("")) {
                                                    if (!zipcodeStr.equalsIgnoreCase("")) {
                                                        if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                            if (!sessionamtStr.equalsIgnoreCase("")) {
                                                                callCreateSessionApi();
                                                            } else {
                                                                addSessionBinding.sessionPriceEdt.setError("Please enter session amount.");
                                                            }
                                                        } else {
                                                            addSessionBinding.sportsEdt.setError("Please enter session capacity.");
                                                        }
                                                    } else {
                                                        addSessionBinding.zipcodeEdt.setError("Please enter zipcode.");
                                                    }
                                                } else {
                                                    addSessionBinding.stateEdt.setError("Please enter session state.");
                                                }
                                            } else {
                                                addSessionBinding.cityEdt.setError("Please enter session city.");
                                            }
                                        } else {
                                            addSessionBinding.areaEdt.setError("Please enter session area.");
                                        }
                                    } else {
                                        addSessionBinding.addressEdt.setError("Please enter session address.");
                                    }
                                } else {
                                    addSessionBinding.sessionTimeTxt.setError("Please enter session time.");
                                }
                            } else {
                                Util.ping(mContext, "Please select session EndDate.");
                            }
                        } else {
                            Util.ping(mContext, "Please select session StartDate.");
                        }
                    } else {
                        addSessionBinding.subjectEdt.setError("Please enter lession name.");
                    }
                } else {
                    addSessionBinding.sessionNameEdt.setError("Please enter session name.");
                }
            }
        } else {
            if (flag.equalsIgnoreCase("edit")) {
                if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                    if (!boardStr.equalsIgnoreCase("")) {
                        if (!standardStr.equalsIgnoreCase("")) {
                            if (!streamStr.equalsIgnoreCase("")) {
                                if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                                    if (!address1Str.equalsIgnoreCase("")) {
                                        if (!regionStr.equalsIgnoreCase("")) {
                                            if (!cityStr.equalsIgnoreCase("")) {
                                                if (!stateStr.equalsIgnoreCase("")) {
                                                    if (!zipcodeStr.equalsIgnoreCase("")) {
                                                        if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                            if (!sessionamtStr.equalsIgnoreCase("")) {
                                                                callUpdateSessionApi();
                                                            } else {
                                                                addSessionBinding.sessionPriceEdt.setError("Please enter session amount.");
                                                            }
                                                        } else {
                                                            addSessionBinding.sportsEdt.setError("Please enter session capacity.");
                                                        }
                                                    } else {
                                                        addSessionBinding.zipcodeEdt.setError("Please enter zipcode.");
                                                    }
                                                } else {
                                                    addSessionBinding.stateEdt.setError("Please enter session state.");
                                                }
                                            } else {
                                                addSessionBinding.cityEdt.setError("Please enter session city.");
                                            }
                                        } else {
                                            addSessionBinding.areaEdt.setError("Please enter session area.");
                                        }
                                    } else {
                                        addSessionBinding.addressEdt.setError("Please enter session address.");
                                    }
                                } else {
                                    addSessionBinding.subjectEdt.setError("Please enter lession name.");
                                }
                            } else {
                                addSessionBinding.streamEdt.setError("Please enter stream name.");
                            }
                        } else {
                            addSessionBinding.standardEdt.setError("Please enter standard name.");
                        }
                    } else {
                        addSessionBinding.boardNameEdt.setError("Please enter board name.");
                    }
                } else {
                    addSessionBinding.sessionNameEdt.setError("Please enter session name.");
                }
            } else {
                if (!coachIdStr.equalsIgnoreCase("") && !sessionNameStr.equalsIgnoreCase("")) {
                    if (!boardStr.equalsIgnoreCase("")) {
                        if (!standardStr.equalsIgnoreCase("")) {
                            if (!streamStr.equalsIgnoreCase("")) {
                                if (!lessionTypeNameStr.equalsIgnoreCase("")) {
                                    if (!startDateStr.equalsIgnoreCase("")) {
                                        if (!endDateStr.equalsIgnoreCase("")) {
                                            if (!scheduleStr.equalsIgnoreCase("")) {
                                                if (!address1Str.equalsIgnoreCase("")) {
                                                    if (!regionStr.equalsIgnoreCase("")) {
                                                        if (!cityStr.equalsIgnoreCase("")) {
                                                            if (!stateStr.equalsIgnoreCase("")) {
                                                                if (!zipcodeStr.equalsIgnoreCase("")) {
                                                                    if (!sessioncapacityStr.equalsIgnoreCase("")) {
                                                                        if (!sessionamtStr.equalsIgnoreCase("")) {
                                                                            callCreateSessionApi();
                                                                        } else {
                                                                            addSessionBinding.sessionPriceEdt.setError("Please enter session amount.");
                                                                        }
                                                                    } else {
                                                                        addSessionBinding.sportsEdt.setError("Please enter session capacity.");
                                                                    }
                                                                } else {
                                                                    addSessionBinding.zipcodeEdt.setError("Please enter zipcode.");
                                                                }
                                                            } else {
                                                                addSessionBinding.stateEdt.setError("Please enter session state.");
                                                            }
                                                        } else {
                                                            addSessionBinding.cityEdt.setError("Please enter session city.");
                                                        }
                                                    } else {
                                                        addSessionBinding.areaEdt.setError("Please enter session area.");
                                                    }
                                                } else {
                                                    addSessionBinding.addressEdt.setError("Please enter session address.");
                                                }
                                            } else {
                                                addSessionBinding.sessionTimeTxt.setError("Please enter session time.");
                                            }
                                        } else {
                                            Util.ping(mContext, "Please select session EndDate.");
                                        }
                                    } else {
                                        Util.ping(mContext, "Please select session StartDate.");
                                    }
                                } else {
                                    addSessionBinding.subjectEdt.setError("Please enter lession name.");
                                }
                            } else {
                                addSessionBinding.streamEdt.setError("Please enter stream name.");
                            }
                        } else {
                            addSessionBinding.standardEdt.setError("Please enter standard.");
                        }
                    } else {
                        addSessionBinding.boardNameEdt.setError("Please enter board.");
                    }
                } else {
                    addSessionBinding.sessionNameEdt.setError("Please enter session name.");
                }
            }
        }

    }

    public void disableControl() {
        addSessionBinding.sessionTimeLinear.setVisibility(View.GONE);
        addSessionBinding.submitBtn.setVisibility(View.GONE);
        addSessionBinding.recurringRb.setEnabled(false);
        addSessionBinding.singleRb.setEnabled(false);
        addSessionBinding.sessionNameEdt.setEnabled(false);
        addSessionBinding.boardNameEdt.setEnabled(false);
        addSessionBinding.standardEdt.setEnabled(false);
        addSessionBinding.streamEdt.setEnabled(false);
        addSessionBinding.subjectEdt.setEnabled(false);
        addSessionBinding.addressEdt.setEnabled(false);
        addSessionBinding.areaEdt.setEnabled(false);
        addSessionBinding.cityEdt.setEnabled(false);
        addSessionBinding.stateEdt.setEnabled(false);
        addSessionBinding.zipcodeEdt.setEnabled(false);
        addSessionBinding.descriptionEdt.setEnabled(false);
        addSessionBinding.freeRb.setEnabled(false);
        addSessionBinding.paidRb.setEnabled(false);
        addSessionBinding.sportsEdt.setEnabled(false);
        addSessionBinding.alertBtn.setEnabled(false);
    }
}
