package com.project.controller;

import com.project.entity.Comment;
import com.project.entity.EntityType;
import com.project.entity.HostHolder;
import com.project.entity.Question;
import com.project.service.CommentService;
import com.project.service.QuestionService;
import com.project.utils.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * Created by ql on 2019/5/3.
 */
@Controller
public class CommentController {
    private static final Logger log= LoggerFactory.getLogger(CommentController.class);
    @Autowired
    CommentService commentService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    QuestionService questionService;




    @RequestMapping(value = {"/addComment"} ,method = {RequestMethod.POST})
    public String addComment(@RequestParam("questionId") int questionId,@RequestParam("content")String content){
        try {
            Comment comment=new Comment();
            comment.setContent(content);
            if (hostHolder.getUser()==null){
                comment.setUserId(ProjectUtil.ANONYMOUS_USERID);
                return "redirect:/reglogin";
            }else{
                comment.setUserId(hostHolder.getUser().getId());
            }

            comment.setCreateDate(new Date());
            comment.setEntityId(questionId);
            comment.setEntityType(EntityType.ENTITY_COMMENT);
            commentService.addComment(comment);
            int count=commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            //需要在questionService中更新
            questionService.updateCommentCount(comment.getEntityId(),count);
//            eventProducer.fireEvent(new EventModel(EventType.COMMENT).setActorId(comment.getUserId())
//                    .setEntityId(questionId));
        }catch (Exception e){
            log.error("增加评论失败",e.getMessage());
        }
        return "redirect:/question/"+questionId;
    }


}
