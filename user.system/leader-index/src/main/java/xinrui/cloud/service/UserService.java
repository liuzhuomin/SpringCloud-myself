package xinrui.cloud.service;

import xinrui.cloud.domain.User;
import xinrui.cloud.service.BaseService;

/**
 * <B>Title:</B>UserService</br>
 * <B>Description:</B>用户接口  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/10 16:24
 */
public interface UserService extends BaseService<User> {
    /**
     * 添加当前领导用户的关联企业
     * @param leaderId      用户id
     * @param companyId     企业id
     */
    void focusCompany(Long leaderId, Long companyId);
}
