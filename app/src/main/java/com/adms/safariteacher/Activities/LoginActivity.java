package com.adms.safariteacher.Activities;

import android.content.Context;
import android.content.Intent;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.adms.safariteacher.R;
import com.adms.safariteacher.databinding.ActivityLoginBinding;

public class LoginActivity extends AppCompatActivity {

    ActivityLoginBinding loginScreenBinding;
    Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loginScreenBinding = DataBindingUtil.setContentView(this, R.layout.activity_login);
        mContext = LoginActivity.this;
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
                Intent indashboard = new Intent(mContext, DashBoardActivity.class);
                startActivity(indashboard);
            }
        });
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
}
