package com.lengshao.rabbitmq.simple;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    private static final String NAME="test_rabbitmq_simple";
    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(NAME,false,false,false,null);

        String msg = "hello Simple !";
        channel.basicPublish("",NAME,null,msg.getBytes());

        System.out.println("send msg :"+msg);

        channel.close();
        connection.close();

    }

}
