# spring-rabbitmq-demo
本模块基于 Spring Boot 3 和 Spring AMQP，演示如何在 Spring 应用中集成和使用 RabbitMQ，重点介绍消息生产者和消费者的配置与使用，适用于异步消息处理、解耦系统组件等场景。
## 教程
### 1、添加依赖
``` xml
<dependencies>
    <!-- Spring AMQP - RabbitMQ -->
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-amqp</artifactId>
    </dependency>
    <!-- Jackson 用于消息对象序列化 -->
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
    </dependency>
</dependencies>
```
### 2、核心配置
#### 2.1 RabbitMQ 连接配置
在 `application.yml` 中配置 RabbitMQ 连接信息：
``` yaml
spring:
  rabbitmq:
    host: localhost       # RabbitMQ 服务器地址
    port: xxxx            # RabbitMQ 服务器连接端口
    username: xxxxx       # RabbitMQ 默认用户名
    password: xxxxx       # RabbitMQ 默认密码
    virtual-host: /       # 虚拟主机
```
#### 2.2 RabbitMQ 配置类
在 `RabbitConfig.java` 中配置交换机、队列和绑定关系：
``` java
@Configuration
public class RabbitConfig {

    // 定义交换机
    @Bean
    public DirectExchange directExchange() {
        return new DirectExchange("demo.direct.exchange");
    }
    
    // 定义队列
    @Bean
    public Queue demoQueue() {
        return new Queue("demo.queue");
    }
    
    // 绑定队列到交换机
    @Bean
    public Binding demoBinding(Queue demoQueue, DirectExchange directExchange) {
        return BindingBuilder.bind(demoQueue)
                .to(directExchange)
                .with("demo.routing.key");
    }
    
    // 配置消息转换器，使用JSON序列化
    @Bean
    public MessageConverter messageConverter() {
        return new Jackson2JsonMessageConverter();
    }
    
    // 配置RabbitTemplate
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(messageConverter());
        return rabbitTemplate;
    }
}
```
### 3、消息生产与消费
#### 3.1 消息生产者
使用 `RabbitTemplate` 发送消息：
``` java
@Service
public class MessageProducer {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    
    /**
     * 发送消息到指定交换机
     */
    public void sendMessage(Object message) {
        rabbitTemplate.convertAndSend("demo.direct.exchange", "demo.routing.key", message);
    }
    
    /**
     * 发送延迟消息
     */
    public void sendDelayedMessage(Object message, long delayInMs) {
        MessagePostProcessor processor = msg -> {
            msg.getMessageProperties().setDelay((int) delayInMs);
            return msg;
        };
        
        rabbitTemplate.convertAndSend(
            "demo.delayed.exchange", 
            "demo.delayed.key", 
            message,
            processor
        );
    }
}
```
#### 3.2 消息消费者
使用 `@RabbitListener` 注解监听队列：
``` java
@Component
public class MessageConsumer {

    /**
     * 基本消息监听
     */
    @RabbitListener(queues = "demo.queue")
    public void receiveMessage(Object message) {
        // 处理收到的消息
        System.out.println("收到消息: " + message);
    }
    
    /**
     * 带有手动确认的消息监听
     */
    @RabbitListener(queues = "demo.manual.queue", ackMode = "MANUAL")
    public void receiveWithManualAck(Object message, Channel channel, 
                                     @Header(AmqpHeaders.DELIVERY_TAG) long tag) 
            throws IOException {
        try {
            // 处理消息
            System.out.println("处理消息: " + message);
            // 手动确认消息
            channel.basicAck(tag, false);
        } catch (Exception e) {
            // 处理失败，拒绝消息，不重新入队
            channel.basicReject(tag, false);
        }
    }
}
```
### 4、高级特性
#### 4.1 消息确认与返回
配置发布确认和消息返回，确保消息可靠投递：
``` yaml
spring:
  rabbitmq:
    publisher-confirm-type: correlated  # 发布确认模式
    publisher-returns: true             # 开启发布返回
    template:
      mandatory: true                   # 启用强制消息
```

``` java
@Autowired
private RabbitTemplate rabbitTemplate;

@PostConstruct
public void init() {
    // 确认回调
    rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
        if (ack) {
            System.out.println("消息发送到交换机成功");
        } else {
            System.out.println("消息发送到交换机失败: " + cause);
        }
    });
    
    // 返回回调
    rabbitTemplate.setReturnsCallback(returned -> {
        System.out.println("消息路由失败：" + returned.getMessage());
    });
}
```
#### 4.2 消费者限流
配置消费者限流，避免消费者过载：
``` java
@Bean
public SimpleRabbitListenerContainerFactory rabbitListenerContainerFactory(
        ConnectionFactory connectionFactory) {
    SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
    factory.setConnectionFactory(connectionFactory);
    factory.setPrefetchCount(1); // 一次只处理一条消息
    factory.setAcknowledgeMode(AcknowledgeMode.MANUAL); // 手动确认
    return factory;
}
```
#### 4.3 消息重试
配置消费者重试机制：
``` yaml
spring:
  rabbitmq:
    listener:
      simple:
        retry:
          enabled: true           # 开启重试
          initial-interval: 1000  # 初始重试间隔(ms)
          max-attempts: 3         # 最大重试次数
          multiplier: 1.0         # 重试间隔乘数
```
### 5、完整示例
完整的示例代码可以在项目的 `src/main/java` 目录下查看。包括：
- 生产者应用：`ProducerDemoApplication.java`
- 消费者应用：`ConsumerDemoApplication.java`
- 配置类：`RabbitConfig.java`
- 消息实体类、生产者服务和消费者服务等

## 运行示例
1. 确保 RabbitMQ 服务已启动
2. 运行消费者应用：`ConsumerDemoApplication`
3. 运行生产者应用：`ProducerDemoApplication`
4. 通过 API 调用生产者发送消息
5. 观察消费者应用控制台输出

## 实用链接
- [Spring AMQP 官方文档](https://spring.io/projects/spring-amqp)
- [RabbitMQ 官网](https://www.rabbitmq.com/)
- [RabbitMQ 管理界面](http://localhost:15672/)
