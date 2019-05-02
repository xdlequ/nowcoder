package com.project.entity;

import lombok.Getter;
import lombok.Setter;

/**
 * Created by ql on 2019/5/1.
 */

@Setter
@Getter
public class User {
    private int id;
    private String name;
    private String password;
    private String salt;
    private String headUrl;
    public User(){}
    public User(String name){
        this.name=name;
        this.password="";
        this.salt="";
        this.headUrl="";
    }
}
