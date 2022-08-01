package com.xd.kafka.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class UserService {
    private Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;


    public void register(String userName) {
        //执行注册逻辑....
        logger.info("[register][执行用户（{}）的注册逻辑]", userName);

        //<2>. ......发布
        applicationEventPublisher.publishEvent(new UserRegisterEvent(this, userName));
    }
}
