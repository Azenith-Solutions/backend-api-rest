package com.azenithsolutions.backendapirest.v2.infrastructure.config.bean;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.config.RetryInterceptorBuilder;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.RabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.retry.RepublishMessageRecoverer;
import org.springframework.amqp.support.converter.Jackson2JavaTypeMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.interceptor.RetryOperationsInterceptor;

import java.util.Map;

@Configuration
public class RabbitMQConfig {
    @Value("${broker.order.command.exchange}")
    private String orderExchangeName;
    @Value("${broker.order.command.queue}")
    private String orderQueueName;
    @Value("${broker.order.command.routing-key}")
    private String orderRoutingKey;
    @Value("${broker.order.command.dlx}")
    private String orderDlxName;
    @Value("${broker.order.command.dlq}")
    private String orderDlqName;

    @Value("${broker.order.event.exchange}")
    private String orderEventExchangeName;
    @Value("${broker.order.event.queue}")
    private String orderEventQueueName;
    @Value("${broker.order.event.routing-key}")
    private String orderEventRoutingKey;
    @Value("${broker.order.event.dlx}")
    private String orderEventDlxName;
    @Value("${broker.order.event.dlq}")
    private String orderEventDlqName;

    @Bean
    public AmqpAdmin amqpAdmin(ConnectionFactory connectionFactory) {
        return new RabbitAdmin(connectionFactory);
    }

    @Bean
    public DirectExchange orderExchange() {
        return ExchangeBuilder
                .directExchange(orderExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public DirectExchange orderDeadLetterExchange() {
        return ExchangeBuilder
                .directExchange(orderDlxName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderQueue() {
        return QueueBuilder.durable(orderQueueName)
                .withArguments(Map.of(
                        "x-dead-letter-exchange", orderDlxName,
                        "x-dead-letter-routing-key", orderRoutingKey
                ))
                .build();
    }

    @Bean
    public Queue orderDeadLetterQueue() {
        return QueueBuilder.durable(orderDlqName).build();
    }

    @Bean
    public Binding orderBinding(Queue orderQueue, DirectExchange orderExchange) {
        return BindingBuilder
                .bind(orderQueue)
                .to(orderExchange)
                .with(orderRoutingKey);
    }

    @Bean
    public Binding orderDlqBinding(Queue orderDeadLetterQueue, DirectExchange orderDeadLetterExchange) {
        return BindingBuilder
                .bind(orderDeadLetterQueue)
                .to(orderDeadLetterExchange)
                .with(orderRoutingKey);
    }

    @Bean
    public TopicExchange orderEventExchange() {
        return ExchangeBuilder
                .topicExchange(orderEventExchangeName)
                .durable(true)
                .build();
    }

    @Bean
    public TopicExchange orderEventDeadLetterExchange() {
        return ExchangeBuilder
                .topicExchange(orderEventDlxName)
                .durable(true)
                .build();
    }

    @Bean
    public Queue orderEventQueue() {
        return QueueBuilder.durable(orderEventQueueName)
                .withArguments(Map.of(
                        "x-dead-letter-exchange", orderEventDlxName,
                        "x-dead-letter-routing-key", orderEventRoutingKey
                ))
                .build();
    }

    @Bean
    public Queue orderEventDeadLetterQueue() {
        return QueueBuilder.durable(orderEventDlqName).build();
    }

    @Bean
    public Binding orderEventBinding(Queue orderEventQueue, TopicExchange orderEventExchange) {
        return BindingBuilder
                .bind(orderEventQueue)
                .to(orderEventExchange)
                .with(orderEventRoutingKey);
    }

    @Bean
    public Binding orderEventDlqBinding(Queue orderEventDeadLetterQueue, TopicExchange orderEventDeadLetterExchange) {
        return BindingBuilder
                .bind(orderEventDeadLetterQueue)
                .to(orderEventDeadLetterExchange)
                .with(orderEventRoutingKey);
    }


    @Bean
    public RetryOperationsInterceptor orderRetryInterceptor(RabbitTemplate rabbitTemplate) {
    RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(
        rabbitTemplate,
        orderDlxName,
        orderRoutingKey
    );

    return RetryInterceptorBuilder.stateless()
        .maxAttempts(3)
        .backOffOptions(1000, 2.0, 10000)
        .recoverer(recoverer)
        .build();
    }

    @Bean
    public RetryOperationsInterceptor orderEventRetryInterceptor(RabbitTemplate rabbitTemplate) {
        RepublishMessageRecoverer recoverer = new RepublishMessageRecoverer(
                rabbitTemplate,
                orderEventDlxName,
                orderEventRoutingKey
        );

        return RetryInterceptorBuilder.stateless()
                .maxAttempts(3)
                .backOffOptions(1000, 2.0, 10000)
                .recoverer(recoverer)
                .build();
    }

    @Bean
    public MessageConverter jacksonMessageConverter(ObjectMapper objectMapper) {
        Jackson2JsonMessageConverter converter = new Jackson2JsonMessageConverter(objectMapper);
        converter.setTypePrecedence(Jackson2JavaTypeMapper.TypePrecedence.INFERRED);
        return converter;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter);
        return template;
    }

    @Bean
    public RabbitListenerContainerFactory<?> orderListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter,
            RetryOperationsInterceptor orderRetryInterceptor) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jacksonMessageConverter);
        factory.setAdviceChain(orderRetryInterceptor);
        return factory;
    }

    @Bean
    public RabbitListenerContainerFactory<?> orderEventListenerContainerFactory(
            ConnectionFactory connectionFactory,
            MessageConverter jacksonMessageConverter,
            RetryOperationsInterceptor orderEventRetryInterceptor) {

        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setConcurrentConsumers(2);
        factory.setMaxConcurrentConsumers(5);
        factory.setDefaultRequeueRejected(false);
        factory.setMessageConverter(jacksonMessageConverter);
        factory.setAdviceChain(orderEventRetryInterceptor);
        return factory;
    }
}
