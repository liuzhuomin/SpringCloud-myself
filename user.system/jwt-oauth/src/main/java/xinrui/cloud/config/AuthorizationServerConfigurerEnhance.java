package xinrui.cloud.config;

import xinrui.cloud.dto.ResultDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.error.WebResponseExceptionTranslator;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import java.security.KeyPair;

/**
 * <B>Title:</B>OuathConfig</br>
 * <B>Description:</B> 定义令牌颁发机制和验证机制 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/25 15:17
 */
@Configuration("OAuth2Config")
@PropertySource("classpath:application.properties")
public class AuthorizationServerConfigurerEnhance extends AuthorizationServerConfigurerAdapter {

    @Value("${security.clientId:xinrui-ouath}")
    private String clientId;

    @Value("${security.secret:xinrui2017}")
    private String secret;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    ClientDetailsService clientDetailsService;

    @Autowired
    JwtAccessTokenConverter jwtAccessTokenConverter;


    /**
     * 配置客户端详情信息，使用数据库来存储或读取应用配置的详情信息（client_id ，client_secret，redirect_uri 等配置信息）。
     *
     * @param clients
     * @throws Exception
     */
    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {

        clients.inMemory().withClient(clientId).secret(secret)
                .authorizedGrantTypes("password", "refresh_token")
                .scopes("all")
                .authorities("password")
                .accessTokenValiditySeconds(3600).refreshTokenValiditySeconds(1000)

                .and()
                .withClient(clientId + 1)
                .secret(secret)
                .authorizedGrantTypes("client_credentials")
                .scopes("all", "read", "write")
                .authorities("client_credentials")
                .accessTokenValiditySeconds(7200)


                .and().withClient(clientId + 2).authorities("authorization_code", "refresh_token")
                .secret(secret)
                .authorizedGrantTypes("authorization_code")
                .scopes("all", "read", "write")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(10000)

                .and().withClient(clientId + 3)
                .secret(secret)
                .authorizedGrantTypes("all flow")
                .authorizedGrantTypes("authorization_code", "client_credentials", "refresh_token", "password", "implicit")
                .scopes("all", "read", "write")
                .accessTokenValiditySeconds(7200)
                .refreshTokenValiditySeconds(10000);
    }

    /**
     * 用来配置授权以及令牌（Token）的访问端点和令牌服务
     *
     * @param endpoints
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        JwtAccessTokenConverter jwtAccessTokenConverter = jwtTokenEnhancer();
        endpoints.tokenStore(tokenStore()).tokenEnhancer(jwtAccessTokenConverter)
                .authenticationManager(authenticationManager).accessTokenConverter(jwtAccessTokenConverter)
                .userDetailsService(userDetailsService)
                .allowedTokenEndpointRequestMethods(HttpMethod.values()).exceptionTranslator(
                new WebResponseExceptionTranslator() {
                    @Override
                    public ResponseEntity translate(Exception e) throws Exception {
                        return ResponseEntity.ok(ResultDto.error(401,"身份验证失败!"));
                    }
                });
    }

    /**
     * 用来配置令牌端点(Token Endpoint)的安全与权限访问。
     *
     * @param security
     * @throws Exception
     */
    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
        security.tokenKeyAccess("permitAll()")
                .checkTokenAccess("permitAll()")
                .allowFormAuthenticationForClients();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JwtTokenStore(jwtTokenEnhancer());
    }

    @Bean
    protected JwtAccessTokenConverter jwtTokenEnhancer() {
        KeyStoreKeyFactory keyStoreKeyFactory = new KeyStoreKeyFactory(new ClassPathResource("xinrui-jwt.jks"), "xinrui2017!@#".toCharArray());
        KeyPair keyPair = keyStoreKeyFactory.getKeyPair("xinrui-jwt");
        jwtAccessTokenConverter.setKeyPair(keyPair);
        return jwtAccessTokenConverter;
    }


}
