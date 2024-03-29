package xinrui.cloud.service.impl;

import xinrui.cloud.domain.ProjectAudit;
import xinrui.cloud.domain.dto.ProjectAuditDto;
import xinrui.cloud.service.ProjectAuditService;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class ProjectAuditServiceImpl extends BaseServiceImpl<ProjectAudit> implements  ProjectAuditService{

    @Transactional
    @Override
    public void projectAudit(ProjectAuditDto proAuditDto) {
        //获取项目ID,校验项目是否存在
/*        Long  projectId = proAuditDto.getProjectInnerDto().getId();
        ProjectInner projectInner = proInService.findById(projectId);
        Assert.notNull(projectInner, "项目入驻详情对象找不到!");

        //保存审核的信息
        ProjectAudit proAudit = ProjectAuditDto.toBean(proAuditDto);
        proAudit.setProjectInner(projectInner);
        //添加或修改载体信息ID
        proAudit.setCarrierId(proAuditDto.getCarrierId());
        //获取用户的信息,根据Id查询
        Long uid = proAuditDto.getUid();
        if (uid == null){
            throw new BootException("请填写当前登录的用户ID!");
        }
        UserDto userData = userClientService.findUserByUserId(uid);//ID查询
        proAudit.setUName(userData.getUsername());//设置用户名
        proAudit.setUid(userData.getId());//设置用户ID
        //设置项目的状态
        proAudit.setProAuditFlag(proAuditDto.getProjectState());//与项目的审核状态一致
        ProjectAudit merge = merge(proAudit);
        //根据项目ID修改项目的状态
        projectInner.setProjectState(proAuditDto.getProjectState());//修改状态
//        projectInner.getProAudits().add(merge);
        dao.merge(ProjectInner.class,projectInner);*/
    }
}
