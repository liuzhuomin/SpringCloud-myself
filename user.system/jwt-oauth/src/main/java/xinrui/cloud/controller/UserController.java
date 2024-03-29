package xinrui.cloud.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import xinrui.cloud.config.EhcacheComponent;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.dto.UserDto;
import xinrui.cloud.service.UserService;
import net.sf.ehcache.Cache;
import net.sf.ehcache.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.oauth2.provider.endpoint.CheckTokenEndpoint;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.vto.UserVto;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * <B>Title:</B>UserController</br>
 * <B>Description:</B>用户相关接口</br>
 * <B>Copyright: </B>Copyright (c) 2019 </br>
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:17
 */
@Api("用户相关接口")
@RestController
@RequestMapping("user")
public class UserController {

    private Logger log = LoggerFactory.getLogger(UserController.class);


    @Autowired
    UserService userService;

    @Autowired
    EhcacheComponent ehcacheComponent;

    @Autowired
    CheckTokenEndpoint checkTokenEndpoint;

    /**
     * 根据用户名称查询用户
     *
     * @param username
     * @return
     */
    @GetMapping("find/{username}")
    @ApiOperation(position = 1, value = "通过用户名称获取用户详情", notes = "通过用户名称获取用户详情", tags = "用户相关接口", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParam(name = "username", value = "用户名", paramType = "query", dataType = "String", required = true, readOnly = true)
    public ResultDto<UserDto> findUserByUserName(@PathVariable("username") String username) {
        log.info("当前查询的用户：{},当前的token{}", username);
        return ResponseDto.success(userService.findUserByUserName(username));
    }

    /**
     * 根据用户id查询用户
     *
     * @param id
     * @return
     */
    @GetMapping("find/by/{id}")
    @ApiOperation(position = 2, value = "通过用户id获取用户详情", notes = "通过用户id获取用户详情", tags = "用户相关接口", httpMethod = "GET", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "用户id", paramType = "query", dataType = "String", required = true, readOnly = true)
    public ResultDto<UserDto> findUserByUserId(@PathVariable("id") Long id) {
        log.info("当前查询的用户：" + id);
        return ResponseDto.success(userService.findUserByUserId(id));
    }

    /**
     * 注销用户，原理是将用户的token放置进缓存中，如果缓存中存在此token的key，则在网关层禁止向下路由
     *
     * @param token 用户的access_token
     * @return {@link ResultDto}
     */
    @ApiOperation(position = 3, value = "通过令牌注销用户", notes = "通过令牌注销用户", tags = "用户相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParam(name = "token", value = "经过授权的令牌", paramType = "query", dataType = "String", required = true, readOnly = true)
    @PostMapping("logout/token")
    public ResultDto<?> logoutToken(@RequestParam("token") @NotNull String token) {
        Cache defaultTokenCache = ehcacheComponent.getDefaultTokenCache();
        try {
            checkTokenEndpoint.checkToken(token);
        } catch (Exception e) {
            return ResultDto.error("注销失败,不合法的Token!");
        }
        defaultTokenCache.put(new Element(token, new byte[1]));
        Element element = defaultTokenCache.get(token);
        log.debug("accept token : {}", token);
        log.debug("defaultTokenCache:" + defaultTokenCache);
        log.debug("saved element:" + element);
        return ResultDto.success("注销用户成功!");
    }

    @PostMapping("/create/user")
    @ApiOperation(position = 4, value = "创建用户", notes = "通过json数据创建用户", tags = "用户相关接口"
            , httpMethod = "POST", produces = "application/json")
    public ResultDto<UserDto> createUser(@RequestBody @ApiParam("用户数据传输对象") UserVto userVto) {
        return ResponseDto.success(userService.createByVto(userVto));
    }

    @PostMapping("/alias/group")
    @ApiOperation(position = 5, value = "用户关联组织", notes = "通过json数据创建用户", tags = "用户相关接口"
            , httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "用户id", paramType = "query", dataType = "INT", required = true),
            @ApiImplicitParam(name = "groupId", value = "组织id", paramType = "query", dataType = "INT", required = true),
            @ApiImplicitParam(name = "groupType", value = "组织类型", paramType = "query", dataType = "STRING", required = true)
    })
    public ResultDto<UserDto> aliasUser2Group(@RequestParam("id") Long id,
                                              @RequestParam("groupId") Long groupId,
                                              @RequestParam("groupType") String groupType) {
        log.info("id:{}\tgroupId:{}\tgroupType{}", id, groupId, groupType);
        return ResponseDto.success(userService.aliasUser2Group(id, groupId, groupType));
    }

    @DeleteMapping("/delete/{id}")
    @ApiOperation(position = 6, value = "删除用户", notes = "通过用户id删除用户", tags = "用户相关接口"
            , httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "用户id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> deleteUser(@PathVariable("id") Long id) {
        userService.remove(id);
        return ResultDto.success("删除成功!");
    }

    @PostMapping
    @ApiOperation(position = 4, value = "修改用户", notes = "通过json数据修改用户", tags = "用户相关接口"
            , httpMethod = "POST", produces = "application/json")
    public ResultDto<UserDto> putUser(@RequestBody @ApiParam("用户数据传输对象") UserVto userVto) {
        return ResponseDto.success(userService.editByVto(userVto));
    }

    @GetMapping("list/{groupType}/{currentPage}/{pageSize}")
    @ApiOperation(position = 4, value = "根据用户组织查找用户列表", notes = "根据用户组织查找用户列表", tags = "用户相关接口"
            , httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "groupType", value = "用户组织", paramType = "path", dataType = "STRING", required = true),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", paramType = "path", dataType = "INT", required = true),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", paramType = "path", dataType = "INT", required = true)
    })
    public ResultDto<PageDto<List<UserDto>>> listUserByType(
            @PathVariable("groupType") String groupType,
            @PathVariable("currentPage") int currentPage,
            @PathVariable("pageSize") int pageSize) {
        return ResponseDto.success(userService.listByGroupType(groupType, currentPage, pageSize));
    }

}


