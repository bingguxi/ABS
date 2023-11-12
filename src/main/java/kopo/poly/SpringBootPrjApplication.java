package kopo.poly;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.client.RestTemplate;

@EnableScheduling // 스프링 스케줄러 활용을 위한 어노테이션 추가!!!
@SpringBootApplication
public class SpringBootPrjApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootPrjApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate(){return new RestTemplate();}
}
