package cn.clancy.model;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

/**
 * @Author ClancyLv
 * @Date 2025/7/7 10:00
 * @Description 消息包装类，统一消息格式
 */
@Data
@Accessors(chain = true)
public class MessageWrapper<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private String messageId;

    /**
     * 消息类型
     */
    private String messageType;

    /**
     * 消息内容
     */
    private T payload;

    /**
     * 发送时间
     */
    private LocalDateTime timestamp;

    /**
     * 创建消息
     */
    public static <T> MessageWrapper<T> of(T payload) {
        return new MessageWrapper<T>()
                .setMessageId(UUID.randomUUID().toString())
                .setPayload(payload)
                .setTimestamp(LocalDateTime.now());
    }
}
