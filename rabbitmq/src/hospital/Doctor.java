package hospital;

import com.rabbitmq.client.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import static hospital.Constants.ADMIN_EXCHANGE_NAME;
import static hospital.Constants.EXCHANGE_NAME;
import static hospital.Constants.HOST;

public class Doctor {

    public static void main(String[] argv) throws Exception {
        System.out.println("DOCTOR");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(HOST);
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(ADMIN_EXCHANGE_NAME, BuiltinExchangeType.FANOUT);
        String adminQueue = channel.queueDeclare().getQueue();
        channel.queueBind(adminQueue, ADMIN_EXCHANGE_NAME, "");

        Consumer adminConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Message from admin: " + message);
            }
        };
        channel.basicConsume(adminQueue, true, adminConsumer);


        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        while (true) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            System.out.println("Enter examination type: (or exit to finish)");
            String examinationType = reader.readLine();
            System.out.println("Enter patient surname: ");
            String patientSurname = reader.readLine();

            if ("exit".equals(examinationType)) {
                break;
            }

            String queueName = channel.queueDeclare().getQueue();
            channel.queueBind(queueName, EXCHANGE_NAME, patientSurname);
            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    System.out.println("Examination result: " + message);
                    channel.basicCancel(consumerTag);
                }
            };
            channel.basicConsume(queueName, true, consumer);

            channel.basicPublish(EXCHANGE_NAME, examinationType, null, patientSurname.getBytes("UTF-8"));
            System.out.println("Sent: " + patientSurname);
            System.out.println("Waiting for result...");
        }
    }
}
