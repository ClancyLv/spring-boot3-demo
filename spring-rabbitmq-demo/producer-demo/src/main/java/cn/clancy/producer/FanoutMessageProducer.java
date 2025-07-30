package cn.clancy.producer;

import cn.clancy.constants.RabbitMqConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 广播交换机消息生产者
 */
@Slf4j
@Component
public class FanoutMessageProducer extends BaseMessageProducer {

    /**
     * 发送消息到广播交换机
     *
     * @param payload 消息内容
     * @param <T>    消息内容的类型
     */
    public <T> void sendMessage(T payload) {
        sendMessage(
            RabbitMqConstants.FANOUT_EXCHANGE,
            // 广播交换机不需要路由键
            "",
            payload,
            RabbitMqConstants.MESSAGE_TYPE_FANOUT
        );
    }
}
