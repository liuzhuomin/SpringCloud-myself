package xinrui.cloud.service;

import xinrui.cloud.domain.dto.TokenModel;

/**
 * Token处理
 */
public interface TokenManager {

  /**
   * 创建一个 token 关联上指定用户
   * token为key uid为value
   * @param userId 指定用户的 id
   * @return 生成的 token
   */
   TokenModel createToken(String userId);

  /**
   * 创建一个token 关联指定用户
   * token为value key 为uid
   * @param uid
   * @param token
   * @return
   */
   TokenModel createTokenByUid(String uid, String token);
  /**
   * 检查 token 是否有效
   * @param token token
   * @return 是否有效
   */
   boolean checkToken(String token);

  /**
   * 从字符串中解析 token
   * @param authentication 加密后的字符串
   * @return
   */
   TokenModel getToken(String authentication);

  /**
   * 清除 token
   * @param userId 登录用户的 id
   */
   void deleteToken(String userId);

  /**
   * 通过key获取value
   * @param token
   * @return
   */
   String getTokenValue(String token);

  /**
   * 通过uid获取token
   * @param uid
   * @return
   */
   String getTokenByUid(String uid);
}
