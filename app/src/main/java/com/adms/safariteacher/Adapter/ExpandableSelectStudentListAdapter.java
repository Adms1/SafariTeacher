package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioGroup;

import com.adms.safariteacher.Interface.onChlidClick;
import com.adms.safariteacher.Interface.onViewClick;
import com.adms.safariteacher.R;
import com.adms.safariteacher.databinding.AddStudentHeaderBinding;
import com.adms.safariteacher.databinding.ListGroupFamilyListBinding;
import com.adms.safariteacher.databinding.ListItemSelectStudentBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by admsandroid on 3/26/2018.
 */

public class ExpandableSelectStudentListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> _listDataHeader;
    private HashMap<String, List<String>> _listDataChild;
    private onViewClick onViewClick;
    private onChlidClick onChlidClick;

    public ExpandableSelectStudentListAdapter(Context mContext, List<String> listDataHeader, HashMap<String, List<String>> listDataChild, onChlidClick onChlidClick, onViewClick session) {
        this.mContext = mContext;
        this._listDataChild = listDataChild;
        this._listDataHeader = listDataHeader;
        this.onViewClick = session;
        this.onChlidClick = onChlidClick;
    }


    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(int groupPosition, final int childPosition,
                             boolean isLastChild, View convertView, ViewGroup parent) {
        final ListItemSelectStudentBinding itembinding;
        AddStudentHeaderBinding headerBinding;

        if (childPosition > 0) {// && childPosition < getChildrenCount(groupPosition) - 1

            itembinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.list_item_select_student, parent, false);
            convertView = itembinding.getRoot();
            itembinding.nameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int RadioButtonid = itembinding.nameRg.getCheckedRadioButtonId();
                    switch (RadioButtonid) {
                        case R.id.name_rb:
                            onViewClick.getViewClick();
                            break;
                    }

                }
            });

        } else {//if (childPosition == 0)
            headerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.add_student_header, parent, false);
            convertView = headerBinding.getRoot();
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition));
    }

    @Override
    public int getGroupCount() {
        return this._listDataHeader.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded,
                             View convertView, ViewGroup parent) {
        ListGroupFamilyListBinding groupbinding;
        String headerTitle = (String) getGroup(groupPosition);
        String[] spiltValue = headerTitle.split("\\|");
        if (convertView == null) {

        }
        groupbinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                R.layout.list_group_family_list, parent, false);
        convertView = groupbinding.getRoot();

        if (groupPosition % 4 == 0) {
            convertView.setBackgroundResource(R.drawable.first_row);
        } else if (groupPosition % 4 == 1) {
            convertView.setBackgroundResource(R.drawable.second_row);
        } else if (groupPosition % 4 == 2) {
            convertView.setBackgroundResource(R.drawable.third_row);
        } else {
            convertView.setBackgroundResource(R.drawable.fourth_row);
        }
        String sr = String.valueOf(groupPosition + 1);

        if (isExpanded) {
            groupbinding.arrowImg.setBackgroundResource(R.drawable.round_yello);
            groupbinding.arrowImg.setImageResource(R.drawable.arrow_1_42_up);
        } else {
            groupbinding.arrowImg.setBackgroundResource(R.drawable.round_yello);
            groupbinding.arrowImg.setImageResource(R.drawable.arrow_1_42_down);
        }

        groupbinding.addchildTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChlidClick.getChilClick();
            }
        });
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}



