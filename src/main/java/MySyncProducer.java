import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.KafkaException;
import org.apache.kafka.common.errors.AuthorizationException;
import org.apache.kafka.common.errors.OutOfOrderSequenceException;
import org.apache.kafka.common.errors.ProducerFencedException;
import org.apache.kafka.common.serialization.StringSerializer;

import java.util.Properties;
/*
注意到这里的示例依旧是将消息发送到缓冲区，但是立刻commit，
 */
public class MySyncProducer {
    public static void main(String[] args) {
        Properties props = new Properties();
        props.put("bootstrap.servers", "192.168.31.145:9092");
        props.put("transactional.id", "my-transactional-id");
        Producer<String, String> producer = new KafkaProducer(props, new StringSerializer(), new StringSerializer());

        producer.initTransactions();

        try {
            producer.beginTransaction();
            for (int i = 0; i < 100; i++)
                producer.send(new ProducerRecord("car", Integer.toString(i), "sync"+Integer.toString(i)));
            producer.commitTransaction();
        /*} catch (ProducerFencedException | OutOfOrderSequenceException | AuthorizationException e1) {
                *//* We can't recover from these exceptions, so our only option is to close the producer and exit. *//*
                producer.close();*/
        } catch (KafkaException e) {
            // For all other exceptions, just abort the transaction and try again.
            producer.abortTransaction();
        }
        producer.close();

    }
}