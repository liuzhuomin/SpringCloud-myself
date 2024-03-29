package xinrui.cloud.config;

import xinrui.cloud.BootException;
import xinrui.cloud.common.utils.TokenUtil;
import xinrui.cloud.controller.AuthenticationController;
import xinrui.cloud.dto.ResultDto;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * <B>Title:</B>authServerConfig</br>
 * <B>Description:</B>
 * 保护应用程序url、验证提交的用户名和密码、重定向到登录表单，等等
 * </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019/6/25 15:44
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
@EnableWebSecurity
public class WebSecurityConfigurerEnhance extends WebSecurityConfigurerAdapter {


    @Autowired
    UserDetailsService userDetailsService;

    @Autowired
    AuthenticationTokenFilter authenticationTokenFilter;

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService);
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.authorizeRequests().antMatchers("/user/**","/oauth/**", "/user/logout/**", "/eureka/**").permitAll();
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().and().httpBasic();
        http.addFilterBefore(authenticationTokenFilter, UsernamePasswordAuthenticationFilter.class).headers().cacheControl();
    }

}

/**
 * <B>Title:</B>AuthenticationTokenFilter</br>
 * 用于密码验证之前，判断当前的token是否存在于退出登录的缓存之中。
 * </br>
 * @author liuliuliu
 * @version 1.0
 *  2019/6/25 15:44
 */
@Configuration
class AuthenticationTokenFilter extends OncePerRequestFilter {

    private final Logger logger = LoggerFactory.getLogger(AuthenticationTokenFilter.class);

    @Autowired
    AuthenticationController authenticationController;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
            String token = TokenUtil.extractToken(request);
            if (StringUtils.isNotBlank(token)) {
                ResultDto<?> resultDto = authenticationController.checkOutToken(token);
                logger.debug("The Object ResultDto : {}",  resultDto);
                if (resultDto.getCode() != 200) {
                    throw new BootException(401,"身份验证过期!");
                }
            }
            filterChain.doFilter(request, response);
    }
}
