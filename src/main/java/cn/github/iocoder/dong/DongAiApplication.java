package cn.github.iocoder.dong;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = DataSourceAutoConfiguration.class)
public class DongAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(DongAiApplication.class,args);
    }
}
