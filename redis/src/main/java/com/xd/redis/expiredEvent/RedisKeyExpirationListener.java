package com.xd.redis.expiredEvent;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.Message;
import org.springframework.data.redis.listener.KeyExpirationEventMessageListener;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class RedisKeyExpirationListener extends KeyExpirationEventMessageListener {
    public RedisKeyExpirationListener(RedisMessageListenerContainer redisMessageListenerContainer) {
        super(redisMessageListenerContainer);
    }

    /**
     * 针对 redis 数据失效事件，进行数据处理
     */
    @Override
    public void onMessage(Message message, byte[] pattern) {
        //向redis中添加标识
        //stringRedisTemplate.opsForValue().set(“zkjlOrder:” + zkjlOrder.getOrderNum(), zkjlOrder.getOrderNum(), 12, TimeUnit.HOURS);

        // 拿到key
        //log.info("监听Redis key过期，key：{}，channel：{}", message.toString(), new String(pattern));
        log.info("监听Redis key：{}", message.toString()+" 过期");


        String key = new String(message.getBody());
        log.info("======================redis time out========================");
        if(key.startsWith("zkjlOrder")){
            // 取到业务数据进行处理  订单超时
            String orderNum=key.split(":")[1];
            log.info("======================"+orderNum+"======================");
            //查询到订单,判断为未支付,删除订单
            //zkjlOrderService.selectOrder(orderNum);

        }
    }
}
