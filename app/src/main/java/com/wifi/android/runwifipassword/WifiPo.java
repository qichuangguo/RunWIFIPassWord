package com.wifi.android.runwifipassword;

/**
 * Created by chuangguo.qi on 2016/10/25.
 */

public class WifiPo {

    private String name;
    private String strength;
    private int type;
    private int state;
    private int netid;

    public int getNetid() {
        return netid;
    }

    public void setNetid(int netid) {
        this.netid = netid;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrength() {
        return strength;
    }

    public void setStrength(String strength) {
        this.strength = strength;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "WifiPo{" +
                "name='" + name + '\'' +
                ", strength='" + strength + '\'' +
                ", type='" + type + '\'' +
                ", state=" + state +
                '}';
    }
}
