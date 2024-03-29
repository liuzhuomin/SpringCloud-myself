package xinrui.cloud.controller;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Lists;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import xinrui.cloud.domain.dto.ProblemLimitInnerDto;
import xinrui.cloud.domain.dto.ProblemTriggerDto;
import xinrui.cloud.domain.dto.ProblemTriggerInnerDto;
import xinrui.cloud.dto.ResponseDto;
import xinrui.cloud.dto.ResultDto;
import xinrui.cloud.service.ProblemTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Api("触发结果相关接口")
@RestController
@RequestMapping("problem/trigger")
public class ProblemTriggerController {

    @Autowired
    ProblemTriggerService triggerService;

    @ApiOperation(value = "添加触发结果之前需要获取的基础数据(code 50020)", notes = "添加触发结果之前需要获取的基础数据", tags = "触发结果相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limitId", value = "问题限制数据模板的id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("listData/before/add")
    public ResultDto<ProblemTriggerDto> listDataBeForAdd(@RequestParam Long limitId) {
        return ResponseDto.success(triggerService.listDataBeForAdd(limitId));
    }

    @ApiOperation(value = "添加触发结果(code 50021)", notes = "添加触发结果到模板中,json数据 详情见json接口", tags = "触发结果相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "limitId", value = "问题限制数据模板的id", paramType = "query", dataType = "Long", required = true),
            @ApiImplicitParam(name = "json", value = "json数据 详情见json接口", paramType = "query", dataType = "String", required = true)})
    @PostMapping("add")
    public ResultDto<ProblemTriggerDto> add(@RequestParam Long limitId, @RequestParam String json) {
        ProblemTriggerDto parseObject = JSON.parseObject(json, ProblemTriggerDto.class);
        Assert.notNull(parseObject, "json字符串解析错误！");
        parseObject = triggerService.add(limitId, parseObject);
        return ResponseDto.success(parseObject);
    }

    @ApiOperation(value = "删除触发结果(code 50022)", notes = "删除触发结果", tags = "触发结果相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "triggerId", value = "触发结果数据模板的id", paramType = "query", dataType = "Long", required = true)})
    @PostMapping("delete")
    public ResultDto<?> delete(@RequestParam Long triggerId) {
        triggerService.remove(triggerId);
        return ResponseDto.success();
    }

    @ApiOperation(value = "修改触发结果(code 50023)", notes = "修改触发结果", tags = "触发结果相关接口", httpMethod = "POST", produces = "application/json")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "triggerId", value = "触发结果数据模板的id", paramType = "query", dataType = "Long", required = true)
            , @ApiImplicitParam(name = "json", value = "触发结果的json数据，详情见json接口", paramType = "query", dataType = "String", required = true)})
    @PostMapping("edit")
    public ResultDto<ProblemTriggerDto> edit(@RequestParam Long triggerId, @RequestParam String json) {
        ProblemTriggerDto dto = JSON.parseObject(json, ProblemTriggerDto.class);
        Assert.notNull(dto, "json字符串不能为空!");
        return ResponseDto.success(triggerService.edit(triggerId, dto));
    }

    @ApiOperation(value = "触发结果的json数据示例(code 50024)", notes = "触发结果的json数据示例", tags = "触发结果相关接口", httpMethod = "POST", produces = "application/json")
    @PostMapping("json")
    public ProblemTriggerDto json() {
        ProblemTriggerDto problemTriggerDto = new ProblemTriggerDto();
        List<ProblemTriggerInnerDto> result = Lists.newArrayList();
        for (int i = 0; i < 3; i++) {
            ProblemTriggerInnerDto dto = new ProblemTriggerInnerDto();
            dto.setLogicOne("你" + i);
            dto.setLogicTwo("你" + i);
            dto.setId(666L);
            ProblemTriggerInnerDto dto1 = new ProblemTriggerInnerDto();
            dto1.setLogicOne("你" + i);
            dto1.setLogicTwo("你" + i);
            dto1.setId(Long.parseLong((666 + i) + ""));
            dto.getChildren().add(dto1);
            result.add(dto);
        }
        final List<ProblemLimitInnerDto> variables = problemTriggerDto.getVariables();
        variables.add(new ProblemLimitInnerDto(2L, "你说呢"));
        problemTriggerDto.setVariables(variables);
        problemTriggerDto.setChild(result);
        return problemTriggerDto;
    }

}
