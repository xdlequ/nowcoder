package com.project.controller;

import com.project.entity.HostHolder;
import com.project.entity.Message;
import com.project.entity.User;
import com.project.entity.ViewObject;
import com.project.service.MessageService;
import com.project.service.SensitiveService;
import com.project.service.UserService;
import com.project.utils.ProjectUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

/**
 * Created by ql on 2019/5/4.
 */
@Controller
public class MessageController {
    @Autowired
    MessageService messageService;
    @Autowired
    SensitiveService sensitiveService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    private static final Logger logger= LoggerFactory.getLogger(MessageController.class);
    @RequestMapping(path = {"/msg/list"},method = {RequestMethod.GET})
    public String getConversationList(Model model){
        if (hostHolder.getUser()==null){
            //return ProjectUtil.getJSONString(999);
            return "redirect:/reglogin";
        }
        int localUserId=hostHolder.getUser().getId();
        List<Message>conversationList=messageService.getConversationList(localUserId,0,10);
        List<ViewObject>conversations=new ArrayList<>();
        for (Message message:conversationList){
            ViewObject vo=new ViewObject();
            vo.set("message",message);
            int targetId=message.getFromId()==localUserId?message.getToId():localUserId;
            vo.set("user",userService.getUserById(message.getFromId()));
            vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
            conversations.add(vo);
        }
        model.addAttribute("conversations",conversations);
        return "letter";
    }

    @RequestMapping(path = {"/msg/detail"},method = {RequestMethod.GET})
    public String getConversationDetail(Model model, @RequestParam("conversationId")String conversationId){
        try{
            List<Message>conversationList=messageService.getConversationDatail(conversationId,0,10);
            List<ViewObject>conversations=new ArrayList<>();
            for (Message message:conversationList){
                ViewObject vo=new ViewObject();
                vo.set("message",message);
                vo.set("user",userService.getUserById(message.getFromId()));
                //vo.set("unread",messageService.getConversationUnreadCount(localUserId,message.getConversationId()));
                conversations.add(vo);
            }
            model.addAttribute("conversations",conversations);
        }catch (Exception e){
            logger.error(e.getMessage());
        }
        return "letterDetail";
    }

    @RequestMapping(path = {"/msg/addMessage"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addMessage(@RequestParam("toName")String toName,@RequestParam("content")String content){
        try {
            if (hostHolder.getUser()==null){
                return ProjectUtil.getJSONString(999,"未登录");
            }
            User user=userService.selectByName(toName);
            if (user==null){
                return ProjectUtil.getJSONString(0,"用户不存在");
            }
            Message message=new Message();
            message.setContent(content);
            message.setFromId(hostHolder.getUser().getId());
            message.setToId(user.getId());
            message.setCreateDate(new Date());
            messageService.addMessage(message);
            return ProjectUtil.getJSONString(0);
        } catch (Exception e) {
            logger.error("发送消息失败",e.getMessage());
            return ProjectUtil.getJSONString(1,"发送失败");
        }
    }
}
