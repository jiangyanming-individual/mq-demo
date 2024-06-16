package com.itheima.publisher.config;


import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.ReturnedMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.context.annotation.Configuration;
import javax.annotation.PostConstruct;


/**
 *  * 生产者消息回调配置类
 */
@Configuration
@Slf4j
@AllArgsConstructor
public class RabbitMQConfig {

    private final RabbitTemplate rabbitTemplate;
    /**
     * 配置回调
     */
    @PostConstruct
    public void init(){
        rabbitTemplate.setReturnsCallback(new RabbitTemplate.ReturnsCallback() {
            @Override
            public void returnedMessage(ReturnedMessage returnedMessage) {
                log.error("触发return callback,");
                log.debug("交换机为： "+ returnedMessage.getExchange());
                log.debug("消息为： "+ returnedMessage.getMessage());
                log.debug("RoutingKey为： "+ returnedMessage.getRoutingKey());
                log.debug("ReplyText为： "+ returnedMessage.getReplyText());
                log.debug("ReplyCode为： "+ returnedMessage.getReplyCode());
            }
        });
    }
}
