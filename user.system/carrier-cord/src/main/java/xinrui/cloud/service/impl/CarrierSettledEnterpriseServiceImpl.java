package xinrui.cloud.service.impl;

import xinrui.cloud.domain.CarrierSettledEnterprise;
import xinrui.cloud.service.CarrierSettledEnterpriseService;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Service;

/**
 * @author Jihy
 * @since 2019-06-25 23:49
 */
@Service public class CarrierSettledEnterpriseServiceImpl extends BaseServiceImpl<CarrierSettledEnterprise> implements
    CarrierSettledEnterpriseService {

  @Override public int getCarrierSettledEnterPriseByOrgCode(String orgCode) {
    String sql = "select count(*) from zhjf_carrier_settled_enterprise where org_code = ?";
    return dao.getJdbcTemplate().queryForObject(sql, Integer.class, orgCode);
  }

  @Override
  public CarrierSettledEnterprise getCarrierSettledEnterPriseByOrgCodeToEntity(String orgCode) {
    CarrierSettledEnterprise enterprise = findSingleCriteria(
        DetachedCriteria.forClass(CarrierSettledEnterprise.class).add(
            Restrictions.eq("orgCode", orgCode)));
    return enterprise;
  }
}
