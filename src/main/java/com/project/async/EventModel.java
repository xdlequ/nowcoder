package com.project.async;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by ql on 2019/5/3.
 */
@Component
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
    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType() {
        return entityType;
    }

    public EventModel setEntityType(int entityType) {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId() {
        return entityId;
    }

    public EventModel setEntityId(int entityId) {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId() {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId) {
        this.entityOwnerId = entityOwnerId;
        return this;
    }
    public String getExt(String key){
        return ext.get(key);
    }
    public EventModel(EventType type){
        this.type=type;
    }
}
