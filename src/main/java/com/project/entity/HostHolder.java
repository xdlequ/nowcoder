package com.project.entity;

import org.springframework.stereotype.Component;

/**
 * Created by ql on 2019/5/1.
 */
@Component
public class HostHolder {
    private static ThreadLocal<User> users=new ThreadLocal<>();
    public User getUser(){
        return users.get();
    }
    public void setUser(User user){
        users.set(user);
    }
    public void clear(){
        users.remove();
    }

}
