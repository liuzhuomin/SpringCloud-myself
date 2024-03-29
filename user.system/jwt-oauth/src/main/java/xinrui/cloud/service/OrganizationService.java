package xinrui.cloud.service;

import xinrui.cloud.domain.Organization;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.vto.OtherGroupVto;

import java.util.List;

/**
 * <B>Title:</B>OrganizationService</br>
 * <B>Description:</B> 组织机构service，用于获取组织相关信息 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/3 9:40
 */
public interface OrganizationService extends BaseService<Organization> {

    /**
     * 通过企业名称获取企业组织数据传输对象{@link OtherGroupDto}
     * @param name  企业名称
     * @return  {@link OtherGroupDto}
     */
    OtherGroupDto findByCompanyName(String name);

    /**
     * 通过企业id获取企业组织数据传输对象{@link OtherGroupDto}
     * @param id  企业id
     * @return  {@link OtherGroupDto}
     */
    OtherGroupDto findByCompanyId(Long id);

    /**
     * 根据{@link OtherGroupVto}创建{@link xinrui.cloud.domain.OtherGroup}对象
     * @param vto   预期接受的数据对象{@link OtherGroupVto}
     * @return  {@link OtherGroupDto}
     */
    OtherGroupDto createByVto(OtherGroupVto vto);

    /**
     * 根据类型查找{@code  List<OtherGroupDto>}组织机构列表
     * @param type  预期的组织机构的类型
     * @return  {@code  List<OtherGroupDto>}
     */
    List<OtherGroupDto> listByType(String type);

}
