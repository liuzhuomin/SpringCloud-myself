package xinrui.cloud.config;

import xinrui.cloud.dto.UserDetailDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.Map;

/**
 * <B>Title:</B>JWTAccessTokenConverter</br>
 * <B>Description:</B>自定义token的实现，用于增加额外的信息到jwt的token中</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 19:29
 */
@Component
public class JwtAccessTokenConverterEnhance extends JwtAccessTokenConverter {

    /**
     * token失效期限
     */
    public final static long EXPIRATION_TIME = 120 * 60 * 60 * 1000;

    private final static Logger LOGGER = LoggerFactory.getLogger(JwtAccessTokenConverterEnhance.class);

    /**
     * 生成token
     *
     * @param accessToken
     * @param authentication
     * @return
     */
    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        DefaultOAuth2AccessToken defaultOAuth2AccessToken = new DefaultOAuth2AccessToken(accessToken);
        defaultOAuth2AccessToken.setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME));
        UserDetailDto principal = (UserDetailDto) authentication.getPrincipal();
        defaultOAuth2AccessToken.getAdditionalInformation().put("user", principal);
        OAuth2AccessToken enhance = super.enhance(defaultOAuth2AccessToken, authentication);
        LOGGER.debug("Current token {}", enhance);
        return enhance;
    }

    /**
     * 解析token
     *
     * @param value
     * @param map
     * @return
     */
    @Override
    public OAuth2AccessToken extractAccessToken(String value, Map<String, ?> map) {
        return super.extractAccessToken(value, map);
    }
}
