package xinrui.cloud.service;

import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.domain.dto.CompanyDto;

import java.util.List;

/**
 * 企业service
 */
public interface OtherGroupService extends BaseService<OtherGroup> {
    /**
     * 按照名称模糊搜索企业名称
     * @param name  企业名称
     * @param leaderId
     * @return  企业数据传输对象
     */
    List<CompanyDto> searchNames(String name, Long leaderId);

    /**
     * 列出当前领导用户的所有企业，简洁对象，只包含企业id，企业名称等。
     * @param leaderId  领导用户的id
     * @return     企业简洁对象
     */
    List<CompanyDto> listCompany(Long leaderId);

    /**
     * 删除领导的关注企业
     * @param leaderId  领导id
     * @param companyId 企业id
     * @return
     */
    void deleteFocusCompany(Long leaderId, Long companyId);
}
