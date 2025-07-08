package cn.clancy.config;

import cn.clancy.constants.RabbitMqConstants;
import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 13:00
 * @Description 配置类--直连交换机
 */
@Configuration
public class DirectExchangeConfig {

    /**
     * 定义并配置一个直连交换机。
     * 该方法用于创建并返回一个DirectExchange实例。
     *
     * @return 配置完成的直连交换机实例
     */
    @Bean
    public DirectExchange directExchange() {
        // 默认持久化、非自动删除
        return new DirectExchange(RabbitMqConstants.DIRECT_EXCHANGE);
    }

    /**
     * 定义并配置一个直连队列。
     * 该队列是持久化的，并且配置了死信交换机、死信路由键以及消息的存活时间（TTL）。
     *
     * @return 配置完成的直连队列实例
     */
    @Bean
    public Queue directQueue() {
        return QueueBuilder.durable(RabbitMqConstants.DIRECT_QUEUE)
                .withArgument("x-dead-letter-exchange", RabbitMqConstants.DEAD_LETTER_EXCHANGE)
                .withArgument("x-dead-letter-routing-key", RabbitMqConstants.DEAD_LETTER_ROUTING_KEY)
                // 可选：设置消息TTL（毫秒）
                .withArgument("x-message-ttl", 10000)
                .build();
    }

    /**
     * 定义一个绑定，将直连队列绑定到直连交换机，并指定路由键。
     * 该绑定用于确保消息由直连交换机根据路由键精确匹配后，投递到绑定的队列中。
     *
     * @return 配置完成的队列与直连交换机的绑定实例
     */
    @Bean
    public Binding directBinding() {
        return BindingBuilder.bind(directQueue())
                .to(directExchange())
                .with(RabbitMqConstants.DIRECT_ROUTING_KEY);
    }
}
