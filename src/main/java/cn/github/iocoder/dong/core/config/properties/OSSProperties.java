package cn.github.iocoder.dong.core.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties(prefix = OSSProperties.PREFIX)
public class OSSProperties {
    public static final String PREFIX = "dong.image.oss";
    /**
     * 上传地址
     */
    private String endpoint;

    /**
     * accessKey
     */
    private String accessKey;

    /**
     * secretKey
     */
    private String secretKey;

    /**
     * 桶名称
     */
    private String bucketName;

    /**
     * 上传路径
     */
    private String prefix;

    /**
     * 访问路径
     */
    private String host;
}
