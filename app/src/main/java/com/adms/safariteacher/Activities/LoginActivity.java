package com.adms.safariteacher.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.adms.safariteacher.Model.TeacherInfo.TeacherInfoModel;
import com.adms.safariteacher.R;
import com.adms.safariteacher.Utility.ApiHandler;
import com.adms.safariteacher.Utility.AppConfiguration;
import com.adms.safariteacher.Utility.Util;
import com.adms.safariteacher.databinding.ActivityLoginBinding;
import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import retrofit.RetrofitError;
import retrofit.client.Response;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginScreenBinding;
    Context mContext;
    CallbackManager callbackManager;
    private LoginManager mLoginManager;
    private AccessTokenTracker mAccessTokenTracker;
    private boolean loggedin;

    String usernameStr, passwordStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;
        checkUnmPwd();

        // Init
        setupInit();

        // Facebook
        setupFacebook();


        setListner();
    }

    public void setListner() {
        loginScreenBinding.registerTxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent inregister = new Intent(mContext, RegistrationActivity.class);
                startActivity(inregister);
            }
        });
        loginScreenBinding.loginBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getInsertedValue();
                if (!usernameStr.equalsIgnoreCase("") && Util.isValidEmaillId(usernameStr)&&!passwordStr.equalsIgnoreCase("")&& passwordStr.length() >= 6 && passwordStr.length() <= 12) {
                        callTeacherLoginApi();
                } else {
                  Util.ping(mContext,"Invalid Email Address or Password.");
                }
            }
        });
        loginScreenBinding.facebookImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (loggedin) {
                    LoginManager.getInstance().logOut();
                    loggedin = false;
                } else {
                    mAccessTokenTracker.startTracking();
                    mLoginManager.logInWithReadPermissions(LoginActivity.this, Arrays.asList("public_profile", "email", "user_birthday"));
                }
            }
        });
    }

    private void setupInit() {
        FacebookSdk.sdkInitialize(getApplicationContext());
        loggedin = false;
    }

    private void setupFacebook() {
        mLoginManager = LoginManager.getInstance();
        callbackManager = CallbackManager.Factory.create();
        mAccessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                // handle
            }
        };

        LoginManager.getInstance().registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Util.ping(mContext, "HEllo Adms");
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });
        if (AccessToken.getCurrentAccessToken() != null) {
            requestObjectUser(AccessToken.getCurrentAccessToken());
        }
    }

    private void requestObjectUser(final AccessToken accessToken) {
        GraphRequest request = GraphRequest.newMeRequest(accessToken, new GraphRequest.GraphJSONObjectCallback() {
            @Override
            public void onCompleted(JSONObject object, GraphResponse response) {
                if (response.getError() != null) {
                    // handle error
                } else {
                    Toast.makeText(LoginActivity.this, "Access Token: " + accessToken.getToken(), Toast.LENGTH_SHORT).show();

                    loggedin = true;

//                    mFacebookText.setText("Log out");
                    Log.d("response", response.toString());
                    Log.d("First Name: ", "" + object.optString("first_name"));
                    Log.d("Last Name: ", "" + object.optString("last_name"));
                    Log.d("Email: ", "" + object.optString("email"));


                    try {
                        String userId = String.valueOf(object.get("id"));
                        String profilePic = "https://graph.facebook.com/" + userId + "/picture?type=large";
                        Log.d("Profile Pic", profilePic);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
        });

        Bundle parameters = new Bundle();
        parameters.putString("fields", "first_name,last_name,email");
        request.setParameters(parameters);
        request.executeAsync();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        finish();
        System.exit(0);
    }

    //Use for Login Teacher
    public void callTeacherLoginApi() {
        if (Util.isNetworkConnected(mContext)) {

            Util.showDialog(mContext);
            ApiHandler.getApiService().getTeacherLogin(getTeacherLoginDetail(), new retrofit.Callback<TeacherInfoModel>() {
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
                        Util.ping(mContext, "Invalid Email Address or Password.");
                        return;
                    }
                    if (teacherInfoModel.getSuccess().equalsIgnoreCase("True")) {
                        String[]splitCoachID=teacherInfoModel.getCoachID().split("\\,");
                        Util.setPref(mContext, "coachID",splitCoachID[0]);
                        Util.setPref(mContext,"coachTypeID",splitCoachID[1]);
                        AppConfiguration.coachId = teacherInfoModel.getCoachID();

                        Intent inLogin = new Intent(mContext, DashBoardActivity.class);
                        startActivity(inLogin);
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

    private Map<String, String> getTeacherLoginDetail() {
        Map<String, String> map = new HashMap<>();
        map.put("EmailAddress", usernameStr);
        map.put("Password", passwordStr);

        return map;
    }

    public void getInsertedValue() {
        usernameStr = loginScreenBinding.emailEdt.getText().toString();
        passwordStr = loginScreenBinding.passwordEdt.getText().toString();
    }

    public void checkUnmPwd() {
        if (!Util.getPref(mContext, "coachID").equalsIgnoreCase("")) {
            Intent intentDashboard = new Intent(LoginActivity.this, DashBoardActivity.class);
            startActivity(intentDashboard);
            finish();
        }
    }

}
