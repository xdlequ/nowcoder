package com.project.async.handler;

import com.project.async.EventHandler;
import com.project.async.EventModel;
import com.project.async.EventType;

import java.util.List;

/**
 * Created by ql on 2019/5/4.
 */
public class FollowHandler implements EventHandler {
    @Override
    public void doHandler(EventModel model) {

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return null;
    }
}
