package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.wifi.android.runwifipassword.util.Root;
import com.wifi.android.runwifipassword.util.WifiInfo;
import com.wifi.android.runwifipassword.util.WifiManage;

import java.io.File;
import java.util.List;

public class WifiPasswordLookActivity extends Activity {

    private ListView listView;
    private List<WifiInfo> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wifi_password_look);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (Root.isDeviceRoots()){

            try {
                initData();
            } catch (Exception e) {
                e.printStackTrace();
            }

        }else {

            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("提示");
            builder.setMessage("当前手机未获得ROOT权限，请先ROOT");
            builder.setPositiveButton("我知道了", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                        finish();
                }
            });
            builder.setCancelable(false);
            builder.create().show();

        }


        initView();

    }

    private void initData() throws Exception {

        WifiManage wifiData = new WifiManage();
        data = wifiData.Read();
    }

    private void initView() {

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new myAdapter());
    }

    class myAdapter extends BaseAdapter{


        @Override
        public int getCount() {
            if (data!=null && data.size()>0) {
                return data.size();
            }else {

                return 0;
            }
        }

        @Override
        public Object getItem(int position) {
            return data.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            ViewHold viewHold =null;
            if (convertView==null){

                viewHold = new ViewHold();
                convertView = LayoutInflater.from(WifiPasswordLookActivity.this).inflate(R.layout.look_listview_item,null,false);
                viewHold.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                viewHold.tv_password = (TextView) convertView.findViewById(R.id.tv_password);

                convertView.setTag(viewHold);
            }else {

                viewHold = (ViewHold) convertView.getTag();
            }

            viewHold.tv_name.setText(data.get(position).Ssid);
            viewHold.tv_password.setText(data.get(position).Password);
            return convertView;
        }

        class ViewHold {

            TextView tv_name;
            TextView tv_password;
        }
    }


}
