package xinrui.cloud.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;


@Configuration
@Import(BaseSwaggerConfig.class)
public class SwaggerConfig {

    @Autowired
    BaseSwaggerConfig baseSwaggerConfig;

    /**
     * 用户相关文档
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket userApi() {
        return baseSwaggerConfig.create(new SwaggerApiInfo().groupName("用户相关文档").title("用户相关接口").creator("liuliuliu")
                .description("用于用户相关").scannerPath(RequestHandlerSelectors.any())
                .filterUrl(PathSelectors.ant("/user/**")));
    }

    /**
     * 企业相关文档
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket companyApi() {
        return baseSwaggerConfig.create(new SwaggerApiInfo().groupName("企业信息相关文档").title("企业信息接口").creator("liuliuliu")
                .description("用于获取或者修改企业信息").scannerPath(RequestHandlerSelectors.any())
                .filterUrl(PathSelectors.ant("/company/**")));
    }

    /**
     * 组织信息相关文档
     *
     * @return {@link Docket}
     */
    @Bean
    public Docket organizationApi() {
        return baseSwaggerConfig.create(new SwaggerApiInfo().groupName("组织信息相关文档").title("组织信息相关接口").creator("liuliuliu")
                .description("用于获取组织信息").scannerPath(RequestHandlerSelectors.any())
                .filterUrl(PathSelectors.ant("/organization/**")));
    }

}

