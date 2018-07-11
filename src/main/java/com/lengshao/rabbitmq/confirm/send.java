package com.lengshao.rabbitmq.confirm;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeoutException;

public class send {

    public static final String EXCHANGE_NAME="test_exchange_confirm";
    public static final String EXCHANGE_NAME_DLX="test_exchange_confirm_dlx";
    public static final String QUEUE_NAME="test_queue_confirm";
    public static final String QUEUE_NAME_DLX="test_queue_confirm_dlx";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME_DLX,"direct",true);
        channel.exchangeDeclare(EXCHANGE_NAME,"topic",true);
        Map<String,Object> arg = new HashMap<String, Object>();
        arg.put("x-message-ttl",10000);
        arg.put("x-dead-letter-exchange",EXCHANGE_NAME_DLX);
        arg.put("x-dead-letter-routing-key","routingkey");
        Map<String,Object> arg2 = new HashMap<String, Object>();
        arg2.put("x-message-ttl",10000);
        channel.queueDeclare(QUEUE_NAME,true,false,false,arg);
        channel.queueDeclare(QUEUE_NAME_DLX,true,false,false,arg2);
        channel.queueBind(QUEUE_NAME,EXCHANGE_NAME,"confirm_synic");
        channel.queueBind(QUEUE_NAME_DLX,EXCHANGE_NAME_DLX,"routingkey");
        channel.basicQos(10);
        channel.confirmSelect();
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        channel.addConfirmListener(new ConfirmListener() {
            //成功的
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                System.out.println("handleAck"+l);
            }
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                System.out.println("handleNack"+l);
            }
        });

        String msg = "hello confirm+topic!";
        boolean flg =true;
        while(flg){
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            AMQP.BasicProperties properties = new AMQP.BasicProperties.Builder()
                    .deliveryMode(2).build();
            channel.basicPublish(EXCHANGE_NAME,"confirm_synic",properties,(msg+"_"+nextPublishSeqNo).getBytes());
            Thread.sleep(nextPublishSeqNo*300);
            System.out.println("send msg :"+msg+"_"+nextPublishSeqNo);
//            if(nextPublishSeqNo%2==0){
//                properties= properties.builder().expiration("5000").build();
//                channel.basicPublish(EXCHANGE_NAME,"confirm_synic",properties,(msg+"_"+nextPublishSeqNo).getBytes());
//                System.out.println("send msg :"+msg+"_"+nextPublishSeqNo);
//            }else{
//                properties=properties.builder().expiration("6000").build();
//                channel.basicPublish(EXCHANGE_NAME,"confirm_synic",properties,(msg+"_"+nextPublishSeqNo).getBytes());
//                System.out.println("send msg2 :"+msg+"_"+nextPublishSeqNo);
//            }
            confirmSet.add(nextPublishSeqNo);
            if(nextPublishSeqNo>=20){
                flg=false;
            }
        }

    }
}
