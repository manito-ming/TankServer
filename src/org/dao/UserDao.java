package org.dao;

import org.apache.log4j.Logger;


import java.sql.*;


public class UserDao {

    private Logger log=Logger.getLogger(UserDao.class);

    //接收到json取出来uid
    public String search(String uid){
        String uname=null;
        Connector conn=new Connector();
        ResultSet rs=null;
        try {
        String sql="select uname from player where uid=?";
        PreparedStatement psm=conn.connect().prepareCall(sql);
        psm.setString(1,uid);
        rs=psm.executeQuery();
        while (rs.next()) {
            uname = rs.getString("uname");
        }
        psm.close();
        conn.connect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uname;
    }


    public String searchPasswd(String uid){
        String sql = "select upasswd from player where uid="+uid;
        String upasswd=null;
        try {
            Connector conn=new Connector();
            ResultSet rs=null;
            PreparedStatement psm=conn.connect().prepareCall(sql);
            rs=psm.executeQuery();
            while (rs.next()) {
                upasswd=rs.getString("upasswd");
            }
            psm.close();
            conn.connect().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return upasswd;

    }
    public String searchName(String uid){
        String sql = "select uname from player where uid="+uid;
        String uname=null;
        try {
            Connector conn=new Connector();
            ResultSet rs=null;
            PreparedStatement psm=conn.connect().prepareCall(sql);
            rs=psm.executeQuery();
            while (rs.next()) {
                uname=rs.getString("uname");
            }
            psm.close();
            conn.connect().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return uname;
    }


    public int searchAccount(String uid){
        String sql = "select account from player where uid="+uid;
        int account=0;
        try {
            Connector conn=new Connector();
            ResultSet rs=null;
            PreparedStatement psm=conn.connect().prepareCall(sql);
            rs=psm.executeQuery();
            while (rs.next()) {
                account=rs.getInt("account");
            }
            psm.close();
            conn.connect().close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return account;
    }


    public void updateAccount(int account,String uid){
        String sql ="update player set account = "+account+" where id= '"+uid+"'";
        PreparedStatement pstmt;
        int i;
        try {
            Connector conn=new Connector();
            Connection con=conn.connect();
            pstmt = (PreparedStatement) con.prepareStatement(sql);
            i = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }


    public void updateStore(String store,String uid){
        String sql ="update player set store = '"+store+"' where id= '"+uid+"'";
        PreparedStatement pstmt;
        int i;
        try {
            Connector conn=new Connector();
            Connection con=conn.connect();
            pstmt = (PreparedStatement) con.prepareStatement(sql);
            i = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

    }



}
