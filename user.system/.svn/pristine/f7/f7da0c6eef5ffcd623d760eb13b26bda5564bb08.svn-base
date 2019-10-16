package xinrui.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan(value = {"xinrui.cloud"})
@EntityScan(value = "xinrui.cloud")
@EnableCaching
@EnableDiscoveryClient
@EnableFeignClients({ "xinrui.cloud" })
@EnableCircuitBreaker
public class ApplicationPolicyMatching {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationPolicyMatching.class, args);
    }
}

