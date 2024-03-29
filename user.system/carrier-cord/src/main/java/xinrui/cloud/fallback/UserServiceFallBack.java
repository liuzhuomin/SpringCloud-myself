package xinrui.cloud.fallback;

import org.springframework.stereotype.Component;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.feign.UserServiceFeign;
import xinrui.cloud.vto.UserVto;

import java.util.List;

/**
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:50
 */
@Component
public class UserServiceFallBack implements UserServiceFeign {

    @Override
    public ResultDto<UserDto> findUserByUserName(String username) {
        return null;
    }

    @Override
    public ResultDto<UserDto> findUserByUserId(Long id) {
        return null;
    }

    @Override
    public ResultDto<?> logoutToken(String token) {
        return null;
    }

    @Override
    public ResultDto<UserDto> createUser(UserVto userVto) {
        return null;
    }

    @Override
    public ResultDto<UserDto> aliasUser2Group(Long id, Long groupId, String groupType) {
        return null;
    }

    @Override
    public ResultDto<?> deleteUser(Long id) {
        return null;
    }

    @Override
    public ResultDto<UserDto> putUser(UserVto userVto) {
        return null;
    }

    @Override
    public ResultDto<PageDto<List<UserDto>>> listUserByType(String groupType, int currentPage, int pageSize) {
        return null;
    }
}
