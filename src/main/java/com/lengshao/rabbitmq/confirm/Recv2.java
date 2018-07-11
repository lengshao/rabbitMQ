package com.lengshao.rabbitmq.confirm;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Recv2 {

    public static final String QUEUE_NAME = "test_queue_confirm_dlx";
    public static final String EXCHANGE_NAME = "test_exchange_confirm_dlx";
    static int a = 1;

    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        Map<String,Object> arg2 = new HashMap<String, Object>();
        arg2.put("x-message-ttl",10000);
        channel.queueDeclare(QUEUE_NAME, true, false, false, arg2);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "routingkey");
        channel.basicQos(5);
        channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String recMsg = "";
                try {
                    //                    if(body!=null){
                    recMsg = new String(body);
                    Thread.sleep(50);
//                    System.out.println("recv2 all:" + recMsg);
                    if (Integer.parseInt(recMsg.split("_")[1]) % 3== 0) {
                        System.out.println("recv2 success:" + recMsg);
//                            System.out.println(envelope.getDeliveryTag());
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } else {
                        channel.basicNack(envelope.getDeliveryTag(), false, true);
                    }
//                }
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, false);
                    e.printStackTrace();
                } finally {
//                    System.out.println(Integer.parseInt(recMsg.split("_")[1]));


                }
            }
        });

    }
}
