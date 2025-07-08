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
 * @Description 广播交换机消息消费者
 */
@Slf4j
@Component
public class FanoutMessageConsumer extends BaseMessageConsumer {

    @RabbitListener(queues = RabbitMqConstants.FANOUT_QUEUE1)
    public void onFanoutQueue1Message(@Payload MessageWrapper<?> messageWrapper,
                                    Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        processMessage(messageWrapper, channel, deliveryTag);
    }

    @RabbitListener(queues = RabbitMqConstants.FANOUT_QUEUE2)
    public void onFanoutQueue2Message(@Payload MessageWrapper<?> messageWrapper,
                                    Channel channel,
                                    @Header(AmqpHeaders.DELIVERY_TAG) long deliveryTag) {
        processMessage(messageWrapper, channel, deliveryTag);
    }

    @Override
    protected void handleMessage(MessageWrapper<?> messageWrapper) {
        log.info("收到广播队列消息: {}", messageWrapper);
        // 在这里添加具体的业务处理逻辑
        // 如果处理失败，抛出异常会触发重试机制
    }
}
