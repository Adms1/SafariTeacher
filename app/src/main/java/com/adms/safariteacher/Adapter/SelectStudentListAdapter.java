package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.adms.safariteacher.Interface.onViewClick;
import com.adms.safariteacher.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/26/2018.
 */

public class SelectStudentListAdapter extends RecyclerView.Adapter<SelectStudentListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList;
    private onViewClick onViewClick;

    public SelectStudentListAdapter(Context mContext, ArrayList<String> arrayList, onViewClick onViewClick) {
        this.mContext = mContext;
        this.arrayList = arrayList;
        this.onViewClick = onViewClick;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {
        RadioGroup name_rg;
        RadioButton name_rb;
        Button add_child;

        public MyViewHolder(View view) {
            super(view);
            name_rg = (RadioGroup) view.findViewById(R.id.name_rg);
            name_rb = (RadioButton) view.findViewById(R.id.name_rb);
            add_child=(Button)view.findViewById(R.id.add_child);
        }
    }

    @Override
    public SelectStudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_item_select_student, parent, false);

        return new SelectStudentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SelectStudentListAdapter.MyViewHolder holder, int position) {
        if (position % 2 == 0) {
            holder.itemView.setBackgroundColor(Color.parseColor("#4dcecece"));
        } else {
            holder.itemView.setBackgroundColor(Color.parseColor("#e9e9e9"));
        }

//        holder.name_rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup radioGroup, int i) {
//                int RadioButtionId = holder.name_rg.getCheckedRadioButtonId();
//                switch (RadioButtionId) {
//                    case R.id.name_rb:
//                        onViewClick.getViewClick();
//                        break;
//                }
//            }
//        });
        holder.add_child.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onViewClick.getViewClick();
            }
        });
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}

