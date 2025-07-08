package cn.clancy.constants;

/**
 * @Author ClancyLv
 * @Date 2025/07/07 15:30
 * @Description 常量类--RabbitMQ相关常量
 * 该类定义了与RabbitMQ配置相关的常量，包括交换机名称、队列名称以及路由键名称。
 * 这便于在使用RabbitMQ时统一管理常量，避免硬编码。
 */
public interface RabbitMqConstants {
    /** 直连交换机名称 */
    String DIRECT_EXCHANGE = "direct.exchange";
    /** 主题交换机名称 */
    String TOPIC_EXCHANGE = "topic.exchange";
    /** 广播交换机名称 */
    String FANOUT_EXCHANGE = "fanout.exchange";
    /** 死信交换机名称 */
    String DEAD_LETTER_EXCHANGE = "dead.letter.exchange";

    /*
     * 队列名称常量
     */
    /** 直连队列名称 */
    String DIRECT_QUEUE = "direct.queue";
    /** 主题队列1名称 */
    String TOPIC_QUEUE1 = "topic.queue1";
    /** 主题队列2名称 */
    String TOPIC_QUEUE2 = "topic.queue2";
    /** 广播队列1名称 */
    String FANOUT_QUEUE1 = "fanout.queue1";
    /** 广播队列2名称 */
    String FANOUT_QUEUE2 = "fanout.queue2";
    /** 死信队列名称 */
    String DEAD_LETTER_QUEUE = "dead.letter.queue";

    /*
     * 路由键常量
     */
    /** 直连交换机路由键 */
    String DIRECT_ROUTING_KEY = "direct.routing.key";
    /** 主题交换机路由键1，'#'表示0个或多个单词 */
    String TOPIC_ROUTING_KEY1 = "topic.routing.#";
    /** 主题交换机路由键2，'#'表示0个或多个单词 */
    String TOPIC_ROUTING_KEY2 = "topic.#";
    /** 死信队列路由键 */
    String DEAD_LETTER_ROUTING_KEY = "dead.letter.routing.key";

    /*
     * 消息类型常量
     */
    /** 直连交换机消息类型 */
    String MESSAGE_TYPE_DIRECT = "DIRECT";
    /** 主题交换机消息类型 */
    String MESSAGE_TYPE_TOPIC = "TOPIC";
    /** 广播交换机消息类型 */
    String MESSAGE_TYPE_FANOUT = "FANOUT";
    /** 死信消息类型 */
    String MESSAGE_TYPE_DEAD_LETTER = "DEAD_LETTER";
}
