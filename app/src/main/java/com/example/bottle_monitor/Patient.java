package com.example.bottle_monitor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class Patient {
    String name;
    String diseaseName;
    private int id;
    public static int curPatientId = 0;

    ArrayList<Integer> bottles = new ArrayList<Integer>();

    public Patient(){

    }

    public Patient(String name, String diseaseName, ArrayList<Integer> bottles) {
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

    public ArrayList<Integer> getBottles() {
        return bottles;
    }

    public int getId() {
        return id;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        if(obj instanceof Patient){
            if(this.id == ((Patient) obj).getId()){
                return true;
            }
        }
        return false;
    }

    @NonNull
    @Override
    public String toString() {
        return this.id +" " + this.name + " "+ this.diseaseName;
    }
}
