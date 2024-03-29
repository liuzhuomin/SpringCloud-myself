package xinrui.cloud.base;

import xinrui.cloud.BootException;
import xinrui.cloud.compoment.Application;
import xinrui.cloud.compoment.UserContext;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.dto.UserDto;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;

/**
 * Common methods of packaging .
 *
 * @author Jihy
 * @since 2019-06-25 10Au0
 */
@ApiIgnore
public class BaseController {

    @Autowired
    private UserContext userContext;

    /**
     * 判断当前用户是否是企业用户
     *
     * @return
     */
    protected UserDto ensureCompany() {
        UserDto user = getCurrentUser();
        if (!StringUtils.equals(user.getUniqueGroup().getGroupType(), GroupType.OTHER)) {
            throw new BootException(401, "该资源只对企业用户开放");
        }
        return user;
    }

    /**
     * 判断当前用户是否是政府用户
     *
     * @return
     */
    protected UserDto ensureGovernment() {
        UserDto user = getCurrentUser();
        if (!StringUtils.equals(user.getUniqueGroup().getGroupType(), GroupType.ORGANIZATION)) {
            throw new BootException(501, "该资源只对政府用户开放!");
        }
        return user;
    }

    /**
     * 获取当前登录的用户
     *
     * @return
     */
    protected UserDto getCurrentUser() {
        return userContext.getCurrentUser();
    }



}
