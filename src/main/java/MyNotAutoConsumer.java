import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class MyNotAutoConsumer {
    public static void main(String args[]){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.31.145:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "false");//这里代表是否自动提交,true则自动提交，false为不自动提交
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        consumer.subscribe(Arrays.asList("car"));//这里填入的字段是topic的name，如果没有则会创建
        final int minBatchSize = 200;
        List<ConsumerRecord<String, String>> buffer = new ArrayList();
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
                buffer.add(record);
            }
            if (buffer.size() >= minBatchSize) {
                //TODO 做些对数据的处理
                consumer.commitSync();
                buffer.clear();
            }
        }
    }
}