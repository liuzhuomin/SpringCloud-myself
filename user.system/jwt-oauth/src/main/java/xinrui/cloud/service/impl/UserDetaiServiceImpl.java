package xinrui.cloud.service.impl;

//import xinrui.cloud.dao.UserDao;

import xinrui.cloud.BootException;
import xinrui.cloud.dto.UserDetailDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 * <B>Title:</B>UserDetaiServiceImpl</br>
 * <B>Description:</B>对用户数据进行处理，并且返回给oauth2验证需要的格式</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 9:08
 */
@Service
public class UserDetaiServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserDto user = userService.findUserByUserName(username);
        if (user == null) {
            throw new BootException(401, "用户名或者密码错误");
        }
        return UserDetailDto.copy(user);
    }

}
