package com.project.entity;

import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by ql on 2019/5/3.
 */
@Component
public class ViewObject {
    private Map<String,Object>objs=new HashMap<>();
    public void set(String key,Object value){
        objs.put(key,value);
    }
    public Object get(String key){
        return objs.get(key);
    }
}
