package com.lengshao.rabbitmq.utils;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class ConnectionUtils {


    public static Connection getConnection() throws IOException, TimeoutException {

        ConnectionFactory connectionFactory = new ConnectionFactory();

        connectionFactory.setHost("192.168.2.124");

        connectionFactory.setPort(5672);

        connectionFactory.setVirtualHost("/vhost_lengshao");

        connectionFactory.setUsername("lengshao");

        connectionFactory.setPassword("123456789");

        return connectionFactory.newConnection();
    }

}
