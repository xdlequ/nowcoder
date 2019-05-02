package com.project.entity;

import com.alibaba.fastjson.JSONObject;
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
public class Feed {
    private int id;
    private int type;
    private int userId;
    private Date createDate;
    private String data;
    private JSONObject dataJSON=null;
}
