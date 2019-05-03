package com.project.controller;

import com.project.entity.*;
import com.project.service.CommentService;
import com.project.service.QuestionService;
import com.project.service.SensitiveService;
import com.project.service.UserService;
import com.project.utils.ProjectUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Created by ql on 2019/5/3.
 */
@Controller
public class QuestionController {
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);
    @Autowired
    QuestionService questionService;
    @Autowired
    CommentService commentService;
    @Autowired
    HostHolder hostHolder;
    @Autowired
    UserService userService;
    @Autowired
    SensitiveService sensitiveService;

    @RequestMapping(value = "/question/{qid}" ,method = {RequestMethod.GET})
    public String questionDeatil(Model model, @PathVariable("qid")int qid){
        Question question=questionService.getById(qid);
        model.addAttribute("question",question);
        List<Comment> commentList=commentService.getCommentByEntity(qid, EntityType.ENTITY_COMMENT);
        List<ViewObject>comments=new ArrayList<>();
        for (Comment comment:commentList){
            ViewObject vo=new ViewObject();
            vo.set("comment",comment);
//            if (hostHolder.getUser()==null){
//
//            }
            vo.set("user",userService.getUserById(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments",comments);
        //需要补充点赞信息和关注信息
        return "detail";
    }
    @RequestMapping(value = "/question/add",method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title")String title,@RequestParam("content") String content){
        try{
            Question question=new Question();
            question.setContent(sensitiveService.filter(content));
            question.setCreateDate(new Date());
            question.setTitle(sensitiveService.filter(title));
            if (hostHolder.getUser()==null){
                question.setUserId(ProjectUtil.ANONYMOUS_USERID);
                return "redirect:/reglogin";
            }else{
                question.setUserId(hostHolder.getUser().getId());
            }
            if (questionService.addQuestion(question)>0){
                //增加事件通知机制
                return ProjectUtil.getJSONString(0);
            }
        }catch (Exception e){
            logger.error("发表问题失败",e.getMessage());
        }
        return ProjectUtil.getJSONString(1,"failure");
    }
}
