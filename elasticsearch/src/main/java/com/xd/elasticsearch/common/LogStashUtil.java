package com.xd.elasticsearch.common;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;

import java.util.Date;

@Slf4j
public class LogStashUtil {

    public static void sendMessage(String username, String type, String content,
                                   Date createTime, String parameters){
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("username",username);
        jsonObject.put("type",type);
        jsonObject.put("content",content);
        jsonObject.put("parameters",parameters);
        jsonObject.put("createTime", createTime);
        log.info(jsonObject.toString());
    }
}

