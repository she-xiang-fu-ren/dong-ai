/**
 * Copyright (c)  2020~2023, LinkingCloud/DT
 * All rights reserved.
 * Project:spring-boot-websocket-chat-demo
 * Id: RabbitConfig.java   2023-05-09 13:31:10
 * Author: Evan
 */
package cn.github.iocoder.dong.core.config;

import cn.github.iocoder.dong.core.helper.WsAnswerHelper;
import cn.github.iocoder.dong.core.helper.dto.RabbitMqDTO;
import cn.github.iocoder.dong.core.utils.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;


@Slf4j
@Configuration
public class RabbitConfig {

    @Resource
    private RabbitMqProperties rabbitMqProperties;

    @Autowired
    private WsAnswerHelper answerHelper;

    @Bean
    public Queue queue() {
        return new Queue(rabbitMqProperties.getMSG_TOPIC_QUEUE(), true);
    }


    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(rabbitMqProperties.getMQ_EXCHANGE(), true, false);
    }

    @Bean
    public Binding binding(Queue  queue,TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(rabbitMqProperties.getMSG_TOPIC_KEY());
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory(rabbitMqProperties.getHost(), rabbitMqProperties.getPort());
        connectionFactory.setUsername(rabbitMqProperties.getUsername());
        connectionFactory.setPassword(rabbitMqProperties.getPassword());
        connectionFactory.setVirtualHost(rabbitMqProperties.getVirtualHost());
        connectionFactory.setPublisherConfirmType(CachingConnectionFactory.ConfirmType.CORRELATED);
        connectionFactory.setPublisherReturns(Boolean.TRUE);
        return connectionFactory;
    }

    @Bean
    public RabbitTemplate createRabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate();
        rabbitTemplate.setConnectionFactory(connectionFactory);
        //设置开启Mandatory,才能触发回调函数,无论消息推送结果怎么样都强制调用回调函数
        rabbitTemplate.setMandatory(true);

        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("ConfirmCallback:     " + "相关数据：" + correlationData);
            log.info("ConfirmCallback:     " + "确认情况：" + ack);
           log.info("ConfirmCallback:     " + "原因：" + cause);
        });

        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
           log.info("ReturnCallback:     " + "消息：" + message);
           log.info("ReturnCallback:     " + "回应码：" + replyCode);
           log.info("ReturnCallback:     " + "回应信息：" + replyText);
           log.info("ReturnCallback:     " + "交换机：" + exchange);
           log.info("ReturnCallback:     " + "路由键：" + routingKey);
        });

        return rabbitTemplate;
    }


    /**
     * 接受消息的监听，这个监听会接受消息队列topicQueue的消息
     * 针对消费者配置
     *
     * @return
     */
    @Bean
    public SimpleMessageListenerContainer messageContainer(Queue  queue) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory());
        container.setQueues(queue);
        container.setExposeListenerChannel(true);
        container.setMaxConcurrentConsumers(1);
        container.setConcurrentConsumers(1);
        //设置确认模式手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(new ChannelAwareMessageListener() {
            public void onMessage(Message message, com.rabbitmq.client.Channel channel) throws Exception {
                byte[] body = message.getBody();
                String msg = new String(body);
               log.info("rabbitmq收到消息 : " + msg);
                RabbitMqDTO rabbitMqDTO = JsonUtil.toObj(msg, RabbitMqDTO.class);
                Boolean sendToWebsocket = answerHelper.sendMsgToUser(rabbitMqDTO);

                if (sendToWebsocket) {
                   log.info("消息处理成功!");
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), true); //确认消息成功消费

                }
            }

        });
        return container;
    }


}
