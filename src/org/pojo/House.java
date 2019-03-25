package org.pojo;

public class House {
    private String hid;               //房间id
    private String hcount;             //当前房间人数
    private String hplayer1_id;         //玩家１id
    private String hplayer2_id;          //玩家２id

    public String getHid() {
        return hid;
    }

    public void setHid(String hid) {
        this.hid = hid;
    }

    public String getHcount() {
        return hcount;
    }

    public void setHcount(String hcount) {
        this.hcount = hcount;
    }

    public String getHplayer1_id() {
        return hplayer1_id;
    }

    public void setHplayer1_id(String hplayer1_id) {
        this.hplayer1_id= hplayer1_id;
    }

    public String getHplayer2_id() {
        return hplayer2_id;
    }

    public void setHplayer2_id(String hplayer2_id) {
        this.hplayer2_id = hplayer2_id;
    }
}
