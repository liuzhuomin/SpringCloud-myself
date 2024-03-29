package xinrui.cloud.config;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import springfox.documentation.RequestHandler;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;


@Configuration
@EnableSwagger2
@PropertySource({"classpath:application.properties"})
public class BaseSwaggerConfig implements InitializingBean {

    private final static Logger LOGGER = LoggerFactory.getLogger(BaseSwaggerConfig.class);

    @Value("${swagger.title: swaggerTitle}")
    private String swaggerTitle;

    @Value("${swagger.concatName: swaggerConcatName}")
    private String swaggerConcatName;

    @Value("${swagger.concatUrl:swaggerConcatUrl}")
    private String swaggerConcatUrl;

    @Value("${swagger.concatEmail:swaggerConcatEmail}")
    private String swaggerConcatEmail;

    @Value("${swagger.version:1.0}")
    private String swaggerVersion;

    @Value("${swagger.description: restfulAPI}")
    private String swaggerDescription;

    @Value("${swagger.basePackage:xinrui}")
    private String swaggerBasePackage;

    @Value("${swagger.exportPath:/}")
    private String swaggerExportPath;

    @Value("${swagger.host:127.0.0.1:8080}")
    private String swaggerHost;

    /**
     * 创建基础的docket
     *
     * @return
     */
    private Docket createBaseDocket() {
        return (Docket) new Docket(DocumentationType.SWAGGER_2)
                .host(swaggerHost).pathMapping("/").enable(true)
                .directModelSubstitute(Date.class, String.class)
                .globalOperationParameters(globalParameters());
    }

    /**
     * 设置swagger的api相关描述信息
     *
     * @return
     */
    private ApiInfo createApi(String title, String creator, String swaggerDescription) {
        return new ApiInfoBuilder().title(title)
                .contact(new Contact(creator, iso2Utf8(swaggerConcatUrl), iso2Utf8(swaggerConcatEmail)))
                .version(iso2Utf8(swaggerVersion)).description(swaggerDescription).build();
    }


    /**
     * 设置全局参数属性
     *
     * @return
     */
    private ArrayList<Parameter> globalParameters() {
        ParameterBuilder tokenParameter = new ParameterBuilder();
        return Lists.newArrayList(tokenParameter.name("access_token").description("Token令牌")
                .modelRef(new ModelRef("String")).parameterType("header").required(false).build());
    }

//
//    /**
//     * 设置swagger-ui相关属性
//     * @return
//     */
//    @Bean
//    @Primary
//      UiConfiguration uiConfig() {
//        return new UiConfiguration(null,"list","sort","model",UiConfiguration.Constants.DEFAULT_SUBMIT_METHODS,true,true,3000L);
//    }


    /**
     * ISO-8859-1编码转换成utf-8编码
     *
     * @param str 需要被转换编码的字符串
     * @return 转换完成后的字符串
     */
    private static String iso2Utf8(String str) {
        try {
            return new String(str.getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public void afterPropertiesSet() {
        LOGGER.info("swagger配置加载完成");
    }

    /**
     * 根据{@link SwaggerApiInfo}创建swagger文档
     *
     * @param swaggerApiInfo
     * @return
     */
    public Docket create(SwaggerApiInfo swaggerApiInfo) {
        return createBaseDocket().groupName(swaggerApiInfo.groupName())
                .apiInfo(createApi(swaggerApiInfo.title(), swaggerApiInfo.creator(), swaggerApiInfo.description()))
                .select().apis(swaggerApiInfo.scannerPath())
                .paths(swaggerApiInfo.filterUrl()).build();
    }
}

class SwaggerApiInfo {
    /**
     * 文档分组名称
     */
    private String groupName;
    /**
     * 文档标题
     */
    private String title;
    /**
     * 文档创建者
     */
    private String creator;
    /**
     * 文档描述
     */
    private String description;
    /**
     * 文档生成选择的路径
     */
    private Predicate<RequestHandler> scannerPath;
    /**
     * 文档接口的url匹配规则
     */
    private Predicate<String> filterUrl;


    public SwaggerApiInfo groupName(String groupName) {
        this.groupName = groupName;
        return this;
    }

    public String groupName() {
        return this.groupName;
    }

    public SwaggerApiInfo title(String title) {
        this.title = title;
        return this;
    }

    public SwaggerApiInfo creator(String creator) {
        this.creator = creator;
        return this;
    }

    public SwaggerApiInfo description(String description) {
        this.description = description;
        return this;
    }

    public SwaggerApiInfo scannerPath(Predicate<RequestHandler> scannerPath) {
        this.scannerPath = scannerPath;
        return this;
    }

    public SwaggerApiInfo filterUrl(Predicate<String> filterUrl) {
        this.filterUrl = filterUrl;
        return this;
    }

    public String title() {
        return title;
    }

    public String creator() {
        return creator;
    }

    public String description() {
        return description;
    }

    public Predicate<RequestHandler> scannerPath() {
        return scannerPath;
    }

    public Predicate<String> filterUrl() {
        return filterUrl;
    }

}
