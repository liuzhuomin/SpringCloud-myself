package xinrui.cloud.service.impl;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.dao.UserRepository;
import xinrui.cloud.domain.Group;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.domain.User;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.BankGroupService;
import xinrui.cloud.service.CompanyService;
import xinrui.cloud.service.OrganizationService;
import xinrui.cloud.service.UserService;
import xinrui.cloud.vto.UserVto;

import java.util.List;

/**
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:34
 */
@Service
//@Slf4j
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {
    private Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    OrganizationService organizationService;

    @Autowired
    CompanyService companyService;

    @Autowired
    BankGroupService bankGroupService;

    @Override
    public UserDto findUserByUserName(String username) {
        return UserDto.copy(userRepository.findUserByUserName(username));
    }

    @Override
    public UserDto findUserByUserId(Long userId) {
        return UserDto.copy(findById(userId));
    }

    @Override
    public UserDto createByVto(UserVto userVto) {
        User user = new User();
        BeanUtilsEnhance.copyDateFiledEnhance(userVto, user);
        return UserDto.copy(persistAndGet(user));
    }

    @Override
    public UserDto aliasUser2Group(Long id, Long groupId, String groupType) {
        User user = findById(id);
        log.info("user==null ? {}", user == null);
        Assert.notNull(user, "用户找不到!");
        Group group = null;
        if (GroupType.BANK.equals(groupType)) {
            group = bankGroupService.findById(groupId);
        } else if (GroupType.OTHER.equals(groupType)) {
            group = companyService.findById(groupId);
        } else if (GroupType.ORGANIZATION.equals(groupType)) {
            group = organizationService.findById(groupId);
        }
        log.info("group==null ? {}", group == null);
        Assert.notNull(group, "组织找不到");
        user.setUniqueGroup(group);
        return UserDto.copy(merge(user));
    }

    @Override
    public UserDto editByVto(UserVto userVto) {
        User user = findById(userVto.getId());
        BeanUtilsEnhance.copyDateFiledEnhance(userVto, user);
        return UserDto.copy(merge(user));
    }

    @Override
    public PageDto<List<UserDto>> listByGroupType(String groupType, int currentPage, int pageSize) {
        DetachedCriteria userCriteria = DetachedCriteria.forClass(User.class)
                .createAlias("uniqueGroup", "u", JoinType.INNER_JOIN)
                .add(Restrictions.eq("u.groupType", groupType));
        PageDto<List<User>> page = new PageDto<>(--currentPage * pageSize, pageSize, userCriteria);
        dao.pageByCriteria(page);
        return new PageDto<List<UserDto>>(page.getTotalPage(), UserDto.copy(page.getT()));
    }
}
