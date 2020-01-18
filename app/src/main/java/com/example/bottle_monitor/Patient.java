package com.example.bottle_monitor;

import java.util.ArrayList;

public class Patient {
    String name;
    String diseaseName;
    private int id;
    public static int curPatientId = 0;

    ArrayList<String> bottles = new ArrayList<String>();

    public Patient(){

    }

    public Patient(String name, String diseaseName, ArrayList<String> bottles) {
        this.name = name;
        this.diseaseName = diseaseName;
        this.bottles = bottles;
        this.id = curPatientId++;
    }

    public String getName() {
        return name;
    }

    public String getDiseaseName() {
        return diseaseName;
    }

    public ArrayList<String> getBottles() {
        return bottles;
    }
}
