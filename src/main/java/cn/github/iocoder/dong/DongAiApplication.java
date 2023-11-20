package cn.github.iocoder.dong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.web.servlet.ServletComponentScan;

@SpringBootApplication
@ServletComponentScan //用于自动扫描指定包下的Servlet组件
public class DongAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongAiApplication.class,args);
    }
}
