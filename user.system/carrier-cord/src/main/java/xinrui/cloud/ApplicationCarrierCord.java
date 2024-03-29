package xinrui.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.context.annotation.ComponentScan;

/**
 * 龙岗相关微服务
 * ------------------------------------
 * api地址:http://${host}:${port}/swagger-ui.html#/
 * ------------------------------------
 */
@SpringBootApplication
@ComponentScan(value = {"xinrui.cloud"})
@EntityScan(value = "xinrui.cloud")
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients({"xinrui.cloud"})
@EnableCircuitBreaker
@EnableHystrixDashboard
public class ApplicationCarrierCord {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationCarrierCord.class, args);
    }
}

