package xinrui.cloud.compoment;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.netflix.hystrix.strategy.concurrency.HystrixRequestVariableDefault;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.stereotype.Component;
import xinrui.cloud.BootException;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.feign.UserServiceFeign;

/**
 * 获取当前用户的信息
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/28 14:52
 */
@Component
@Scope("singleton")
@Slf4j
public class UserContext {

    @Autowired
    UserServiceFeign userClientService;

    private HystrixRequestVariableDefault<Boolean> tokenChange = new HystrixRequestVariableDefault<>();

    private HystrixRequestVariableDefault<UserDto> userDtoThreadLocal = new HystrixRequestVariableDefault<>();

    private static HystrixRequestVariableDefault<String> currentTokenLocal = new HystrixRequestVariableDefault<>();

    private final static Logger LOGGER = LoggerFactory.getLogger(UserContext.class);

    public synchronized void setToken(String token) {
        String currentToken = currentTokenLocal.get();
        if (StringUtils.isNotBlank(currentToken) && !currentToken.equals(token)) {
            tokenChange.set(true);
        } else {
            tokenChange.set(false);
        }
        currentTokenLocal.set(token);
    }

    public String getToken() {
        return currentTokenLocal.get();
    }

    public synchronized UserDto getCurrentUser() {
        String token = getToken();
        Assert.notNull(token, "token不能为空!");
        LOGGER.debug("解析token并且获取用户信息");
        LOGGER.info("tokenChange.get() {}", tokenChange.get());
        if (tokenChange.get()) {
            return userDtoThreadLocal.get();
        }
        Jwt jwt = getJwt(token);
        String claims = jwt.getClaims();
        JSONObject jsonObject = JSON.parseObject(claims);
        JSONObject user = jsonObject.getJSONObject("user");
        Long id = user.getLong("userId");
        LOGGER.debug("当前的id为{}", id);
        UserDto userByUserId = userClientService.findUserByUserId(id).getData();
        userDtoThreadLocal.set(userByUserId);
        return userDtoThreadLocal.get();
    }

    private Jwt getJwt(String token) {
        try {
            return JwtHelper.decodeAndVerify(token, new RsaVerifier("-----BEGIN PUBLIC KEY-----\n" +
                    "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEA2WTuH+Zl1W+bBCo3oC0D\n" +
                    "Kix2OVwPgjiA6Mk8WtxQ/Blzln7UsE1ORS2j9Q6O7NIFOqqlWsSP45ND1YvyZe/V\n" +
                    "iCQginSMAptANTUT5a4WWr5Q4RRuEcvKYsLTChgN54wnxugy6d6haoQf33doshO1\n" +
                    "/6M9ovpgUTh/Zvy1jy80tejr/3o0taDAJz+6E4CAaYxKhn8iSEqslS2d6GNXr/bH\n" +
                    "/h+Ih6ptz67e1URNX2DU2IWJJd94ffaZ2Qq8w5tHPENPsBfEUs8ZdrDEKQUbpR8i\n" +
                    "uCi8Eu8qoZRhTw4VSGRLGBemngRzNpo6LJuXb4tNJVHa0Jy0/xK5Na7JYqK1hdsG\n" +
                    "BwIDAQAB\n" +
                    "-----END PUBLIC KEY-----"));
        } catch (Exception e) {
            throw new BootException("过期的token!");
        }
    }


}
