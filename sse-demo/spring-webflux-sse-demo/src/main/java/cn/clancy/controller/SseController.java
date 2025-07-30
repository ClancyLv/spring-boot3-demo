package cn.clancy.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.codec.ServerSentEvent;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.time.LocalTime;
import java.util.UUID;

/**
 * @Author ClancyLv
 * @Date 2025/7/22 16:13
 * @Description 控制层--Sse
 */
@Slf4j
@RestController
@RequestMapping("/sse")
public class SseController {
    /**
     * 默认事件名称
     */
    private static final String DEFAULT_EVENT_NAME = "default_event";

    /**
     * 创建SSE连接点
     * produces = MediaType.TEXT_EVENT_STREAM_VALUE 表示这是一个SSE端点.
     *
     * @return 返回一个事件流 (Flux), 每个事件都是一个 ServerSentEvent 对象.
     */
    @GetMapping(path = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<ServerSentEvent<String>> streamEvents() {
        // 创建一个每秒触发一次的 Flux
        return Flux.interval(Duration.ofSeconds(1))
                // 使用 map 操作将每个 tick 转换为一个 ServerSentEvent 对象.
                .map(sequence -> {
                    // 构建 ServerSentEvent 对象
                    return ServerSentEvent.<String>builder()
                            // 设置事件ID. 客户端可以根据这个ID实现断线重连.
                            .id(UUID.randomUUID().toString())
                            // 设置事件名称, 客户端可以监听特定名称的事件.
                            .event(DEFAULT_EVENT_NAME)
                            // 设置事件的数据内容.
                            .data("SSE消息内容：" + LocalTime.now())
                            .build();
                })
                // 订阅日志
                .doOnSubscribe(subscription -> log.info("SSE客户端订阅成功!"))
                // 取消日志(客户端断开时)
                .doOnCancel(() -> log.info("客户端断开连接"));
    }
}
