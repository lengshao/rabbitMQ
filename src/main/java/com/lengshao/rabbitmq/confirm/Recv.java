package com.lengshao.rabbitmq.confirm;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class Recv {

    public static final String EXCHANGE_NAME="test_exchange_confirm";
    public static final String EXCHANGE_NAME_DLX="test_exchange_confirm_dlx";
    public static final String QUEUE_NAME="test_queue_confirm";


    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        Map<String,Object> arg = new HashMap<String, Object>();
        arg.put("x-message-ttl",10000);
        arg.put("x-dead-letter-exchange",EXCHANGE_NAME_DLX);
        arg.put("x-dead-letter-routing-key","routingkey");
        channel.queueDeclare(QUEUE_NAME, true, false, false, arg);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, "confirm_synic");
        channel.basicQos(5);
        channel.basicConsume(QUEUE_NAME, false, new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String recMsg = "";
                try {
                if(body!=null){
                    recMsg = new String(body);
                }
//                    System.out.println("recv all:" + recMsg);
                    if (Integer.parseInt(recMsg.split("_")[1]) % 2 != 0) {
                        System.out.println("recv success:" + recMsg);
                        //     System.out.println(envelope.getDeliveryTag());
                        channel.basicAck(envelope.getDeliveryTag(), false);
                    } else {
                        int i = 1/0;
                    }
                } catch (Exception e) {
                    channel.basicNack(envelope.getDeliveryTag(), false, true);
//                    e.printStackTrace();
                } finally {
                   // System.out.println(Integer.parseInt(recMsg.split("_")[1]));


                }
            }
        });

    }
}
