package com.adms.safariteacher.Adapter;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.adms.safariteacher.R;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by admsandroid on 3/15/2018.
 */

public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<Integer> checkboxArray;
    FragmentActivity activity;
    static String starttimeStr = "", endtimeStr;
    Button add_session_btn, end_session_btn;
    static TextView start_time_txt, end_time_txt;
    private static boolean isFromDate = false;

    public CheckboxAdapter(Context mContext, FragmentActivity activity, ArrayList<Integer> checkboxArray) {
        this.mContext = mContext;
        this.checkboxArray = checkboxArray;
        this.activity = activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {


        public MyViewHolder(View view) {
            super(view);

            add_session_btn = (Button) view.findViewById(R.id.add_session_btn);
            start_time_txt = (TextView) view.findViewById(R.id.start_time_txt);
            end_session_btn = (Button) view.findViewById(R.id.end_session_btn);
            end_time_txt = (TextView) view.findViewById(R.id.end_time_txt);

        }
    }

    @Override
    public CheckboxAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_checkbox, parent, false);

        return new CheckboxAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final CheckboxAdapter.MyViewHolder holder, final int position) {
        add_session_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate=true;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(activity.getFragmentManager(), "Select time");
            }
        });
        start_time_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate=false;
                TimePicker mTimePicker = new TimePicker();
                mTimePicker.show(activity.getFragmentManager(), "Select time");
            }
        });

//        if (!starttimeStr.equalsIgnoreCase("")) {
//       start_time_txt.setText(starttimeStr);
//            Log.d("startTime",starttimeStr);
//        }

    }

    @Override
    public int getItemCount() {
        return checkboxArray.size();
    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            TimePickerDialog tpd4 = new TimePickerDialog(getActivity(),
                    AlertDialog.THEME_HOLO_LIGHT, this, hour, minute, DateFormat.is24HourFormat(getActivity()));

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
                starttimeStr = hour_of_12_hour_format + ":" + minute + ":" + status;
            } else {
                endtimeStr = hour_of_12_hour_format + ":" + minute + ":" + status;
            }
            Log.d("startTime", starttimeStr + "endTime" + endtimeStr);
//            start_time_txt.setText(starttimeStr);
        }
    }
//    public ArrayList<String> getTime() {
//        return getTime;
//    }
}



