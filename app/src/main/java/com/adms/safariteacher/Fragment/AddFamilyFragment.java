package com.adms.safariteacher.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.databinding.DataBindingUtil;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
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
import com.adms.safariteacher.Activities.LoginActivity;
import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.FragmentAddFamilyBinding;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddFamilyFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private FragmentAddFamilyBinding addFamilyBinding;
    private View rootView;
    private Context mContext;
    String MonthInt;
    int Year, Month, Day;
    Calendar calendar;
    private DatePickerDialog datePickerDialog;
    int mYear, mMonth, mDay;
    String pageTitle, type, firstNameStr, lastNameStr, emailStr = "", passwordStr, phonenoStr, gendarIdStr = "1", dateofbirthStr, contactTypeIDStr, familyIDStr, contatIDstr, orderIDStr, sessionIDStr;
    Dialog confimDialog;
    TextView cancel_txt, confirm_txt, session_student_txt, session_name_txt, location_txt, duration_txt, time_txt, session_fee_txt, session_student_txt_view;

    public AddFamilyFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addFamilyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_family, container, false);

        rootView = addFamilyBinding.getRoot();
        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        mContext = getActivity();
        pageTitle = getArguments().getString("session");
        type = getArguments().getString("type");
        familyIDStr = getArguments().getString("familyID");
        sessionIDStr = Util.getPref(mContext, "SessionID");
        ((DashBoardActivity) getActivity()).setActionBar(Integer.parseInt(pageTitle), "false");
        initViews();
        setListners();
        Log.d("type", type);
        return rootView;
    }

    public void initViews() {
        calendar = Calendar.getInstance();
        Year = calendar.get(Calendar.YEAR);
        Month = calendar.get(Calendar.MONTH);
        Day = calendar.get(Calendar.DAY_OF_MONTH);
    }

    public void setListners() {
        addFamilyBinding.dateOfBirthEdt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(AddFamilyFragment.this, Year, Month, Day);
                datePickerDialog.setThemeDark(false);
                datePickerDialog.setOkText("Done");
                datePickerDialog.showYearPickerFirst(false);
                datePickerDialog.setAccentColor(Color.parseColor("#f2552c"));
//                datePickerDialog.setTitle("Select Date From DatePickerDialog");
                datePickerDialog.show(getActivity().getFragmentManager(), "Datepickerdialog");
            }
        });

        addFamilyBinding.cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new OldFamilyListFragment();
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.frame, fragment);
                fragmentTransaction.addToBackStack(null);
                fragmentTransaction.commit();
            }
        });
        addFamilyBinding.emailEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                emailStr = addFamilyBinding.emailEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!emailStr.equalsIgnoreCase("") && Util.isValidEmaillId(emailStr)) {
                    } else {
                        addFamilyBinding.emailEdt.setError("Please Enter Valid Email Address.");
                    }

                }
                return false;
            }
        });
        addFamilyBinding.passwordEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                passwordStr = addFamilyBinding.passwordEdt.getText().toString();
                if (actionId == EditorInfo.IME_ACTION_NEXT) {
                    if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 6 && passwordStr.length() <= 12) {

                    } else {
                        addFamilyBinding.passwordEdt.setError("Password must be 6-12 Characters.");
                    }
                }
                return false;
            }
        });
        addFamilyBinding.genderGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
                int radioButtonId = addFamilyBinding.genderGroup.getCheckedRadioButtonId();
                switch (radioButtonId) {
                    case R.id.male_chk:
                        gendarIdStr = addFamilyBinding.maleChk.getTag().toString();
                        break;
                    case R.id.female_chk:
                        gendarIdStr = addFamilyBinding.femaleChk.getTag().toString();
                        break;
                    default:
                }
            }
        });

        addFamilyBinding.registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInsertedValue();
                if (!firstNameStr.equalsIgnoreCase("") && firstNameStr.length() > 3) {
                    if (!lastNameStr.equalsIgnoreCase("") && lastNameStr.length() > 3) {
                        if (!emailStr.equalsIgnoreCase("") && Util.isValidEmaillId(emailStr)) {
                            if (!passwordStr.equalsIgnoreCase("") && passwordStr.length() >= 6 && passwordStr.length() <= 12) {
                                if (!phonenoStr.equalsIgnoreCase("") && phonenoStr.length() >= 10) {
                                    if (!gendarIdStr.equalsIgnoreCase("")) {
                                        if (!dateofbirthStr.equalsIgnoreCase("") && Util.getAge(dateofbirthStr)) {
                                            if (type.equalsIgnoreCase("Family")) {
                                                callFamilyApi();
                                            } else {
                                                callNewChildApi();
                                            }
                                        } else {
                                            addFamilyBinding.dateOfBirthEdt.setError("Please Select Your Birth Date.");
                                        }
                                    } else {
                                        addFamilyBinding.femaleChk.setError("Select Gender.");
                                    }
                                } else {
                                    addFamilyBinding.phoneNoEdt.setError("Enter 10 digit Phone Number.");
                                }
                            } else {
                                addFamilyBinding.passwordEdt.setError("Password must be 6-12 Characters.");
                            }
                        } else {
                            addFamilyBinding.emailEdt.setError("Please Enter Valid Email Addres.");
                        }
                    } else {
                        addFamilyBinding.lastNameEdt.setError("Please Enter LastName.");
                    }
                } else {
                    addFamilyBinding.firstNameEdt.setError("Please Enter First Name");
                }
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
        addFamilyBinding.dateOfBirthEdt.setText(MonthInt);

    }

    //Use for New Family
    public void callFamilyApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_Create_Family(getNewFamilyetail(), new retrofit.Callback<TeacherInfoModel>() {
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

                        Util.setPref(mContext, "FamilyID", familyInfoModel.getContactID());
                        contatIDstr = familyInfoModel.getContactID();
//                        Util.ping(mContext, "Family Added Sucessfully.");
                        ConformationDialog();
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

    private Map<String, String> getNewFamilyetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", emailStr);
        map.put("Password", passwordStr);
        map.put("GenderID", gendarIdStr);
        map.put("DateOfBirth", dateofbirthStr);
        map.put("PhoneNumber", phonenoStr);
        return map;
    }

    //Use for Check Register emailId exist or not
    public void callCheckEmailIdApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().getCheckEmailAddress(getcheckEmailidDetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel teacherInfoModel, Response response) {
                    Util.dismissDialog();
                    if (teacherInfoModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        addFamilyBinding.emailEdt.setError("Already Exist!");
                        addFamilyBinding.emailEdt.setText("");
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

    private Map<String, String> getcheckEmailidDetail() {

        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", addFamilyBinding.emailEdt.getText().toString());

        return map;
    }


    public void getInsertedValue() {
        firstNameStr = addFamilyBinding.firstNameEdt.getText().toString();
        lastNameStr = addFamilyBinding.lastNameEdt.getText().toString();
        emailStr = addFamilyBinding.emailEdt.getText().toString();
        passwordStr = addFamilyBinding.passwordEdt.getText().toString();
        phonenoStr = addFamilyBinding.phoneNoEdt.getText().toString();
        dateofbirthStr = addFamilyBinding.dateOfBirthEdt.getText().toString();
        if (type.equalsIgnoreCase("Family")) {
            contactTypeIDStr = "1";
        } else {
            contactTypeIDStr = "3";
        }
    }

    //Use for Family add NewChild
    public void callNewChildApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_AddFamilyContact(getNewChildetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel childInfoModel, Response response) {
                    Util.dismissDialog();
                    if (childInfoModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (childInfoModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (childInfoModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (childInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        contatIDstr = childInfoModel.getContactID();
//                        Util.ping(mContext, "Child Added Sucessfully.");
                        ConformationDialog();
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

    private Map<String, String> getNewChildetail() {
        Map<String, String> map = new HashMap<>();
        map.put("FamilyID", familyIDStr);
        map.put("FirstName", firstNameStr);
        map.put("LastName", lastNameStr);
        map.put("EmailAddress", emailStr);
        map.put("Password", passwordStr);
        map.put("GenderID", gendarIdStr);
        map.put("DateOfBirth", dateofbirthStr);
        map.put("PhoneNumber", phonenoStr);
        map.put("ContactTypeID", contactTypeIDStr);
        return map;
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
        session_student_txt_view = (TextView) confimDialog.findViewById(R.id.session_student_txt_view);
        session_student_txt = (TextView) confimDialog.findViewById(R.id.session_student_txt);
        session_name_txt = (TextView) confimDialog.findViewById(R.id.session_name_txt);
        location_txt = (TextView) confimDialog.findViewById(R.id.location_txt);
        duration_txt = (TextView) confimDialog.findViewById(R.id.duration_txt);
        time_txt = (TextView) confimDialog.findViewById(R.id.time_txt);
        session_fee_txt = (TextView) confimDialog.findViewById(R.id.session_fee_txt);
        confirm_txt = (TextView) confimDialog.findViewById(R.id.confirm_txt);
        cancel_txt = (TextView) confimDialog.findViewById(R.id.cancel_txt);

        cancel_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                confimDialog.dismiss();
            }
        });
        confirm_txt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!contatIDstr.equalsIgnoreCase("") && !sessionIDStr.equalsIgnoreCase("") && !AppConfiguration.SessionPrice.equalsIgnoreCase("0.00")) {
                    callpaymentRequestApi();
                } else {
                   callSessionConfirmationApi();
                }
                confimDialog.dismiss();
            }
        });
        if (type.equalsIgnoreCase("Child")) {
            session_student_txt_view.setText("STUDENT NAME");
        } else {
            session_student_txt_view.setText("FAMILY NAME");
        }
        session_name_txt.setText(AppConfiguration.SessionName);
        location_txt.setText(AppConfiguration.SessionLocation);
        duration_txt.setText(AppConfiguration.SessionDuration);
        time_txt.setText(AppConfiguration.SessionTime);
        session_student_txt.setText(firstNameStr + " " + lastNameStr);
        session_fee_txt.setText("₹ " + AppConfiguration.SessionPrice);

        AppConfiguration.UserName = session_student_txt.getText().toString();
        confimDialog.show();

    }


    //Use for paymentRequest
    public void callpaymentRequestApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().get_GeneratePaymentRequest(getpaymentRequestdetail(), new retrofit.Callback<TeacherInfoModel>() {
                @Override
                public void success(TeacherInfoModel paymentRequestModel, Response response) {
                    Util.dismissDialog();
                    if (paymentRequestModel == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess() == null) {
                        Util.ping(mContext, getString(R.string.something_wrong));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("false")) {
                        Util.ping(mContext, getString(R.string.false_msg));
                        return;
                    }
                    if (paymentRequestModel.getSuccess().equalsIgnoreCase("True")) {
                        orderIDStr = paymentRequestModel.getOrderID();
                        Fragment fragment = new PaymentFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        Bundle args = new Bundle();
                        args.putString("orderID", orderIDStr);
                        args.putString("amount", AppConfiguration.SessionPrice);
                        args.putString("mode", "TEST");
                        args.putString("username", session_student_txt.getText().toString());
                        args.putString("sessionID", sessionIDStr);
                        args.putString("contactID", contatIDstr);
                        args.putString("type", type);
                        fragment.setArguments(args);
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

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

    private Map<String, String> getpaymentRequestdetail() {
        Map<String, String> map = new HashMap<>();
        map.put("ContactID", contatIDstr);
        map.put("SessionID", sessionIDStr);
        map.put("Amount", AppConfiguration.SessionPrice);
        return map;
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
                        if (type.equalsIgnoreCase("Child")) {
                            Util.ping(mContext, "Child Confirmation Successfully.");
                        } else {
                            Util.ping(mContext, "Family Confirmation Successfully.");
                        }
                        Fragment fragment = new OldFamilyListFragment();
                        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                        fragmentTransaction.replace(R.id.frame, fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

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

