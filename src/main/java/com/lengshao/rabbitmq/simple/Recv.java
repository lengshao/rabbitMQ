package com.lengshao.rabbitmq.simple;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {
    private static final String NAME="test_rabbitmq_simple";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(NAME,false,false,false,null);

        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String msgString = new String(body);

                System.out.println("recv msg:"+msgString);
            }
        };

        channel.basicConsume(NAME,true,defaultConsumer);
    }

}
