package com.project.async;

import com.alibaba.fastjson.JSONObject;
import com.project.utils.JedisAdapter;
import com.project.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Created by ql on 2019/5/3.
 */

@Service
public class EventProducer {
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel){
        try {
            String json= JSONObject.toJSONString(eventModel);
            String key= RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        }catch (Exception e){
            return false;
        }
    }
}
