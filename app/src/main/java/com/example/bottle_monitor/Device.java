package com.example.bottle_monitor;

import java.util.ArrayList;

public class Device {
    private static int device_id = 0;
    private float ls;
    private float sm;
    private boolean on_off;
    private float rate;
    ArrayList<String> bottles = new ArrayList<String>();

    public Device(float ls, float sm, boolean on_off, float rate, ArrayList<String> bottles) {
        this.ls = ls;
        this.sm = sm;
        this.on_off = on_off;
        this.rate = rate;
        this.bottles = bottles;
    }

    public Device() {

    }

    public ArrayList<String> getBottles() {
        return bottles;
    }

    public void setBottles(ArrayList<String> bottles) {
        this.bottles = bottles;
    }

    public static int getDevice_id() {
        return device_id;
    }

    public static void setDevice_id(int device_id) {
        Device.device_id = device_id;
    }

    public float getLs() {
        return ls;
    }

    public void setLs(float ls) {
        this.ls = ls;
    }

    public float getSm() {
        return sm;
    }

    public void setSm(float sm) {
        this.sm = sm;
    }

    public boolean isOn_off() {
        return on_off;
    }

    public void setOn_off(boolean on_off) {
        this.on_off = on_off;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }
}
