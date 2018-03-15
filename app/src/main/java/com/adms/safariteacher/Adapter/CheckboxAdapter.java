package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.adms.safariteacher.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/15/2018.
 */

public class CheckboxAdapter extends RecyclerView.Adapter<CheckboxAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<Integer> checkboxArray;

    public CheckboxAdapter(Context mContext, ArrayList<Integer> checkboxArray) {
        this.mContext = mContext;
        this.checkboxArray = checkboxArray;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        CheckBox checkBox;

        public MyViewHolder(View view) {
            super(view);

            checkBox = (CheckBox) view.findViewById(R.id.checkbox);

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


    }

    @Override
    public int getItemCount() {
        return checkboxArray.size();
    }


//    public ArrayList<String> getTime() {
//        return getTime;
//    }
}



