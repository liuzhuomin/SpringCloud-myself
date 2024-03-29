package xinrui.cloud.dao;

import java.util.List;
import java.util.Map;
import xinrui.cloud.service.impl.BaseServiceImpl;
import org.springframework.stereotype.Repository;

/**
 * @author Jihy
 * @since 2019-06-25 10:42
 */
 @Repository public class CompanyDao extends BaseServiceImpl {

  /**
   * 通过企业名称获取企业信息
   * @param name 企业名称
   * @return
   */
  public List<Map<String, Object>> getCompanyByName(String name){
      String sql = "select c.id, c.name, t.org_code from tu_company c "
          + " left join t_corporation t on c.id = t.other_id "
          + " where c.name like '%" + name + "%'";
      return getQueryForToListMap(sql);
   }

  public Map<String, Object> getCompanyInfo(Long companyId) {
    String sql = "select t.id, t.name , t.logo, t.creator_id , t.industry_id, t.region_id, c.org_code from tu_company t \n"
        + "left join t_corporation c on c.other_id = t.id  where t.id =  " + companyId;
    return getQueryForToMap(sql);
  }

  /**
   * 获取某个政府机构
   *
   * @param organizationId
   */
  public Map<String, Object> getOrganization(Long organizationId) {
    String sql = "select id, name,logo,creator_id from tu_organization where id = " + organizationId;
    return getQueryForToMap(sql);
  }
}
