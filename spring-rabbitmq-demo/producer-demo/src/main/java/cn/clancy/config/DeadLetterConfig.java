package cn.clancy.config;

import cn.clancy.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ClancyLv
 * @Date 2025/07/07 15:45
 * @Description 配置类--死信队列
 * 该类用于配置 RabbitMQ 中与死信队列相关的组件，包括死信交换机、死信队列及其绑定关系。
 * 死信队列用于存储无法被正常消费的消息，从而方便系统对这些消息进行监控和处理。
 */
@Configuration
public class DeadLetterConfig {

    /**
     * 定义一个用于配置死信交换机的Bean。
     * 死信交换机用于与死信队列建立绑定关系，以确保未被正常消费的消息能够被路由到指定的死信队列。
     *
     * @return 返回配置的用于处理死信的DirectExchange实例，该实例的名称为RabbitMqConstants.DEAD_LETTER_EXCHANGE。
     */
    @Bean
    public DirectExchange deadLetterExchange() {
        // 默认持久化、非自动删除
        return new DirectExchange(RabbitMqConstants.DEAD_LETTER_EXCHANGE);
    }

    /**
     * 定义一个名为死信队列（Dead Letter Queue）的Bean。
     * 死信队列用于存储未能成功消费或因其他原因被拒绝的消息，在消息达到重试限制或不符合队列规则等情况下，消息会被路由到该队列。
     *
     * @return 返回配置的用于处理死信的Queue实例，该实例为持久化队列，名称为RabbitMqConstants.DEAD_LETTER_QUEUE。
     */
    @Bean
    public Queue deadLetterQueue() {
        // 设置为持久化队列，确保消息在RabbitMQ重启后仍然存在
        return QueueBuilder.durable(RabbitMqConstants.DEAD_LETTER_QUEUE).build();
    }

    /**
     * 定义一个用于配置死信队列与死信交换机绑定关系的Bean。
     * 该方法通过绑定关系确保消息能够正确路由到死信队列，适用于消息达到重试期限、被拒绝等场景。
     *
     * @return 返回配置的Binding实例，用于将死信队列绑定到死信交换机，同时指定路由键为RabbitMqConstants.DEAD_LETTER_ROUTING_KEY。
     */
    @Bean
    public Binding deadLetterBinding() {
        return BindingBuilder.bind(deadLetterQueue())
                .to(deadLetterExchange())
                .with(RabbitMqConstants.DEAD_LETTER_ROUTING_KEY);
    }
}
