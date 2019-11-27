package com.example.innovaccer;

public class HistoryItem {
    private String gname, gid, gphone, itime, outtime, hname, hid, hphone, hadd;

    public HistoryItem(String gname, String gid, String gphone, String itime, String outtime, String hname, String hid, String hphone, String hadd) {
        this.gname = gname;
        this.gid = gid;
        this.gphone = gphone;
        this.itime = itime;
        this.outtime = outtime;
        this.hname = hname;
        this.hid = hid;
        this.hphone = hphone;
        this.hadd = hadd;
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

    public String getOuttime() {
        return outtime;
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

}
