package com.project.async;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by ql on 2019/5/3.
 */
@Component
@Setter
@Getter
public class EventModel {
    private EventType type;
    private int actorId;
    private int entityType;
    private int entityId;
    private int entityOwnerId;
    private Map<String,String>ext=new HashMap<>();
    public EventModel(){}
    public EventModel setExt(String key,String value){
        ext.put(key,value);
        return this;
    }
    public String getExt(String key){
        return ext.get(key);
    }
    public EventModel(EventType type){
        this.type=type;
    }
}
