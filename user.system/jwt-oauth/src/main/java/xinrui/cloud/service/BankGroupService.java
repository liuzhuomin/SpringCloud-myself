package xinrui.cloud.service;

import xinrui.cloud.domain.BankGroup;
import xinrui.cloud.domain.Organization;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.vto.OtherGroupVto;

import java.util.List;

/**
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/7/3 9:40
 */
public interface BankGroupService extends BaseService<BankGroup> {

    /**
     * 通过银行名称获取银行组织数据传输对象{@link OtherGroupDto}
     * @param name  银行名称
     * @return  {@link OtherGroupDto}
     */
    OtherGroupDto findByCompanyName(String name);

    /**
     * 通过银行id获取银行组织数据传输对象{@link OtherGroupDto}
     * @param id  银行id
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
