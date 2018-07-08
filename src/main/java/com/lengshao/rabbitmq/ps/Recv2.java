package com.lengshao.rabbitmq.ps;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv2 {
    public static final String EXCHANGE_NAME = "test_exchange_fanout";
    public static final String QUEUE_NAME="test_queue_ps2";
    public static void main(String[] args) {

        try {
            Connection connection = ConnectionUtils.getConnection();
            final Channel channel = connection.createChannel();
            channel.queueDeclare(QUEUE_NAME,true,false,false,null);
            channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"");
            channel.basicQos(1);
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                    String msg = new String(body);

                    System.out.println("psRecv2 recv:" + msg);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }finally {
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            };
            channel.basicConsume(QUEUE_NAME,false,defaultConsumer);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }


    }

}
