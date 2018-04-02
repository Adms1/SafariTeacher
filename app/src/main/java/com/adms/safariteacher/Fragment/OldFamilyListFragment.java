package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.TextView;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.ExpandableSelectStudentListAdapter;
import com.adms.safariteacher.Interface.onChlidClick;
import com.adms.safariteacher.Interface.onViewClick;
import com.adms.safariteacher.Model.TeacherInfo.ChildDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.FamilyDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentOldFamilyListBinding;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class OldFamilyListFragment extends Fragment {

    private FragmentOldFamilyListBinding oldFamilyListBinding;
    private View rootView;
    private Context mContext;
    private Fragment fragment = null;
    private FragmentManager fragmentManager = null;

    List<FamilyDetailModel> finalFamilyDetail;
    List<String> listDataHeader;
    HashMap<String, List<ChildDetailModel>> listDataChild;
    private int lastExpandedPosition = -1;
    ExpandableSelectStudentListAdapter expandableSelectStudentListAdapter;

    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_name_txt, location_txt, duration_txt, time_txt, session_fee_txt;
    String familyIdStr = "", contatIDstr, orderIDStr, sessionIDStr;
    ArrayList<String> selectedId;

    public OldFamilyListFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        oldFamilyListBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_old_family_list, container, false);

        rootView = oldFamilyListBinding.getRoot();
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(13, "false");
        sessionIDStr = Util.getPref(mContext, "SessionID");
        Log.d("sessionID", sessionIDStr);
        initViews();
        setListners();
        callFamilyListApi();
        return rootView;
    }

    public void initViews() {


    }

    public void setListners() {

        oldFamilyListBinding.addFamily.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new AddFamilyFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                Bundle args = new Bundle();
                args.putString("session", "10");
                args.putString("type", "Family");
                fragment.setArguments(args);
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        oldFamilyListBinding.lvExpfamilylist.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int groupPosition) {
                if (lastExpandedPosition != -1
                        && groupPosition != lastExpandedPosition) {
                    oldFamilyListBinding.lvExpfamilylist.collapseGroup(lastExpandedPosition);
                }
                lastExpandedPosition = groupPosition;
            }

        });
        oldFamilyListBinding.sessionCal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new SessionFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
    }


    public void ConformationDialog() {
        confimDialog = new Dialog(getActivity(), R.style.Theme_Dialog);
        Window window = confimDialog.getWindow();
        WindowManager.LayoutParams wlp = window.getAttributes();
        confimDialog.getWindow().getAttributes().verticalMargin = 0.10f;
        wlp.gravity = Gravity.CENTER;
        window.setAttributes(wlp);

        confimDialog.getWindow().setBackgroundDrawableResource(R.drawable.session_confirm);

        confimDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        confimDialog.setCancelable(false);
        confimDialog.setContentView(R.layout.confirm_session_dialog);

        session_student_txt = (TextView) confimDialog.findViewById(R.id.session_student_txt);
        session_name_txt = (TextView) confimDialog.findViewById(R.id.session_name_txt);
        location_txt = (TextView) confimDialog.findViewById(R.id.location_txt);
        duration_txt = (TextView) confimDialog.findViewById(R.id.duration_txt);
        time_txt = (TextView) confimDialog.findViewById(R.id.time_txt);
        session_fee_txt = (TextView) confimDialog.findViewById(R.id.session_fee_txt);
        confirm_txt = (TextView) confimDialog.findViewById(R.id.confirm_txt);
        cancel_txt = (TextView) confimDialog.findViewById(R.id.cancel_txt);

        session_fee_txt.setText(AppConfiguration.SessionPrice);
        session_name_txt.setText(AppConfiguration.SessionName);
        location_txt.setText(AppConfiguration.SessionLocation);
        duration_txt.setText("Duration" + " : " + AppConfiguration.SessionDuration + " hr");
        time_txt.setText("Time"+" : "+ AppConfiguration.SessionTime);

        getsessionID();
        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("")) {
                    callSessionConfirmationApi();
                } else {
                    Util.ping(mContext, "Please fill all Detail");
                }
                confimDialog.dismiss();
            }
        });


        confimDialog.show();

    }

    //Use for Get FamilyList
    public void callFamilyListApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_FamiliyByFamilyID(getFamilyListDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel familyInfoModel, Response response) {
                    Util.dismissDialog();
                    if (familyInfoModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (familyInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        finalFamilyDetail = familyInfoModel.getData();
                        if (familyInfoModel.getData() != null) {
                            fillExpLV();
                            expandableSelectStudentListAdapter = new ExpandableSelectStudentListAdapter(getActivity(), listDataHeader, listDataChild, new onChlidClick() {
                                @Override
                                public void getChilClick() {
                                    getFamilyID();
                                    Fragment fragment = new AddFamilyFragment();
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    Bundle args = new Bundle();
                                    args.putString("session", "11");
                                    args.putString("type", "Child");
                                    args.putString("familyID", familyIdStr);
                                    fragment.setArguments(args);
                                    fragmentTransaction.replace(R.id.frame, fragment);
                                    fragmentTransaction.addToBackStack(null);
                                    fragmentTransaction.commit();
                                }
                            }, new onViewClick() {
                                @Override
                                public void getViewClick() {
                                    ConformationDialog();
                                }
                            });
                            oldFamilyListBinding.lvExpfamilylist.setAdapter(expandableSelectStudentListAdapter);
                            oldFamilyListBinding.lvExpfamilylist.expandGroup(0);
                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getFamilyListDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", "0");
        return map;
    }

    //Use for fill Family List
    public void fillExpLV() {
        listDataHeader = new ArrayList<>();
        listDataChild = new HashMap<String, List<ChildDetailModel>>();
        for (int i = 0; i < finalFamilyDetail.size(); i++) {
            listDataHeader.add(finalFamilyDetail.get(i).getFirstName() + "|"
                    + finalFamilyDetail.get(i).getLastName() + "|"
                    + finalFamilyDetail.get(i).getContactPhoneNumber() + "|"
                    + finalFamilyDetail.get(i).getFamilyID() + "|"
                    + finalFamilyDetail.get(i).getContactID());
            Log.d("header", "" + listDataHeader);
            ArrayList<ChildDetailModel> row = new ArrayList<ChildDetailModel>();
            for (int j = 0; j < finalFamilyDetail.get(i).getFamilyContact().size(); j++) {
//                if (finalFamilyDetail.get(i).getFamilyContact().get(i) != null) {
                row.add(finalFamilyDetail.get(i).getFamilyContact().get(j));
                Log.d("row", "" + row);
//                }


            }
            listDataChild.put(listDataHeader.get(i), row);
            Log.d("child", "" + listDataChild);
        }
    }

    public void getFamilyID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableSelectStudentListAdapter.getFamilyID();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
            familyIdStr = selectedId.get(i);
            Log.d("selectedIdStr", familyIdStr);
        }
    }

    public void getsessionID() {
        selectedId = new ArrayList<String>();

        selectedId = expandableSelectStudentListAdapter.getSessionDetail();
        Log.d("selectedId", "" + selectedId);
        for (int i = 0; i < selectedId.size(); i++) {
//            contatIDstr = selectedId.get(i);
            String[] spilt = selectedId.get(i).split("\\|");
            contatIDstr = spilt[2];
            session_student_txt.setText(spilt[0] + " " + spilt[1]);
            Log.d("selectedIdStr", contatIDstr);
        }
    }

    //Use for Family and Child Session Confirmation
    public void callSessionConfirmationApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Session_ContactEnrollment(getSessionConfirmationdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel sessionconfirmationInfoModel, Response response) {
                    Util.dismissDialog();
                    if (sessionconfirmationInfoModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (sessionconfirmationInfoModel.getSuccess().equalsIgnoreCase("True")) {
//                        orderIDStr = sessionconfirmationInfoModel.getContactID();
//                        if (!orderIDStr.equalsIgnoreCase("")) {
//                            Fragment fragment = new PaymentFragment();
//                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
//                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//                            Bundle args = new Bundle();
//                            args.putString("orderID", orderIDStr);
//                            fragment.setArguments(args);
//                            fragmentTransaction.replace(R.id.frame, fragment);
//                            fragmentTransaction.addToBackStack(null);
//                            fragmentTransaction.commit();
//                        }else{
//                            Util.ping(mContext,"orderID Not found.");
//                        }
                    }
                }

                @Override
                public void failure(RetrofitError error) {
                    Util.dismissDialog();
                    error.printStackTrace();
                    error.getMessage();
                    Util.ping(mContext, getString(R.string.something_wrong));
                }
            });
        } else {
            Util.ping(mContext, getString(R.string.internet_connection_error));
        }
    }

    private Map<String, String> getSessionConfirmationdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("SessionID", sessionIDStr);
        map.put("ContactID", contatIDstr);
        return map;
    }

}

