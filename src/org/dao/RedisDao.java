package org.dao;

import org.pojo.User;

import java.sql.Connection;

public class RedisDao {
    Connector connector = new Connector();
    UserDao userDao = new UserDao();
    RedisUser redisUser = new RedisUser();
    User user = new User();

    public String RedisLogin(String id)
    {
        String passwd=null;

        if(redisUser.hexists("user_"+id, "username_")){

            passwd=redisUser.hget("user_"+id, "username_");

            System.out.println("-------------Welcome Redis! User "+" login success");
            return passwd;

        }else{
            passwd = userDao.searchPasswd(id);
            redisUser.hset("user_"+id, "username_",passwd);
            System.out.println("Welcome mysql! User "+" login success");
            return passwd;

        }
    }
}
