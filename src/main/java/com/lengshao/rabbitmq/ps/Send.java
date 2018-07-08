package com.lengshao.rabbitmq.ps;

import com.lengshao.rabbitmq.utils.ConnectionUtils;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Send {

    public static final String EXCHANGE_NAME = "test_exchange_fanout";

    public static void main(String[] args) {
        Connection connection = null;
        Channel channel = null;
        try {
            connection = ConnectionUtils.getConnection();
            channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
            channel.basicQos(1);
            String msg = "hello ps";
            channel.basicPublish(EXCHANGE_NAME, "", null, msg.getBytes());
            System.out.println("ps send:"+msg);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        } finally {
            try {
                if (channel != null) {
                    channel.close();
                }

                if(connection!=null){
                    connection.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (TimeoutException e) {
                e.printStackTrace();
            }

        }

    }


}
