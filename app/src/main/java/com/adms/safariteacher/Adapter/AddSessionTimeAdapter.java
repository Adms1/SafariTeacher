package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.safariteacher.R;
import com.adms.safariteacher.onViewClick;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/15/2018.
 */

public class AddSessionTimeAdapter extends RecyclerView.Adapter<AddSessionTimeAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> timegapArray;
    ArrayList<String> getTime = new ArrayList<>();
    ArrayList<Integer> checkboxArray;
    CheckboxAdapter checkboxAdapter;

    public AddSessionTimeAdapter(Context mContext, ArrayList<String> timegapArray, ArrayList<Integer> checkboxArray) {
        this.mContext = mContext;
        this.timegapArray = timegapArray;
        this.checkboxArray = checkboxArray;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView time_txt;
        RecyclerView checkbox_rcView;
        CheckBox mon_chk, tue_chk, wed_chk, thu_chk, fri_chk, sat_chk;

        public MyViewHolder(View view) {
            super(view);

            time_txt = (TextView) view.findViewById(R.id.time_txt);
            checkbox_rcView = (RecyclerView) view.findViewById(R.id.checkbox_rcView);
//            mon_chk = (CheckBox) view.findViewById(R.id.mon_chk);
//            tue_chk = (CheckBox) view.findViewById(R.id.mon_chk);
//            wed_chk = (CheckBox) view.findViewById(R.id.mon_chk);
//            thu_chk = (CheckBox) view.findViewById(R.id.mon_chk);
//            fri_chk = (CheckBox) view.findViewById(R.id.mon_chk);
//            sat_chk = (CheckBox) view.findViewById(R.id.mon_chk);
        }
    }

    @Override
    public AddSessionTimeAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.time_list_item, parent, false);

        return new AddSessionTimeAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final AddSessionTimeAdapter.MyViewHolder holder, final int position) {
        holder.time_txt.setText(timegapArray.get(position));
        checkboxAdapter = new CheckboxAdapter(mContext,checkboxArray);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(mContext,7);
        holder.checkbox_rcView.setLayoutManager(mLayoutManager);
        holder.checkbox_rcView.setItemAnimator(new DefaultItemAnimator());
        holder.checkbox_rcView.setAdapter(checkboxAdapter);
    }

    @Override
    public int getItemCount() {
        return timegapArray.size();
    }


    public ArrayList<String> getTime() {
        return getTime;
    }
}



