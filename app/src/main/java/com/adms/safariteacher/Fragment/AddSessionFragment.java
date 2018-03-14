package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.adms.safariteacher.Adapter.AlertListAdapter;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Util;
import com.adms.safariteacher.databinding.FragmentAddSessionBinding;
import com.adms.safariteacher.onViewClick;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class AddSessionFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private FragmentAddSessionBinding addSessionBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
    String monthDisplayStr, MonthInt, TimeInt, finaldateStr;
    String[] spiltmonth;
    String[] spilttime;
    int Year, Month, Day;
    Calendar calendar;
    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
    int CalendarHour, CalendarMinute;
    private static boolean isFromTime = false;

    //Use for Alert Dialog
    ArrayList<String> timegapArray;
    AlertListAdapter alertListAdapter;

    public AddSessionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addSessionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_session, container, false);

        rootView = addSessionBinding.getRoot();
        mContext = getActivity().getApplicationContext();

        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {
        MonthInt = Util.getTodaysDate();
        Log.d("Date", MonthInt);
        spiltmonth = MonthInt.split("\\/");
        getMonthFun(Integer.parseInt(spiltmonth[1]));

        TimeInt = Util.getCurrentTime();
        Log.d("Time", TimeInt);
        spilttime = TimeInt.split("\\:");

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
        CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = calendar.get(Calendar.MINUTE);
        addSessionBinding.displayStarttimeTxt.setText(Util.getCurrentTime());
        addSessionBinding.displayEndtimeTxt.setText(Util.getCurrentTime());


//        if (Integer.parseInt(spilttime[0]) > 12) {
//            addSessionBinding.displayStarttimeTxt.setText(Util.getCurrentTime() + "PM");
//            addSessionBinding.displayEndtimeTxt.setText(Util.getCurrentTime() + "PM");
//        } else {
//            addSessionBinding.displayStarttimeTxt.setText(Util.getCurrentTime() + "AM");
//            addSessionBinding.displayEndtimeTxt.setText(Util.getCurrentTime() + "AM");
//        }
        finaldateStr = spiltmonth[0] + ", " + monthDisplayStr + " " + spiltmonth[2];
        addSessionBinding.displayDateTxt.setText(finaldateStr);

        timegapArray = new ArrayList<String>();
        timegapArray.add("none");
        for (int i = 1; i < 12; i++) {
            timegapArray.add(String.valueOf(i * 5) + " minit");
        }
        timegapArray.add("1 hours");
        timegapArray.add("1 day");
        Log.d("timegapArray", "" + timegapArray);

    }

    public void setListners() {
        addSessionBinding.dateLinear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        addSessionBinding.displayStarttimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromTime = true;
                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AddSessionFragment.this, CalendarHour, CalendarMinute, false);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setOkText("Done");
                timePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                timePickerDialog.setTitle("Select Time From TimePickerDialog");
                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });
        addSessionBinding.displayEndtimeTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromTime = false;
                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AddSessionFragment.this, CalendarHour, CalendarMinute, false);
                timePickerDialog.setThemeDark(false);
                timePickerDialog.setOkText("Done");
                timePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                timePickerDialog.setTitle("Select Time From TimePickerDialog");
                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
            }
        });
        addSessionBinding.fessStatusRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int radioButtonId = addSessionBinding.fessStatusRg.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.free_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.GONE);
                        break;
                    case R.id.paid_rb:
                        addSessionBinding.sessionPriceEdt.setVisibility(View.VISIBLE);
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
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;

        String minuteString = minute < 10 ? "0" + minute : "" + minute;

        String secondString = second < 10 ? "0" + second : "" + second;

        String time = hourString + "/" + minuteString + "/" + secondString;
        Log.d("hours", "" + hourOfDay);
        Log.d("Selectedtime", time);

//        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:a");
//        String strDate = mdformat.format(calendar.getTime());
//        Log.d("Currenttime",strDate);
        if (isFromTime) {
            if (hourOfDay > 12) {
                addSessionBinding.displayStarttimeTxt.setText(hourString + ":" + minuteString + " PM");
            } else {
                addSessionBinding.displayStarttimeTxt.setText(hourString + ":" + minuteString + " AM");
            }
            Log.d("Starttime", time);
        } else {
            if (hourOfDay > 12) {
                addSessionBinding.displayEndtimeTxt.setText(hourString + ":" + minuteString + " PM");
            } else {
                addSessionBinding.displayEndtimeTxt.setText(hourString + ":" + minuteString + " AM");
            }
            Log.d("Endtime", time);
        }

        CalculateTime(addSessionBinding.displayStarttimeTxt.getText().toString(), addSessionBinding.displayEndtimeTxt.getText().toString());
    }

    public void CalculateTime(String stTime, String enTime) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
        Date startDate = null, endDate = null;
        try {
            startDate = simpleDateFormat.parse(stTime);
            endDate = simpleDateFormat.parse(enTime);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        long difference = endDate.getTime() - startDate.getTime();
        if (difference < 0) {
            Date dateMax = null, dateMin = null;
            try {
                dateMax = simpleDateFormat.parse("24:00");
                dateMin = simpleDateFormat.parse("00:00");

            } catch (ParseException e) {
                e.printStackTrace();
            }

            difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
        }
        int days = (int) (difference / (1000 * 60 * 60 * 24));
        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
        Log.d("log_tag", "Hours: " + hours + ", Mins: " + min);

        addSessionBinding.displayDurationTxt.setText("Duration : " + hours + " hr");
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
        getMonthFun(Integer.parseInt(m));

        addSessionBinding.displayDateTxt.setText(d + ", " + monthDisplayStr + " " + y);
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
                }, 400);

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

}

