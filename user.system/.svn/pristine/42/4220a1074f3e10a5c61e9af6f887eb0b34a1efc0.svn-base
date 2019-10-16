package xinrui.cloud.controller;

import io.swagger.annotations.*;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.annotation.Group;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.domain.OtherGroup;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.dto.*;
import xinrui.cloud.domain.vto.TechnologyFinancialVto;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.TechnologyFinancialAppointmentService;
import xinrui.cloud.service.TechnologyFinancialService;

import javax.annotation.Resource;
import java.util.List;

/**
 * 科技金融相关项目
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/8/5 16:23
 */
@Api("科技金融模块（银行端）")
@RestController
@RequestMapping("technology/bank")
@Group(GroupType.BANK)
@SuppressWarnings("unused")
public class TechnologyFinancialBankController {

    @Resource
    TechnologyFinancialService technologyFinancialService;

    @Resource
    TechnologyFinancialAppointmentService technologyFinancialAppointmentService;


    @GetMapping("/dates")
    @Group(GroupType.ORGANIZATION)
    @ApiOperation(position = 1, value = "获取贷款期限列表", notes = "获取贷款期限列表(3个月、5个月等)", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<TechnologyLoanDateDto>> dates() {
        return ResponseDto.success(technologyFinancialService.dates());
    }

    @GetMapping("/types")
    @ApiOperation(position = 2, value = "获取贷款类型列表", notes = "获取贷款类型列表(有价证券、票据等)", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<TechnologyLoanTypeDto>> types() {
        return ResponseDto.success(technologyFinancialService.types());
    }

    @GetMapping("/amounts")
    @ApiOperation(position = 3, value = "获取贷款额度列表", notes = "获取贷款额度列表(100万以下、200万等)", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<TechnologyLoanAmountDto>> amounts() {
        return ResponseDto.success(technologyFinancialService.amounts());
    }

    @PostMapping("/save")
    @ApiOperation(position = 4, value = "银行端创建产品（保存）", notes = "银行端创建产品", tags = "科技金融模块（银行端）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<TechnologyFinancialDto> saveTechnology(@RequestBody @ApiParam(value = "传递科技金融核心对象", required = true) TechnologyFinancialVto technologyFinancialVto) {
        return ResponseDto.success(technologyFinancialService.saveByDto(technologyFinancialVto));
    }

    @PostMapping("/public")
    @ApiOperation(position = 5, value = "银行端创建产品（发布）", notes = "银行端创建产品", tags = "科技金融模块（银行端）", httpMethod = "POST", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<TechnologyFinancialDto> publicTechnology(@RequestBody @ApiParam(value = "传递科技金融核心对象", required = true) TechnologyFinancialVto technologyFinancialVto) {
        technologyFinancialVto.setStatus(TechnologyFinancial.TechnologyStatus.APPLYING.value());
        return ResponseDto.success(technologyFinancialService.saveByDto(technologyFinancialVto));
    }

    @PatchMapping("/public/{id}")
    @ApiOperation(position = 6, value = "草稿箱发布", notes = "根据科技金融产品id,发布草稿箱产品，并且返回最新的草稿箱列表", tags = "科技金融模块（银行端）",
            httpMethod = "PATCH", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "科技金融对象id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<List<TechnologyFinancialSimpleDto>> publicTechnology(@PathVariable("id") Long id) {
        return ResponseDto.success(technologyFinancialService.publicById(id));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(position = 7, value = "删除产品", notes = "银行端根据科技金融id删除产品", tags = "科技金融模块（银行端）", httpMethod = "DELETE", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "科技金融对象id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> deleteTechnology(@PathVariable("id") Long id) {
        try {
            technologyFinancialService.remove(id);
        } catch (Exception e) {
            return ResponseDto.error("数据不存在!");
        }
        return ResponseDto.success("删除成功!");
    }

    @PutMapping("/{id}")
    @ApiOperation(position = 8, value = "编辑产品", notes = "根据传递过来的json编辑产品", tags = "科技金融模块（银行端）", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "科技金融对象id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> putTechnology(@PathVariable("id") Long id, @RequestBody @ApiParam(value = "传递科技金融核心对象", required = true) TechnologyFinancialVto technologyFinancialVto) {
        technologyFinancialService.put(id, technologyFinancialVto);
        return ResponseDto.success("编辑成功!");
    }

    @GetMapping("/list/draft")
    @ApiOperation(position = 9, value = "获取草稿箱产品列表", notes = "获取草稿箱产品列表", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<List<TechnologyFinancialSimpleDto>> listDraft() {
        return ResponseDto.success(technologyFinancialService.drafts());
    }

    @GetMapping("/list/audit")
    @ApiOperation(position = 10, value = "获取审核产品列表", notes = "获取审核产品列表", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<TechnologyFinalAuditDto> listAudit() {
        return ResponseDto.success(technologyFinancialService.audits());
    }

    @GetMapping("/list/online")
    @ApiOperation(position = 11, value = "获取上线产品列表", notes = "获取上线产品列表", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResultDto<TechnologyFinancialOnlineDto> listOnline() {
        return ResponseDto.success(technologyFinancialService.onlines());
    }

    @GetMapping("/list/appointment/{id}")
    @ApiOperation(position = 12, value = "获取预约名单", notes = "获取预约名单", tags = "科技金融模块（银行端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "科技金融对象id", paramType = "path", dataType = "INT", required = true),
            @ApiImplicitParam(name = "status", value = "标识，0代表查找预约的，1代表查找未曾预约的", paramType = "query", dataType = "INT", defaultValue = "0"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", paramType = "query", dataType = "INT", required = true),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", paramType = "query", dataType = "INT", required = true)
    })
    public ResultDto<PageDto<List<TechnologyFinancialAppointmentUserDto>>> listAppointment(
            @PathVariable(value = "id") Long id,
            @RequestParam(value = "status", defaultValue = "0", required = false) int status,
            @RequestParam(value = "currentPage", defaultValue = "1", required = false) int currentPage,
            @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return ResponseDto.success(technologyFinancialAppointmentService.listAppointmentBank(id, status, currentPage, pageSize));
    }

    @PatchMapping("/process/{appointmentId}")
    @ApiOperation(position = 13, value = "处理预约的用户", notes = "处理预约的用户", tags = "科技金融模块（银行端）", httpMethod = "PATCH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "bankId", value = "预约对象的id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> processAppointment(@PathVariable("appointmentId") Long appointmentId) {
        technologyFinancialAppointmentService.processAppointment(appointmentId);
        return ResponseDto.success("处理成功!");
    }

    @PatchMapping("/read/{id}")
    @ApiOperation(position = 14, value = "阅读被拒绝的产品", notes = "阅读被拒绝的产品", tags = "科技金融模块（银行端）", httpMethod = "PATCH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "预约对象的id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> readRefused(@PathVariable("id") Long id) {
        technologyFinancialService.readRefused(id);
        return ResponseDto.success("阅读成功!");
    }


}

