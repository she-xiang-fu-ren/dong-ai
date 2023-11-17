package cn.github.iocoder.dong.core.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * mq配置
 */
@Component
@Data
@ConfigurationProperties(RabbitMqProperties.PREFIX)
public class RabbitMqProperties {
    public static final String PREFIX = "dong.mq";

    /**
     * 主机
     */
    private String host;

    /**
     * 端口
     */
    private Integer port;

    /**
     * stomp端口
     */
    private Integer relayPort;

    /**
     * 虚拟host
     */
    private String virtualHost;

    /**
     * 密码
     */
    private String password;

    /**
     * 账号
     */
    private String username;

    /**
     * 交换机
     */
    private String MQ_EXCHANGE;

    /**
     * 队列
     */
    private String MSG_TOPIC_QUEUE;

    /**
     * key
     */
    private String MSG_TOPIC_KEY;
}
