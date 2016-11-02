package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;
import android.widget.Toast;

import android.support.v7.app.AppCompatActivity;

import com.wifi.android.runwifipassword.push.PMan;
import com.wifi.android.runwifipassword.util.SharedPrefsUtil;
import com.yow.PointListener;
import com.yow.YoManage;

import www.yiba.com.wifisdk.activity.YIbaWifiActivity;
import www.yiba.com.wifisdk.manager.WiFiSDKManager;

public class MainActivity extends Activity implements View.OnClickListener {

    private int myIntegral=0;
    private String TAG="chuangguo.qi";
    private TextView tv_integral;
    private boolean isNetwork=true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        YoManage.getInstance(this,"6f5f9c64467a90f2d1ece2a70f89fb83", "").init();
        BaMan.getInstance(this);

        PMan phMan =  PMan.get(getApplication(),"6f5f9c64467a90f2d1ece2a70f89fb83", "official");
        phMan.getMessage(this,true);

        IntentFilter mFilter = new IntentFilter();
        mFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(myNetReceiver, mFilter);

        BanView	banView2=new BanView(this);
        FrameLayout.LayoutParams layoutParams	=Tools.getBanLayoutParams(this);
        layoutParams.gravity= Gravity.BOTTOM|Gravity.CENTER_HORIZONTAL;
        this.addContentView(banView2,layoutParams);

        initView();
    }

    private void initView() {

        findViewById(R.id.bt_start_crack).setOnClickListener(this);
        findViewById(R.id.bt_look_password).setOnClickListener(this);
        findViewById(R.id.bt_integral).setOnClickListener(this);
        findViewById(R.id.bt_commonality).setOnClickListener(this);
        findViewById(R.id.bt_user).setOnClickListener(this);
        findViewById(R.id.bt_exit).setOnClickListener(this);

        tv_integral = (TextView) findViewById(R.id.tv_integral);
        tv_integral.setText(SharedPrefsUtil.getValue(MainActivity.this,"myIntegral",0) + "");
        YoManage.getPoints(new PointListener() {
            @Override
            public void getPointResult(int i) {
                Log.i(TAG, "getPointResult:getPointResult "+i);
                if (isNetwork) {
                    SharedPrefsUtil.putValue(MainActivity.this, "myIntegral", i);
                    tv_integral.setText(i + "");
                }
            }

            @Override
            public void givePointResult(int i, int i1) {
                Log.i(TAG, "getPointResult:givePointResult "+i+"i1--"+i1);
            }

            @Override
            public void consumePointResult(int i, int i1) {
                Log.i(TAG, "getPointResult:consumePointResult "+i+"i1--"+i1);
            }

            @Override
            public void rewardPointResult(String s, int i, int i1) {
                Log.i(TAG, "getPointResult:rewardPointResult "+s+"i---"+i+"i1--"+i1);
            }
        });
    }

    @Override
    public void onClick(View view) {

        int id = view.getId();
        Intent intent=null;

        if (id==R.id.bt_start_crack){

            intent = new Intent(this,WifiCrackActivity.class);
            startActivity(intent);


        }else if (id==R.id.bt_look_password){

            //Toast.makeText(MainActivity.this,"暂未开放",Toast.LENGTH_LONG).show();
            intent = new Intent(this,WifiPasswordLookActivity.class);
            startActivity(intent);

        }else if (id==R.id.bt_integral){

            intent = new Intent(MainActivity.this,IntegralMainActivity.class);
            startActivity(intent);

        }else if (id==R.id.bt_commonality){
            WiFiSDKManager.getInstance().setFreeWifiToggle( this , false );
            WiFiSDKManager.getInstance().setOpenWifiToggle( this , false );
            WiFiSDKManager.getInstance().setNotificationToggle( this , false );
           intent = new Intent( MainActivity.this , YIbaWifiActivity.class) ;
            startActivity( intent );

        }else if (id==R.id.bt_user){

            intent = new Intent(this,CrackPasswordLookActivity.class);
            startActivity(intent);

        }else if (id==R.id.bt_exit){

            finish();

        }

    }

    private BroadcastReceiver myNetReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {

            String action = intent.getAction();
            if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {

                ConnectivityManager mConnectivityManager = (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = mConnectivityManager.getActiveNetworkInfo();
                if(netInfo != null && netInfo.isAvailable()) {

                    /////////////网络连接
                    String name = netInfo.getTypeName();

                    if(netInfo.getType()==ConnectivityManager.TYPE_WIFI){
                        /////WiFi网络

                    }else if(netInfo.getType()==ConnectivityManager.TYPE_ETHERNET){
                        /////有线网络

                    }else if(netInfo.getType()==ConnectivityManager.TYPE_MOBILE){
                        /////////3g网络

                    }
                } else {
                    ////////网络断开
                    isNetwork=false;
                    Toast.makeText(MainActivity.this,"当前无可用网络",Toast.LENGTH_LONG).show();
                }
            }

        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(myNetReceiver!=null){
            unregisterReceiver(myNetReceiver);
        }
    }
}
