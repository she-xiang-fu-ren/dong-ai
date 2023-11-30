package cn.github.iocoder.dong.core.config.properties;


import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = Ip2RegionProperties.PREFIX)
public class Ip2RegionProperties {
    public static final String PREFIX = "ip2region";

    /**
     * 是否开启自动配置
     */
    private boolean enabled = false;

    /**
     * db数据文件位置
     * <p>
     * ClassPath目录下
     * </p>
     */
    private String dbFile = "data/ip2region.xdb";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getDbFile() {
        return dbFile;
    }

    public void setDbFile(String dbFile) {
        this.dbFile = dbFile;
    }
}
