package xinrui.cloud.config;

import org.apache.http.conn.HttpClientConnectionManager;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;

/**
 * <B>Title:</B>RestTemplateConfig</br>
 * <B>Description:</B>配置生成{@link RestTemplate}的bean，采用http连接池</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/17 10:37
 */
@Configuration
@PropertySource("classpath:application.properties")
public class RestTemplateConfig {

    /**
     * 连接池最大连接数
     */
    @Value("${rest.max.total:1000}")
    private int restMaxTotal;

    /**
     *  每个主机的并发
     */
    @Value("${rest.max.per.route:500}")
    private int restMaxPerRoute;

    /**
     *  连接超时，毫秒
     */
    @Value("${rest.connect.timeout:2000}")
    private int restConnectTimeout;

    /**
     *  读写超时，毫秒
     */
    @Value("${rest.read.timeout:2000}")
    private int restReadTimeOut;

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getMessageConverters().set(1, new StringHttpMessageConverter(StandardCharsets.UTF_8));
        restTemplate.setRequestFactory(clientHttpRequestFactory());
        return restTemplate;
    }

    @Bean
    public HttpClientConnectionManager poolingConnectionManager() {
        PoolingHttpClientConnectionManager poolingConnectionManager = new PoolingHttpClientConnectionManager();
        poolingConnectionManager.setMaxTotal(restMaxTotal);
        poolingConnectionManager.setDefaultMaxPerRoute(restMaxPerRoute);
        return poolingConnectionManager;
    }

    @Bean
    public HttpClientBuilder httpClientBuilder() {
        HttpClientBuilder httpClientBuilder = HttpClientBuilder.create();
        httpClientBuilder.setConnectionManager(poolingConnectionManager());
        return httpClientBuilder;
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        HttpComponentsClientHttpRequestFactory clientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory();
        clientHttpRequestFactory.setHttpClient(httpClientBuilder().build());
        clientHttpRequestFactory.setConnectTimeout(restConnectTimeout);
        clientHttpRequestFactory.setReadTimeout(restReadTimeOut);
        return clientHttpRequestFactory;
    }

}
