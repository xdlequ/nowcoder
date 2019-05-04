package com.project.entity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * Created by ql on 2019/5/1.
 */
@Setter
@Getter
@Component
public class Comment {
    private int id;
    private int userId;
    private int entityId;//评论归属问题Id
    private int entityType;//归属的类型
    private String content;

    private Date createDate;
    private int status;


}
