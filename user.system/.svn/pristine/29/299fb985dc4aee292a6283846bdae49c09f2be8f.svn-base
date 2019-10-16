package xinrui.cloud.filter;

import com.alibaba.fastjson.JSON;
import org.springframework.cloud.netflix.zuul.filters.route.ZuulFallbackProvider;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Component;
import xinrui.cloud.dto.ResultDto;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

/**
 * <p/>zuul快速降级处理类，默认拦截所有url，配置为*;一旦请求失败，返回指定格式的错误信息。
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/28 10:10
 */
@Component
@SuppressWarnings("unused")
public class FallBackProvider implements ZuulFallbackProvider {

    /**
     * 表示对所有服务进行降级处理，*标识所有服务，单个可指定服务名称。
     *
     * @return 当前过滤器需要过滤的服务id，*代表所有
     */
    @Override
    public String getRoute() {
        return "*";
    }

    @Override
    public ClientHttpResponse fallbackResponse() {
        return new ClientHttpResponse() {

            @Override
            public HttpHeaders getHeaders() {
                HttpHeaders headers = new HttpHeaders();
                headers.setContentType(MediaType.APPLICATION_JSON_UTF8);
                return headers;
            }

            @Override
            public InputStream getBody() {
                String errorMessage = JSON.toJSONString(ResultDto.error(500, "服务器故障"));
                return new ByteArrayInputStream(errorMessage.getBytes(StandardCharsets.UTF_8));
            }

            @Override
            public HttpStatus getStatusCode() {
                return HttpStatus.OK;
            }

            @Override
            public int getRawStatusCode() {
                return 200;
            }

            @Override
            public String getStatusText() {
                return "OK";
            }

            @Override
            public void close() {
            }

        };

    }
}

