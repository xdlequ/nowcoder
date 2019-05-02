package com.project.entity;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Created by ql on 2019/5/1.
 */
@Getter
@Setter
public class LoginTicket {
    private int id;
    private int userId;
    private Date expired;
    private int status;// 0有效，1失效
    private String ticket;
}
