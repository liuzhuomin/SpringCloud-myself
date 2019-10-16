package xinrui.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.ComponentScan;
@SpringBootApplication
@ComponentScan(value = {"xinrui.cloud"})
@EntityScan(value = "xinrui.cloud")
@EnableCaching                //缓存支持
//@EnableDiscoveryClient        //发现服务
//@EnableFeignClients({ "xinrui.cloud" })   //注册服务
//@EnableCircuitBreaker     //熔断支持
//@EnableAspectJAutoProxy(exposeProxy = true)
/**
 * 挂点领导微服务
 * ------------------------------------
 *  api地址:http://localhost:8290/swagger-ui.html#/
 * ------------------------------------
 */
@SuppressWarnings({"unused"})
public class ApplicationLeaderIndex {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationLeaderIndex.class, args);
    }
}

