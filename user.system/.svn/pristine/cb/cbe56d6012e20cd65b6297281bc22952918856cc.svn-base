package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.BankGroupService;
import xinrui.cloud.vto.OtherGroupVto;

/**
 * 银行组织相关接口
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:17
 */
@Api("银行组织相关接口")
@RestController
@RequestMapping("bank")
@Slf4j
public class BankGroupController {


    @Autowired
    BankGroupService bankGroupService;

    @GetMapping("find/{name}")
    public ResultDto<OtherGroupDto> findUserByUserName(@PathVariable("name") String name) {
        log.info("当前查询的组织{}", name);
        return ResponseDto.success(bankGroupService.findByCompanyName(name));
    }

    @GetMapping("find/by/{id}")
    public ResultDto<OtherGroupDto> findUserByUserId(@PathVariable("id") Long id) {
        return ResponseDto.success(bankGroupService.findByCompanyId(id));
    }

    @PostMapping
    public ResultDto<OtherGroupDto> create(@RequestBody OtherGroupVto vto) {
        return ResponseDto.success(bankGroupService.createByVto(vto));
    }

    @DeleteMapping("{id}")
    public ResultDto deleteById(@PathVariable("id") Long id) {
        bankGroupService.remove(id);
        return ResultDto.success();
    }

    @GetMapping("/list/{type}")
    public ResultDto listByType(@PathVariable("type") String type) {
        return ResultDto.success(bankGroupService.listByType(type));
    }


}


