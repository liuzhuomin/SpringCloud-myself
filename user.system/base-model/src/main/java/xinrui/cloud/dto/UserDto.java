package xinrui.cloud.dto;

import com.google.common.collect.Lists;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import xinrui.cloud.BootException;
import xinrui.cloud.common.utils.BeanUtilsEnhance;
import xinrui.cloud.common.utils.CopyListFilter;
import xinrui.cloud.domain.IdEntity;
import xinrui.cloud.domain.User;
import org.springframework.beans.BeanUtils;

import java.util.List;

/**
 * <B>Title:</B>UserDto</br>
 * <B>Description:</B> 用户数据传输对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:19
 */
@ApiModel("用户数据传输对象")
public class UserDto extends IdEntity {

    @ApiModelProperty(value = "用户登录名", position = 1)
    private String username;

    @ApiModelProperty(value = "用户登录密码(加密)", position = 2)
    private String shaPassword;

    @ApiModelProperty(value = "用户姓名", position = 3)
    private String realName;

    @ApiModelProperty(value = "用户简称", position = 4)
    private String nickname;

    @ApiModelProperty(value = "用户角色", position = 5)
    private List<RoleDto> roles = Lists.newArrayList();

    @ApiModelProperty(value = "领导关注企业", position = 6)
    private List<OtherGroupDto> focusCompanies = Lists.newArrayList();

    @ApiModelProperty(value = "用户电话", position = 7)
    private String phone;

    @ApiModelProperty(value = "企业对象", position = 8)
    private OtherGroupDto uniqueGroup;

    @ApiModelProperty(value = "联系人", position = 9)
    private String concat;

    @ApiModelProperty(value = "邮箱", position = 10)
    private String email;

    @ApiModelProperty(value = "创建时间", position = 11)
    private String createDate;

    public UserDto(Long id) {
        this.id = id;
    }

    public UserDto() {
    }

    /**
     * 将当前的用户pojo类转换成dto类
     *
     * @param user
     * @return
     */
    public static UserDto copy(User user) {
        UserDto dto = new UserDto();
        if (user == null) {
            throw new BootException(401, "用户不存在");
        }
        BeanUtilsEnhance.copyDateFiledEnhance(user, dto, "roles", "focusCompanies", "uniqueGroup");
        dto.setRoles(RoleDto.copy(user.getRoles()));
        dto.setUniqueGroup(OtherGroupDto.copy(user.getUniqueGroup()));
        return dto;
    }

    public static List<UserDto> copy(List<User> sources) {
        return BeanUtilsEnhance.copyList(sources, UserDto.class, new CopyListFilter<UserDto, User>() {
            @Override
            public UserDto copy(User source, UserDto target) {
                return UserDto.copy(source);
            }
        });
    }


    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getShaPassword() {
        return shaPassword;
    }

    public void setShaPassword(String shaPassword) {
        this.shaPassword = shaPassword;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<RoleDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDto> roles) {
        this.roles = roles;
    }

    public List<OtherGroupDto> getFocusCompanies() {
        return focusCompanies;
    }

    public void setFocusCompanies(List<OtherGroupDto> focusCompanies) {
        this.focusCompanies = focusCompanies;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public OtherGroupDto getUniqueGroup() {
        return uniqueGroup;
    }

    public void setUniqueGroup(OtherGroupDto uniqueGroup) {
        this.uniqueGroup = uniqueGroup;
    }

    public String getConcat() {
        return concat;
    }

    public void setConcat(String concat) {
        this.concat = concat;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }
}
