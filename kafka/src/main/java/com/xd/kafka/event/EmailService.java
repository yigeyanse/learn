package com.xd.kafka.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService implements ApplicationListener<UserRegisterEvent> {
    //实现 ApplicationListener 接口，通过 E 泛型设置感兴趣的事件。

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 实现 onApplicationEvent(E event)方法，针对监听的 UserRegisterEvent 事件，进行自定义处理。
     * @param event
     */
    @Async
    @Override
    public void onApplicationEvent(UserRegisterEvent event) {
        logger.info("[onApplicationEvent][给用户({}) 发送邮件]", event.getUserName());
    }
}
