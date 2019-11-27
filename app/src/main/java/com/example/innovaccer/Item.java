package com.example.innovaccer;

public class Item {

    private String gname, gid, gphone, itime, hname, hid, hphone, hadd, id;

    public Item(String gname, String gid, String gphone, String itime, String hname, String hid, String hphone, String hadd, String id) {
        this.gname = gname;
        this.gid = gid;
        this.gphone = gphone;
        this.itime = itime;
        this.hname = hname;
        this.hid = hid;
        this.hphone = hphone;
        this.hadd = hadd;
        this.id = id;
    }

    public String getGname() {
        return gname;
    }

    public String getGid() {
        return gid;
    }

    public String getGphone() {
        return gphone;
    }

    public String getItime() {
        return itime;
    }

    public String getHname() {
        return hname;
    }

    public String getHid() {
        return hid;
    }

    public String getHphone() {
        return hphone;
    }

    public String getHadd() {
        return hadd;
    }

    public String getId() {
        return id;
    }
}
