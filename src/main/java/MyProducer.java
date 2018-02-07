

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

import java.util.Properties;
/*
异步发送 用例1：这个例子作用是发送100条数据，注意：例子中的send是异步，缓存到本地，
然后一次性发送，所以一方面没有办法保证发送一定成功，另一方面send类似数据库连接池，
没有调用close方法的时候，连接不会释放。所以要手动调用close
 */
public class MyProducer {
    public static void main(String args[]){
        Properties props = new Properties();//java.util.Properties
        props.put("bootstrap.servers", "192.168.31.145:9092");
        //这里的地址指得是kafka server的地址，zookeeper目前没有体现。
        //如果有多个服务器则用逗号隔开,例props.put("bootstrap.servers","ip:port,ip:port")
        props.put("acks", "all");//
        props.put("retries", 0);
        props.put("batch.size", 16384);
        props.put("linger.ms", 1);
        props.put("buffer.memory", 33554432);
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        Producer<String, String> producer = new KafkaProducer(props);//建立连接
        for (int i = 0; i < 100; i++){
            producer.send(new ProducerRecord<String, String>("car", Integer.toString(i), "send msg"+i));
            //发送消息，格式：new ProducerRecord<String, String>(topicName, key, value))
            //这里发送的消息是异步的，先发送到缓冲区，所以这里消息没有办法保证kafka总是能收到发送消息。
        }
        producer.close();//发送完成之后要关闭生产者
    }
}