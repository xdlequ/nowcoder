package com.project.service;

import com.project.dao.MessageDao;
import com.project.entity.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.*;
/**
 * Created by ql on 2019/5/3.
 */
@Service
public class MessageService {

    @Autowired
    MessageDao messageDao;
    @Autowired
    SensitiveService sensitiveService;

    public int addMessage(Message message){
        message.setContent(sensitiveService.filter(message.getContent()));
        return messageDao.addMessage(message);
    }
    public List<Message>getConversationDatail(String conversationId,int offset,int limit){
        return messageDao.getConversationDetail(conversationId,offset,limit);
    }
    public List<Message>getConversationList(int userId,int offset,int limit){
        return messageDao.getConversationList(userId,offset,limit);
    }
    public int getConversationUnreadCount(int userId,String conversationId){
        return messageDao.getConversationUnreadCount(userId,conversationId);
    }
}
