import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Arrays;
import java.util.Properties;

public class MyAutoConsumer {
    public static void main(String args[]){
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.31.145:9092");
        props.put("group.id", "test");
        props.put("enable.auto.commit", "true");//这里代表是否自动提交,true则自动提交，false为不自动提交
        props.put("auto.commit.interval.ms", "1000");
        props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
        KafkaConsumer<String, String> consumer = new KafkaConsumer(props);
        System.out.println("after connect");
        consumer.subscribe(Arrays.asList("car"));//这里填入的字段是topic的name，如果没有则会创建
        System.out.println("after subscribe");
        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s, value = %s%n", record.offset(), record.key(), record.value());
            }
        }
    }
}