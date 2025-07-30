package cn.clancy.producer;

import cn.clancy.model.MessageWrapper;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 消息生产者基类，提供基础的消息发送功能
 */
@Slf4j
public abstract class BaseMessageProducer {
    @Resource
    protected RabbitTemplate rabbitTemplate;

    @Resource
    protected RetryTemplate retryTemplate;

    /**
     * 发送消息的基础方法
     *
     * @param exchange    交换机名称
     * @param routingKey  路由键
     * @param payload     消息内容
     * @param messageType 消息类型
     * @param <T>        消息内容的类型
     */
    protected <T> void sendMessage(String exchange, String routingKey, T payload, String messageType) {
        MessageWrapper<T> messageWrapper = MessageWrapper.of(payload)
                .setMessageType(messageType);
        CorrelationData correlationData = new CorrelationData(messageWrapper.getMessageId());

        retryTemplate.execute(context -> {
            rabbitTemplate.convertAndSend(
                    exchange,
                    routingKey,
                    messageWrapper,
                    correlationData
            );
            log.info("发送消息完成 - 交换机: {}, 路由键: {}, 消息: {}", exchange, routingKey, messageWrapper);
            return null;
        });
    }
}
