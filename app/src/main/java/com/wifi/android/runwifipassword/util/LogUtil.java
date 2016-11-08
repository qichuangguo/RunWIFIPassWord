package com.wifi.android.runwifipassword.util;

import android.util.Log;

/**
 * Created by chuangguo.qi on 2016/11/7.
 */

public class LogUtil {

    private static boolean isShowLog=false;
    private static String tag="chuangguo.qi";
    public static void i(String messager){
        if (isShowLog) {
            Log.i(tag, messager);
        }
    }
}
