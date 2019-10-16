package xinrui.cloud.config;

import xinrui.cloud.filter.HandleResultInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;


@Configuration()
@DependsOn({"handleResultInterceptor"})
public class WebMvcConfig extends WebMvcConfigurationSupport {

    @Autowired
    HandleResultInterceptor handler;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(handler).excludePathPatterns("/swagger-resources/**")
                .excludePathPatterns("/webjars/**");
    }

    @Override
    protected void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        registry.addResourceHandler("/templates/**").addResourceLocations("classpath:/templates/");
        registry.addResourceHandler("swagger-ui.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
        super.addResourceHandlers(registry);
    }


}
