package com.adms.safariteacher.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adms.safariteacher.R;
import com.adms.safariteacher.databinding.ActivityRegistrationBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

public class RegistrationActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    ActivityRegistrationBinding registrationBinding;
    Context mContext;
    String selectedValue = "Parents", flag, finalDate;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        registrationBinding = DataBindingUtil.setContentView(this, R.layout.activity_registration);
        mContext = RegistrationActivity.this;

        init();
        setListner();
    }

    public void init() {

        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
//        registrationBinding.dateOfBirthEdt.setText(Util.getTodaysDate());
    }

    public void setListner() {
        registrationBinding.back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inback = new Intent(mContext, LoginActivity.class);
//                inback.putExtra("flag", flag);
                startActivity(inback);
            }
        });

        registrationBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (selectedValue.equalsIgnoreCase("Parents")) {
//                    Intent inaddstudent = new Intent(mContext, AddStudentScreen.class);
//                    inaddstudent.putExtra("flag", flag);
//                    startActivity(inaddstudent);
//                }
            }
        });
        registrationBinding.dateOfBirthEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(RegistrationActivity.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getFragmentManager(), "Datepickerdialog");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent inback = new Intent(mContext, LoginActivity.class);
//        inback.putExtra("flag", flag);
        startActivity(inback);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
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

        finalDate = d + "/" + m + "/" + y;

        registrationBinding.dateOfBirthEdt.setText(finalDate);
    }
}
