package xinrui.cloud.filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.catalina.util.RequestUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.util.RequestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.ServletRequestUtils;
import xinrui.cloud.dto.ResultDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.servlet.ServletRegistration;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Enumeration;
import java.util.Map;

/**
 * 专门处理Options的拦截器。
 * 优先级最高。
 *
 * @author Jihy
 * @since 2019-07-01 21:11
 */
@Component
@SuppressWarnings("unused")
public class OptionsFilter extends ZuulFilter {


    private final Logger logger = LoggerFactory.getLogger(OptionsFilter.class);

    /**
     * 表示在请求前进行拦截
     *
     * @return  拦截的类型详情见 {@link ZuulFilter#filterType()}
     */
    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    /**
     * 此过滤器的排序顺序，为0代表第一个
     *
     * @return  越小代表过滤器越先执行
     */
    @Override
    public int filterOrder() {
        return 0;
    }

    /**
     * 只过滤options请求
     *
     * @return 需要过滤时返回true，如果是options类型的请求，则返回true
     */
    @Override
    public boolean shouldFilter() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletRequest request = ctx.getRequest();
        if (request.getMethod().equals(HttpMethod.OPTIONS.name())) {
            return true;
        }
        return false;
    }

    /**
     * 终止options的请求，返回成功状态，但是不再往zuul下游路由。
     *
     * @return
     */
    @Override
    public Object run() {
        RequestContext ctx = RequestContext.getCurrentContext();
        HttpServletResponse response = ctx.getResponse();
        HttpServletRequest request = ctx.getRequest();

        logger.debug("*****************FirstFilter run start*****************");
        response.setHeader("Access-Control-Allow-Origin", request.getHeader("Origin"));
        response.setHeader("Access-Control-Allow-Credentials", "true");
        response.setHeader("Access-Control-Allow-Methods","*");
        response.setHeader("Access-Control-Expose-Headers", "X-forwared-port, X-forwarded-host");
        response.setHeader("Vary", "Origin,Access-Control-Request-Method,Access-Control-Request-Headers");
        response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));

        ctx.setSendZuulResponse(false);
        ctx.setResponseStatusCode(200);
        logger.debug("*****************FirstFilter run end*****************");
        return ResultDto.SUCCESS;
    }
}


