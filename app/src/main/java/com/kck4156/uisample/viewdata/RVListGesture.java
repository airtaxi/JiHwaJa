package com.kck4156.uisample.viewdata;

public class RVListGesture {
    public String name;
    public boolean isOrig;
    public Double[] emgData;
    public double[] origData;
    public RVListGesture(String name, boolean isOrig, Double[] emgData, double[] origData) {
        this.name = name;
        this.isOrig = isOrig;
        this.emgData = emgData;
        this.origData = origData;
    }
}