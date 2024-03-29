package xinrui.cloud.config;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spring.web.plugins.Docket;
import xinrui.cloud.config.BaseSwaggerConfig;
import xinrui.cloud.config.SwaggerApiInfo;

import java.net.UnknownHostException;


@Configuration
@Import(BaseSwaggerConfig.class)
public class SwaggerConfig {

    @Autowired
    BaseSwaggerConfig baseSwaggerConfig;

    /**
     * 载体相关文档
     * @return {@link Docket}
     */
    @Bean
    public Docket carrierApi() {
        return baseSwaggerConfig.create( new SwaggerApiInfo().groupName("载体相关文档").title("载体备案接口").creator("JHY")
                .description("用于载体备案").scannerPath(RequestHandlerSelectors.any())
                .filterUrl(PathSelectors.ant("/carrier/**")));
    }

    /**
     * 活动通知相关文档
     * @return {@link Docket}
     */
    @Bean
    public Docket noticeApi() {
        return baseSwaggerConfig.create( new SwaggerApiInfo().groupName("活动通知相关文档").title("活动通知接口").creator("LMS")
                .description("用于活动通知").scannerPath(RequestHandlerSelectors.any())
                .filterUrl(PathSelectors.ant("/notice/**")));
    }

    /**
     * 项目入驻相关文档
     * @return {@link Docket}
     */
    @Bean
    public Docket projectInApi() {
        return baseSwaggerConfig.create( new SwaggerApiInfo().groupName("项目入驻相关文档").title("项目入驻通知接口").creator("LP")
                .description("用于项目入驻").scannerPath(RequestHandlerSelectors.any())
                .filterUrl(PathSelectors.ant("/projectIn/**")));
    }

    /**
     * 科技金融相关文档
     * @return {@link Docket}
     */
    @Bean
    public Docket technologyApi() {
        return baseSwaggerConfig.create( new SwaggerApiInfo().groupName("科技金融相关文档").title("科技金融接口").creator("LZM")
                .description("用于科技金融。" +
                        "\nHTTP 请求规范\n" +
                        "GET（SELECT）：查询; 从服务器取出资源（一项或多项）。\n" +
                        "POST（CREATE）：新增; 在服务器新建一个资源。\n" +
                        "PUT（UPDATE）：覆盖,全部更新 ; 在服务器更新资源（客户端提供改变后的完整资源）。\n" +
                        "PATCH（UPDATE）：更新;  在服务器更新资源（客户端提供改变的属性）。\n" +
                        "DELETE（DELETE）：删除; 从服务器删除资源。").scannerPath(RequestHandlerSelectors.any())
                .filterUrl( Predicates.or(
                        PathSelectors.ant("/technology/**"),
                        PathSelectors.ant("/file/**"),
                        PathSelectors.ant("/bank/**"))));
    }

}

