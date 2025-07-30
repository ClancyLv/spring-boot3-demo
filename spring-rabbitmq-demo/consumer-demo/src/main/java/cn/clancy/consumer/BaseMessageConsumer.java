package cn.clancy.consumer;

import cn.clancy.model.MessageWrapper;
import com.rabbitmq.client.Channel;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.retry.support.RetryTemplate;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 消息消费者基类，提供基础的消息处理功能
 */
@Slf4j
public abstract class BaseMessageConsumer {
    @Resource
    protected RetryTemplate retryTemplate;

    /**
     * 处理消息的通用方法
     *
     * @param messageWrapper 消息包装对象，包含消息ID、类型、内容等信息
     * @param channel        消息通道，用于手动确认消息
     * @param deliveryTag    消息投递标签，表示具体的消息位置
     */
    protected void processMessage(MessageWrapper<?> messageWrapper, Channel channel, long deliveryTag) {
        try {
            // 使用RetryTemplate处理消息重试
            retryTemplate.execute(context -> {
                handleMessage(messageWrapper);
                return null;
            });
            // 手动确认消息已被处理（如果是自动确认消息机制，下面代码可省略）
            channel.basicAck(deliveryTag, false);
        } catch (Exception e) {
            log.error("处理消息时发生错误: {}", messageWrapper, e);
            try {
                // 手动拒绝消息且不重新入队，消息会进入死信队列（如果是自动确认消息机制，下面代码可省略）
                channel.basicNack(deliveryTag, false, false);
                log.error("消息进入死信队列: {}", messageWrapper);
            } catch (Exception ex) {
                log.error("消息拒绝失败", ex);
            }
        }
    }

    /**
     * 具体的消息处理逻辑，由子类实现
     *
     * @param messageWrapper 消息包装对象
     */
    protected abstract void handleMessage(MessageWrapper<?> messageWrapper);
}
