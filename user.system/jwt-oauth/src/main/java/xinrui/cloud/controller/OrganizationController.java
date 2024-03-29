package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.dto.OtherGroupDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.OrganizationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import xinrui.cloud.vto.OtherGroupVto;

/**
 * 政府组织相关接口
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/6/26 20:17
 */
@Api("政府组织相关接口")
@RestController
@RequestMapping("organization")
public class OrganizationController {

    private final static Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

    @Autowired
    OrganizationService organizationService;

    @GetMapping("find/{name}")
    public ResultDto<OtherGroupDto> findUserByUserName(@PathVariable("name") String name) {
        LOGGER.info("当前查询的组织{}", name);
        return ResponseDto.success(organizationService.findByCompanyName(name));
    }

    @GetMapping("find/by/{id}")
    public ResultDto<OtherGroupDto> findUserByUserId(@PathVariable("id") Long id) {
        return ResponseDto.success(organizationService.findByCompanyId(id));
    }

    @PostMapping
    public ResultDto<OtherGroupDto> create(@RequestBody OtherGroupVto vto) {
        return ResponseDto.success(organizationService.createByVto(vto));
    }

    @DeleteMapping("{id}")
    public ResultDto deleteById(@PathVariable("id") Long id) {
        organizationService.remove(id);
        return ResultDto.success();
    }

    @GetMapping("/list/{type}")
    public ResultDto listByType(@PathVariable("type") String type) {
        return ResultDto.success(organizationService.listByType(type));
    }


}


