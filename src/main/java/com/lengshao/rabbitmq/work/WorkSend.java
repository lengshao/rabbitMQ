package com.lengshao.rabbitmq.work;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import javax.naming.Name;
import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class WorkSend {

    public static final String NANME="test_rabbitmq_work";

    public static void main(String[] args) throws IOException, TimeoutException, InterruptedException {

        Connection connection = ConnectionUtils.getConnection();

        Channel channel = connection.createChannel();

        channel.queueDeclare(NANME,false,false,false,null);
        channel.basicQos(1);

        for (int i = 0; i < 50; i++) {

            String msg ="hello work"+i+"!";

            channel.basicPublish("",NANME,null,msg.getBytes());

            System.out.println("send msg:"+msg);

            Thread.sleep(i*5);
        }

        channel.close();
        connection.close();
    }
}
