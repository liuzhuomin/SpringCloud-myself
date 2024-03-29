package xinrui.cloud.service.impl;

import xinrui.cloud.BootException;
import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.domain.User;
import xinrui.cloud.domain.dto.CompanyDto;
import xinrui.cloud.service.OtherGroupService;
import xinrui.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <B>Title:</B>UserServiceImpl</br>
 * <B>Description:</B>  </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/10 16:25
 */
@Service
public class UserServiceImpl extends BaseServiceImpl<User> implements UserService {

    @Autowired
    OtherGroupService otherGroupService;

    @Override
    @Transactional
    public void focusCompany(Long leaderId, Long companyId) {
        OtherGroup group = otherGroupService.findById(companyId);
        Assert.notNull(group, "组织找不到");
        List<CompanyDto> companyDtos = otherGroupService.listCompany(leaderId);
        if(!CollectionUtils.isEmpty(companyDtos)){
            for (int i=0;i<companyDtos.size();i++){
                CompanyDto companyDto = companyDtos.get(i);
                if(companyId.intValue()==companyDto.getId().intValue()) {
                    throw new BootException("改企业已经是您的挂点企业或者，关注企业了，无法再次添加:【"+companyDto.getName()+"】");
                }
            }
        }
        User user = findById(leaderId);
        Assert.notNull(user, "用户找不到");
        group.setFucosLeader(user);
        user.getFocusCompanies().add(group);
        merge(user);
    }
}
