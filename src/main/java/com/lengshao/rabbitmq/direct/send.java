package com.lengshao.rabbitmq.direct;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class send {

    public static final String EXCHANGE_NAME="test_exchange_direct";
    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE_NAME,"direct");
        channel.basicQos(1);
        String msg="hello direct";
        String routeKey="infoo";
        channel.basicPublish(EXCHANGE_NAME,routeKey,false,null,msg.getBytes());
        System.out.println("send :"+msg);
        channel.close();
        connection.close();
    }
}
