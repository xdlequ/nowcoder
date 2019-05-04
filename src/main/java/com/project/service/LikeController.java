package com.project.service;

import com.project.async.EventHandler;
import com.project.async.EventModel;
import com.project.async.EventProducer;
import com.project.async.EventType;
import com.project.entity.Comment;
import com.project.entity.EntityType;
import com.project.entity.HostHolder;
import com.project.utils.ProjectUtil;
import com.project.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by ql on 2019/5/4.
 */
@Controller
public class LikeController {
    @Autowired
    LikeService likeService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    CommentService commentService;
    @Autowired
    EventProducer eventProducer;

    @RequestMapping(path = {"/like"},method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId")int commentId){
        if (hostHolder.getUser()==null){
            return ProjectUtil.getJSONString(999);
        }
        Comment comment=commentService.getCommentById(commentId);
        //String key= RedisKeyUtil.getLikeKey(EventType.COMMENT,commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE).setActorId(hostHolder.getUser().getId())
        .setEntityId(commentId).setEntityType(EntityType.ENTITY_COMMENT).setEntityOwnerId(comment.getUserId())
        .setExt("questionId",String.valueOf(comment.getEntityId())));
        long likeCount=likeService.getLikeCount(EntityType.ENTITY_COMMENT,commentId);

        return ProjectUtil.getJSONString(0,String.valueOf(likeCount));
    }
    @RequestMapping(path = {"/dislike"},method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId")int commentId){
        if (hostHolder.getUser()==null){
            return ProjectUtil.getJSONString(999);
        }
        Comment comment=commentService.getCommentById(commentId);
        //String key= RedisKeyUtil.getLikeKey(EventType.COMMENT,commentId);
        long likeCount=likeService.disLike(comment.getUserId(),EntityType.ENTITY_COMMENT,commentId);
        return ProjectUtil.getJSONString(0,String.valueOf(likeCount));

    }


}
