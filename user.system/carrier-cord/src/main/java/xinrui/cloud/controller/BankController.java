package xinrui.cloud.controller;

import io.swagger.annotations.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.domain.dto.BankDto;
import xinrui.cloud.domain.dto.BankUserDto;
import xinrui.cloud.domain.vto.OtherGroupVto;
import xinrui.cloud.domain.vto.TechnologyBankUserVto;
import xinrui.cloud.dto.*;
import xinrui.cloud.service.feign.BankGroupServiceFeign;
import xinrui.cloud.service.feign.UserServiceFeign;
import xinrui.cloud.vto.UserVto;

import javax.annotation.Resource;
import java.util.List;

/**
 * 科技金融模块（银行账号相关）
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/8/9 14:57
 */
@Api("科技金融模块（银行账号相关）")
@RestController
@RequestMapping("bank")
@Slf4j
@SuppressWarnings("unused")
public class BankController {

    @Resource
    BankGroupServiceFeign bankGroupServiceFeign;

    @Resource
    UserServiceFeign userServiceFeign;

    @PostMapping("/{name}")
    @ApiOperation(position = 1, value = "添加银行组织", notes = "根据银行名称创建银行组织",
            tags = "科技金融模块（银行账号相关）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "name", value = "银行的名称", paramType = "path", dataType = "STRING", required = true)
    public ResultDto<?> createBank(@PathVariable("name") String name) {
        ResultDto<OtherGroupDto> otherGroupDtoResultDto = bankGroupServiceFeign.create(new OtherGroupVto(name));
        if (otherGroupDtoResultDto != null) {
            return ResponseDto.success("创建成功!");
        }
        return ResponseDto.success("创建失败!");
    }

    @DeleteMapping("/{id}")
    @ApiOperation(position = 2, value = "删除银行组织", notes = "根据银行id删除银行组织",
            tags = "科技金融模块（银行账号相关）", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "银行组织对象的id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> deleteBank(@PathVariable("id") Long id) {
        ResultDto resultDto = bankGroupServiceFeign.deleteById(id);
        if (resultDto != null) {
            return ResponseDto.success("删除成功!");
        }
        return ResponseDto.success("删除失败!");
    }

    @GetMapping
    @ApiOperation(position = 3, value = "获取银行组织列表", notes = "获取银行组织列表",
            tags = "科技金融模块（银行账号相关）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<BankDto>> listBanks() {
        ResultDto<List<OtherGroupDto>> bank = bankGroupServiceFeign.listByType("bank");
        List<BankDto> result = BankDto.copy(bank.getData());
        return ResponseDto.success(result);
    }

    @PostMapping("/user")
    @ApiOperation(position = 4, value = "创建银行所属账号", notes = "根据所选银行组织的id，创建银行所属账号",
            tags = "科技金融模块（银行账号相关）", httpMethod = "POST",
            produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<?> createUser(@RequestBody @ApiParam("银行账号对象") TechnologyBankUserVto technologyBankUserVto) {
        UserVto userVto = TechnologyBankUserVto.toUserVto(technologyBankUserVto);
        ResultDto<UserDto> result = userServiceFeign.createUser(userVto);
        log.info("result  :{}", result);
        if (result != null) {
            Long id = result.getData().getId();
            ResultDto<UserDto> userDtoResultDto = userServiceFeign.aliasUser2Group(id,
                    technologyBankUserVto.getBankId(), GroupType.BANK);
            log.info("Alias2Group :{}", userDtoResultDto);
            if (userDtoResultDto != null) {
                return ResponseDto.success("创建成功!");
            } else {
                userServiceFeign.deleteUser(id);
            }
        }
        return ResponseDto.error("创建失败!");
    }

    @DeleteMapping("/user/{id}")
    @ApiOperation(position = 5, value = "删除用户", notes = "通过用户id删除用户", tags = "科技金融模块（银行账号相关）"
            , httpMethod = "DELETE", produces = "application/json")
    @ApiImplicitParam(name = "id", value = "用户id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> deleteUser(@PathVariable("id") Long id) {
        if (userServiceFeign.deleteUser(id) != null) {
            return ResultDto.success("删除成功!");
        } else {
            return ResultDto.error("删除失败!");
        }
    }

    @PutMapping("/user")
    @ApiOperation(position = 6, value = "修改用户", notes = "通过json数据修改用户", tags = "科技金融模块（银行账号相关）"
            , httpMethod = "PUT", produces = "application/json")
    public ResultDto<UserDto> putUser(@RequestBody @ApiParam("科技金融银行账户对象(接收)") TechnologyBankUserVto technologyBankUserVto) {
        return userServiceFeign.putUser(TechnologyBankUserVto.toUserVto(technologyBankUserVto));
    }

    @GetMapping("list/user/{currentPage}/{pageSize}")
    @ApiOperation(position = 7, value = "分页查找用户列表", notes = "分页查找用户列表", tags = "科技金融模块（银行账号相关）"
            , httpMethod = "GET", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "currentPage", value = "当前页码", paramType = "path", dataType = "INT", required = true),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", paramType = "path", dataType = "INT", required = true)
    })
    public ResultDto<PageDto<List<BankUserDto>>> listUserByType(
            @PathVariable("currentPage") int currentPage,
            @PathVariable("pageSize") int pageSize) {
        ResultDto<PageDto<List<UserDto>>> pageDtoResultDto = userServiceFeign.listUserByType(GroupType.BANK, currentPage, pageSize);
        if (pageDtoResultDto != null && pageDtoResultDto.getCode() == 200) {
            PageDto<List<UserDto>> data = pageDtoResultDto.getData();
            List<BankUserDto> result = BankUserDto.copy(data.getT());
            return ResponseDto.success(new PageDto<List<BankUserDto>>(data.getTotalPage(), result));
        }
        throw new BootException("创建失败!");
    }


}
