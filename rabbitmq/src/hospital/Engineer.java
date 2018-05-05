package hospital;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static hospital.Constants.*;

public class Engineer {
    public static void main(String[] argv) throws Exception {
        System.out.println("Engineer");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("First examination type:");
        String firstExaminationType = reader.readLine();
        System.out.println("Second examination type:");
        String secondExaminationType = reader.readLine();

        String firstQueueName = channel.queueDeclare(firstExaminationType, false, false, true, null).getQueue();
        String secondQueueName = channel.queueDeclare(secondExaminationType, false, false, true, null).getQueue();
        channel.queueBind(firstQueueName, EXCHANGE_NAME, firstExaminationType);
        channel.queueBind(secondQueueName, EXCHANGE_NAME, secondExaminationType);

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String patientName = new String(body, "UTF-8");
                System.out.println("Got examination to do: " + envelope.getRoutingKey() + " " + patientName);

                System.out.println("Doing examination...");
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String result = String.format("%s %s %s", patientName, envelope.getRoutingKey(), "done");

                System.out.println("Sending results " + result);
                channel.basicPublish(EXCHANGE_NAME, patientName, null, result.getBytes("UTF-8"));
            }
        };

        channel.basicConsume(firstQueueName, true, consumer);
        channel.basicConsume(secondQueueName, true, consumer);

        channel.exchangeDeclare(ADMIN_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String adminQueue = channel.queueDeclare().getQueue();
        channel.queueBind(adminQueue, ADMIN_EXCHANGE_NAME, "*");
        Consumer adminConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Message from admin: " + message);
            }
        };
        channel.basicConsume(adminQueue, true, adminConsumer);
    }
}
