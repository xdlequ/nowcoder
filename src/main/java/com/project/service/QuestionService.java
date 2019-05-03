package com.project.service;

import com.project.dao.QuestionDao;
import com.project.entity.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;
import java.util.*;
/**
 * Created by ql on 2019/5/3.
 */
@Service
public class QuestionService {

    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;


    public int addQuestion(Question question){
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setContent(sensitiveService.filter(question.getContent()));
        return questionDao.addQuestion(question)>0?question.getId():0;
    }

    public List<Question>getLatestQuestions(int userId,int offset,int limit){
        return questionDao.selectLatestQuestions(userId,offset,limit);
    }
    public int updateCommentCount(int id,int commentCount){
        return questionDao.updateCommentCount(id,commentCount);
    }

}
