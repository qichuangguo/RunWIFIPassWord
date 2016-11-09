package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;

/**
 * 一款快速破解WiFi密码的软件，其中有四种破解方式可供选择，破解成功率依次增强，对破解的WiFi密码进行保存方便以后查看。
 * 还可以查看你手机已连接过得WiFi密码。
 * 还可以查询在公共场合一些未加密的WiFi。
 *
 *
 * 一款快速破解WiFi密码的软件，其中有四种破解方式可供选择，破解成功率依次增强，对破解的WiFi密码进行保存方便以后查看当前版本为1.0，可能存在一些bug,但是会慢慢的进行修复，
 */

public class WelcomeActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //取消标题栏
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        //取消状态栏
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_welcome);
        StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START, 1, false);
        StatService.start(this);
        ImageView iv = (ImageView) findViewById(R.id.iv);
        iv.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(WelcomeActivity.this,MainActivity.class));
                finish();
            }
        },3000);
        //
        //
    }
}
