package org.dao;
import org.pojo.User;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.temp.Tempfield;

import java.io.IOException;

public class MybatisTest {
    public static int status=1;
    //注册账号
    public void addCustomer(String uid, String uname, String upasswd, String utel){

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //通过容器获取userDao实例
        UserMapper yonghuMapper1=  applicationContext.getBean(UserMapper.class);
        User user = new User();
        user.setUid(uid);
        user.setUname(uname);
        user.setUpasswd(upasswd);
        user.setUtel(utel);
        try {
            yonghuMapper1.addCustomer(user);
        }catch (Exception e){
            MybatisTest.status=0;
            System.out.println("数据插入有误，账号已存在.");
        }

    }
    //校验登录
    public String findCustomerTest(String account) {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //通过容器获取userDao实例
        UserMapper yonghuMapper1=  applicationContext.getBean(UserMapper.class);
        User yonghu1 = yonghuMapper1.findCustomer(account);
        if(yonghu1 == null){
            return "-1";
        }
        return  yonghu1.getUpasswd();
    }
    public User findCustomerTest1(String account) throws IOException {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
        //通过容器获取userDao实例
        UserMapper yonghuMapper1=  applicationContext.getBean(UserMapper.class);
        User yonghu1 = yonghuMapper1.findCustomer1(account);
        return  yonghu1;
    }

    //检测登录类和注册类是否正确
    public static void main(String[] args) throws IOException {
        MybatisTest m = new MybatisTest();
//        String account = "333";
//        String paswsword = m.findCustomerTest(account);
//        System.out.println(account+"密码为"+paswsword);


        m.addCustomer("999","wudi","123","789456");
        System.out.println("注册成功");
    }
}
