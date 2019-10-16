package xinrui.cloud.service;

import xinrui.cloud.domain.CarrierSettledEnterprise;

/**
 * @author Jihy
 * @since 2019-06-25 23:49
 */
public interface CarrierSettledEnterpriseService extends BaseService<CarrierSettledEnterprise> {

  /**
   * 通过机构代码获取该机构是否已经入驻过载体
   * @param orgCode
   * @return
   */
   int getCarrierSettledEnterPriseByOrgCode(String orgCode);

  /**
   * 通过机构代码获取企业入驻的机构信息
   * @param orgCode
   * @return
   */
  CarrierSettledEnterprise getCarrierSettledEnterPriseByOrgCodeToEntity(String orgCode);
}
