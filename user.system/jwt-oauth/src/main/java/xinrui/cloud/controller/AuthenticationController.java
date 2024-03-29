package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.dto.TokenDto;
import xinrui.cloud.config.EhcacheComponent;
import xinrui.cloud.dto.ResultDto;
import net.sf.ehcache.Cache;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

/**
 * 用户验证相关
 * @author liuliuliu
 * @version 1.0
 * 2019/6/25 16:11
 */
@RestController
@RequestMapping("oauth")
@Api("用户授权验证相关接口")
public class AuthenticationController {

    @Autowired
    EhcacheComponent ehcacheComponent;

    private final static Logger LOGGER = LoggerFactory.getLogger(AuthenticationController.class);

    @ApiOperation("通过token令牌获取用户相关简略信息,包含用户名和角色")
    @RequestMapping(value = "user", produces = "application/json")
    public TokenDto user(OAuth2Authentication auth2Authentication, HttpServletRequest request) throws IllegalAccessException {
//        String token = TokenUtil.extractToken(request);
//        ResultDto<?> resultDto = checkOutToken(token);
//        LOGGER.debug("Check result : {}",resultDto);
//        if(resultDto.getCode()!=200){
//            throw new IllegalAccessException("过期");
//        }
        Object principal = auth2Authentication.getUserAuthentication().getPrincipal();
        Set<String> strings = AuthorityUtils.authorityListToSet(auth2Authentication.getUserAuthentication().getAuthorities());
        LOGGER.info("principal{}", principal);
        return new TokenDto(principal, strings);
    }

    /**
     * 检查当前的token是否有效，
     *
     * @param token 用户的access_token
     * @return {@link ResultDto}
     */
    @PostMapping("check/token")
    public ResultDto<?> checkOutToken(@RequestParam("token") @NotNull String token) {
        Jwt decode = JwtHelper.decode(token);
        LOGGER.debug("Decode jwt : {}", decode);
        LOGGER.debug("Accept Token : {}", token);
        Cache defaultTokenCache = ehcacheComponent.getDefaultTokenCache();
        Assert.notNull(defaultTokenCache, "服务器故障");
        List keys = defaultTokenCache.getKeys();
//        token = token.substring(0, token.lastIndexOf('.'));
        for (Object key : keys) {
            LOGGER.debug("key: {}", key);
            LOGGER.debug("token: {}", token);
            LOGGER.debug("token equals key ：{}", token.equals(key));
            LOGGER.debug("token equals key ：{}", token.equals(key.toString()));
            if (token.equals(key)) {
                return ResultDto.error(401, "身份验证失败!");
            }
        }
        return ResultDto.success();
    }

}
