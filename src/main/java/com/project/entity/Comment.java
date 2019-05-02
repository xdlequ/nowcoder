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
    private int entityId;
    private int entityType;
    private String content;

    private Date createDate;
    private int status;


}
