package com.wifi.android.runwifipassword;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ListViewCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.sql.SQLException;
import java.util.List;

public class CrackPasswordLookActivity extends Activity {

    private List<CrackWifiPo> users;
    private String TAG="chuangguo.qi";
    private TextView tv_noData;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crack_password_look);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        DatabaseHelper helper = DatabaseHelper.getHelper(this);
        try {
            users = helper.getUserDao().queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        initView();

        }

    private void initView() {

        tv_noData = (TextView) findViewById(R.id.tv_noData);
        if (users.size()==0){
            tv_noData.setVisibility(View.VISIBLE);

        }else {
            tv_noData.setVisibility(View.GONE);

        }
        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new Myadapter());
    }

    class Myadapter extends BaseAdapter{


        @Override
        public int getCount() {
            if (users!=null && users.size()>0) {
                return users.size();
            }else {

                return 0;
            }
        }

        @Override
        public Object getItem(int i) {
            return users.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {

            view = LayoutInflater.from(CrackPasswordLookActivity.this).inflate(R.layout.look_listview_item,null,false);
            TextView tv_name= (TextView) view.findViewById(R.id.tv_name);
            TextView tv_password= (TextView) view.findViewById(R.id.tv_password);

            tv_name.setText(Stringformat(users.get(i).getName()));
            tv_password.setText(Stringformat(users.get(i).getPassword()));

            return view;
        }
    }

    public String Stringformat(String str){

        return str.substring(1,str.length()-1);
    }

}
