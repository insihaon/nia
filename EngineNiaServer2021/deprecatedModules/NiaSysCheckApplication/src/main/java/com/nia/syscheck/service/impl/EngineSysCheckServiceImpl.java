package com.nia.syscheck.service.impl;

import com.nia.syscheck.service.SysCheckService;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;
import lombok.RequiredArgsConstructor;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.TimeoutException;


@Service("EngineSysCheckService")
@RequiredArgsConstructor
public class EngineSysCheckServiceImpl implements SysCheckService {
    private final org.apache.log4j.Logger LOGGER = Logger.getLogger(SysCheckService.class);

    private static final String EXCHANGE_NAME = "nia.exchangeEngineDirectly";
    private static final String ROUTING_KEY = "nia.EngineIndexDirectly";
    private static final String QUEUE_NAME = "nia.EngineIndexDirectly";

    @Override
    public void sysCheck() throws IOException, TimeoutException {

        LOGGER.info(">>>>> [ SysCheckService 'Engine' ] <<<<<");

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("116.89.191.47");

        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, "fanout");
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        channel.queueBind(QUEUE_NAME, EXCHANGE_NAME, ROUTING_KEY);

        DeliverCallback deliverCallback = (consumerTag, message) -> {
            String messageBody = new String(message.getBody());
            System.out.println("Received message: " + messageBody);
        };

        channel.basicConsume(QUEUE_NAME, true, deliverCallback, (consumerTag, cause) -> {
            System.out.println("Error consuming message: " + cause.getMessage());
        });

        long messageCount = channel.messageCount(QUEUE_NAME);
        if (messageCount > 0) {
            System.out.println("There are " + messageCount + " messages in the queue");
            /*
            * 큐에 메세지가 있으면 ok
            * */
        } else {
            System.out.println("The queue is empty");
        }

        channel.close();
        connection.close();
    }

    }
