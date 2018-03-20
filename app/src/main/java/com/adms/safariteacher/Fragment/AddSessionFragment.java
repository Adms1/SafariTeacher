package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
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
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.TextView;


import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.AddSessionTimeAdapter;
import com.adms.safariteacher.Adapter.AlertListAdapter;
import com.adms.safariteacher.Adapter.CheckboxAdapter;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentAddSessionBinding;
import com.adms.safariteacher.Interface.onViewClick;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


public class AddSessionFragment extends Fragment implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener {
    //implements com.wdullaer.materialdatetimepicker.date.DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener
    private FragmentAddSessionBinding addSessionBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;
//    String monthDisplayStr, MonthInt, TimeInt, finaldateStr;
//    String[] spiltmonth;
//    String[] spilttime;
//    int Year, Month, Day;
//    Calendar calendar;
//    private com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog;
//    int mYear, mMonth, mDay;
//    com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog;
//    int CalendarHour, CalendarMinute;
//    private static boolean isFromTime = false;

    //Use for Alert Dialog
    ArrayList<String> timegapArray;
    AlertListAdapter alertListAdapter;

    //Use for AddSession Time Dialog

    private AlertDialog alertDialogAndroid = null;
    TextView start_date_txt;
    TextView end_date_txt;
    static TextView sun_start_time_txt;
    static TextView sun_end_time_txt;
    TextView mon_start_time_txt;
    TextView mon_end_time_txt;
    TextView tue_start_time_txt;
    TextView tue_end_time_txt;
    TextView wed_start_time_txt;
    TextView wed_end_time_txt;
    TextView thu_start_time_txt;
    TextView thu_end_time_txt;
    TextView fri_end_time_txt;
    TextView fri_start_time_txt;
    TextView sat_end_time_txt;
    TextView sat_start_time_txt;
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
    ArrayList<String> hoursArray;
    AddSessionTimeAdapter addSessionTimeAdapter;
    ArrayList<Integer> checkboxArray;
    public Dialog popularDialog;
    String flag;

    public AddSessionFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addSessionBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_session, container, false);

        rootView = addSessionBinding.getRoot();
        mContext = getActivity().getApplicationContext();
        flag = getArguments().getString("flag");
        if (flag.equalsIgnoreCase("edit")) {
            ((DashBoardActivity) getActivity()).setActionBar(1, "edit");
        } else {
            ((DashBoardActivity) getActivity()).setActionBar(1, "add");
        }
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
//        addSessionBinding.dateLinear.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddSessionFragment.this, Year, Month, Day);
//                datePickerDialog.setThemeDark(false);
//                datePickerDialog.setOkText("Done");
//                datePickerDialog.showYearPickerFirst(false);
//                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
//                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
//            }
//        });
//
//        addSessionBinding.displayStarttimeTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isFromTime = true;
//                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AddSessionFragment.this, CalendarHour, CalendarMinute, false);
//                timePickerDialog.setThemeDark(false);
//                timePickerDialog.setOkText("Done");
//                timePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                timePickerDialog.setTitle("Select Time From TimePickerDialog");
//                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
//            }
//        });
//        addSessionBinding.displayEndtimeTxt.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                isFromTime = false;
//                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AddSessionFragment.this, CalendarHour, CalendarMinute, false);
//                timePickerDialog.setThemeDark(false);
//                timePickerDialog.setOkText("Done");
//                timePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                timePickerDialog.setTitle("Select Time From TimePickerDialog");
//                timePickerDialog.show(getActivity().getFragmentManager(), "Timepickerdialog");
//            }
//        });

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

        addSessionBinding.sessionCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
//                ComponentName cn;
//                if (isPackageInstalled("com.android.calendar", getActivity())) {
//                    Intent i = new Intent();
//                    cn = new ComponentName("com.android.calendar", "com.android.calendar.LaunchActivity");
//                    i.setComponent(cn);
//                    startActivity(i);
//                } else {
//                    Intent i = new Intent();
//                    cn = new ComponentName("com.google.android.calendar", "com.android.calendar.LaunchActivity");
//                    i.setComponent(cn);
//                    startActivity(i);
//                }
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

//    public void getMonthFun(int month) {
//        SimpleDateFormat monthParse = new SimpleDateFormat("MM");
//        SimpleDateFormat monthDisplay = new SimpleDateFormat("MMM");
//        try {
//            monthDisplayStr = monthDisplay.format(monthParse.parse(String.valueOf(month)));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        Log.d("month", "" + monthDisplayStr);
//    }

//    @Override
//    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
//        String hourString = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
//
//        String minuteString = minute < 10 ? "0" + minute : "" + minute;
//
//        String secondString = second < 10 ? "0" + second : "" + second;
//
//        String time = hourString + "/" + minuteString + "/" + secondString;
//        Log.d("hours", "" + hourOfDay);
//        Log.d("Selectedtime", time);
//
////        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:a");
////        String strDate = mdformat.format(calendar.getTime());
////        Log.d("Currenttime",strDate);
//        if (isFromTime) {
//            if (hourOfDay > 12) {
//                addSessionBinding.displayStarttimeTxt.setText(hourString + ":" + minuteString + " PM");
//            } else {
//                addSessionBinding.displayStarttimeTxt.setText(hourString + ":" + minuteString + " AM");
//            }
//            Log.d("Starttime", time);
//        } else {
//            if (hourOfDay > 12) {
//                addSessionBinding.displayEndtimeTxt.setText(hourString + ":" + minuteString + " PM");
//            } else {
//                addSessionBinding.displayEndtimeTxt.setText(hourString + ":" + minuteString + " AM");
//            }
//            Log.d("Endtime", time);
//        }
//
//        CalculateTime(addSessionBinding.displayStarttimeTxt.getText().toString(), addSessionBinding.displayEndtimeTxt.getText().toString());
//    }
//
//    public void CalculateTime(String stTime, String enTime) {
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH:mm");
//        Date startDate = null, endDate = null;
//        try {
//            startDate = simpleDateFormat.parse(stTime);
//            endDate = simpleDateFormat.parse(enTime);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//        long difference = endDate.getTime() - startDate.getTime();
//        if (difference < 0) {
//            Date dateMax = null, dateMin = null;
//            try {
//                dateMax = simpleDateFormat.parse("24:00");
//                dateMin = simpleDateFormat.parse("00:00");
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//            difference = (dateMax.getTime() - startDate.getTime()) + (endDate.getTime() - dateMin.getTime());
//        }
//        int days = (int) (difference / (1000 * 60 * 60 * 24));
//        int hours = (int) ((difference - (1000 * 60 * 60 * 24 * days)) / (1000 * 60 * 60));
//        int min = (int) (difference - (1000 * 60 * 60 * 24 * days) - (1000 * 60 * 60 * hours)) / (1000 * 60);
//        Log.d("log_tag", "Hours: " + hours + ", Mins: " + min);
//
//        addSessionBinding.displayDurationTxt.setText("Duration : " + hours + " hr");
//    }
//
//    @Override
//    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
//        mDay = dayOfMonth;
//        mMonth = monthOfYear + 1;
//        mYear = year;
//        String d, m, y;
//        d = Integer.toString(mDay);
//        m = Integer.toString(mMonth);
//        y = Integer.toString(mYear);
//
//        if (mDay < 10) {
//            d = "0" + d;
//        }
//        if (mMonth < 10) {
//            m = "0" + m;
//        }
//
//        MonthInt = d + "/" + m + "/" + y;
//        getMonthFun(Integer.parseInt(m));
//
//        addSessionBinding.displayDateTxt.setText(d + ", " + monthDisplayStr + " " + y);
//    }


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
//        popularDialog.getWindow().getAttributes().verticalMargin = 0.10F;
        wlp.gravity = Gravity.CENTER;
        window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        window.setAttributes(wlp);

        popularDialog.getWindow().setBackgroundDrawableResource(R.drawable.grid_shape);

        popularDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        popularDialog.setCancelable(false);
        popularDialog.setContentView(R.layout.add_session_dialog);
        popularDialog.show();
//        LayoutInflater lInflater = (LayoutInflater) mContext
//                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
//        final View layout = lInflater.inflate(R.layout.dialog_add_session_time, null);
//
//        AlertDialog.Builder alertDialogBuilderUserInput = new AlertDialog.Builder(getActivity());
//        alertDialogBuilderUserInput.setView(layout);
//
//        alertDialogAndroid = alertDialogBuilderUserInput.create();
//        alertDialogAndroid.setCancelable(false);
//        alertDialogAndroid.show();
//        Window window = alertDialogAndroid.getWindow();
//        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
//        WindowManager.LayoutParams wlp = window.getAttributes();
//
//        wlp.gravity = Gravity.CENTER;
//        wlp.flags = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
//        window.setAttributes(wlp);
//        alertDialogAndroid.show();


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
        sat_end_time_txt = (TextView) popularDialog.findViewById(R.id.sat_start_time_txt);
        sat_start_time_txt = (TextView) popularDialog.findViewById(R.id.sat_end_time_txt);
        sun_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        mon_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        tue_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        wed_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        thu_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        fri_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        sat_start_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        sun_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        mon_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        tue_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        wed_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        thu_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        fri_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);
        sat_end_linear = (LinearLayout) popularDialog.findViewById(R.id.sun_start_linear);


        day_name_rcView = (RecyclerView) popularDialog.findViewById(R.id.day_name_rcView);

        done_btn = (Button) popularDialog.findViewById(R.id.done_btn);
        start_date_txt.setText(Util.getTodaysDate());
        end_date_txt.setText(Util.getTodaysDate());
        done_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popularDialog.dismiss();
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
        end_date_txt.setOnClickListener(new View.OnClickListener() {
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
        sun_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sun_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        mon_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        mon_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        tue_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        tue_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        wed_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        wed_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        thu_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        thu_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        fri_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        fri_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        sat_start_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });

        sat_end_linear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(getActivity().getFragmentManager(), "Select time");
            }
        });
        for (int i = 0; i < 6; i++) {
            Calendar sCalendar = Calendar.getInstance();

            String dayLongName = new SimpleDateFormat("EE").format(Calendar.MONDAY);
            Log.d("dayName", "" + dayLongName);
        }
//        String time = "07:00 AM";
        hoursArray = new ArrayList<>();

        hoursArray.add("Sunday");
        hoursArray.add("Monday");
        hoursArray.add("Tuesday");
        hoursArray.add("Wednesday");
        hoursArray.add("Thursday");
        hoursArray.add("Friday");
        hoursArray.add("Saturday");
//        try {
//            SimpleDateFormat sdf = new SimpleDateFormat("hh:mm a");
//            Date dateObj = sdf.parse(time);
//
//            for (int i = 1; i <13; i++) {
//                Calendar calendar = Calendar.getInstance();
//                calendar.setTime(dateObj);
//                calendar.add(Calendar.HOUR, i);
//
//                String startTime = new SimpleDateFormat("hh:mm a").format(calendar.getTime()).split(" ")[0];
//
//                calendar.add(Calendar.HOUR, 1);
//
//                String endTime = new SimpleDateFormat("hh:mm a").format(calendar.getTime());
//
//                System.out.println("Time here " + startTime + "-" + endTime);
//
//                hoursArray.add(startTime + " - " + endTime);
//            }
//
//        } catch (final ParseException e) {
//            e.printStackTrace();
//        }
//
        checkboxArray = new ArrayList<>();
        for (int k = 0; k < 2; k++) {
            checkboxArray.add(k);
        }
//        addSessionTimeAdapter = new AddSessionTimeAdapter(mContext,getActivity(), hoursArray, checkboxArray);
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
//        day_name_rcView.setLayoutManager(mLayoutManager);
//        day_name_rcView.setItemAnimator(new DefaultItemAnimator());
//        day_name_rcView.setAdapter(addSessionTimeAdapter);

//        List<String> days = getDates("20/03/2018", "25/03/2018");
//        System.out.println(days);
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
        dateFinal = d + "/" + m + "/" + y;
        if (isFromDate) {
            start_date_txt.setText(dateFinal);
        } else {
            end_date_txt.setText(dateFinal);
        }
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
        return days;
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
//            displayCurrentTime.setText("Selected Time: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute));
//            if(hourOfDay>12){
//                displayCurrentTime.setText("Selected Time: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute)+" "+"PM");
//            }else{
//                displayCurrentTime.setText("Selected Time: " + String.valueOf(hourOfDay) + " : " + String.valueOf(minute)+" "+"AM");
//            }
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
            if (isFromDate) {
                sun_start_time_txt.setText(hour_of_12_hour_format + ":" + minute + ":" + status);
            } else {
                sun_end_time_txt.setText(hour_of_12_hour_format + ":" + minute + ":" + status);
            }

        }
    }
}

