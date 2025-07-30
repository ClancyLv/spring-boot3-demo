package cn.clancy.config;

import cn.clancy.constants.RabbitMqConstants;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author ClancyLv
 * @Date 2025/07/07 10:00
 * @Description 配置类--配置广播交换机及相关队列和绑定
 *
 * 该类提供了一个广播交换机以及与其绑定的多个队列的配置。广播交换机会将消息广播发送到与其绑定的所有队列。
 */
@Configuration
public class FanoutExchangeConfig {

    /**
     * 定义并创建一个广播交换机(Fanout Exchange)的Bean。
     * 广播交换机会将收到的消息广播到所有与之绑定的队列。
     *
     * @return FanoutExchange 实例，表示名为"fanout.exchange"的广播交换机
     */
    @Bean
    public FanoutExchange fanoutExchange() {
        // 默认持久化、非自动删除
        return new FanoutExchange(RabbitMqConstants.FANOUT_EXCHANGE);
    }

    /**
     * 定义并创建广播队列1的Bean。
     * 广播队列1是绑定到广播交换机(Fanout Exchange)的队列之一，用于接收该交换机广播的所有消息。
     *
     * @return Queue 实例，表示名称为"fanout.queue1"的队列
     */
    @Bean
    public Queue fanoutQueue1() {
        // 默认持久化、非自动删除
        return new Queue(RabbitMqConstants.FANOUT_QUEUE1);
    }

    /**
     * 定义并创建广播队列2的Bean。
     * 广播队列2是绑定到广播交换机(Fanout Exchange)的队列之一，用于接收该交换机广播的所有消息。
     *
     * @return Queue 实例，表示名称为"fanout.queue2"的队列
     */
    @Bean
    public Queue fanoutQueue2() {
        // 默认持久化、非自动删除
        return new Queue(RabbitMqConstants.FANOUT_QUEUE2);
    }

    /**
     * 定义并创建广播队列1与广播交换机(Fanout Exchange)的绑定关系。
     * 该绑定确保广播队列1能够接收到广播交换机发送的所有消息。
     *
     * @return Binding 实例，表示广播队列1与广播交换机的绑定关系
     */
    @Bean
    public Binding fanoutBinding1() {
        return BindingBuilder.bind(fanoutQueue1()).to(fanoutExchange());
    }

    /**
     * 定义并创建广播队列2与广播交换机(Fanout Exchange)的绑定关系。
     * 该绑定确保广播队列2能够接收到广播交换机发送的所有消息。
     *
     * @return Binding 实例，表示广播队列2与广播交换机的绑定关系
     */
    @Bean
    public Binding fanoutBinding2() {
        return BindingBuilder.bind(fanoutQueue2()).to(fanoutExchange());
    }
}
