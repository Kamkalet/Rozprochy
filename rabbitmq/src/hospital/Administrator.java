package hospital;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static hospital.Constants.*;


public class Administrator {

    private static Channel channel;

    public static void main(String[] argv) throws Exception {
        System.out.println("Administrator");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);

        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "*");

        initConsumer(queueName);

        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            System.out.println("Enter message:");
            String message = reader.readLine();
            channel.basicPublish(ADMIN_EXCHANGE_NAME, "*", null, message.getBytes("UTF-8"));
        }
    }

    private static void initConsumer(String queueName) throws IOException {


    }
}
