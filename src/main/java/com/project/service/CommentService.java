package com.project.service;

import com.project.dao.CommentDao;
import com.project.entity.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * Created by ql on 2019/5/3.
 */
@Service
public class CommentService {
    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    public void addComment(Comment comment){
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        commentDao.addComment(comment);
    }
    public List<Comment> getCommentByEntity(int entityId,int entityType){
        return commentDao.selectCommentByEntity(entityId,entityType);
    }

    public int getUserCommentCount(int id){
        return commentDao.getUserCommentCount(id);
    }
    public int getCommentCount(int entityId, int entityType){
        return commentDao.getCommentCount(entityId,entityType);
    }

    public boolean deleteComment(int commentId){
        return commentDao.updateStatus(commentId,1)>0;
    }

    public Comment getCommentById(int commentId){
        return commentDao.selectCommentById(commentId);
    }

}
