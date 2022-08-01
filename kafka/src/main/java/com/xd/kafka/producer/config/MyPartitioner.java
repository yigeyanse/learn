package com.xd.kafka.producer.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Partitioner;
import org.apache.kafka.common.Cluster;

import java.util.Map;

@Slf4j
public class MyPartitioner implements Partitioner {

    /**
     * 分区策略核心方法
     * @param topic
     * @param key
     * @param keyBytes
     * @param value
     * @param valueBytes
     * @param cluster
     * @return
     */
    @Override
    public int partition(String topic, Object key, byte[] keyBytes, Object value, byte[] valueBytes, Cluster cluster) {

        /*log.info("use MySelfPartitioner ...");
        //拿到主题中的分区信息
        List<PartitionInfo> partitionInfos = cluster.partitionsForTopic(topic);
        //获取分区数
        int num = partitionInfos.size();
        //计算value的hashcode，然后取模，获取一个分区
        int parId = value.hashCode()%num;
        return parId;*/

        //具体分区逻辑，这里全部发送到0号分区
        return 0;
    }

    @Override
    public void close() {

    }

    @Override
    public void configure(Map<String, ?> configs) {

    }
}
