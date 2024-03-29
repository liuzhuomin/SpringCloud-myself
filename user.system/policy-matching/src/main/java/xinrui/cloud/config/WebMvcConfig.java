package xinrui.cloud.config;

import xinrui.cloud.filter.HandleResultInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import springfox.documentation.spring.web.SpringfoxWebMvcConfiguration;


@Configuration()
@DependsOn({"handleResultInterceptor"})
@ConditionalOnClass(SpringfoxWebMvcConfiguration.class)
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    HandleResultInterceptor handler;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handler).excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**").excludePathPatterns("/v2/api-docs/**").excludePathPatterns("/v2/api-docs-ext/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("doc.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }


}
