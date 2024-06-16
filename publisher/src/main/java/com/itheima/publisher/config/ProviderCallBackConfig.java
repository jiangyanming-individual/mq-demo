//package com.itheima.publisher.config;
//
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.amqp.core.ReturnedMessage;
//import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
//import org.springframework.amqp.rabbit.connection.CorrelationData;
//import org.springframework.amqp.rabbit.core.RabbitTemplate;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import javax.annotation.Resource;
//
//@Configuration
//@Slf4j
//public class ProviderCallBackConfig {
//
//
//    @Resource
//    private CachingConnectionFactory cachingConnectionFactory;
//    @Bean
//    public RabbitTemplate rabbitTemplate() {
//        RabbitTemplate rabbitTemplate = new RabbitTemplate(cachingConnectionFactory);
//        // 当mandatory设置为true时，若exchange根据自身类型和消息routingKey无法找到一个合适的queue存储消息，
//        //那么broker会调用basic.return方法将消息返还给生产者。
//        // 当mandatory设置为false时，出现上述情况broker会直接将消息丢弃。
//        rabbitTemplate.setMandatory(true);
//
//        /**
//         * Return
//         */
//        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
//            log.error("触发return callback,");
//            log.debug("交换机为: "+ exchange);
//            log.debug("消息为："+message);
//            log.debug("RoutingKey为: "+routingKey);
//            log.debug("回应内容为: "+ replyText);
//            log.debug("回应码为: "+ replyCode);
//        });
//
//        /**
//         * Confirm
//         */
//        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            if (ack) {
//                //消息发送成功后，更新数据库消息状态等逻辑
//                log.info("------生产者发送消息至exchange成功,消息唯一标识: {}, 确认状态: {}, 造成原因: {}-----",correlationData, ack, cause);
//            } else {
//                //信息发送失败，打印日志后，可以根据业务选择是否重发消息
//                log.info("------生产者发送消息至exchange失败,消息唯一标识: {}, 确认状态: {}, 造成原因: {}-----", correlationData, ack, cause);
//            }
//        });
//        return rabbitTemplate;
//    }
//
//}
