package cn.clancy.controller;

import cn.clancy.manager.SseManager;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

/**
 * @Author ClancyLv
 * @Date 2025/7/22 14:42
 * @Description 控制层--SSE
 */
@RequiredArgsConstructor
@RestController
@RequestMapping("/sse")
public class SseController {
    private final SseManager sseManager;

    /**
     * 订阅SSE事件.
     *
     * @param id 客户端ID
     * @return SseEmitter
     */
    @GetMapping("/subscribe/{id}")
    public SseEmitter subscribe(@PathVariable String id) {
        return sseManager.createSseEmitter(id);
    }

    /**
     * 推送消息到指定客户端.
     *
     * @param id      客户端ID
     * @param content 消息内容
     * @return 结果
     */
    @PostMapping("/push/{id}")
    public String push(@PathVariable String id, @RequestParam String content) {
        boolean success = sseManager.sendMessage(id, content);
        if (success) {
            return "消息已发送至 " + id;
        } else {
            return "客户端 " + id + " 未找到或发送失败";
        }
    }

    /**
     * 推送消息到所有客户端.
     *
     * @param content 消息内容
     * @return 结果
     */
    @PostMapping("/push/all")
    public String push(@RequestParam String content) {
        boolean success = sseManager.sendMessageToAll(content);
        if (success) {
            return "消息已发送至所有客户端";
        } else {
            return "消息发送至所有客户端失败";
        }
    }
}
