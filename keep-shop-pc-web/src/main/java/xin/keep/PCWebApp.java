package xin.keep;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@EnableEurekaClient
@SpringBootApplication
public class PCWebApp {
    public static void main(String[] args) {
        SpringApplication.run(PCWebApp.class,args);
    }
}
