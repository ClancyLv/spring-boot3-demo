package cn.clancy.producer;

import cn.clancy.constants.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 主题交换机消息生产者
 */
@Slf4j
@Component
public class TopicMessageProducer extends BaseMessageProducer {

    /**
     * 发送消息到主题交换机
     *
     * @param payload    消息内容
     * @param routingKey 路由键
     * @param <T>       消息内容的类型
     */
    public <T> void sendMessage(T payload, String routingKey) {
        sendMessage(
            RabbitMqConstants.TOPIC_EXCHANGE,
            routingKey,
            payload,
            RabbitMqConstants.MESSAGE_TYPE_TOPIC
        );
    }
}
