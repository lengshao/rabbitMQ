package com.lengshao.rabbitmq.confirm;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Recv {

    public static final String QUEUE_NAME="test_queue_confirm";
    public static final String EXCHANGE_NAME="test_exchange_confirm";
    public static void main(String[] args) throws IOException, TimeoutException {
        Connection connection = ConnectionUtils.getConnection();
        final Channel channel = connection.createChannel();
        channel.queueDeclare(QUEUE_NAME,false,false,false,null);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"confirm_synic");
        channel.basicQos(1);
        channel.basicConsume(QUEUE_NAME,false,new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                String recMsg="";
                try {
                    if(body!=null){
                        recMsg= new String(body);
                    }
                }catch (Exception e){
                    channel.basicNack(envelope.getDeliveryTag(),false,true);
                    e.printStackTrace();
                }finally {
                    if("0".equals((envelope.getDeliveryTag()%2)+"")){
                        channel.basicNack(envelope.getDeliveryTag(),false,false);
                    }else{
                        System.out.println("recv success:"+recMsg);
                        channel.basicAck(envelope.getDeliveryTag(),false);
                    }
                }
            }
        });

    }
}
