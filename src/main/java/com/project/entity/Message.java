package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by ql on 2019/5/1.
 */
@Setter
@Getter
public class Message {
    private int id;
    private int fromId;
    private int toId;
    private String content;
    private Date createDate;
    private int hasRead;
    private String conversationId;
}
