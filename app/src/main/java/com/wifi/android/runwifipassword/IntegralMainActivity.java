package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.yow.YoManage;

public class IntegralMainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_integral_main);
        YoManage.showOffer(this,null);
        finish();
    }
}
