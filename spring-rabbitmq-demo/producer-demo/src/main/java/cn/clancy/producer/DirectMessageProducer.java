package cn.clancy.producer;

import cn.clancy.constants.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 直连交换机消息生产者
 */
@Slf4j
@Component
public class DirectMessageProducer extends BaseMessageProducer {

    /**
     * 发送消息到直连交换机
     *
     * @param payload 消息内容
     * @param <T>    消息内容的类型
     */
    public <T> void sendMessage(T payload) {
        sendMessage(
            RabbitMqConstants.DIRECT_EXCHANGE,
            RabbitMqConstants.DIRECT_ROUTING_KEY,
            payload,
            RabbitMqConstants.MESSAGE_TYPE_DIRECT
        );
    }
}
