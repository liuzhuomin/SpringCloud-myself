package xinrui.cloud.dto;

import java.util.Set;

/**
 * <B>Title:</B>TokenDto</br>
 * <B>Description:</B> 简略token信息传输对象 </br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/25 16:13
 */
public class TokenDto {
    /**
     * 用户名
     */
    Object principal;
    /**
     * 角色集合
     */
    Set<String> strings;

    public Object getPrincipal() {
        return principal;
    }

    public void setPrincipal(Object principal) {
        this.principal = principal;
    }

    public Set<String> getStrings() {
        return strings;
    }

    public void setStrings(Set<String> strings) {
        this.strings = strings;
    }

    public TokenDto(Object principal, Set<String> strings) {
        this.principal = principal;
        this.strings = strings;
    }
}
