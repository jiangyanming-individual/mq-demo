package com.itheima.publisher;


import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.core.MessagePostProcessor;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.Resource;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@SpringBootTest
@Slf4j
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

    @Test
    public void  testSendObjectMessage() throws InterruptedException {
        Map<String,Object> message = new HashMap<>();
        message.put("name", "柳岩");
        message.put("age", 21);
        //发送消息
        rabbitTemplate.convertAndSend("object.queue", message);
    }


    @Test
    public void  testSendPublisherConfirm() throws InterruptedException {
        // 1.创建CorrelationData
        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString()); //设置唯一的一个id
        //给future 添加callback
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }
            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if (result.isAck()){
                    log.debug("发送消息成功，收到 ack!");
                }else {
                    log.error("消息发送失败,返回nack，错误原因为：{}",result.getReason());
                }
            }
        });
        String exchange="hmall.direct";
        String message="hello";
        //发送消息
        rabbitTemplate.convertAndSend(exchange, "red",message,cd);
    }


    @Test
    public void  testSendLazyQueue() throws InterruptedException {

        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());
        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                // 2.1.Future发生异常时的处理逻辑，基本不会触发
                log.error("send message fail", ex);
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if (result.isAck()){
                    log.debug("消息发送成功，成功收到ack");
                }else {
                    log.error("消息发送失败，返回nack, 失败原因是{}", result.getReason());
                }
            }
        });
        Map<String,Object> message = new HashMap<>();
        message.put("name", "柳岩");
        message.put("age", 21);
        //发送消息
        rabbitTemplate.convertAndSend("lazy.queue", message,cd);
    }


    /**
     * 设置过期时间
     * @throws InterruptedException
     */
    @Test
    public void  testSendDlxMessage() throws InterruptedException {


        CorrelationData cd = new CorrelationData(UUID.randomUUID().toString());

        cd.getFuture().addCallback(new ListenableFutureCallback<CorrelationData.Confirm>() {
            @Override
            public void onFailure(Throwable ex) {
                log.error("消息发送失败");
            }

            @Override
            public void onSuccess(CorrelationData.Confirm result) {
                if (result.isAck()){
                    log.debug("消息发送成功");
                }else {
                    log.error("消息发送失败 {}",result.getReason());
                }
            }
        });
        //普通队列交换机
        String exchangeName = "toDlx.direct";
        String message = "Hello, this is todlx message";
        String routingKey="todlx";
        //发送消息
        rabbitTemplate.convertAndSend(exchangeName, routingKey, message, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws AmqpException {
                //设置过期时间为10s
                message.getMessageProperties().setExpiration(String.valueOf(10000));
                return message;
            }
        },cd);
        log.info("消息发送成功！");
    }

}
