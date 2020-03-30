package cn.bytecloud.smartCommunity;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import java.util.concurrent.*;

@Slf4j
@SpringBootApplication
@EnableScheduling
//@EnableCaching
public class SmartCommunityApplication  {

	public static void main(String[] args) {
		int a =4;
		int b = 5;
		double c = (a + b )/2;
		System.out.println(c);
		log.info("准备启动");
		SpringApplication.run(SmartCommunityApplication.class, args);
		log.info("启动成功！");
	}
}

