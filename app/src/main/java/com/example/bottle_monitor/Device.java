package com.example.bottle_monitor;

import java.util.ArrayList;

public class Device {
    private static int cur_device_id = 0;
    private int id = 0;
    private float ls;
    private float sm_reading;
    private boolean on_off;
    private float rate;
    ArrayList<String> bottles = new ArrayList<String>();

    public Device(float ls, float sm, boolean on_off, float rate, ArrayList<String> bottles) {
        this.ls = ls;
        this.sm_reading = sm;
        this.on_off = on_off;
        this.rate = rate;
        this.bottles = bottles;
        this.id = cur_device_id++;
    }

    public Device() {

    }

    public ArrayList<String> getBottles() {
        return bottles;
    }

    public void setBottles(ArrayList<String> bottles) {
        this.bottles = bottles;
    }

    public static int getCur_device_id() {
        return cur_device_id;
    }

    public static void setCur_device_id(int cur_device_id) {
        Device.cur_device_id = cur_device_id;
    }

    public float getLs() {
        return ls;
    }

    public void setLs(float ls) {
        this.ls = ls;
    }

    public float getSm() {
        return sm_reading;
    }

    public void setSm(float sm) {
        this.sm_reading = sm;
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
