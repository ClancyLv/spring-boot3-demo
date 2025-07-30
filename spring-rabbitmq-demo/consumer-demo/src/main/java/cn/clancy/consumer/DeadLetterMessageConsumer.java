package cn.clancy.consumer;

import cn.clancy.constants.RabbitMqConstants;
import cn.clancy.model.MessageWrapper;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 死信队列消息消费者
 */
@Slf4j
@Component
public class DeadLetterMessageConsumer extends BaseMessageConsumer {

    @RabbitListener(queues = RabbitMqConstants.DEAD_LETTER_QUEUE)
    public void onDeadLetterMessage(
            @Payload MessageWrapper<?> messageWrapper,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag,
            @Header(AmqpHeaders.RECEIVED_EXCHANGE) String exchange,
            @Header(AmqpHeaders.RECEIVED_ROUTING_KEY) String routingKey
    ) {
        try {
            log.error("收到死信消息: exchange={}, routingKey={}, message={}",
                    exchange, routingKey, messageWrapper);

            // 处理死信消息，可以进行告警通知、持久化等操作
            handleMessage(messageWrapper);

            // 确认消息已处理
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("处理死信消息时发生错误", e);
            try {
                // 死信队列的消息处理失败后，可以选择继续放入死信队列或者直接丢弃
                channel.basicNack(deliveryTag, false, false);
            } catch (Exception ex) {
                log.error("死信消息拒绝失败", ex);
            }
        }
    }

    @Override
    protected void handleMessage(MessageWrapper<?> messageWrapper) {
        // 这里实现死信消息的具体处理逻辑
        // 例如：保存到数据库、发送告警通知等
        log.info("正在处理死信消息: {}", messageWrapper);
        // TODO: 添加实际的业务处理逻辑
    }
}
