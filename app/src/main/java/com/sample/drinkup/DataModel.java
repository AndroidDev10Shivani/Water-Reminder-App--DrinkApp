package com.sample.drinkup;

public class DataModel {

    String hmtime;
    String ampmtype;

    public DataModel(String hmtime, String ampmtype) {
        this.hmtime=hmtime;
        this.ampmtype=ampmtype;
    }

    public DataModel(String hmtime) {
        this.hmtime=hmtime;
    }

    public String getHmtime() {
        return hmtime;
    }

    public String getAmpmtype() {
        return ampmtype;
    }

}
