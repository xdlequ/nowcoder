package com.project.async.handler;

import com.project.async.EventHandler;
import com.project.async.EventModel;
import com.project.async.EventType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by ql on 2019/5/4.
 */
@Component
public class AddQuestionHandler implements EventHandler {
    private static final Logger logger= LoggerFactory.getLogger(AddQuestionHandler.class);

//    @Autowired
//    SearchService searchService;

    @Override
    public void doHandler(EventModel model) {

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return null;
    }
}
