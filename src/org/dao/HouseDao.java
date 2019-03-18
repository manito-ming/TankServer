package org.dao;

import com.alibaba.fastjson.JSON;
import org.pojo.House;

import javax.swing.plaf.PanelUI;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HouseDao {
    private House house=new House();
    private Connector conn=new Connector();
    private Statement sm=null;
    private ResultSet rs=null;

    public String search(){
        String hid=null;
        String hcount=null;
        Map<String,Object> map= new HashMap<String, Object>();
        List<Object> list=new ArrayList<>();
        String listJson=null;
        try {
            String sql="select hid,hcount from house";
            sm=conn.connect().createStatement();
            rs=sm.executeQuery(sql);
            int i =0;
            while(rs.next()){
                House h=new House();
                h.setHid(rs.getString("hid"));
                h.setHcount(rs.getString("hcount"));
                map.put("h"+i,h);
                i++;
                list.add(map);
            }
            listJson = JSON.toJSONString(list);
            sm.close();
            conn.connect().close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return  listJson;
    }
    public void insert(){
//        String sql="insert into "
    }

}
