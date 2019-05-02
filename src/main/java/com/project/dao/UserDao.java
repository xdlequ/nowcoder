package com.project.dao;

import com.project.entity.User;

/**
 * Created by ql on 2019/5/2.
 */
public interface UserDao {
    int addUser(User user);

    User selectById(int id);

    User selectByName(String name);

    void updatePassword(User user);

    void deleteById(int id);

}
