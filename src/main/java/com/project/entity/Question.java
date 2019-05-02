package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by ql on 2019/5/1.
 */
@Setter
@Getter
public class Question {
    private int id;
    private String title;
    private String content;
    private Date createDate;
    private int userId;
    private int commentCount;
}
