package hospital;


import java.io.IOException;


/**
 * Created by AD on 20.04.2018.
 */
public class AdminConsumer {

    private Channel channel;

    AdminConsumer(Channel channel){
        this.channel = channel;
    }

    public void initConsumer(String queueName){
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println("Log: " + message);
            }
        };
        try {
            channel.basicConsume(queueName, true, consumer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
