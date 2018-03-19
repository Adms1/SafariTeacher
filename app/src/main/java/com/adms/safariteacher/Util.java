package com.adms.safariteacher;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

/**
 * Created by admsandroid on 3/8/2018.
 */

public class Util {
    public static String getTodaysDate() {
        final Calendar calendar = Calendar.getInstance();
        int yy = calendar.get(Calendar.YEAR);
        int mm = calendar.get(Calendar.MONTH) + 1;
        int dd = calendar.get(Calendar.DAY_OF_MONTH);


        String mDAY, mMONTH, mYEAR;

        mDAY = Integer.toString(dd);
        mMONTH = Integer.toString(mm);
        mYEAR = Integer.toString(yy);

        if (dd < 10) {
            mDAY = "0" + mDAY;
        }
        if (mm < 10) {
            mMONTH = "0" + mMONTH;
        }

        return mDAY + "/" + mMONTH + "/" + mYEAR;
    }

    public static String getCurrentTime() {

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("HH:mm:a");
        String strDate = mdformat.format(calendar.getTime());
        Log.d("Currenttime",strDate);
        return strDate;
    }
    public static void ping(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void pong(Context context, String message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

}
