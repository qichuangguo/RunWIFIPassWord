package com.wifi.android.runwifipassword.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.os.Environment;

import com.wifi.android.runwifipassword.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyData_File {
    private String DB_PATH = "";
    private String ASSET_NAME_1 = "password";
    private String ASSET_NAME_2 = "complex";
    private String ASSET_NAME_3 = "simpleness";
    private String ASSET_NAME_4 = "superpassword";
    private String DATAPATH_1 = DB_PATH + ASSET_NAME_1;
    private Context mContext;

    public CopyData_File(Context context) {
        this.mContext = context;
        DB_PATH = getSDPath() + "/";
        DATAPATH_1 = DB_PATH + ASSET_NAME_1;
    }

    public String getSDPath() {
        File sdDir = null;
        String path="";
        boolean sdCardExist = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
        if (sdCardExist) {
            sdDir = Environment.getExternalStorageDirectory();//获取跟目录
            path = sdDir + "/.reality";
            File file = new File(path);
            if (!file.exists())
                file.mkdir();

        }

        return path;
    }


    public void DoCopy(String db_path,String datapath_1,int category) {
        try {
            File file = new File(db_path);
            if (!file.exists()) {
                file.mkdir();
            }
            if (!(new File(datapath_1).exists())) {
                InputStream ips = this.mContext.getResources().openRawResource(category);
                FileOutputStream os = new FileOutputStream(datapath_1);
                byte[] buffer = new byte[1024 * 1024];
                int count = 0;
                while ((count = ips.read(buffer)) > 0) {
                    os.write(buffer, 0, count);
                }
                os.close();
                ips.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
