package com.lengshao.rabbitmq.direct;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {

    public static final String EXCHANGE_NAME="test_exchange_direct";
    public static final String QUEUE_NAME="test_queue_direct";
    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"error");
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                System.out.println("Recv1 :" + new String(body));
            }
        };
        channel.basicConsume(QUEUE_NAME,true,defaultConsumer);

    }
}
