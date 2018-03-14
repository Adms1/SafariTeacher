package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.safariteacher.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/12/2018.
 */

public class StudentAttendanceAdapter extends RecyclerView.Adapter<StudentAttendanceAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList;

    public StudentAttendanceAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        public MyViewHolder(View view) {
            super(view);

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

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

