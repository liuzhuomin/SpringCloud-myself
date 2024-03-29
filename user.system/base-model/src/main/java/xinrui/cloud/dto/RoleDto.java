package xinrui.cloud.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.domain.Role;
import xinrui.cloud.domain.TreeEntity;
import org.springframework.beans.BeanUtils;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <B>Title:</B>RoleDto</br>
 * <B>Description:</B> 角色数据传输对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:22
 */
@ApiModel("角色数据传输对象")
public class RoleDto extends TreeEntity<Role> {

    @ApiModelProperty(value = "角色的名称", position = 5)
    private String name;

    public static List<RoleDto> copy(List<Role> roles) {
        if(!CollectionUtils.isEmpty(roles)){
            List<RoleDto> result = Lists.newArrayList();
            for (int i=0;i<roles.size();i++){
                result.add(copy(roles.get(i)));
            }
            return result;
        }
        return null;
    }

    public static RoleDto copy(Role role){
        RoleDto roleDto = new RoleDto();
        BeanUtils.copyProperties(role,roleDto);
        return roleDto;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void setName(String name) {
        this.name = name;
    }
}
