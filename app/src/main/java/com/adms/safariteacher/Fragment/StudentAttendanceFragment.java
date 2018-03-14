package com.adms.safariteacher.Fragment;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.adms.safariteacher.Adapter.StudentAttendanceAdapter;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Util;
import com.adms.safariteacher.databinding.FragmentAddSessionBinding;
import com.adms.safariteacher.databinding.FragmentStudentAttendanceBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;


public class StudentAttendanceFragment extends Fragment implements DatePickerDialog.OnDateSetListener{

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

    public StudentAttendanceFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        studentAttendanceBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_student_attendance, container, false);

        rootView = studentAttendanceBinding.getRoot();
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

        finaldateStr = spiltmonth[0] + ", " + monthDisplayStr + " " + spiltmonth[2];
        studentAttendanceBinding.dateTxt.setText(Util.getTodaysDate());

        arrayList = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayList.add(String.valueOf(i));
        }
        Log.d("arrayList",""+arrayList.size());
        studentAttendanceAdapter = new StudentAttendanceAdapter(mContext, arrayList);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
        studentAttendanceBinding.studentListRcView.setLayoutManager(mLayoutManager);
        studentAttendanceBinding.studentListRcView.setItemAnimator(new DefaultItemAnimator());
        studentAttendanceBinding.studentListRcView.setAdapter(studentAttendanceAdapter);
    }

    public void setListners() {
        studentAttendanceBinding.dateTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(StudentAttendanceFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
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
}

