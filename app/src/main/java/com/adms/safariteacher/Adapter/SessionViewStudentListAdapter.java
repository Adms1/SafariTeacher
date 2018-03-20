package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.adms.safariteacher.R;

import java.util.ArrayList;

/**
 * Created by admsandroid on 3/20/2018.
 */

public class SessionViewStudentListAdapter extends RecyclerView.Adapter<SessionViewStudentListAdapter.MyViewHolder> {

    private Context mContext;
    ArrayList<String> arrayList;

    public SessionViewStudentListAdapter(Context mContext, ArrayList<String> arrayList) {
        this.mContext = mContext;
        this.arrayList = arrayList;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView no_txt, name_txt;

        public MyViewHolder(View view) {
            super(view);
            no_txt = (TextView) view.findViewById(R.id.no_txt);
            name_txt = (TextView) view.findViewById(R.id.name_txt);
        }
    }

    @Override
    public SessionViewStudentListAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.view_session_row_item, parent, false);

        return new SessionViewStudentListAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final SessionViewStudentListAdapter.MyViewHolder holder, int position) {
        String str = String.valueOf(position + 1);
        holder.no_txt.setText(str);
        holder.name_txt.setText(arrayList.get(position));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }
}


