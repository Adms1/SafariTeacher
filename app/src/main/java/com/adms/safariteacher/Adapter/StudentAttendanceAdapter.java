package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.safariteacher.Model.Session.sessionDataModel;
import com.adms.safariteacher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admsandroid on 3/12/2018.
 */

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder> {

    private Context mContext;
    List<sessionDataModel> arrayList;

    public StudentAttendanceAdapter(Context mContext, List<sessionDataModel> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name_txt;
        EditText remark_txt;
        RadioGroup attendance_status_rg;
        RadioButton present_chk, absent_chk, leave_chk;

        public MyViewHolder(View view) {
            super(view);
            name_txt = (TextView) view.findViewById(R.id.name_txt);
            attendance_status_rg = (RadioGroup) view.findViewById(R.id.attendance_status_rg);
            present_chk = (RadioButton) view.findViewById(R.id.present_chk);
            absent_chk = (RadioButton) view.findViewById(R.id.absent_chk);
            leave_chk = (RadioButton) view.findViewById(R.id.leave_chk);
            remark_txt = (EditText) view.findViewById(R.id.remark_txt);

        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_student_attendance, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {

        final sessionDataModel session = arrayList.get(position);
        holder.name_txt.setText(session.getFirstName() + " " + session.getLastName());
        holder.remark_txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                session.setRemarks(holder.remark_txt.getText().toString());
            }
        });


        holder.attendance_status_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int RadioButtonId = holder.attendance_status_rg.getCheckedRadioButtonId();
                switch (RadioButtonId) {
                    case R.id.present_chk:
                        session.setCheckboxStatus("1");
                        break;

                    case R.id.absent_chk:
                        session.setCheckboxStatus("0");
                        break;

                    case R.id.leave_chk:
                        session.setCheckboxStatus("-1");
                        break;
                }

            }
        });

        switch (Integer.parseInt(session.getCheckboxStatus())) {
            case 1:
                holder.present_chk.setChecked(true);
                break;
            case 2:
                holder.absent_chk.setChecked(true);
                break;
            case 3:
                holder.leave_chk.setClickable(false);
                break;
            default:
        }
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

