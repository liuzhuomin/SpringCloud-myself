package xinrui.cloud.dao;

import java.util.List;
import java.util.Map;
import xinrui.cloud.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author Jihy
 * @since 2019-06-25 10:25
 */
@Repository  public class UserDao extends BaseServiceImpl {

  /**
   * 获取当前的用户，并且获得当前用户的角色（政府还是企业）
   */
  public List<Map<String, Object>> getUserInfo(String userId) {
    String sql = "select t.id , t.unique_group_id, t.user_status, t.username, t.thelephone phone, t.password, t.sha_password, t.realname, t.logo, t.tel,"
        + " IFNULL((select name from tu_organization where id = t.unique_group_id), "
        + " (select name from tu_company where id = t.unique_group_id)) as groupName,"
        + " IF((select 1 from tu_company c where c.id = t. unique_group_id) = 1, \"other\", \"organization\") as groupType "
        + " from tu_user t where id = " + userId;
    return getQueryForToListMap(sql);
  }

}
