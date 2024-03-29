package xinrui.cloud.filter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import xinrui.cloud.common.utils.TokenUtil;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.compoment.UserContext;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import java.util.Enumeration;

/**
 * 请求前为执行，请求时未渲染，请求后渲染时
 */
@Component("handleResultInterceptor")
public class HandleResultInterceptor implements HandlerInterceptor {

    private final static  Logger LOGGER = LoggerFactory.getLogger(HandleResultInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        LOGGER.info("request uri: {}",request.getRequestURI());
        Application.startThreadContext();
        Application.setRequestAndResponse(request,response);
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) throws Exception {
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
            throws Exception {
        Application.destroyThreadContext();
    }

}
