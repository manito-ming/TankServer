package org.dao;

import java.sql.*;

public class Connector {
    private String driver="com.mysql.jdbc.Driver";
    private String uri="jdbc:mysql://localhost:3306/tankserver";
    private String name="root";
    private String passwd="1234567";

    public Connection connect(){
        Connection conn=null;
        PreparedStatement psm=null;
        try {
            Class.forName(driver);
            conn=DriverManager.getConnection(uri,name,passwd);
            if (!conn.isClosed()){
//                System.out.println("mysql链接成功");
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  conn;
    }
}
