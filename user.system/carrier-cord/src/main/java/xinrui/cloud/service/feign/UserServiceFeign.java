package xinrui.cloud.service.feign;

import io.swagger.annotations.ApiParam;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.cloud.netflix.feign.FeignClient;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.fallback.UserServiceFallBack;
import xinrui.cloud.vto.UserVto;

import javax.validation.constraints.NotNull;
import java.util.List;

@FeignClient(value = "authorization", path = "user", fallback = UserServiceFallBack.class)
public interface UserServiceFeign {


    /**
     * 根据用户名称查询用户
     *
     * @param username
     * @return
     */
    @LoadBalanced
    @GetMapping("find/{username}")
    public ResultDto<UserDto> findUserByUserName(@PathVariable("username") String username);

    /**
     * 根据用户id查询用户
     *
     * @param id
     * @return
     */
    @GetMapping("find/by/{id}")
    public ResultDto<UserDto> findUserByUserId(@PathVariable("id") Long id);

    /**
     * 注销用户，原理是将用户的token放置进缓存中，如果缓存中存在此token的key，则在网关层禁止向下路由
     *
     * @param token 用户的access_token
     * @return {@link ResultDto}
     */
    @PostMapping("logout/token")
    public ResultDto<?> logoutToken(@RequestParam("token") @NotNull String token);

    @PostMapping("/create/user")
    public ResultDto<UserDto> createUser(@RequestBody @ApiParam("用户数据传输对象") UserVto userVto);

    @PostMapping("/alias/group")
    public ResultDto<UserDto> aliasUser2Group(@RequestParam("id") Long id,
                                              @RequestParam("groupId") Long groupId,
                                              @RequestParam("groupType") String groupType);

    @DeleteMapping("/delete/{id}")
    public ResultDto<?> deleteUser(@PathVariable("id") Long id);

    @PostMapping
    public ResultDto<UserDto> putUser(@RequestBody @ApiParam("用户数据传输对象") UserVto userVto);

    @GetMapping("list/{groupType}/{currentPage}/{pageSize}")
    public ResultDto<PageDto<List<UserDto>>> listUserByType(
            @PathVariable("groupType") String groupType,
            @PathVariable("currentPage") int currentPage,
            @PathVariable("pageSize") int pageSize);
}
