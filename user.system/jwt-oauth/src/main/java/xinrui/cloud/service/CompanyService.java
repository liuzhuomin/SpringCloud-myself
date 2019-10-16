package xinrui.cloud.service;

import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.dto.OtherGroupDto;

import java.util.List;
import java.util.Map;

/**
 * 企业service，用于获取企业详情等
 */
public interface CompanyService extends BaseService<OtherGroup> {

    /**
     * 通过企业id获取企业组织数据传输对象{@link OtherGroupDto}
     * @param id  企业id
     * @return  {@link OtherGroupDto}
     */
    OtherGroupDto findCompanyDtoById(Long id);

    /**
     * 通过企业id获取企业组织数据传输对象集合{@link OtherGroupDto}
     * @param name  企业名称
     * @return  {@link OtherGroupDto}
     */
    List<OtherGroupDto> listCompanyDtoByName(String name);

  /**
   * 通过企业ID获取企业信息
   * @param id
   * @return
   */
  Map<String, Object> getCompanyInfo(Long id);
}
