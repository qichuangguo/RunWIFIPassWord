package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiConfiguration;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.wifi.android.runwifipassword.view.PinnedSectionListView;

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
    private String TAG = "chuangguo.qi";
    private List<WifiConfiguration> configuredNetworks;
    private List<WifiPo> myWifi;
    private List<WifiPo> crackWifi;
    private List<WifiPo> openWifi;
    public static final String WIFI_AUTH_OPEN = "";
    public static final String WIFI_AUTH_ROAM = "[ESS]";
    public static final String WIFI_AUTH_WPS = "[WPS][ESS]";
    private Comparator<WifiPo> comparator;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_crack);

        //初始化wifi管理器
        wifimanager = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        connectionInfo = wifimanager.getConnectionInfo();
        scanResults = wifimanager.getScanResults();
        configuredNetworks = wifimanager.getConfiguredNetworks();
        initView();
        initData();
        getData();
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
                int leve1 = Integer.parseInt(wifiPo.getStrength());
                int leve2 = Integer.parseInt(t1.getStrength());
                if (leve1>leve2){
                    return -1;
                }else if (leve1<leve2){

                    return 1;
                }else {
                    return 0;
                }
            }
        };

    }


    public void getData(){

        if (scanResults==null){
            return;
        }

        FIFL:for (int i = 0; i <scanResults.size() ; i++) {

            WifiPo wifipo = new WifiPo();
            String scanSSID = scanResults.get(i).SSID;
            if (scanSSID.length()<1){
                continue;
            }

            int level = WifiManager.calculateSignalLevel(scanResults.get(i).level,
                    100);
            String capabilities=scanResults.get(i).capabilities.trim();

            if (capabilities != null && (capabilities.equals(WIFI_AUTH_OPEN) || capabilities.equals(WIFI_AUTH_ROAM)|| capabilities.equals(WIFI_AUTH_WPS))) {
                wifipo.setState(2);
                wifipo.setName(scanResults.get(i).SSID);
                wifipo.setType(1);
                wifipo.setStrength(String.valueOf(Math.abs(level)));
                openWifi.add(wifipo);
                continue FIFL;
            }

            for (int j = 0; j <configuredNetworks.size() ; j++) {
                String confguredSSID = configuredNetworks.get(j).SSID;

                if (confguredSSID.length()<1){
                    break;
                }
                confguredSSID=confguredSSID.substring(1,confguredSSID.length()-1);
                if (scanSSID.equals(confguredSSID)){
                    wifipo.setState(1);
                    wifipo.setName(scanResults.get(i).SSID);
                    wifipo.setType(1);
                    wifipo.setStrength(String.valueOf(Math.abs(level)));
                    myWifi.add(wifipo);
                    continue FIFL;
                }else {
                    wifipo.setState(0);
                }
            }
            wifipo.setName(scanResults.get(i).SSID);
            wifipo.setType(1);
            wifipo.setStrength(String.valueOf(Math.abs(level)));
            crackWifi.add(wifipo);
        }

        if (myWifi.size()>0) {
            Collections.sort(myWifi, comparator);
        }
        if (crackWifi.size()>0) {
            Collections.sort(crackWifi, comparator);
        }
        if (openWifi.size()>0) {
            Collections.sort(openWifi, comparator);
        }

        WifiPo wifi = new WifiPo();
        wifi.setName("我的wifi");
        wifi.setType(0);
        myWifi.add(0,wifi);
        if (myWifi.size()>=2) {
            listData.addAll(myWifi);
        }

        WifiPo wifi3 = new WifiPo();
        wifi3.setName("开放wifi");
        wifi3.setType(0);
        openWifi.add(0,wifi3);
        if (openWifi.size()>=2) {
            listData.addAll(openWifi);
        }

        WifiPo wifi2 = new WifiPo();
        wifi2.setName("未破解wifi");
        wifi2.setType(0);
        crackWifi.add(0,wifi2);
        if (crackWifi.size()>=2) {
            listData.addAll(crackWifi);
        }



        listView.setAdapter(new listViewAdaper());
    }
    /***
     * 初始化View
     */
    private void initView() {

        listView = (PinnedSectionListView) findViewById(R.id.listView);
        listView.setShadowVisible(false);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.i(TAG, "onItemClick: "+listData.get(i).getName());
                int state=listData.get(i).getState();
                if (state==0){//我的


                }else if (state==1){//破解



                }else if (state==2){//无需


                }
            }
        });
    }

    public class listViewAdaper extends BaseAdapter implements PinnedSectionListView.PinnedSectionListAdapter{


        @Override
        public int getCount() {
            if (listData!=null){
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
            ViewHold hold=null;
            if (view==null){
                hold = new ViewHold();
                if (listData.get(i).getType()==0) {
                    view = LayoutInflater.from(WifiCrackActivity.this).inflate(R.layout.listview_item_title, null, false);
                    hold.title_tv = (TextView) view.findViewById(R.id.titile_tv);
                    view.setTag(hold);
                }else {
                    view = LayoutInflater.from(WifiCrackActivity.this).inflate(R.layout.listview_item, null, false);
                    hold.textView = (TextView) view.findViewById(R.id.tv);
                    hold.strengt_tv= (TextView) view.findViewById(R.id.strengt_tv);
                    hold.classify_tv = (TextView) view.findViewById(R.id.classify_tv);
                    view.setTag(hold);
                }
            }else {

                hold= (ViewHold) view.getTag();
            }

            WifiPo wifipo = listData.get(i);
            if (listData.get(i).getType()==0) {
                hold.title_tv.setText(wifipo.getName());
            }else {
                hold.textView.setText(wifipo.getName());
                hold.strengt_tv.setText(wifipo.getStrength()+"%");
                if (listData.get(i).getState()==1){
                    hold.classify_tv.setText("已保存");

                }else if (listData.get(i).getState()==0){
                    hold.classify_tv.setText("需密码");
                }else if (listData.get(i).getState()==2){
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
            return listData.get(viewType).getType()==0?true : false;
        }

        class ViewHold{

            TextView textView,title_tv,strengt_tv,classify_tv;
        }
    }
}
