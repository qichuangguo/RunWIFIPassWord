package com.wifi.android.runwifipassword.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class PasswordGetter {
    private String password;
    private File file;
    private FileReader reader;
    private BufferedReader br;
    
    public PasswordGetter(String passwordFile) throws FileNotFoundException {
        password = null;
        
        //File file = new File("/sdcard/password.txt");
        file = new File(passwordFile);
        if (!file.exists())
            throw new FileNotFoundException();
        reader = new FileReader(file);
        br = new BufferedReader(reader);
    }
    
    public void reSet(){
        try {
            br.close();
            reader.close();
            reader = new FileReader(file);
            br = new BufferedReader(reader);
        } catch (IOException e) {
            e.printStackTrace();
            password = null;
        }
    }
    
    public String getPassword(){
        try {
            password = br.readLine();
        } catch (IOException e) {
            e.printStackTrace();
            password = null;
        }
        return password;
    }
    
    public void Clean(){
        try {
            br.close();
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
