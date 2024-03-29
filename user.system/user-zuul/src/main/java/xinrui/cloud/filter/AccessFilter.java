package xinrui.cloud.filter;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.web.util.UrlPathHelper;
import xinrui.cloud.common.utils.ResponseUtils;
import xinrui.cloud.common.utils.TokenUtil;
import xinrui.cloud.dto.ResultDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

/**
 * 访问过滤器
 *
 * @author liuliuliu
 * @author Jhy
 * @version 1.0
 */
@Component
public class AccessFilter extends ZuulFilter {

    private final Logger logger = LoggerFactory.getLogger(AccessFilter.class);

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    OrRequestMatcher requestMatcher;

    /**
     * 过滤器类型选择： pre 为路由前 route 为路由过程中 post 为路由过程后 error 为出现错误的时候 同时也支持static
     * ，返回静态的响应，详情见StaticResponseFilter的实现
     * 以上类型在会创建或添加或运行在FilterProcessor.runFilters(type)
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 用来过滤器排序执行的
     *
     * @return 排序的序号
     */
    @Override
    public int filterOrder() {
        return 1;
    }

    /**
     * 是否通过这个过滤器，默认为true，改成false则不启用
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (request.getMethod().equals(RequestMethod.OPTIONS.name())) {
            return false;
        }
        return true;
    }

    /**
     * 过滤器的逻辑
     */
    @Override
    public Object run() {

        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        HttpServletResponse response = ctx.getResponse();
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Expose-Headers", "X-forwarded-port, X-forwarded-host");
        response.setHeader("Vary", "Origin,Access-Control-Request-Method,Access-Control-Request-Headers");

        ctx.setSendZuulResponse(true);

        if (!isIgnoreUrl(request)) {
            logger.info("The request must validation!");
            String accessToken;
            if (StringUtils.isNotBlank(accessToken = getToken(ctx, request))) {
                /**
                 * 是否需要去资源服务器检查资源 ?
                 */
                checkTokenByResourceServer(ctx, response, accessToken);
            } else {
                logger.info("The request has not contains access_token!");
                logError(ctx, response);
            }
        } else {
            logger.info("The request url is ignore the urls, skip validation!");
        }


        if (logger.isDebugEnabled()) {
            printHeaderAndParameters(request);
        }

        return ResultDto.SUCCESS;
    }

    /**
     * 分别打印消息头和参数
     *
     * @param request
     */
    private void printHeaderAndParameters(HttpServletRequest request) {
        logger.debug("------------------------------------------");
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            String header = request.getHeader(headerName);
            logger.debug("当前请求的header头为{}:{}", headerName, header);
        }
        Map<String, String[]> parameterMap = request.getParameterMap();
        for (Map.Entry<String, String[]> m : parameterMap.entrySet()) {
            String key = m.getKey();
            String[] value = m.getValue();
            logger.debug("当前请求的参数为{}:{}", key, value[0]);
        }
        logger.debug("------------------------------------------");
    }

    /**
     * 去资源服务器认证token
     *
     * @param ctx
     * @param response
     * @param accessToken
     */
    @HystrixCommand(fallbackMethod = "checkTokenByResourceServerFallback")
    private void checkTokenByResourceServer(RequestContext ctx, HttpServletResponse response, String accessToken) {
        ResponseEntity<ResultDto> oauthEntity = restTemplate.getForEntity("http://authorization:8200/oauth/check_token?token=" + accessToken, ResultDto.class);
        logger.info("response entity :{}", oauthEntity);
        if (!oauthEntity.getStatusCode().is2xxSuccessful()) {
            logger.warn("request fail! and status code is {}", oauthEntity.getStatusCode());
            logError(ctx, response);
        } else {
            ResultDto body = oauthEntity.getBody();
            int code = body.getCode();
            if (code != 0 && code != HttpStatus.OK.value()
                    || ResultDto.ERROR.equals(body.getMessage())) {
                logger.info("request successful ,but response not except!");
                logError(ctx, response, body);
            } else {
                logger.info("request through the verification");
            }
        }
        String baseUrl = getBaseUrl(ctx.getRequest());
        logger.debug("fullRequestUrl {}", baseUrl);
        ctx.addZuulRequestHeader("fullRequestUrl", baseUrl);
    }

    /**
     * 从消息头或者参数中获取token
     *
     * @param ctx
     * @param request
     * @return
     */
    private String getToken(RequestContext ctx, HttpServletRequest request) {

        logger.debug("request url:{},http method：{}", request.getRequestURL().toString(), request.getMethod());

        Map<String, List<String>> requestQueryParams = ctx.getRequestQueryParams();

        String accessToken;
        if (!CollectionUtils.isEmpty(requestQueryParams) && requestQueryParams.containsKey(TokenUtil.TOKEN_KEY)) {
            logger.debug("Find the {} from RequestParameters!", TokenUtil.TOKEN_KEY);
            changeHeaderParameter(ctx, accessToken = requestQueryParams.get(TokenUtil.TOKEN_KEY).get(0));
            requestQueryParams.remove(TokenUtil.TOKEN_KEY);
        } else {
            logger.debug("Find the {} from RequestHeaders!", TokenUtil.TOKEN_KEY);
            changeHeaderParameter(ctx, accessToken = request.getHeader(TokenUtil.TOKEN_KEY));
        }
        return accessToken;
    }

    /**
     * 判断当前url是否是可以直方通的url
     *
     * @param httpServletRequest 当前请求上下文对象
     * @return 如果与忽略的url规则匹配，则返回true，否则返回false
     */
    private boolean isIgnoreUrl(HttpServletRequest httpServletRequest) {
        Assert.notNull(requestMatcher, "OrRequestMatcher object must not be null!");
        return requestMatcher.matches(httpServletRequest);
    }

    /**
     * 响应指定的json格式错误状态码。
     *
     * @param ctx      zuul请求上下文
     * @param response 响应处理对象
     */
    public void logError(RequestContext ctx, HttpServletResponse response, ResultDto... resultDtos) {
        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(401);
        if (resultDtos == null || resultDtos.length != 1) {
            ResponseUtils.sendResponse(response, ResultDto.error(401, "身份验证失败"));
        } else {
            ResponseUtils.sendResponse(response, resultDtos[0]);
        }
    }

    /**
     * 添加accessToken到request的parameter中
     *
     * @param ctx
     * @param accessToken
     */
    public void changeHeaderParameter(RequestContext ctx, String accessToken) {
        logger.debug("current accessToken:{}", accessToken);
        if (StringUtils.isNotBlank(accessToken)) {
            if (ctx.getZuulRequestHeaders().containsKey(TokenUtil.TOKEN_KEY)) {
                ctx.getZuulRequestHeaders().remove(TokenUtil.TOKEN_KEY);
            }
            ctx.addZuulRequestHeader("Authorization", "Bearer" + accessToken);
        }
    }

    public void checkTokenByResourceServerFallback() {
        logger.info("into fallback");
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        logError(ctx, response);
    }

    /**
     * 获取服务地址
     *
     * @return
     */
    public String getBaseUrl(HttpServletRequest request) {
        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
        Route route = route(request);
        return baseUrl + "/" + route.getId() + "/";
    }

    @Autowired
    RouteLocator routeLocator;

    //核心逻辑，获取请求路径，利用RouteLocator返回路由信息
    protected Route route(HttpServletRequest request) {
        String requestURI = new UrlPathHelper().getPathWithinApplication(request);
        logger.info("requestURI {}", requestURI);
        return routeLocator.getMatchingRoute(requestURI);
    }

}
