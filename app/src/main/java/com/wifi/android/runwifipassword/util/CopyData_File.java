package com.wifi.android.runwifipassword.util;

import android.content.Context;
import android.os.Environment;

import com.wifi.android.runwifipassword.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class CopyData_File {
	private String DB_PATH = "";
	private String ASSET_NAME_1 = "password.txt";
	private String DATAPATH_1 = DB_PATH + ASSET_NAME_1;
	private Context mContext;
	
	public CopyData_File(Context context){
		 this.mContext = context;
		 DB_PATH=getSDPath()+"/";
		 DATAPATH_1=DB_PATH + ASSET_NAME_1;
	}
	
	public String getSDPath(){
	       File sdDir = null;
	       boolean sdCardExist = Environment.getExternalStorageState()
	                           .equals(Environment.MEDIA_MOUNTED);   //判断sd卡是否存在
	       if   (sdCardExist)   
	       {                               
	         sdDir = Environment.getExternalStorageDirectory();//获取跟目录
	         
	      }   
	       return sdDir.toString(); 
	       
	}
	
	public void DoCopy(){
		try{
			 File file = new File(DB_PATH);
			 if(!file.exists()){
				   file.mkdir();
			 }
			 if(!(new File(DATAPATH_1).exists())){
				 InputStream ips = this.mContext.getResources().openRawResource(R.raw.password);
				 FileOutputStream os = new FileOutputStream(DATAPATH_1);
				 byte[] buffer = new byte[1024*1024];
				 int count = 0;
 			     while((count = ips.read(buffer)) > 0){
 				    os.write(buffer,0,count);    
 			     }
 			     os.close();
 			     ips.close();	   
			 }
			 
		}catch(IOException e){
			 e.printStackTrace();
		}
		
	}

}
