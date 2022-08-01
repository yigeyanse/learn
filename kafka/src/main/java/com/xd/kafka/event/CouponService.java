package com.xd.kafka.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class CouponService {

    private Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * 在方法上，添加 @EventListener 注解，并设置监听的事件为 UserRegisterEvent。这是另一种监听的使用方式
     * @param event
     */
    @EventListener
    public void addCoupon(UserRegisterEvent event){
        logger.info("[addCoupon][给用户({}) 发放优惠劵]", event.getUserName());
    }
}
