package com.project.service;

import com.project.dao.LoginTicketDao;
import com.project.dao.UserDao;
import com.project.entity.LoginTicket;
import com.project.entity.User;
import com.project.utils.ProjectUtil;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Created by ql on 2019/5/2.
 */
@Service
public class UserService {
    private static final Logger log= LoggerFactory.getLogger(UserService.class);

    @Autowired
    UserDao userDao;

    @Autowired
    LoginTicketDao loginTicketDao;

    public User selectByName(String name){
        return userDao.selectByName(name);
    }

    public User getUserById(int id){
        return userDao.selectById(id);
    }

    public Map<String,Object> register(String userName, String password){
        Map<String,Object>map=new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDao.selectByName(userName);

        if (user != null) {
            map.put("msg", "用户名已经被注册");
            return map;
        }
        user=new User();
        user.setName(userName);
        user.setSalt(UUID.randomUUID().toString().substring(0,5));
        user.setPassword(ProjectUtil.MD5(password+user.getSalt()));
        String head = String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000));
        user.setHeadUrl(head);
        userDao.addUser(user);


        //手动添加cookie
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    private String addLoginTicket(int id) {
        LoginTicket loginticket=new LoginTicket();
        loginticket.setUserId(id);
        loginticket.setStatus(0);
        Date date=new Date();
        date.setTime(date.getTime()+1000*3600*24);
        loginticket.setExpired(date);
        loginticket.setTicket(UUID.randomUUID().toString().replace("-",""));
        loginTicketDao.addTicket(loginticket);
        return loginticket.getTicket();
    }
    public Map<String,Object> login(String userName,String password){
        Map<String,Object>map=new HashMap<>();
        if (StringUtils.isBlank(userName)) {
            map.put("msg", "用户名不能为空");
            return map;
        }

        if (StringUtils.isBlank(password)) {
            map.put("msg", "密码不能为空");
            return map;
        }

        User user = userDao.selectByName(userName);

        if (user == null) {
            map.put("msg", "用户名不存在");
            return map;
        }

        if (!ProjectUtil.MD5(password+user.getSalt()).equals(user.getPassword())){
            map.put("msg","密码错误");
            return map;
        }
        String ticket=addLoginTicket(user.getId());
        map.put("ticket",ticket);
        map.put("userId",user.getId());
        return map;
    }
    public void logout(String ticket){
        loginTicketDao.updateStatus(ticket,1);
    }
}
