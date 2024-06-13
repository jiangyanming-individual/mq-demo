package com.itheima.publisher;


import org.junit.jupiter.api.Test;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class publisherTest {


    @Resource
    private RabbitTemplate rabbitTemplate;
    /**
     * 发送消息
     */
    @Test
    public void  testSendMessage() throws InterruptedException {
        String queueName = "simple.queue";
        String message = "Hello rabbitMQ";
            //发送消息
        rabbitTemplate.convertAndSend(queueName, message );
    }


    @Test
    public void  testSendWorkQueueMessage() throws InterruptedException {
        String queueName = "work.queue";
        String message = "Hello rabbitMQ_";
        for (int i = 1; i <= 50; i++) {
            //发送消息
            rabbitTemplate.convertAndSend(queueName, message + i);
            Thread.sleep(20);
        }
    }

    /**
     * fanout交换机
     * @throws InterruptedException
     */
    @Test
    public void  testSendFanoutExchangeMessage() throws InterruptedException {
        String exchangeName = "fanoutexchange";
        String message = "Hello everyOne";
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName,null, message);

    }

    /**
     * Direct交换机
     * @throws InterruptedException
     */
    @Test
    public void  testSendDirectExchangeMessage() throws InterruptedException {
        String exchangeName = "directExchange";
        String message = "Hello everyOne,this is Direct Exchange";
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName,"blue", message);

    }

    /**
     * Toptic交换机
     * @throws InterruptedException
     */
    @Test
    public void  testSendTopticExchangeMessage() throws InterruptedException {
        String exchangeName = "topticExchange";
        String message = "Hello everyOne,this is Toptic Exchange";
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName,"china.news", message);

    }

}
