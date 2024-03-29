package xinrui.cloud.config;

import feign.RequestInterceptor;
import feign.RequestTemplate;
import xinrui.cloud.compoment.UserContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * <B>Title:</B>FeignConfig</br>
 * <B>Description:</B>拦截feign发起的请求，将token放在消息头转递给下游</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/5 13:38
 */
@Configuration
@PropertySource("classpath:application.properties")
public class FeignConfig implements RequestInterceptor, InitializingBean {

    @Value("${security.oauth2.resource.userInfoUri:none}")
    private String userInfoUri;

    private String ignoreUrl;

    private Logger LOGGER = LoggerFactory.getLogger(FeignConfig.class);

    @Autowired
    UserContext userContext;

    @Override
    public void apply(RequestTemplate template) {
        final  String accessToken = userContext.getToken();
        LOGGER.debug("携带token是否为空?\t{}", StringUtils.isEmpty(accessToken));
        template.header("Authorization","Bearer "+accessToken);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        if(!"none".equals(userInfoUri)){
            String substring = userInfoUri.substring(userInfoUri.indexOf("/") + 2);
            ignoreUrl =substring.substring(0, substring.indexOf("/"));
        }
    }
}
