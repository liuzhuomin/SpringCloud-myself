package xinrui.cloud.controller;

import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.BootException;
import xinrui.cloud.domain.ProblemLimit;
import xinrui.cloud.domain.dto.ProblemLimitDataDto;
import xinrui.cloud.domain.dto.ProblemLimitDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.ProblemLimitService;
import xinrui.cloud.util.AppUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("问题限制相关接口")
@RestController
@RequestMapping("problem/limit")
public class ProblemLimitController {

    @Autowired
    ProblemLimitService problemLimitService;

    @ApiOperation(value = "获取添加问题限制和添加出发结构必须的数据项(code 50011)", notes = "获取添加问题限制必须的数据项", tags = "问题限制相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "问题编辑数据模板的id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("basedata")
    public ResultDto<ProblemLimitDataDto> listBaseData(@RequestParam Long problemId) {
        return ResponseDto.success(problemLimitService.listBaseData(problemId),
                AppUtils.getRealUrl("problem/limit/add"));
    }

    @ApiOperation(value = "添加问题限制(code 50012)", notes = "添加问题模板，适用于单选,需要传递一级选项和二级选项的json数据，数据格式参见testJson接口</br>子选项只能是 单选(0)、填空(2)", tags = "问题限制相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "problemId", value = "问题编辑数据模板的id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "公式的json形式", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "ids", value = "变量的id使用英文下的逗号隔开,用于前段编辑回显选中的变量", paramType = "query", dataType = "String", required = false)})
    @PostMapping("add")
    public ResultDto<List<ProblemLimitDto>> add(@RequestParam Long problemId, @RequestParam String json,
                                                @RequestParam(required = false) String ids) {
        if (StringUtils.isBlank(json)) {
            throw new BootException("参数错误");
        }
        List<ProblemLimit> parseArray = JSONArray.parseArray(json, ProblemLimit.class);
        return ResponseDto.success(problemLimitService.addLimitData(problemId, parseArray, ids),
                AppUtils.getRealUrl("problem/limit/delete"));
    }

    @ApiOperation(value = "删除问题限制模板及其子选项(code 50017)", notes = "获取添加问题限制必须的数据项", tags = "问题限制相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limitId", value = "问题限制模板的id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("delete")
    public ResultDto<?> delete(@RequestParam Long limitId) {
        problemLimitService.remove(limitId);
        return ResponseDto.success();
    }

    @ApiOperation(value = "编辑问题限制模板(code 50018)", notes = "编辑问题限制模板", tags = "问题限制相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limitId", value = "问题限制模板的id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "问题限制的json格式字符串", paramType = "query", dataType = "String", required = true),
            @ApiImplicitParam(name = "ids", value = "变量的id使用英文下的逗号隔开", paramType = "query", dataType = "String", required = false)})
    @PostMapping("edit")
    public ResultDto<ProblemLimitDto> edit(@RequestParam Long limitId, @RequestParam String json,
                                           @RequestParam(required = false) String ids) {
        if (StringUtils.isBlank(json)) {
            throw new BootException("参数错误");
        }
        return ResponseDto
                .success(problemLimitService.edit(limitId, JSONArray.parseArray(json, ProblemLimit.class), ids));
    }

    @ApiOperation(value = "问题限制的json数据示例(code 50019)", notes = "问题限制的json数据示例", tags = "问题限制相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("json")
    public List<ProblemLimitDto> json() {
        List<ProblemLimitDto> result = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            ProblemLimitDto dto = new ProblemLimitDto();
            dto.setLogicalFormLeft("你" + i);
            dto.setLogicalFormRight("我" + i);
            dto.setId(666L);
            dto.setLogic("and");
            dto.setConnector("+");
            result.add(dto);
        }
        return result;
    }

}
