package com.itheima.consumer.listener;


import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ExchangeTypes;
import org.springframework.amqp.rabbit.annotation.*;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Slf4j
public class SpringRabbitListener {


    @RabbitListener(queues = "simple.queue")
    public void listenSimpleQueueMessage(String message){
        System.out.println("spring 消费者接收到消息：【" + message + "】");
//        throw new RuntimeException("故意的");
    }

    /**
     * 一个队列对应多消费者
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue1(String message) throws InterruptedException {
        System.out.println("消费者1接收到消息 WorkQueue：【" + message + "】");
        Thread.sleep(20);
    }

    /**
     * 多消费者
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(queues = "work.queue")
    public void listenWorkQueue2(String message) throws InterruptedException {
        System.out.println("消费者2接收到消息 WorkQueue：【" + message + "】");
        Thread.sleep(200);
    }

    /**
     * fanout交换机
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(queues = "fanout.queue1")
    public void listenFanoutQueue1(String message) throws InterruptedException {
        System.out.println("消费者1接收到 Fanout交换机的消息：【" + message + "】");
    }


    @RabbitListener(queues = "fanout.queue2")
    public void listenFanoutQueue2(String message) throws InterruptedException {
        System.out.println("消费者2接收到 Fanout交换机的消息：【" + message + "】");
    }


    /**
     * Direct 交换机
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(queues = "direct.queue1")
    public void listenDirectQueue1(String message) throws InterruptedException {
        System.out.println("消费者1接收到 Direct交换机的消息：【" + message + "】");
    }


    @RabbitListener(queues = "direct.queue2")
    public void listenDirectQueue2(String message) throws InterruptedException {
        System.out.println("消费者2接收到 Direct交换机的消息：【" + message + "】");
    }

    /**
     * Topic交换机
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(bindings = @QueueBinding(
            value =@Queue(name = "toptic.queue1"),
            exchange = @Exchange(name = "topticExchange",type = ExchangeTypes.TOPIC),
            key = {"china.#"}
    ))
    public void listenTopicQueue1(String message) throws InterruptedException {
        System.out.println("消费者1接收到 Topic交换机的消息：【" + message + "】");
    }


    @RabbitListener(bindings = @QueueBinding(
            value =@Queue(name = "toptic.queue2"),
            exchange = @Exchange(name = "topticExchange",type = ExchangeTypes.TOPIC),
            key = {"china.#"}
    ))
    public void listenTopicQueue2(String message) throws InterruptedException {
        System.out.println("消费者2接收到 Topic交换机的消息：【" + message + "】");
    }

    /**
     * 定义消息转换器：
     * @param message
     * @throws InterruptedException
     */

    @RabbitListener(queues = "object.queue")
    public void listenObjectQueue(Map<String, Object> message) throws InterruptedException {
        System.out.println("转换器转后的消息：【" + message + "】");
    }

    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("hmall.queue"),
            exchange = @Exchange(value = "hmall.direct"),
            key = "red"
    ))
    public void listenPublisherConfirm(String message) throws InterruptedException {
        System.out.println("转换器转后的消息：【" + message + "】");
    }


    /**
     * 惰性队列
     * @param message
     * @throws InterruptedException
     */
    @RabbitListener(queuesToDeclare = @Queue(
            name = "lazy.queue",
            durable = "true",
            arguments = @Argument(name = "x-queue-mode",value = "lazy")
    ))
    public void listenLazyQueue(Map<String,Object> message) throws InterruptedException {
        System.out.println("转换器转后的消息：【" + message + "】");
    }

    /**
     * 绑定死信队列和交换机,死信监听：
     * @param message
     */
    @RabbitListener(bindings = @QueueBinding(
            value = @Queue("dlx.queue"),
            exchange = @Exchange("dlx.direct"),
            key = "dlx"
    ))
    public void listenDlxQueueMessage(String message){
        System.out.println("消费者接收到过期的消息，由死信交换机进行处理：【" + message + "】");
    }



    @RabbitListener(bindings = @QueueBinding(
            value = @Queue(name = "delay.queue", durable = "true"),
            exchange = @Exchange(name = "delay.direct", delayed = "true"),
            key = "delay"
    ))
    public void listenDelayMessage(String msg){
        log.info("接收到delay.queue的延迟消息：{}", msg);
    }

}
