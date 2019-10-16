package xinrui.cloud.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * <B>Title:</B>ResourceConfig</br>
 * <B>Description:</B> 用于保护 OAuth2 要开放的资源，
 * 同时主要作用于client端以及token的认证(Bearer Auth),并且可重写拦截请求的函数</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 14:28
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfigurerEnhance extends ResourceServerConfigurerAdapter {

    @Override
    public void configure(HttpSecurity http) throws Exception {
        http.csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().authorizeRequests().antMatchers("/v2/api-docs",
                "/swagger-resources/**",
                "/swagger-ui.html**",
                "/webjars/**","/oauth/**","/user/logout/**","/eureka/**","/user/**").permitAll()
                .anyRequest().authenticated();
    }

//    @Override
//    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {
//        AuthenticationEntryPoint authenticationEntryPoint = new OAuth2AuthenticationEntryPoint();
//        ((OAuth2AuthenticationEntryPoint) authenticationEntryPoint).setExceptionTranslator(
//                new WebResponseExceptionTranslator() {
//                    @Override
//                    public ResponseEntity translate(Exception e) throws Exception {
//                        if(e instanceof InsufficientAuthenticationException){
//                            return ResponseEntity.ok(ResultDto.error(402, "身份验证过期！"));
//                        }
//                        return ResponseEntity.ok(ResultDto.error(401, "身份验证失败！"));
//                    }
//                });
//        resources.authenticationEntryPoint(authenticationEntryPoint);
//    }
}
