package com.example.bottle_monitor;

import java.util.Date;

public class Bottle {

    static int curBottleId = 0;

    String content;
    float quantity;
    String deviceAssigned;
    Date used;
    String patientId;
    String roomNo;
    private int id;

    public Bottle(){

    }

    public Bottle(String content, float quantity, String deviceAssigned, Date used, String patientId, String roomNo) {
        this.content = content;
        this.quantity = quantity;
        this.deviceAssigned = deviceAssigned;
        this.used = used;
        this.patientId = patientId;
        this.id = curBottleId++;
        this.roomNo = roomNo;
    }

    public String getRoomNo() {
        return roomNo;
    }

    public void setRoomNo(String roomNo) {
        this.roomNo = roomNo;
    }

    public static int getCurBottleId() {
        return curBottleId;
    }

    public String getContent() {
        return content;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getDeviceAssigned() {
        return deviceAssigned;
    }

    public Date getUsed() {
        return used;
    }

    public String getPatientId() {
        return patientId;
    }

    public int getId() {
        return id;
    }
}
