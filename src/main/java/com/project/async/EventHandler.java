package com.project.async;

import java.util.List;

/**
 * Created by ql on 2019/5/3.
 */
public interface EventHandler {
    void doHandler(EventModel model);
    List<EventType> getSupportEventTypes();
}
