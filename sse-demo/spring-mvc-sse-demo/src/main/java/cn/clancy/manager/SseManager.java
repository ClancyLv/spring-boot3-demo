package cn.clancy.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Author ClancyLv
 * @Date 2025/7/22 14:42
 * @Description 管理器--SSE
 */
@Slf4j
@Component
public class SseManager {
    /**
     * 用于存储当前所有活跃的客户端连接以及其对应的 SseEmitter 实例的映射关系。
     * 键为客户端ID，值为与该客户端通信的 SseEmitter 实例。
     * 此变量采用线程安全的 ConcurrentHashMap 实现，以确保在多线程环境中进行并发读写操作时的安全性。
     * 该变量主要用于管理和操作服务端与客户端之间的长连接，包括创建、发送消息以及关闭连接等操作。
     */
    private static final Map<String, SseEmitter> SSE_EMITTER_MAP = new ConcurrentHashMap<>();

    /**
     * 默认事件名称
     */
    private static final String DEFAULT_EVENT_NAME = "default_event";

    /**
     * 创建并注册一个新的 SseEmitter.
     *
     * @param clientId 客户端ID
     * @return SseEmitter 实例
     */
    public SseEmitter createSseEmitter(String clientId) {
        // 超时时间设置为1小时
        SseEmitter sseEmitter = new SseEmitter(3600_000L);

        // 注册回调
        sseEmitter.onCompletion(completionCallBack(clientId));
        sseEmitter.onTimeout(timeoutCallBack(clientId));
        sseEmitter.onError(errorCallBack(clientId));

        SSE_EMITTER_MAP.put(clientId, sseEmitter);

        try {
            // 发送确认消息
            sseEmitter.send(SseEmitter.event().name("connect").data("连接成功! 客户端ID: " + clientId));
        } catch (IOException e) {
            // 记录错误，但不抛出异常，因为连接可能已经被客户端关闭
            log.error("向客户端 {} 发送初始连接消息时出错: {}", clientId, e.getMessage());
            closeSseEmitter(clientId);
        }

        return sseEmitter;
    }

    /**
     * 向指定客户端发送消息.
     *
     * @param clientId 客户端ID
     * @param content  消息内容
     * @return 是否发送成功
     */
    public boolean sendMessage(String clientId, String content) {
        return sendMessage(clientId, content, DEFAULT_EVENT_NAME);
    }

    /**
     * 向指定客户端发送消息.
     *
     * @param clientId      客户端ID
     * @param content       消息内容
     * @param eventName     事件名称
     * @return 是否发送成功
     */
    public boolean sendMessage(String clientId, String content, String eventName) {
        SseEmitter sseEmitter = SSE_EMITTER_MAP.get(clientId);
        if (sseEmitter != null) {
            try {
                sseEmitter.send(SseEmitter.event()
                        .id(UUID.randomUUID().toString())
                        .name(eventName)
                        .data(content));
                return true;
            } catch (IOException e) {
                // 发生错误时，移除emitter
                log.error("向 {} 发送消息时出错: {}", clientId, e.getMessage());
                closeSseEmitter(clientId);
                return false;
            }
        }
        return false;
    }

    /**
     * 向所有已连接的客户端发送消息.
     * @param content 消息内容
     * @return 是否发送成功
     */
    public boolean sendMessageToAll(String content) {
        return sendMessageToAll(content, DEFAULT_EVENT_NAME);
    }

    /**
     * 向所有已连接的客户端发送消息.
     * @param content   消息内容
     * @param eventName 事件名称
     * @return 是否发送成功
     */
    public boolean sendMessageToAll(String content, String eventName) {
        List<String> failedList = new ArrayList<>();
        for (String clientId : SSE_EMITTER_MAP.keySet()) {
            boolean flag = sendMessage(clientId, content, eventName);
            if (!flag) {
                // 记录失败的客户端id
                failedList.add(clientId);
            }
        }
        if (failedList.isEmpty()) {
            return true;
        } else {
            log.warn("向以下客户端发送消息失败: {}", String.join(", ", failedList));
            return false;
        }
    }

    /**
     * 关闭并移除一个 SseEmitter.
     *
     * @param clientId 客户端ID
     */
    public void closeSseEmitter(String clientId) {
        if (SSE_EMITTER_MAP.containsKey(clientId)) {
            SSE_EMITTER_MAP.remove(clientId);
            log.info("已移除客户端的SseEmitter: {}", clientId);
        }
    }

    private Runnable completionCallBack(String clientId) {
        return () -> {
            log.info("连接完成，客户端: {}", clientId);
            closeSseEmitter(clientId);
        };
    }

    private Runnable timeoutCallBack(String clientId) {
        return () -> {
            log.info("连接超时，客户端: {}", clientId);
            closeSseEmitter(clientId);
        };
    }

    private Consumer<Throwable> errorCallBack(String clientId) {
        return throwable -> {
            log.error("连接出错，客户端 {}: {}", clientId, throwable.getMessage());
            closeSseEmitter(clientId);
        };
    }
}
