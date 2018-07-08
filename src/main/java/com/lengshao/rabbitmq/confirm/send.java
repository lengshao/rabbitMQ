package com.lengshao.rabbitmq.confirm;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.ConfirmListener;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.Collections;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.concurrent.TimeoutException;

public class send {

    public static final String EXCHANGE_NAME="test_exchange_confirm";

    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"topic");
        channel.basicQos(1);
        channel.confirmSelect();
        final SortedSet<Long> confirmSet = Collections.synchronizedSortedSet(new TreeSet<Long>());
        channel.addConfirmListener(new ConfirmListener() {
            //成功的
            @Override
            public void handleAck(long l, boolean b) throws IOException {
                if(b){
                    confirmSet.headSet(l+1).clear();
                }else{
                    confirmSet.remove(l);
                    System.out.println("remove " +l);

                }

            }
            @Override
            public void handleNack(long l, boolean b) throws IOException {
                if(b){
                    confirmSet.headSet(l+1).clear();
                }else{
                    confirmSet.remove(l);
                }

            }
        });

        String msg = "hello confirm+topic!";
        boolean flg =true;
        while(flg){
            long nextPublishSeqNo = channel.getNextPublishSeqNo();
            channel.basicPublish(EXCHANGE_NAME,"confirm_synic",null,(msg+"_"+nextPublishSeqNo).getBytes());
            confirmSet.add(nextPublishSeqNo);
            if(nextPublishSeqNo>=10){
                flg=false;
            }
        }
        for (Long l :confirmSet
             ) {
            System.out.println(l);
        }
    }
}
