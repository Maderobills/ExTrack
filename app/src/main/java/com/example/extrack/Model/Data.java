package com.example.extrack.Model;

import java.util.MissingResourceException;
import java.util.prefs.PreferenceChangeEvent;

public class Data {
    private float amount;
    private String  paymethod,type, note, id, date;

    public Data(float amount,  String paymethod, String type, String note, String id, String date) {
        this.amount = amount;
        this.paymethod = paymethod;
        this.type = type;
        this.note = note;
        this.id = id;
        this.date = date;
    }

    public float getAmount() {
        return amount;
    }

    public void setAmount(float amount) {
        this.amount = amount;
    }

    public String getType() {
        return type;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Data(){

    }

}
