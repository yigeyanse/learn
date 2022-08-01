package com.xd.kafka.producer.service;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/kafka")
public class KafkaController {

    @Resource
    private KafkaProducerService kafkaProducerService;

    @GetMapping("testSendMessageSync")
    public void testSendMessageSync() throws Exception {
        String topic = "hello-kafka-test-topic";
        String key = "key1";
        String message = "firstMessage";
        kafkaProducerService.sendMessageSync(topic, key, message);
    }
    @GetMapping("testSendMessageGetResult")
    public void testSendMessageGetResult() throws Exception {
        String topic = "hello-kafka-test-topic";
        String key = "key";
        String message = "helloSendMessageGetResult";
        kafkaProducerService.sendMessageGetResult(topic, key, message);
        kafkaProducerService.sendMessageGetResult(topic, null, message);
    }

    @GetMapping("testSendMessageAsync")
    public void testSendMessageAsync() {
        String topic = "hello-kafka-test-topic";
        String message = "firstAsyncMessage";
        kafkaProducerService.sendMessageAsync(topic, message);
    }

    @GetMapping("testMessageBuilder")
    public void testMessageBuilder() throws Exception {
        String topic = "hello-kafka-test-topic";
        String key = "key1";
        String message = "helloMessageBuilder";
        kafkaProducerService.testMessageBuilder(topic, key, message);
    }

    /**
     * 测试事务
     */
    @GetMapping("testSendMessageInTransaction")
    public void testSendMessageInTransaction() {
        String topic = "hello-kafka-test-topic";
        String key = "key1";
        String message = "helloSendMessageInTransaction";
        kafkaProducerService.sendMessageInTransaction(topic, key, message);
    }

    /**
     * 测试批量消费
     * @throws Exception
     */
    @GetMapping("testConsumerBatch")
    public void testConsumerBatch() throws Exception {
        //写入多条数据到批量topic：hello-batch
        String topic = "hello-batch";
        for(int i = 0; i < 20; i++) {
            kafkaProducerService.sendMessageSync(topic, null, "batchMessage" + i);
        }
    }

    /**
     * 测试消费者拦截器
     * @throws Exception
     */
    @GetMapping("testConsumerInterceptor")
    public void testConsumerInterceptor() throws Exception {
        String topic = "consumer-interceptor";
        for(int i = 0; i < 2; i++) {
            kafkaProducerService.sendMessageSync(topic,null, "normalMessage" + i);
        }
        kafkaProducerService.sendMessageSync(topic, null, "filteredMessage");
        kafkaProducerService.sendMessageSync(topic, null, "filterMessage");
    }
}
