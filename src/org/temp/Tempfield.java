package org.temp;

public class Tempfield {
    private static String uname=null;
    private static String uid=null;

    public  String getUid() {
        return uid;
    }

    public  void setUid(String uid) {
        Tempfield.uid = uid;
    }

    public static int status= 1;
    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }
}
