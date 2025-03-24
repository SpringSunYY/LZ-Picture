package com.lz;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 启动程序
 *
 * @author YY
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class PictureApplication {
    public static void main(String[] args) {
        // System.setProperty("spring.devtools.restart.enabled", "false");
        SpringApplication.run(PictureApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  小猜启动成功啦   耶耶ლ(´ڡ`ლ)ﾞ  \n");
    }
}
