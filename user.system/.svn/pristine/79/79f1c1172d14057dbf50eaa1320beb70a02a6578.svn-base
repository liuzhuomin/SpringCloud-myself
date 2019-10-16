package xinrui.cloud.service.impl;


import java.util.UUID;
import xinrui.cloud.domain.dto.TokenModel;
import xinrui.cloud.service.TokenManager;
import xinrui.cloud.util.ShardedJedisPoolService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * Token处理
 * @author jihy
 */
@Service public class RedisTokenManager implements TokenManager {

  @Autowired
  ShardedJedisPoolService jedis;

  @Override public TokenModel createToken(String userId) {
    //使用uuid作为源token
    String token = UUID.randomUUID().toString().replace("-", "");
    TokenModel model = new TokenModel(userId, token);
    //存储到redis并设置过期时间
     jedis.setex(token, userId, ShardedJedisPoolService.DAY);
     return model;
  }

  @Override public TokenModel createTokenByUid(String uid, String token) {
    //使用uuid作为源token
     TokenModel model = new TokenModel(uid, token);
    //存储到redis并设置过期时间
    jedis.setex(uid, token, ShardedJedisPoolService.DAY);
    return model;
   }

  @Override public boolean checkToken(String key) {
    if (key == null) {
      return false;
    }
    String value = jedis.get(key);
    if(value == null){
      return false;
    }
    //如果验证成功，说明此用户进行了一次有效操作，延长token的过期时间
    jedis.setex(value, key, ShardedJedisPoolService.DAY);
    return true;
  }

  @Override public TokenModel getToken(String authentication) {
    if (authentication == null || authentication.length() == 0) {
      return null;
    }
    String[] param = authentication.split("_");
    if (param.length != 2) {
      return null;
    }
    String userId = param[0];
    String token = param[1];
    return new TokenModel(userId, token);
  }

  @Override public void deleteToken(String token) {
    jedis.del(token);
  }

  @Override public String getTokenValue(String token) {
    if(token == null){
      return null;
    }
    return  jedis.get(token);
  }

  @Override public String getTokenByUid(String uid) {
    if(uid == null){
      return null;
    }
    return  jedis.get(uid);
  }
}
