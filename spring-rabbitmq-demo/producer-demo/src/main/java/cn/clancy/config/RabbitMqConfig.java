package cn.clancy.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.retry.backoff.ExponentialBackOffPolicy;
import org.springframework.retry.policy.SimpleRetryPolicy;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description RabbitMQ核心配置类，提供消息转换、重试机制等基础功能
 */
@Slf4j
@Configuration
public class RabbitMqConfig {

    /**
     * 配置消息转换器
     */
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }

    /**
     * 配置生产者重试模板
     */
    @Bean
    public RetryTemplate retryTemplate() {
        RetryTemplate retryTemplate = new RetryTemplate();
        ExponentialBackOffPolicy backOffPolicy = new ExponentialBackOffPolicy();
        // 单位毫秒，默认100毫秒
        backOffPolicy.setInitialInterval(1000);
        // 最大重试间隔，单位毫秒，默认30000毫秒
        backOffPolicy.setMaxInterval(10000);
        // 重试间隔倍数，默认2
        backOffPolicy.setMultiplier(2);
        retryTemplate.setBackOffPolicy(backOffPolicy);
        // 重试次数 默认3次
        retryTemplate.setRetryPolicy(new SimpleRetryPolicy(3));
        return retryTemplate;
    }

    /**
     * 配置RabbitTemplate
     */
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter messageConverter) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter);
        // 消息发送成功的回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            if (ack) {
                log.info("消息成功到达交换机: {}", correlationData);
            } else {
                log.error("消息未到达交换机: {} 原因: {}", correlationData, cause);
            }
        });

        // 消息发送失败的回调
        rabbitTemplate.setReturnsCallback(returned -> log.error("消息返回: {} ，回复编码: {} ，回复内容: {}",
                returned.getMessage(),
                returned.getReplyCode(),
                returned.getReplyText()));
        return rabbitTemplate;
    }
}
