package com.services.notification_ms.amqp;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationAMQPConfiguration {


    @Bean
    public Queue notificationOrder(){
        return new Queue("notification.order");
    }

    @Bean
    public FanoutExchange fanoutExchange(){
        return new FanoutExchange("ex.orders-created");
    }

    @Bean
    public Binding bindOrderNotification(FanoutExchange fanoutExchange){
        return BindingBuilder
                .bind(notificationOrder())
                .to(fanoutExchange());
    }


    @Bean
    public Queue notificationPayment(){ return new Queue("notification.payment");}

    @Bean
    public FanoutExchange ordersPaidExchange(){
        return new FanoutExchange("ex.orders-paid");
    }

    @Bean
    public Binding bindOrdersPaid(){
        return BindingBuilder
                .bind(notificationPayment())
                .to(ordersPaidExchange());
    }

    @Bean
    public RabbitAdmin createRabbitAdmin(ConnectionFactory conn) {
        return new RabbitAdmin(conn);
    }

    @Bean
    public ApplicationListener<ApplicationReadyEvent> initializeAdmin(RabbitAdmin rabbitAdmin) {
        return event -> rabbitAdmin.initialize();
    }

    @Bean
    public Jackson2JsonMessageConverter jackson2JsonMessageConverter(){
        return new Jackson2JsonMessageConverter();
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, Jackson2JsonMessageConverter jackson2JsonMessageConverter){
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(jackson2JsonMessageConverter);
        return rabbitTemplate;
    }
}
