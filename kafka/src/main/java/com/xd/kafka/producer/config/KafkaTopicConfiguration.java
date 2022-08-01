package com.xd.kafka.producer.config;

import org.apache.kafka.clients.admin.AdminClientConfig;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaAdmin;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class KafkaTopicConfiguration {

    /**
     * 创建 KafkaAmin，可以自动检测集群中是否存在topic，不存在则创建
     * @return
     */
    @Bean
    public KafkaAdmin kafkaAdmin() {
        Map<String, Object> props = new HashMap<>();
        props.put(AdminClientConfig.BOOTSTRAP_SERVERS_CONFIG, "127.0.0.1:9092");
        return new KafkaAdmin(props);
    }

    @Bean
    public NewTopic newTopic() {
        // 创建 topic，指定 名称、分区数、副本数
        return new NewTopic("hello-kafka-test-topic", 3, (short) 0);
    }

}
