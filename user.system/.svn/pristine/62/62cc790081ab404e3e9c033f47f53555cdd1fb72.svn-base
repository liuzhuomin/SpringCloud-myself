package xinrui.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;

import java.net.UnknownHostException;


@Configuration
@Import(BaseSwaggerConfig.class)
public class SwaggerConfig {

    @Autowired
    BaseSwaggerConfig baseSwaggerConfig;

    /**
     * 政策匹配相关文档
     * @return {@link Docket}
     * @throws UnknownHostException
     */
    @Bean
    public Docket carrierApi() throws UnknownHostException {
        return baseSwaggerConfig.create( new SwaggerApiInfo().groupName("政策匹配相关文档").title("政策匹配接口").creator("LZM")
                .description("用于政策匹配").scannerPath(RequestHandlerSelectors.any())
                .filterUrl( PathSelectors.any()));
    }


}

