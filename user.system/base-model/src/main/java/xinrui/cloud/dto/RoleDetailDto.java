package xinrui.cloud.dto;

import com.google.common.collect.Lists;
import org.springframework.beans.BeanUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * <B>Title:</B>RoleDetailDto</br>
 * <B>Description:</B> 用于security的角色验证</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:02
 */
public class RoleDetailDto implements GrantedAuthority {

    private String name;

    public static List<RoleDetailDto> copy(List<RoleDto> roles) {
        if(!CollectionUtils.isEmpty(roles)){
            List<RoleDetailDto> result= Lists.newArrayList();
            for (int i=0;i<roles.size();i++){
                result.add(copy(roles.get(i)));
            }
            return result;
        }
        return null;
    }

    public static RoleDetailDto copy(RoleDto role){
        RoleDetailDto roleDetailDto = new RoleDetailDto();
        BeanUtils.copyProperties(role,roleDetailDto);
        return roleDetailDto;
    }

    @Override
    public String getAuthority() {
        return this.name;
    }
}
