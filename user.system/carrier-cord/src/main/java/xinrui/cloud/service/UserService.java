package xinrui.cloud.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import xinrui.cloud.dao.CompanyDao;
import xinrui.cloud.dao.UserDao;
import xinrui.cloud.domain.dto.GroupTO;
import xinrui.cloud.domain.dto.GroupType;
import xinrui.cloud.domain.dto.UserTO;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 用户Service
 * 已弃用
 * @author Jihy
 * @since 2019-06-25 10:23
 */
@Deprecated @Service public class UserService {

  @Autowired private UserDao userDao;

  @Autowired private TokenManager tokenManager;

  @Autowired private CompanyDao companyDao;


  /**
   * 通过userId获取用户信息
   */
  public UserTO getUser(String userId) {
    List<Map<String, Object>> resultMap = userDao.getUserInfo(userId);
    UserTO userTo = new UserTO();
    for (Map<String, Object> map : resultMap) {
      userTo.setUserId(Long.parseLong(String.valueOf(map.get("id"))));
      userTo.setUserName(String.valueOf(map.get("username")));
      userTo.setPassword(String.valueOf(map.get("password")));
      userTo.setShaPassword(String.valueOf(map.get("sha_password")));
      userTo.setRealName(String.valueOf(map.get("realname")));
      userTo.setStatus(String.valueOf(map.get("user_status")));
      userTo.setPhone(String.valueOf(map.get("phone")));
      String groupId = String.valueOf(map.get("unique_group_id"));
      String groupType = String.valueOf(map.get("groupType"));
      String logo = String.valueOf(map.get("logo"));
      userTo.setLogo(logo);
      userTo.setGroupType("other".equals(groupType) ? GroupType.OTHER : GroupType.ORGANIZATION);
      if (!StringUtils.isEmpty(groupId) && !"null".equals(groupId)) {
        userTo.setCompanyId(Long.valueOf(groupId));
        Map<String, Object> result =  new HashMap<>();
        if ("other".equals(groupType)) {
          //result = companyDao.getCompanyInfo(groupId);
        } else {
          result = companyDao.getOrganization(Long.valueOf(groupId));
        }
        GroupTO group = new GroupTO();
        if(result != null && result.size() > 0){
          group.setGroupType(userTo.getGroupType());
          group.setName(String.valueOf(result.get("name")));
          group.setCreatorId(Long.parseLong(String.valueOf(result.get("creator_id"))));
          group.setId(Long.parseLong(String.valueOf(result.get("id"))));
          group.setLogo(String.valueOf(result.get("logo")));
          userTo.setGroupTo(group);
        }
      }
      System.out.println("userId:"
          + userTo.getUserId()
          + " userName:"
          + userTo.getUserName()
          + "sha_password :"
          + userTo.getShaPassword());
    }
    return userTo;
  }

  /**
   * 从token中获取用户信息
   */
  public UserTO tokeGetUser(String token) {
    String value = tokenManager.getTokenValue(token);
    if (value.contains("-")) {
      String[] params = value.split("-");
      //默认的话使用id查询user对象，也可使用userName
      value = params[0];
    }
    return getUser(value);
  }

  public UserTO isUserAdmin(UserTO userTo, Long companyId) {
    //判断当前用户是否是管理员
    Map<String, Object> r1 = new HashMap<>();
    if(userTo.getGroupType() == GroupType.ORGANIZATION){
      r1 = companyDao.getOrganization(companyId);
    }else if(userTo.getGroupType() == GroupType.OTHER){
      //r1 = companyDao.getCompanyInfo(String.valueOf(companyId));
    }
    System.out.println("r1 size: " + r1.size());
    if (r1.size() > 0) {
      Long creatorId = Long.parseLong(String.valueOf(r1.get("creator_id")));
      System.out.println("userId:" + userTo.getUserId() + "  creatorId:" + creatorId);
      if (userTo.getUserId() == creatorId) {
        userTo.setIsAdmin(true);
      } else {
        userTo.setIsAdmin(false);
      }
    }
    return userTo;
  }
}
