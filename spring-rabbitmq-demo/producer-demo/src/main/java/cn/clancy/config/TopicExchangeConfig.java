package cn.clancy.config;

import cn.clancy.constants.RabbitMqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ClancyLv
 * @Date 2025/07/07 13:00
 * @Description 配置类--主题交换机
 * 该类配置了主题交换机（TopicExchange）的相关组件，包括交换机、队列及其绑定关系。
 * 主题交换机制允许使用通配符匹配路由键，从而实现消息的灵活路由。
 */
@Configuration
public class TopicExchangeConfig {

    /**
     * 定义并配置一个主题交换机（TopicExchange）。
     * 主题交换机允许使用通配符（如 # 和 *）匹配路由键，从而灵活地将消息路由到一个或多个绑定的队列中。
     *
     * @return 配置完成的主题交换机实例
     */
    @Bean
    public TopicExchange topicExchange() {
        // 默认持久化、非自动删除
        return new TopicExchange(RabbitMqConstants.TOPIC_EXCHANGE);
    }

    /**
     * 定义并配置一个主题队列（Topic Queue）。
     * 该队列用于接收与其绑定的主题交换机中符合路由规则的消息。
     *
     * @return 配置完成的主题队列1实例
     */
    @Bean
    public Queue topicQueue1() {
        // 默认持久化、非自动删除
        return new Queue(RabbitMqConstants.TOPIC_QUEUE1);
    }

    /**
     * 定义并配置一个主题队列2（Topic Queue 2）。
     * 此队列用于接收与其绑定的主题交换机中符合路由规则的消息。
     *
     * @return 配置完成的主题队列2实例
     */
    @Bean
    public Queue topicQueue2() {
        // 默认持久化、非自动删除
        return new Queue(RabbitMqConstants.TOPIC_QUEUE2);
    }

    /**
     * 定义并配置主题队列1与主题交换机之间的绑定关系。
     * 使用指定的路由键将符合条件的消息从主题交换机路由到主题队列1。
     *
     * @return 配置完成的绑定实例，将主题队列1绑定到主题交换机，并使用指定的路由键进行消息路由。
     */
    @Bean
    public Binding topicBinding1() {
        return BindingBuilder.bind(topicQueue1())
                .to(topicExchange())
                .with(RabbitMqConstants.TOPIC_ROUTING_KEY1);
    }

    /**
     * 定义并配置主题队列2与主题交换机之间的绑定关系。
     * 使用指定的路由键将符合条件的消息从主题交换机路由到主题队列2。
     *
     * @return 配置完成的绑定实例，将主题队列2绑定到主题交换机，并使用指定的路由键进行消息路由。
     */
    @Bean
    public Binding topicBinding2() {
        return BindingBuilder.bind(topicQueue2())
                .to(topicExchange())
                .with(RabbitMqConstants.TOPIC_ROUTING_KEY2);
    }
}
