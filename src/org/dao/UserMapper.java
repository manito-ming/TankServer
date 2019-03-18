package org.dao;


import org.pojo.User;

public interface UserMapper {
    public User findCustomer(String account);
    public void addCustomer(User user);
    public User findCustomer1(String account);
}
