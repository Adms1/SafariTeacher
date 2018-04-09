package com.adms.safariteacher.Fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Adapter.PaymentSucessReportAdapter;
import com.adms.safariteacher.Adapter.SessionViewStudentListAdapter;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.FamilyDetailModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentAddFamilyBinding;
import com.adms.safariteacher.databinding.FragmentPaymentReportBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;


public class PaymentReportFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentPaymentReportBinding paymentReportBinding;
    private View rootView;
    private Context mContext;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    private static boolean isFromDate = false;
    PaymentSucessReportAdapter paymentSucessReportAdapter;
    List<FamilyDetailModel> paymentReportList;

    public PaymentReportFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        paymentReportBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_report, container, false);

        rootView = paymentReportBinding.getRoot();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        mContext = getActivity();

        ((DashBoardActivity) getActivity()).setActionBar(2, "true");
        initViews();
        setListners();

        return rootView;
    }

    public void initViews() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);

        paymentReportBinding.txtStartDate.setText(Util.getTodaysDate());
        paymentReportBinding.txtEndDate.setText(Util.getTodaysDate());
    }

    public void setListners() {
        paymentReportBinding.txtStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = true;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(PaymentReportFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        paymentReportBinding.txtEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isFromDate = false;
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(PaymentReportFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });
        paymentReportBinding.btnShow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callPaymentReportApi();
            }
        });
    }

    @Override
    public void onDateSet(com.wdullaer.materialdatetimepicker.date.DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        mDay = dayOfMonth;
        mMonth = monthOfYear + 1;
        mYear = year;
        String d, m, y;
        d = Integer.toString(mDay);
        m = Integer.toString(mMonth);
        y = Integer.toString(mYear);

        if (mDay < 10) {
            d = "0" + d;
        }
        if (mMonth < 10) {
            m = "0" + m;
        }

        MonthInt = d + "/" + m + "/" + y;
        if (isFromDate) {
            paymentReportBinding.txtStartDate.setText(MonthInt);
        } else {
            paymentReportBinding.txtEndDate.setText(MonthInt);
        }


    }

    //Use for Create Session
    public void callPaymentReportApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Payment_ByCoachID(getReportDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentInfoModel, Response response) {
                    Util.dismissDialog();
                    if (paymentInfoModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
                        paymentReportList = paymentInfoModel.getData();
                        if (paymentInfoModel.getData().size() > 0) {
                            paymentReportBinding.noRecordTxt.setVisibility(View.GONE);
                            paymentReportBinding.listLinear.setVisibility(View.VISIBLE);
                            paymentSucessReportAdapter = new PaymentSucessReportAdapter(mContext, paymentReportList);
                            RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(mContext);
                            paymentReportBinding.reportRcList.setLayoutManager(mLayoutManager);
                            paymentReportBinding.reportRcList.setItemAnimator(new DefaultItemAnimator());
                            paymentReportBinding.reportRcList.setAdapter(paymentSucessReportAdapter);
                        } else {
                            paymentReportBinding.noRecordTxt.setVisibility(View.VISIBLE);
                            paymentReportBinding.listLinear.setVisibility(View.GONE);
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

    private Map<String, String> getReportDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("CoachID", Util.getPref(mContext, "coachID"));
//        map.put("SessionTypeID", sessiontypeStr);
//        map.put("SessionName", sessionNameStr);

        return map;
    }
}

