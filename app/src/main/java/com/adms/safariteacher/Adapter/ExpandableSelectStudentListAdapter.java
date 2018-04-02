package com.adms.safariteacher.Adapter;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.RadioGroup;

import com.adms.safariteacher.Interface.onChlidClick;
import com.adms.safariteacher.Interface.onViewClick;
import com.adms.safariteacher.Model.TeacherInfo.ChildDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.FamilyDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.AddStudentHeaderBinding;
import com.adms.safariteacher.databinding.ListGroupFamilyListBinding;
import com.adms.safariteacher.databinding.ListItemSelectStudentBinding;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by admsandroid on 3/26/2018.
 */

public class ExpandableSelectStudentListAdapter extends BaseExpandableListAdapter {

    private Context mContext;
    private List<String> _listDataHeader;
    private HashMap<String, List<ChildDetailModel>> _listDataChild;
    private onViewClick onViewClick;
    private onChlidClick onChlidClick;
    String FamilyID;
    private ArrayList<String> familyIdCheck;
    private ArrayList<String> sesionDeatil;
    ArrayList<String> value;

    public ExpandableSelectStudentListAdapter(Context mContext, List<String> listDataHeader, HashMap<String, List<ChildDetailModel>> listDataChild, onChlidClick onChlidClick, onViewClick session) {
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
            final ChildDetailModel childDetail = getChild(groupPosition, childPosition - 1);
            itembinding.nameRb.setText(childDetail.getFirstName() + " " + childDetail.getLastName());

            if (childDetail.getGenderID().equalsIgnoreCase("1")) {
                itembinding.genderTxt.setText("Male");
            } else {
                itembinding.genderTxt.setText("Female");
            }
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat output = new SimpleDateFormat("dd/MM/yyyy");
            Date d = null;
            try {
                d = sdf.parse(childDetail.getDateofBirth());
            } catch (ParseException e) {
                e.printStackTrace();
            }
            String formatedate = output.format(d);
            itembinding.ageTxt.setText(formatedate);
            itembinding.phoneNoTxt.setText(childDetail.getContactPhoneNumber());
            itembinding.nameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup radioGroup, int i) {
                    int RadioButtonid = itembinding.nameRg.getCheckedRadioButtonId();
                    switch (RadioButtonid) {
                        case R.id.name_rb:
                            sesionDeatil = new ArrayList<String>();
                            sesionDeatil.add(childDetail.getFirstName()+"|"+childDetail.getLastName()+"|"+childDetail.getContactID());
                            onViewClick.getViewClick();
                            itembinding.nameRb.setChecked(false);
                            break;
                    }

                }
            });
            itembinding.phoneNoTxt.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean result = Util.checkPermission(mContext);
                    if (result) {

                    }
                    Intent intent = new Intent(Intent.ACTION_DIAL);
                    intent.setData(Uri.fromParts("tel", itembinding.phoneNoTxt.getText().toString(), null));
                    mContext.startActivity(intent);

                }
            });

        } else {
            headerBinding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()),
                    R.layout.add_student_header, parent, false);

            String headerTitle = (String) getGroup(groupPosition);
            String valueStr;
            value = new ArrayList<>();
            for (String key : _listDataChild.keySet()) {
                System.out.println("------------------------------------------------");
                System.out.println("Iterating or looping map using java5 foreach loop");
                System.out.println("key: " + key + " value: " + _listDataChild.get(key));
                value.add(key);


            }
            for (int i=0;i<value.size();i++) {
                if (headerTitle.equalsIgnoreCase(value.get(i))) {
                    valueStr = String.valueOf(_listDataChild.get(value.get(i)));
                    Log.d("value", valueStr);

                    if (valueStr.equalsIgnoreCase("[]")) {
                        headerBinding.tableRowNodata.setVisibility(View.VISIBLE);
                        headerBinding.tableRowHeader.setVisibility(View.GONE);
                    } else {
                        headerBinding.tableRowNodata.setVisibility(View.GONE);
                        headerBinding.tableRowHeader.setVisibility(View.VISIBLE);
                    }
                }
            }
            convertView = headerBinding.getRoot();
        }


        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition)).size() + 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this._listDataHeader.get(groupPosition);
    }

    @Override
    public ChildDetailModel getChild(int groupPosition, int childPosititon) {
        return this._listDataChild.get(this._listDataHeader.get(groupPosition))
                .get(childPosititon);
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
        final ListGroupFamilyListBinding groupbinding;
        String headerTitle = (String) getGroup(groupPosition);
        final String[] spiltValue = headerTitle.split("\\|");
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

        if (isExpanded) {
            groupbinding.arrowImg.setBackgroundResource(R.drawable.round_yello);
            groupbinding.arrowImg.setImageResource(R.drawable.arrow_1_42_up);
        } else {
            groupbinding.arrowImg.setBackgroundResource(R.drawable.round_yello);
            groupbinding.arrowImg.setImageResource(R.drawable.arrow_1_42_down);
        }
        groupbinding.familynameRb.setText(spiltValue[0] + " " + spiltValue[1]);
        groupbinding.noTxt.setText(spiltValue[2]);
        FamilyID = spiltValue[3];

        groupbinding.familynameRg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                int RadioButtonid = groupbinding.familynameRg.getCheckedRadioButtonId();
                switch (RadioButtonid) {
                    case R.id.familyname_rb:
                        sesionDeatil = new ArrayList<String>();
                        sesionDeatil.add(spiltValue[0]+"|"+spiltValue[1]+"|"+spiltValue[4]);
                        onViewClick.getViewClick();
                        break;
                }
            }
        });
        groupbinding.addchildTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                familyIdCheck = new ArrayList<String>();
                familyIdCheck.add(spiltValue[3]);
                onChlidClick.getChilClick();
            }
        });

        groupbinding.noTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean result = Util.checkPermission(mContext);
                if (result) {

                }
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.fromParts("tel", spiltValue[2], null));
                mContext.startActivity(intent);
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

    public ArrayList<String> getFamilyID() {
        return familyIdCheck;
    }

    public ArrayList<String> getSessionDetail() {
        return sesionDeatil;
    }


}



