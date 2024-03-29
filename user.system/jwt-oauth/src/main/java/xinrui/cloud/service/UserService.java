package xinrui.cloud.service;

import xinrui.cloud.domain.User;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.vto.UserVto;

import java.util.List;

/**
 * <B>Title:</B>UserService</br>
 * <B>Description:</B>用户service</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:32
 */
public interface UserService extends BaseService<User> {

    /**
     * 根据用户登录名查找用户
     *
     * @param username
     * @return
     */
    UserDto findUserByUserName(String username);

    /**
     * 根据用户id查找用户信息
     *
     * @param userId
     * @return
     */
    UserDto findUserByUserId(Long userId);

    /**
     * 根据{@link UserVto}创建用户
     *
     * @param userVto
     * @return
     */
    UserDto createByVto(UserVto userVto);

    /**
     * 关键用户和组织
     *
     * @param id        用户ID
     * @param groupId   组织Id
     * @param groupType 组织类型详情见{@link xinrui.cloud.domain.GroupType}
     * @return
     */
    UserDto aliasUser2Group(Long id, Long groupId, String groupType);


    /**
     * 根据{@link UserVto}修改用户
     *
     * @param userVto
     * @return
     */
    UserDto editByVto(UserVto userVto);

    /**
     * 根据用户组织类型查找用户列表
     *
     * @param groupType   用户的组织类型
     * @param currentPage 当前页码
     * @param pageSize    一页大小
     * @return   返回分页对象
     */
    PageDto<List<UserDto>> listByGroupType(String groupType, int currentPage, int pageSize);
}
