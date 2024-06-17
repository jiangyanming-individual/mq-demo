package com.itheima.consumer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.retry.MessageRecoverer;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * 重试策略失败后，失败处理策略,处理失败消息的队列
 */
@Configuration
@Slf4j
public class ErrorMessageConfig {

    //声明交换机
    @Bean
    public DirectExchange errorExchange(){
        return new DirectExchange("error.direct",true,true);
    }
    //声明队列
    @Bean
    public Queue errorQueue(){
        return new Queue("error.queue",true);
    }

    //绑定交换机和队列
    @Bean
    public Binding errorBinding(DirectExchange errorExchange,Queue errorQueue){
        return BindingBuilder.bind(errorQueue).to(errorExchange).with("error");
    }

    //关联交换机和队列
    @Bean
    public MessageRecoverer republisherMessageRecoverer(RabbitTemplate rabbitTemplate){
        log.debug("####消息重试失败，进入错误队列，准备在错误队列进行处理");
        return new RepublishMessageRecoverer(rabbitTemplate,"error.direct","error");
    }
}
