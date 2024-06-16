package com.itheima.publisher.config;


import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;

/**
 * 生产者序列化
 */
@Configuration
public class RabbitMQConfig implements InitializingBean {

    @Resource
    private RabbitTemplate rabbitTemplate;


    /**
     * 发送消息JSON序列化
     */
    @Override
    public void afterPropertiesSet() throws Exception {

        //使用JSON序列化
        Jackson2JsonMessageConverter jackson2JsonMessageConverter = new Jackson2JsonMessageConverter();
        jackson2JsonMessageConverter.setCreateMessageIds(true);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
    }
}
