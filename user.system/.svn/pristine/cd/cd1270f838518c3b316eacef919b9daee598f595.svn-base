package xinrui.cloud.compoment;

import com.netflix.hystrix.strategy.concurrency.HystrixRequestContext;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.util.Assert;
import xinrui.cloud.app.MyApplication;
import xinrui.cloud.common.utils.TokenUtil;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.filter.HandleResultInterceptor;

import javax.security.auth.DestroyFailedException;
import javax.security.auth.Destroyable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Application Object
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/28 14:52
 */
public class Application {

    private static final String ENSURE_INJECT_MESSAGE = "确保%s已注入Application, 检查是否配置SpringyhyWebApplicationFilter";
    private static HystrixRequestVariableDefault<HttpServletRequest> requestHolder = new HystrixRequestVariableDefault<HttpServletRequest>();
    private static HystrixRequestVariableDefault<HttpServletResponse> responseHolder = new HystrixRequestVariableDefault<HttpServletResponse>();
    private static String fullRequestUrl;

    public static void setRequestAndResponse(HttpServletRequest request, HttpServletResponse response) {
        requestHolder.set(request);
        responseHolder.set(response);
        fullRequestUrl = request.getHeader("fullRequestUrl");
        setCurrentUser(request);
    }

    private static void setCurrentUser(HttpServletRequest request) {
        String token = TokenUtil.extractToken(request);
        if (StringUtils.isBlank(token)) {
            token = request.getHeader(OAuth2AccessToken.ACCESS_TOKEN);
            if (StringUtils.isBlank(token)) {
                token = request.getParameter(OAuth2AccessToken.ACCESS_TOKEN);
            }
        }
        UserContext userContext = MyApplication.getApplicationContext().getBean(UserContext.class);
        userContext.setToken(token);
    }

    public static void startThreadContext() {
        HystrixRequestContext.initializeContext();
    }

    public static void destroyThreadContext() {
        HystrixRequestContext.getContextForCurrentThread().shutdown();
    }

    /**
     * 获取当前HttpServletRequest.
     * <p/>
     * 方便工具类访问request. Controller内推荐使用方法参数声明方式获取.
     */
    public static HttpServletRequest getRequest() {
        final HttpServletRequest request = requestHolder.get();
        Assert.notNull(request, String.format(ENSURE_INJECT_MESSAGE, "request"));
        return request;
    }

    /**
     * 获取当前HttpServletResponse.
     * <p/>
     * 方便工具类访问response. Controller内推荐使用方法参数声明方式获取.
     */
    public static HttpServletResponse getResponse() {
        final HttpServletResponse response = responseHolder.get();
        Assert.notNull(response, String.format(ENSURE_INJECT_MESSAGE, "response"));
        return response;
    }

    /**
     * 获取session.
     */
    public static HttpSession getSession() {
        return getRequest().getSession();
    }

    /**
     * 获取当前的用户
     *
     * @return
     */
    public static UserDto getCurrentUser() {
        UserContext userContext = MyApplication.getApplicationContext().getBean(UserContext.class);
        UserDto currentUser = userContext.getCurrentUser();
        Assert.notNull(currentUser, "当前用户服务异常，查找不到该用户!");
        return currentUser;
    }

    /**
     * 获取当前的token
     *
     * @return
     */
    public static String getCurrentToken() {
        UserContext userContext = MyApplication.getApplicationContext().getBean(UserContext.class);
        return userContext.getToken();
    }

    /**
     * 获取服务地址
     *
     * @return
     */
    public static String getBaseUrl() {
        HttpServletRequest request = Application.getRequest();
        return request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    /**
     * 获取完整的微服务路径是通过网关的请求路径+上当前服务的服务id
     *
     * @return
     */
    public static String getBaseServicePath() {
        return fullRequestUrl;
    }
}
