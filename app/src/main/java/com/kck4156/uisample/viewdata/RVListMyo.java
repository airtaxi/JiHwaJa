package com.kck4156.uisample.viewdata;

import eu.darken.myolib.Myo;

public class RVListMyo {
    private String name;
    private String udid;
    private Myo myo;
    public RVListMyo(String name, String udid, Myo myo) {
        this.name = name;
        this.udid = udid;
        this.myo = myo;
    }

    public  void setName(String name) {this.name = name;}
    public  void setUdid(String udid) {this.name = udid;}

    public String getName() {
        return name;
    }

    public String getUdid() {
        return udid;
    }

    public Myo getMyo() {
        return myo;
    }
}
