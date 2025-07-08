package cn.clancy;

import cn.clancy.producer.DirectMessageProducer;
import cn.clancy.producer.FanoutMessageProducer;
import cn.clancy.producer.TopicMessageProducer;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * @Author ClancyLv
 * @Date 2025/7/8 09:59
 * @Description 生产者Demo应用测试类
 */
@SpringBootTest
public class ProducerDemoApplicationTests {
    @Resource
    private DirectMessageProducer directMessageProducer;

    @Resource
    private FanoutMessageProducer fanoutMessageProducer;

    @Resource
    private TopicMessageProducer topicMessageProducer;

    @Test
    public void testSendDirectMessage() {
        // 准备测试数据
        String testMessage = "这是一条直连交换机测试消息";

        directMessageProducer.sendMessage(testMessage);
    }

    @Test
    public void testSendFanoutMessage() {
        // 准备测试数据
        String testMessage = "这是一条广播交换机测试消息";

        fanoutMessageProducer.sendMessage(testMessage);
    }

    @Test
    public void testSendTopicMessage() {
        // 准备测试数据
        String testMessage = "这是一条主题交换机测试消息";

        topicMessageProducer.sendMessage(testMessage, "topic.routing.123");

        topicMessageProducer.sendMessage(testMessage, "topic.123");
    }
}
