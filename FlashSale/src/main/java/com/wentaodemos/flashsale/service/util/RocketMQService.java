package com.wentaodemos.flashsale.service.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

@Slf4j
@Service
public class RocketMQService {

    @Resource
    private RocketMQTemplate rocketMQTemplate;


    /**
     * Send message
     * @param topic
     * @param body
     * @throws Exception
     */

    public void sendMessage(String topic, String body) throws Exception{
        Message message = new Message(topic, body.getBytes());
        log.info("Regular Message created: " + message.toString());
        rocketMQTemplate.getProducer().send(message);
        System.out.println("Regular Message Sent");
    }

    public void sendDelayedMessage(String topic, String body, int delayTimeLevel) throws Exception{
        Message message = new Message(topic, body.getBytes());
        log.info("Delayed Message created: " + message.toString());
        message.setDelayTimeLevel(delayTimeLevel);
        rocketMQTemplate.getProducer().send(message);
        System.out.println("Delayed Message Sent");
    }
}
