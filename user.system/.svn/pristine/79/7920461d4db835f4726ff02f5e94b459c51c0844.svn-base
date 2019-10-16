package xinrui.cloud;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.netflix.zuul.EnableZuulProxy;

/**
 * zuul网关代理
 *
 * @author liuliuliu
 */
@SpringBootApplication
@EnableZuulProxy
@EnableHystrixDashboard
@EnableEurekaServer
@EnableCircuitBreaker
public class ApplicationZuul {


    public static void main(String[] args){
       new SpringApplicationBuilder(ApplicationZuul.class).web(true).run(args);
    }

}
