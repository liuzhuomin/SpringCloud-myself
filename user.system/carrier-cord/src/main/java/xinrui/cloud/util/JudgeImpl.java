package xinrui.cloud.util;

import com.google.common.collect.Lists;
import io.jsonwebtoken.lang.Assert;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import xinrui.cloud.BootException;
import xinrui.cloud.annotation.Group;
import xinrui.cloud.annotation.Role;
import xinrui.cloud.annotation.XinruiSecurity;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.dto.RoleDto;
import xinrui.cloud.dto.UserDto;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author liuliuliu
 * @version 1.0
 * 2019/8/7 10:32
 */
@Slf4j
public class JudgeImpl implements JudgeInterface {

    @Override
    public List<String> judge(Annotation[] annotations) {
        List<String> methods = Lists.newArrayList();
        UserDto currentUser = Application.getCurrentUser();
        Assert.notNull(currentUser, "用户未曾登录!");
        for (Annotation annotation : annotations) {
            Class<? extends Annotation> annotationType = annotation.annotationType();
            if (annotationType == XinruiSecurity.class) {
                XinruiSecurity xinruiSecurity = (XinruiSecurity) annotation;
                Group[] groups = xinruiSecurity.groups();
                Role[] roles = xinruiSecurity.roles();
                methods.addAll(judgeGroup(currentUser, groups));
                methods.addAll(judgeRoles(currentUser, roles));
            }
            if (annotationType == Group.class) {
                log.info("进入了group");
                methods.addAll(judgeGroup(currentUser, (Group) annotation));
            }
            if (annotationType == Role.class) {
                log.info("进入了group");
                methods.addAll(judgeRoles(currentUser, (Role) annotation));
            }
        }
        return methods;
    }

    private List<String> judgeGroup(UserDto currentUser, Group... groups) {
        //一旦添加了Group注解就必须存在单位组织并且存在单位组织名称
        OtherGroupDto uniqueGroup = currentUser.getUniqueGroup();
        Assert.notNull(uniqueGroup, "用户没有组织!");
        String name = uniqueGroup.getGroupType();
        log.info("当前用户的组织为{}", name);
        if (StringUtils.isBlank(name)) {
            throw new BootException("必须拥有单位组织!");
        }
        List<String> ignoreMethod = Lists.newArrayList();
        boolean canContinue = false;
        for (Group group : groups) {
            String value = group.value();
            if (StringUtils.isBlank(value)) {
                continue;
            }
            if (name.equals(value)) {
                canContinue = true;
            }
            String[] strings = group.ignoreMethod();
            if (ArrayUtils.isNotEmpty(strings)) {
                Collections.addAll(ignoreMethod, strings);
            }
        }
        if (!canContinue) {
            throw new BootException("权限不够，该接口必须指定权限才能访问!");
        }
        return ignoreMethod;
    }

    private List<String> judgeRoles(UserDto currentUser, Role... roles) {
        List<RoleDto> roleList = currentUser.getRoles();
        Assert.notEmpty(roleList,"用户没有角色!");

        return null;
    }
}

