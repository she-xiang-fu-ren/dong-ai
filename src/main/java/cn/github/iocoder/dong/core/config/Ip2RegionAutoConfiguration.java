package cn.github.iocoder.dong.core.config;


import cn.github.iocoder.dong.core.config.properties.Ip2RegionProperties;
import cn.github.iocoder.dong.core.helper.Ip2regionSearcher;
import cn.hutool.core.io.IoUtil;
import cn.hutool.core.util.ClassLoaderUtil;
import org.lionsoul.ip2region.xdb.Searcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.io.InputStream;


/**
 * ip归属的配置类
 */
//@AutoConfiguration
@Configuration
@ConditionalOnClass(Ip2regionSearcher.class)
@EnableConfigurationProperties(Ip2RegionProperties.class)
@ConditionalOnProperty(prefix = Ip2RegionProperties.PREFIX, name = "enabled", havingValue = "true")
public class Ip2RegionAutoConfiguration {
    @Bean
    public Ip2regionSearcher ip2regionSearcher(@Autowired Ip2RegionProperties ip2RegionProperties) {
        ClassLoader classLoader = ClassLoaderUtil.getClassLoader();
        try (InputStream inputStream = classLoader.getResourceAsStream(ip2RegionProperties.getDbFile())) {
            Searcher searcher = Searcher.newWithBuffer(IoUtil.readBytes(inputStream));
            return new Ip2regionSearcher(searcher);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
