package com.bonc.turing.cms.common.utils;


import com.alibaba.fastjson.JSONObject;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Map;
import java.util.Properties;

@Component
public class KafkaUtils {
    protected static final Logger logger = LoggerFactory.getLogger(KafkaUtils.class);

    @Value(value = "${kafka.producer.servers}")
    protected  String kafkaConnection;
    @Value(value = "${kafka.producer.linger}")
    private int lingerMs;
    @Value(value = "${kafka.producer.batch.size}")
    private int batchSize;
    @Value(value = "${kafka.producer.buffer.memory}")
    private int bufferMemory;
    @Value(value = "${kafka.producer.retries}")
    private int retries;

    private static KafkaProducer<String, String> producer;
    /**
     *
     * 私有静态方法，创建Kafka生产者
     *
     * @author lky
     * @Date 2017年4月14日 上午10:32:32
     * @version 1.0.0
     * @return KafkaProducer
     */
    @PostConstruct
    private KafkaProducer<String, String> createProducer() {
        Properties properties = new Properties();
        properties.put("bootstrap.servers", kafkaConnection);// 声明kafka
        properties.put("acks", "all");
        properties.put("retries", retries);
        properties.put("batch.size", batchSize);
        properties.put("linger.ms", lingerMs);
        properties.put("buffer.memory", bufferMemory);
        properties.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        properties.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producer = new KafkaProducer<String, String>((properties));
        return producer;
    }

    /**
     *
     * 传入kafka约定的topicName,json格式字符串，发送给kafka集群
     *
     * @author IG
     * @Date 2017年4月14日 下午1:29:09
     * @version 1.0.0
     * @param topicName
     * @param jsonMessage
     */
    public static void sendMessage(String topicName, String jsonMessage) {
        producer.send(new ProducerRecord<String, String>(topicName, jsonMessage));
    }

    /**
     *
     * 传入kafka约定的topicName,json格式字符串数组，发送给kafka集群<br>
     * 用于批量发送消息，性能较高。
     *
     * @author IG
     * @Date 2017年4月14日 下午2:00:12
     * @version 1.0.0
     * @param topicName
     * @param jsonMessages
     * @throws InterruptedException
     */
    public static void sendMessage(String topicName, String... jsonMessages) throws InterruptedException {
        for (String jsonMessage : jsonMessages) {
            producer.send(new ProducerRecord<String, String>(topicName, jsonMessage));
        }
    }

    /**
     *
     * 传入kafka约定的topicName,Map集合，内部转为json发送给kafka集群 <br>
     * 用于批量发送消息，性能较高。
     *
     * @author IG
     * @Date 2017年4月14日 下午2:01:18
     * @version 1.0.0
     * @param topicName
     * @param mapMessageToJSONForArray
     */
    public static void sendMessage(String topicName, List<Map<Object, Object>> mapMessageToJSONForArray) {
        for (Map<Object, Object> mapMessageToJSON : mapMessageToJSONForArray) {
            String array = JSONObject.toJSONString(mapMessageToJSON);
            producer.send(new ProducerRecord<String, String>(topicName, array));
        }
    }

    /**
     *
     * 传入kafka约定的topicName,Map，内部转为json发送给kafka集群
     *
     * @author IG
     * @Date 2017年4月14日 下午1:30:10
     * @version 1.0.0
     * @param topicName
     * @param mapMessageToJSON
     */
    public static void sendMessage(String topicName, Map<Object, Object> mapMessageToJSON) {
        String array = JSONObject.toJSONString(mapMessageToJSON);
        producer.send(new ProducerRecord<String, String>(topicName, array));
    }

}
