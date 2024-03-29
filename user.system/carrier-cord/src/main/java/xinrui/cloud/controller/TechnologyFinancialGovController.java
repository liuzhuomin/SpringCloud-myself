package xinrui.cloud.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;
import xinrui.cloud.annotation.Group;
import xinrui.cloud.common.utils.DataUtil;
import xinrui.cloud.domain.GroupType;
import xinrui.cloud.domain.TechnologyFinancial;
import xinrui.cloud.domain.dto.TechnologyFinancialDto;
import xinrui.cloud.domain.dto.TechnologyFinancialGovDto;
import xinrui.cloud.dto.PageDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.TechnologyFinancialService;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 科技金融接口(政府端)
 *
 * @author liuliuliu
 * @version 1.0
 * 2019/8/9 14:57
 */
@Api("科技金融模块（政府端）")
@RestController
@RequestMapping("technology/gov")
@SuppressWarnings("unused")
@Group(GroupType.ORGANIZATION)
public class TechnologyFinancialGovController {

    @Resource
    TechnologyFinancialService technologyFinancialService;

    @GetMapping("/list/audits")
    @ApiOperation(position = 1, value = "根据状态获取审核产品列表", notes = "根据传入的状态获取产品列表(1.查询待审核的，3查询通过的)",
            tags = "科技金融模块（政府端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "status", value = "状态值(1.查询待审核的，3查询通过的)", paramType = "query", dataType = "INT", required = true),
            @ApiImplicitParam(name = "bank", value = "所选银行", paramType = "query", dataType = "STRING"),
            @ApiImplicitParam(name = "currentPage", value = "当前页码", paramType = "query", dataType = "INT"),
            @ApiImplicitParam(name = "pageSize", value = "一页大小", paramType = "query", dataType = "INT")

    })
    public ResultDto<PageDto<List<TechnologyFinancialGovDto>>> audits(@RequestParam("status") int status,
                                                                      @RequestParam(value = "bank", required = false) String bank,
                                                                      @RequestParam(value = "currentPage", defaultValue = "1", required = false) int currentPage,
                                                                      @RequestParam(value = "pageSize", defaultValue = "10", required = false) int pageSize) {
        return ResponseDto.success(technologyFinancialService.audits(status, bank, currentPage, pageSize));
    }

    @GetMapping("/{id}")
    @ApiOperation(position = 2, value = "根据id获取产品详情", notes = "根据id获取产品详情", tags = "科技金融模块（政府端）", httpMethod = "GET", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "科技金融产品对象的id", paramType = "query", dataType = "INT", required = true)
    public ResultDto<TechnologyFinancialDto> findById(@PathVariable("id") Long id) {
        return ResponseDto.success(technologyFinancialService.findDtoById(id));
    }

    @PatchMapping("/approve/{id}")
    @ApiOperation(position = 3, value = "产品通过审核", notes = "根据科技金融对象的id将产品通过审核",
            tags = "科技金融模块（政府端）", httpMethod = "PATCH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "科技金融对象的id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> approve(@PathVariable("id") Long id) {
        technologyFinancialService.audit(id, TechnologyFinancial.TechnologyStatus.ONLINE, null);
        return ResponseDto.success("审核通过成功");
    }

    @PatchMapping("/refused/{id}/{reason}")
    @ApiOperation(position = 4, value = "产品驳回审核", notes = "根据科技金融对象的id和驳回理由将产品审核驳回",
            tags = "科技金融模块（政府端）", httpMethod = "PATCH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "科技金融对象的id", paramType = "path", dataType = "INT", required = true),
            @ApiImplicitParam(name = "reason", value = "拒绝的理由", paramType = "path", dataType = "STRING", required = true)
    })
    public ResultDto<?> refused(@PathVariable("id") @NotNull Long id, @PathVariable("reason") @NotEmpty String reason) {
        technologyFinancialService.audit(id, TechnologyFinancial.TechnologyStatus.REFUSED, reason);
        return ResponseDto.success("拒绝通过成功!");
    }

    @PatchMapping("/offline/{id}")
    @ApiOperation(position = 5, value = "产品审核下架", notes = "根据科技金融对象的id将产品下架",
            tags = "科技金融模块（政府端）", httpMethod = "PATCH", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "id", value = "科技金融对象的id", paramType = "path", dataType = "INT", required = true)
    public ResultDto<?> offline(@PathVariable("id") @NotNull Long id) {
        technologyFinancialService.audit(id, TechnologyFinancial.TechnologyStatus.OFFLINE, null);
        return ResponseDto.success("下架通过成功!");
    }

    @PutMapping("/offline/batch")
    @ApiOperation(position = 6, value = "批量审核产品下架", notes = "根据科技金融id集合(id用英文下的,分割)将产品批量下架",
            tags = "科技金融模块（政府端）", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "ids", value = "科技金融对象的id集合", paramType = "query", dataType = "STRING", required = true)
    public ResultDto<?> offline(@RequestParam("ids") @NotEmpty String ids) {
        InvokeImpl.invoke(ids, new InvokeInterface() {
            @Override
            public void invoke(Long id) {
                technologyFinancialService.audit(id, TechnologyFinancial.TechnologyStatus.OFFLINE, null);
            }
        });
        return ResponseDto.success("批量下架通过成功!");
    }

    @PutMapping("/approve/batch")
    @ApiOperation(position = 7, value = "产品批量通过审核", notes = "根据科技金融id集合(id用英文下的,分割)将产品批量通过审核",
            tags = "科技金融模块（政府端）", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParam(name = "ids", value = "科技金融对象的id集合，id用英文下的,分开", paramType = "query", dataType = "STRING", required = true)
    public ResultDto<?> approveBatch(@RequestParam("ids") @NotEmpty String ids) {
        InvokeImpl.invoke(ids, new InvokeInterface() {
            @Override
            public void invoke(Long id) {
                technologyFinancialService.audit(id, TechnologyFinancial.TechnologyStatus.ONLINE, null);
            }
        });
        return ResponseDto.success("批量审核通过成功");
    }

    @PutMapping("/refused/batch/{reason}")
    @ApiOperation(position = 8, value = "产品批量驳回审核", notes = "根据科技金融id集合(id用英文下的,分割)将产品批量驳回审核",
            tags = "科技金融模块（政府端）", httpMethod = "PUT", produces = MediaType.APPLICATION_JSON_VALUE)
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "ids", value = "科技金融对象的id集合", paramType = "query", dataType = "STRING", required = true),
            @ApiImplicitParam(name = "reason", value = "拒绝的理由", paramType = "path", dataType = "STRING", required = true)
    })
    public ResultDto<?> refusedBatch(@RequestParam("ids") @NotEmpty String ids, @PathVariable("reason") @NotEmpty final String reason) {
        InvokeImpl.invoke(ids, new InvokeInterface() {
            @Override
            public void invoke(Long id) {
                technologyFinancialService.audit(id, TechnologyFinancial.TechnologyStatus.REFUSED, reason);
            }
        });
        return ResponseDto.success("批量拒绝通过成功!");
    }

    private interface InvokeInterface {
        void invoke(Long id);
    }

    private static class InvokeImpl {
        private static void invoke(String ids, InvokeInterface invokeInterface) {
            List<String> split = DataUtil.split(ids);
            Assert.notEmpty(split, "选中项不能为空!");
            for (String str : split) {
                invokeInterface.invoke(Long.parseLong(str));
            }
        }
    }
}
