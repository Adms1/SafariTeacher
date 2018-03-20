package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.adms.safariteacher.R;

import org.w3c.dom.Text;

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
    FragmentActivity activity;

    public AddSessionTimeAdapter(Context mContext, FragmentActivity activity, ArrayList<String> timegapArray, ArrayList<Integer> checkboxArray) {
        this.mContext = mContext;
        this.timegapArray = timegapArray;
        this.checkboxArray = checkboxArray;
        this.activity = activity;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView day_btn;
        RecyclerView add_time_rcView;

        public MyViewHolder(View view) {
            super(view);

            day_btn = (TextView) view.findViewById(R.id.day_btn);
            add_time_rcView = (RecyclerView) view.findViewById(R.id.add_time_rcView);
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

        holder.day_btn.setText(timegapArray.get(position));
        Log.d("poisitionId", "" + position);

        checkboxAdapter = new CheckboxAdapter(mContext, activity,checkboxArray);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
        holder.add_time_rcView.setLayoutManager(mLayoutManager);
        holder.add_time_rcView.setItemAnimator(new DefaultItemAnimator());
        holder.add_time_rcView.setAdapter(checkboxAdapter);
    }

    @Override
    public int getItemCount() {
        return timegapArray.size();
    }


    public ArrayList<String> getTime() {
        return getTime;
    }
}



