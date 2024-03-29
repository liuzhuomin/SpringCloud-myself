package xinrui.cloud.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

/**
 * <B>Title:</B>UserDetailDto</br>
 * <B>Description:</B>用于security验证用户信息/br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 19:59
 */
public class UserDetailDto  implements UserDetails {


    private String userId;
    /**
     * 登录的用户名称
     */
    private String username;
    /**
     * 登录的用密码
     */
    @JsonIgnore
    private String password;
    /**
     * 登录的用户角色
     */
    private List<RoleDetailDto> roles;

    /**
     * 将{@link UserDto} 对象转换成令牌验证对象{@link UserDetailDto}
     * @param user
     * @return
     */
    public static UserDetails copy(UserDto user) {
        UserDetailDto userDetailDto = new UserDetailDto();
        userDetailDto.setUserId(user.getId().toString());
        userDetailDto.setUsername(user.getUsername());
        userDetailDto.setPassword(user.getShaPassword());
        userDetailDto.setRoles(RoleDetailDto.copy(user.getRoles()));
        return userDetailDto;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }


    public UserDetailDto(String username, String password, List<RoleDetailDto> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    public UserDetailDto() {
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    /**
     * 账户是否过期,过期无法验证
     *
     * @return
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 指定用户是否被锁定或者解锁,锁定的用户无法进行身份验证
     *
     * @return
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 指示是否已过期的用户的凭据(密码),过期的凭据防止认证
     *
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否被禁用,禁用的用户不能身份验证
     *
     * @return
     */
    @Override
    public boolean isEnabled() {
        return true;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<RoleDetailDto> getRoles() {
        return roles;
    }

    public void setRoles(List<RoleDetailDto> roles) {
        this.roles = roles;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
