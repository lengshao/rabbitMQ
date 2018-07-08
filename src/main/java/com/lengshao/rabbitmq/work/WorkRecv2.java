package com.lengshao.rabbitmq.work;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkRecv2 {

    public static final String NANME="test_rabbitmq_work";
    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();

        final Channel channel = connection.createChannel();

        channel.queueDeclare(NANME,false,false,false,null);
        //只发送一个
        channel.basicQos(1);
        DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {

                String msg = new String(body);

                System.out.println("WorkRecv2 recv:"+msg);

                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }finally {
                    System.out.println("WorkRecv2 done");
                    channel.basicAck(envelope.getDeliveryTag(),false);

                }
            }
        };




        channel.basicConsume(NANME,false,defaultConsumer);
    }
}
