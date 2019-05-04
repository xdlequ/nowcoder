package com.project.controller;

import com.project.async.EventModel;
import com.project.async.EventProducer;
import com.project.async.EventType;
import com.project.entity.*;
import com.project.service.CommentService;
import com.project.service.FollowService;
import com.project.service.QuestionService;
import com.project.service.UserService;
import com.project.utils.ProjectUtil;
import org.apache.ibatis.annotations.Param;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import java.util.*;
/**
 * Created by ql on 2019/5/4.
 */
@Controller
public class FollowController {
    @Autowired
    HostHolder hostHolder;
    @Autowired
    FollowService followService;
    @Autowired
    EventProducer eventProducer;
    @Autowired
    QuestionService questionService;
    @Autowired
    UserService userService;
    @Autowired
    CommentService commentService;

    private static final Logger logger= LoggerFactory.getLogger(FollowController.class);
    /**
     关注用户
     */
    @RequestMapping(path = {"/followUser"},method = {RequestMethod.GET,RequestMethod.POST})
    public String followUser(@RequestParam("userId")int userId){
        if (hostHolder.getUser()==null){
            return "redirect:/reglogin";
        }
        boolean ret=followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
        .setEntityOwnerId(userId).setEntityId(userId).setEntityType(EntityType.ENTITY_USER));
        //返回关注人数
        return ProjectUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweesCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }
    /**
        取消关注
     */
    @RequestMapping(path = {"/unfollowUser"},method = {RequestMethod.GET,RequestMethod.POST})
    public String unfollowUser(@RequestParam("userId")int userId){
        if (hostHolder.getUser()==null){
            return "redirect:/reglogin";
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER,userId);
        return ProjectUtil.getJSONString(ret?0:1,String.valueOf(followService.getFolloweesCount(hostHolder.getUser().getId(),EntityType.ENTITY_USER)));
    }

    /**
     关注问题
     */
    @RequestMapping(path = {"/followQuestion"},method = {RequestMethod.GET,RequestMethod.POST})
    public String followQuestion(@RequestParam("questionId")int questionId){
        if (hostHolder.getUser()==null){
            return ProjectUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if (question==null){
            return ProjectUtil.getJSONString(1,"问题不存在");
        }
        boolean ret=followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);

        eventProducer.fireEvent(new EventModel(EventType.FOLLOW).setActorId(hostHolder.getUser().getId())
                .setEntityOwnerId(questionId).setEntityId(questionId).setEntityType(EntityType.ENTITY_QUESTION));
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowersCount(EntityType.ENTITY_QUESTION, questionId));
        //返回关注人数
        return ProjectUtil.getJSONString(ret?0:1,info);
    }
    @RequestMapping(path = {"/unfollowQuestion"},method = {RequestMethod.GET,RequestMethod.POST})
    public String unfollowQuestion(@RequestParam("questionId")int questionId){
        if (hostHolder.getUser()==null){
            return ProjectUtil.getJSONString(999);
        }
        Question question=questionService.getById(questionId);
        if (question==null){
            return ProjectUtil.getJSONString(1,"问题不存在");
        }
        boolean ret=followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION,questionId);


        Map<String, Object> info = new HashMap<>();
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowersCount(EntityType.ENTITY_QUESTION, questionId));
        //返回关注人数
        return ProjectUtil.getJSONString(ret?0:1,info);
    }
    @RequestMapping(path = {"/user/{uid}/followers"},method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId){
        List<Integer>followers=followService.getFollowers(EntityType.ENTITY_USER,userId,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followers",getUserInfo(hostHolder.getUser().getId(),followers));
        }else{
            model.addAttribute("followers",getUserInfo(0,followers));
        }
        model.addAttribute("followerCount",followService.getFollowersCount(userId,EntityType.ENTITY_USER));
        model.addAttribute("curUser",userId);
        return "followers";
    }

    @RequestMapping(path = {"/user/{uid}/followees"},method = {RequestMethod.GET})
    public String followees(Model model,@PathVariable("uid") int userId) {
        List<Integer>followees=followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);
        if(hostHolder.getUser()!=null){
            model.addAttribute("followees",getUserInfo(hostHolder.getUser().getId(),followees));
        }else{
            model.addAttribute("followees",getUserInfo(0,followees));
        }
        model.addAttribute("followerCount",followService.getFolloweesCount(userId,EntityType.ENTITY_USER));
        model.addAttribute("curUser",userId);

        return "followees";
    }

    private List<ViewObject>getUserInfo(int localUserId,List<Integer>userIds){
        List<ViewObject>userInfos=new ArrayList<>();
        for (Integer uid:userIds){
            User user=userService.getUserById(uid);
            if (user==null){
                continue;
            }
            ViewObject vo=new ViewObject();
            vo.set("user",user);
            vo.set("commentCount",commentService.getUserCommentCount(uid));
            vo.set("followerCount",followService.getFollowersCount(uid,EntityType.ENTITY_USER));
            vo.set("followeeCount",followService.getFolloweesCount(uid,EntityType.ENTITY_USER));
            if (localUserId!=0){
                vo.set("followed",followService.isFollower(uid,EntityType.ENTITY_USER,localUserId));
            }else{
                vo.set("followed",false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
