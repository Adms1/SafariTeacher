package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.databinding.DataBindingUtil;
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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.adms.safariteacher.Activities.DashBoardActivity;
import com.adms.safariteacher.Activities.DrawableCalendarEvent;
import com.adms.safariteacher.Adapter.SessionViewStudentListAdapter;
import com.adms.safariteacher.Model.Session.SessionDetailModel;
import com.adms.safariteacher.Model.Session.sessionDataModel;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentCalendarBinding;
import com.adms.safariteacher.databinding.FragmentPaymentBinding;
import com.github.tibolte.agendacalendarview.CalendarPickerController;
import com.github.tibolte.agendacalendarview.models.CalendarEvent;
import com.github.tibolte.agendacalendarview.models.DayItem;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.view.View.GONE;


public class PaymentSucessFragment extends Fragment {

    //    FragmentPaymentSucessBinding paymentSucessBinding;
    private View rootView;
    private Context mContext;

    String status;
    private ImageView imvSuccessFail;
    private TextView txtUserName, txtSucessFail, txtSucessFailDesc, txtTransactionID, txtValue, txtEmail;
    private Button btnSendReceipt, btnNewCharge;

    public PaymentSucessFragment() {
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        paymentSucessBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_payment_sucess, container, false);
        rootView = inflater.inflate(R.layout.fragment_payment_sucess, container, false);
//        rootView = paymentSucessBinding.getRoot();
        mContext = getActivity();
        ((DashBoardActivity) getActivity()).setActionBar(14, "false");

        init();
        setListner();
        callSessionPaymentApi();
        return rootView;
    }

    public void init() {

        txtUserName = (TextView) rootView.findViewById(R.id.txtUserName);
        txtSucessFail = (TextView) rootView.findViewById(R.id.txtSucessFail);
        txtSucessFailDesc = (TextView) rootView.findViewById(R.id.txtSucessFailDesc);
        txtTransactionID = (TextView) rootView.findViewById(R.id.txtTransactionID);
        txtValue = (TextView) rootView.findViewById(R.id.txtValue);
        btnNewCharge = (Button) rootView.findViewById(R.id.btnNewCharge);
        imvSuccessFail = (ImageView) rootView.findViewById(R.id.imvSuccessFail);


        txtUserName.setText(AppConfiguration.UserName);
        if (getArguments().getString("responseCode").equalsIgnoreCase("0")) {
            status = "success";
            imvSuccessFail.setImageResource(R.drawable.success_icon);
            txtSucessFail.setText("Success");
            txtSucessFailDesc.setText("Your transaction was successful");
            txtTransactionID.setText(getArguments().getString("transactionId"));
            txtValue.setText(getArguments().getString("amount"));
            btnNewCharge.setText("Done");


        } else {
            imvSuccessFail.setImageResource(R.drawable.fail_icon);
            status = "fail";
            txtSucessFail.setText("Failure");
            txtSucessFailDesc.setText("Your transaction was not successful\nPlease try again with another card.");
            txtTransactionID.setVisibility(GONE);
            txtValue.setVisibility(GONE);
            btnNewCharge.setText("Try Again");

        }


    }

    public void setListner() {
        btnNewCharge.setOnClickListener(new View.OnClickListener() {
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


    //Use for Family and Child Session Add PAyment
    public void callSessionPaymentApi() {
        if (Util.isNetworkConnected(mContext)) {

//            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Add_Payment(getSessionPaymentdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentInfoModel, Response response) {
                    Util.dismissDialog();
                    if (paymentInfoModel == null) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess() == null) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.dismissDialog();
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        Util.dismissDialog();
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

    private Map<String, String> getSessionPaymentdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("OrderID", getArguments().getString("order_id"));
        map.put("PaymentID", getArguments().getString("transactionId"));
        map.put("Status", status);
        return map;
    }


}

