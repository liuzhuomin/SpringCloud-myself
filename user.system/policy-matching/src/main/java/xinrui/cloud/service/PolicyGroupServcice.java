package xinrui.cloud.service;

import xinrui.cloud.domain.PolicyGroup;
import xinrui.cloud.domain.dto.PolicyGroupDto2;

import java.util.List;

/**
 * <B>Title:</B>PolicyGroupServcice.java</br>
 * <B>Description:</B> 政策组service </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 *  2019年3月28日
 */
public interface PolicyGroupServcice extends BaseService<PolicyGroup> {

    /**
     * 批量修改政策组
     *
     * @param groupJson
     * @return
     */
    List<PolicyGroupDto2> editGroup(String groupJson);

    /**
     * 批量修改政策组
     *
     * @return
     */
    List<PolicyGroupDto2> listGroup();
}
