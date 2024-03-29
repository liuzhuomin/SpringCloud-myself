package xinrui.cloud.config;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.client.RestTemplate;
import springfox.documentation.swagger.web.InMemorySwaggerResourcesProvider;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;
import springfox.documentation.swagger.web.UiConfiguration;
import xinrui.cloud.filter.AccessFilter;

import javax.annotation.Resource;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

/**
 * <p/>swagger的配置类，将所有下游服务列表中的api资源获取到，并且一并展示 。
 * <p>
 * <p/>将所有路由到的服务列表获取到，并且获取其swaggerresource列表，其次将
 * 其添加到默认的swaggerResource列表中；
 * <p>
 * <p/>配置忽略的url，通过正则，并且使用{@link OrRequestMatcher}类做匹配操作，
 * 一旦符合匹配规则，在{@link AccessFilter}中则会忽略此url。
 *
 * @author Jihy
 * @author liuliuliu
 * @version 1.0
 * 2019/7/17 10:37
 */
@Configuration
@Primary
@PropertySource("classpath:application.properties")
@SuppressWarnings("unused")
public class DocumentationConfig implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(DocumentationConfig.class);

    /**
     * 需要忽略的url
     */
    @Value("#{'${zuul.ignore.urls:none}'.split(',')}")
    private List<String> ignoreUrls;

    @Value("${server.port:8080}")
    private String port;

    private String prefix;

    @Resource
    RouteLocator routeLocator;

    @Resource
    RestTemplate restTemplate;

    /**
     * <p/>自定义生成{@link SwaggerResourcesProvider}类,获取到默认的{@link InMemorySwaggerResourcesProvider}类。
     * 通过默认的{@link InMemorySwaggerResourcesProvider}类，获取到当前默认的{@link SwaggerResource}对象。
     * <p>
     * <p/>通过注入{@link RouteLocator}类，获取到zuul的路由服务对象，并且分别请求这些路由的<b>swagger-resources</b>
     * 接口，最终将所有的{@link SwaggerResource}对象集合返回，提供给网关的swagger。
     *
     * @param defaultResourcesProvider 默认的资源代理类
     * @return 资源代理处理器
     */
    @Primary
    @Bean
    public SwaggerResourcesProvider swaggerResourcesProvider(final InMemorySwaggerResourcesProvider defaultResourcesProvider) {
        final  StringBuffer BUFFER = new StringBuffer();
        return new SwaggerResourcesProvider() {
            @Override
            public List<SwaggerResource> get() {
//              默认的不需要，网关所有的与Eureka相关的接口都不要显示
//              List<SwaggerResource> swaggerResources = defaultResourcesProvider.get();
                List<SwaggerResource> resources = new ArrayList<>();
                for (Route route : routeLocator.getRoutes()) {
                    BUFFER.setLength(0);
                    BUFFER.append("http://").append(route.getLocation()).append("/swagger-resources");
                    logger.debug("request from {}", BUFFER);
                    try {
                        requestByRoute(resources, route);
                    } catch (IllegalStateException e) {
                        logger.warn("can not found server  by {}", route.getFullPath());
                    }
                }
                return resources;
            }

            @HystrixCommand(fallbackMethod = "requestByRouteFallBack")
            protected void requestByRoute(List<SwaggerResource> resources, Route route) throws IllegalStateException {
                ResponseEntity<String> forEntity = restTemplate.getForEntity(BUFFER.toString(), String.class);
                if (forEntity.getStatusCode().value() == HttpStatus.OK.value()) {
                    String body = forEntity.getBody();
                    if (logger.isDebugEnabled()) {
                        logger.debug("request successful ,and result = {}", body);
                    }
                    List<SwaggerResource> swaggerResources = JSON.parseArray(body, SwaggerResource.class);
                    for (SwaggerResource resource : swaggerResources) {
                        String location = resource.getLocation();
                        resource.setLocation(prefix + route.getPrefix() + location);
                        resources.add(resource);
                    }
                } else {
                    logger.debug("can not request {},skip add swaggerResources", BUFFER);
                }
            }

            public void requestByRouteFallBack(List<SwaggerResource> resources, Route route) throws IllegalStateException {
                logger.info("请求失败...降级处理!");
            }
        };
    }

    /**
     * 通过生成{@link UiConfiguration}类，配置swagger的UI规则。
     *
     * @return {@link UiConfiguration}
     */
    @Bean
    UiConfiguration uiConfig() {
        return new UiConfiguration(null, "none", "alpha", "schema",
                UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS, false, true, 60000L);
    }

    /**
     * 创建{@link OrRequestMatcher}类，用于验证指定正则及url的逻辑
     *
     * @return {@link OrRequestMatcher}
     */
    @Bean
    public OrRequestMatcher orRequestMatcher() {
        return new OrRequestMatcher(requestMatchers());
    }

    /**
     * 根据{@link #ignoreUrls}属性，生成{@link AntPathRequestMatcher}对象
     *
     * @return {@link List<RequestMatcher>}
     */
    private List<RequestMatcher> requestMatchers() {
        int ignoreUrlListSize = ignoreUrls.size();
        if (ignoreUrlListSize == 1) {
            return Lists.newArrayList((RequestMatcher) new AntPathRequestMatcher(ignoreUrls.get(0)));
        }
        logger.info("Ignore urls: {}", ignoreUrls);
        List<RequestMatcher> matchers = new ArrayList<>(ignoreUrlListSize);
        for (int i = 0; i < ignoreUrlListSize; i++) {
            matchers.add(new AntPathRequestMatcher(ignoreUrls.get(i)));
        }
        return matchers;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        InetAddress localHost = InetAddress.getLocalHost();
        prefix = "http://" + localHost.getHostAddress() + ":" + port;
    }


}


