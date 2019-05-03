package com.project.dao;

import com.project.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

/**
 * Created by ql on 2019/5/2.
 */
@Mapper
public interface UserDao {
    String TABLE_NAME = "user";
    String INSET_FIELDS = " name, password, salt, head_url ";
    String SELECT_FIELDS = " id, name, password, salt, head_url";
    int addUser(User user);

    User selectById(int id);


    User selectByName(String name);

    void updatePassword(User user);

    void deleteById(int id);

}
